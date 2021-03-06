package Code;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import Code.Employee;
import Code.Event;

public class CalendarProgram {
	private Connection con = null;
	public ConnectionToDatabase ctd;
	private List<Room> rooms;
	private List<Employee> employees;
	private Employee current_user;

	private Employee biti;
	private Employee sverre;
	private Employee yolo;

	// login-felter. Legger det til her siden de brukes i både createNewUser()
	// og i login()
	private String username;
	private String password;
	private Boolean admin;

	private Scanner user_input;

	private List<Event> events;
	private ArrayList<Group> groups;

	public void addEmployee(Employee e) {
		employees.add(e);
	}

	public void addRoom(Room room) {
		rooms.add(room);
	}

	public void addEvent(Event e) {
		events.add(e);
	}

	public List<Room> findLocation(Date startTime, Date endTime, int capacity) throws SQLException {
		List<Room> availableRooms = new ArrayList<Room>();
		List<Room> rooms1 = ctd.checkRoomEvents(con); //Denne trengs for � sjekke at room'ene faktisk er ledige
		for (Room room : rooms1) {
			// System.out.println(room.getName());
			// System.out.println("" + room.roomSchedule.size());
			if (room.isAvailable(startTime, endTime)
					&& room.getCapacity() >= capacity) {
				availableRooms.add(room);
			}
		}
		return availableRooms;
	}

	public void getEventInput(Employee employee) throws SQLException {
		// initalisering
		System.out.println("");
		String title = "";
		String description = "";
		Date startTime = new Date();
		Date endTime = new Date();

		// input begynnelse
		@SuppressWarnings("resource")
		Scanner user_input = new Scanner(System.in);
		System.out.println("Tittel: ");
		title = user_input.nextLine();
		System.out.println("Beskrivelse: ");
		description = user_input.nextLine();

		System.out.println("Starttidspunkt, f.eks.: 29/03/2015 12:00:00: ");
		String startTimeString = user_input.nextLine(); // formatet p� disse
														// m� vi ha orden p�
		System.out.println("Sluttidspunkt f.eks.: 29/03/2015 12:00:00: ");
		String endTimeString = user_input.nextLine();
		System.out.println("Kapasitet: ");
		int capacity = Integer.parseInt(user_input.nextLine());
		// input slutt
		// formatering av datogreier
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy H:m:s");

		try {
			startTime = formatter.parse(startTimeString);
			endTime = formatter.parse(endTimeString);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/*
		 * finner strste eventID. Hvis eventlisten er tom, settes den til 1. int
		 * eventID = 1; if(events.size() > 0 ){ eventID =
		 * events.get(events.size()-1).getEventID() + 1; }
		 */
		// oppretter event

		Event newEvent = current_user.createEvent(title, startTime, endTime,
				description);
		
		if (!(newEvent == null)){

		List<Room> availableRooms = new ArrayList<Room>();
		availableRooms = findLocation(startTime, endTime, capacity);

		for (int i = 0; i < availableRooms.size(); i++) {
			int nr = i + 1;
			System.out.println("" + nr + ". Navn: "
					+ availableRooms.get(i).getName() + " | Romspec: "
					+ availableRooms.get(i).getDescription() + " | Kapasitet: "
					+ availableRooms.get(i).getCapacity());
		}

		// bruker velger rom
		System.out.println("\nSkriv nummeret paa rommet du vil benytte deg av! \n\nTrykk enter hvis du ikke vil spesifisere et rom");
		String roomInput = user_input.nextLine();
		if (roomInput.equals("")) {
			System.out.println("Vil du spesifisere et sted eventet skal avholdes?");
			String response1 = user_input.nextLine();
			if (response1.equalsIgnoreCase("ja")) {
				System.out.println("Skriv inn navn p� stedet du vil avholde eventet: ");
				String sted = user_input.nextLine();
				newEvent.SetPlace(sted);
			}
			newEvent.setRoomToNull();
		} else {
			int valg = Integer.parseInt(roomInput);
			valg -= 1;
			newEvent.setRoom(availableRooms.get(valg));
			System.out.println("\nDu har valgt: "+ availableRooms.get(valg).getName() + "\n");
		}
		// print roomSchedulen til Room
		// System.out.println(newEvent.getRoom().getRoomSchedule().toString());

		// legge til deltakere. FLYTTER DENNE TIL EGEN METODE EGET STED
		List<Employee> availableEmployees = getAvailableEmployees(startTime, endTime);
		for (int i = 0; i < getAvailableEmployees(startTime, endTime).size(); i++) {
			System.out.println("" + i + ": " + availableEmployees.get(i));
		}

		System.out.println("Hvem vil du invitere til dette arrangementet[tom streng for aa avslutte]?");
		String input = user_input.nextLine();

		List<Employee> peopleInvited = new ArrayList<Employee>();

		int counter = 0;
		
		while (isInteger(input, 10)){
			
			if (newEvent.getRoom() == null){
				
						if (current_user.inviteEmployeeToEvent(availableEmployees.get(Integer.parseInt(input)), newEvent)) {
							peopleInvited.add(availableEmployees.get(Integer.parseInt(input)));
							ctd.WriteMessageToDatabase(con,availableEmployees.get(Integer.parseInt(input)));
							
							counter += 1;
							if (counter == capacity) {
								System.out.println("Antall inviterte er naa lik oppgitt kapasitet!");
							}
						} else {
							System.out.println("Personen er allerede invitert til dette arrangementet.");
						}
						System.out.println("Noen flere[tom streng for aa avslutte]?");
						input = user_input.nextLine();
					}else{
						if (current_user.inviteEmployeeToEvent(
								availableEmployees.get(Integer.parseInt(input)), newEvent)) {
							peopleInvited.add(availableEmployees.get(Integer.parseInt(input)));
							ctd.WriteMessageToDatabase(con, availableEmployees.get(Integer.parseInt(input)));
							
							counter += 1;
							if (counter < newEvent.getRoom().getCapacity()) {
								System.out.println("Noen flere[tom streng for aa avslutte]?");
								input = user_input.nextLine();
							}
							else{
								System.out.println("Rommet har ikke plass til flere deltakere!");
							}
						} else {
							System.out
							.println("Personen er allerede invitert til dette arrangementet.");
						}
			}
						
		}

					user_input.close();
					peopleInvited.add(current_user);
					newEvent.setEventID(events); //setter eventID til �n st�rre enn den st�rste
					ctd.WriteEventToDatabase(con, newEvent); //skriver event til database
					ctd.WriteEventDeltakelseToDatabase(con, newEvent, peopleInvited); // skriver eventdeltakelse til database
					ctd.updateEventDeltakelsesStatus(con, newEvent, current_user, "a");
					//fortsetter i run
		}
	}

	private List<Employee> getAvailableEmployees(Date startTime, Date endTime) {
		List<Employee> availableEmployees = new ArrayList<Employee>();
		for (Employee employee : employees) {
			if (employee.isAvailable(startTime, endTime)) {
				availableEmployees.add(employee);
			}
		}
		return availableEmployees;
	}

	public static void main(String[] args) throws SQLException {
		CalendarProgram cp = new CalendarProgram();
		cp.initialize();
		cp.run();
		// cp.init2();
		// cp.run2();
	}

	public static void connection() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	private void initialize() throws SQLException {
		ctd = new ConnectionToDatabase();
		connection();
		String host = "jdbc:mysql://mysql.stud.ntnu.no:3306/fredrwit_kalender";
		String username = "fredrwit_admin";
		String password = "12345";
		System.out.println("Connecting to database...");
		System.out.println("");
		
		try {
			con = DriverManager.getConnection(host, username, password);

			System.out.println("Connection to database successful!\n");

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// Handle errors for Class.forName
			e.printStackTrace();
		} finally {
			// finally block used to close resources
			/*
			 * try{ if(stmt1 != null) con.close(); }catch(SQLException se){ }//
			 * do nothing
			 */
			current_user = null;
			System.out.println("Fetching database tables...");
			System.out.println("");
			
			employees = (ArrayList<Employee>) ctd.SporringEmployees(con, "SELECT * FROM Employee");
			
			rooms = (ArrayList<Room>) ctd.sporringRooms(con, "SELECT * FROM Room");
			events = (ArrayList<Event>) ctd.sporringEvents(con, "SELECT * FROM Event", employees);
			groups = (ArrayList<Group>) ctd.SporringGroups(con, "SELECT * FROM Gruppe");
			ListContainer lc = ctd.sporringParticipations(con, "SELECT * FROM Eventdeltakelse", employees, events);
			employees = lc.getEmployees(); // hvorfor hente inn employees over for s� � endre listen til lc.getEmployees??
			events = lc.getEvents(); //samme med events??
			//Sender alle messagene til employeenes innbokser, saa trenger ikke ta vare pa messages utover dette
			ctd.sporringMessages(con, "SELECT * FROM Message"); //denne metoden skal returnere en List<Message> messages, hvor lagres denne? er det n�dvendig � returnere noe i metoden?
			
			System.out.println("Fetching completed.\n\n");
			try{
				if(con == null){
					con.close();					
				}
			} catch (SQLException se) {
				se.printStackTrace();
			}// end finally try

		}// end try
	}

	public Employee login() throws SQLException{
		System.out.println("Velkommen. Vennligst logg inn.");
	//	String sporring = "SELECT * FROM Employee"; //naa blir dette allerede gjort i initialize
	//	this.employees = ctd.SporringEmployees(con, sporring);
		
		
		
		user_input = new Scanner(System.in);
		username = "";
		password = null;

		while (current_user == null) {
			System.out.println("Brukernavn: ");
			username = user_input.nextLine();
			System.out.println("Passord: ");
			password = user_input.nextLine();

			for (Employee employee : employees) {
				if (employee.getUsername().equalsIgnoreCase(username)
						&& employee.getPassword().equals(password)) {
					current_user = employee;
					break;
				}
			}

			if (current_user == null) {
				System.out.println("Feil brukernavn/passord. Proev igjen");
			}
		}
		return current_user;

	}

	private void createNewUser() throws SQLException {
		System.out.println("Fyll inn feltene til den nye brukeren!\n");
		// String sporring = "SELECT * FROM Employee";
		// employees = ctd.SporringEmployees(con, sporring);
		// int employeeID = employees.get(employees.size()-1).getEmployeeID() +
		// 1;
		username = "";
		while (username == null || username.equals("")) {
			System.out.println("Oensket brukernavn: ");
			username = user_input.nextLine();
			if (employees.size() > 0) {
				for (Employee emp : employees) {
					if (emp.getUsername().equals(username)) {
						username = null;
						System.out.println("Brukernavn er opptatt.");
						break;
					}
				}
			}
		}

		System.out.println("Oensket passord:");
		password = user_input.nextLine();

		System.out.println("Ditt navn:");
		String name = user_input.nextLine();
		System.out.println("Stilling:");
		String position = user_input.nextLine();
		System.out.println("Telefonnummer:");
		String telnum = user_input.nextLine();
		int tlf = Integer.parseInt(telnum);
		System.out.println("Make admin? (true/false)");
		admin = Boolean.parseBoolean(user_input.nextLine());

		Employee employee = new Employee(getGreatestEmployeeID(), name,
				position, username, password, tlf, admin);
		employees.add(employee);

		ctd.NewEmployee(con, employee);

		System.out.println("" + employee.getName()
				+ ", er naa lagt til i databasen!\n");
	}

	private String getGreatestEmployeeID() {
		int greatestID = 0;
		for (Employee employee : employees) {
			if (employee.getEmployeeID() > greatestID) {
				greatestID = employee.getEmployeeID();
			}
		}
		return greatestID + "";
	}

	private void run() throws SQLException {
		/*
		System.out.println("Bare grupperom 1 blir hentet ned!");
		for (Event event : events){
			System.out.println(event + " har lokasjon " + event.getRoom());
			System.out.println(event.getPeopleGoing());
		}
		*/
		current_user = login();
		
		
		System.out.println("\nDu er naa logget inn. Trykk '9' for aa logge ut\n");
		System.out.println("Hei, " + current_user.getName() + "!");

		System.out.println("Du har " + current_user.countUnreadMessages()
				+ " uleste meldinger i innboksen din\n");
		
		while (current_user != null) {
			System.out.println("Hva vil du gjoere?\n");
			if(current_user.isAdmin() == true){
				System.out.println("1: Se alle upcoming events[goingTo] | 2: Legg til nytt event | 3: Apne innboks (" + current_user.countUnreadMessages() + ") | 4: Administrer dine events | 5: Administrer brukere | 6: Svar paa invitasjon | 9: quit");				
			} 
			else{
				System.out.println("1: Se alle upcoming events[goingTo] | 2: Legg til nytt event | 3: Apne innboks (" + current_user.countUnreadMessages() + ") | 4: Administrer dine events | 6: Svar paa invitasjon | 9: quit");
			}

			int option = 0;

			while (option == 0) {
				option = Integer.parseInt(user_input.nextLine());
				if (option == 1) {
					int weekOfYear = 1;
					int year = 2015;
					String firstOptionChoice = "";

					while (!firstOptionChoice.equalsIgnoreCase("q")) {
						if (firstOptionChoice.equals("")|| firstOptionChoice.equals("x")) {
							System.out.println("Skriv inn ukenummer: ");
							weekOfYear = Integer.parseInt(user_input.nextLine());
							System.out.println("Skriv inn year: ");
							year = Integer.parseInt(user_input.nextLine());
						}

						current_user.printWeeklySchedule(weekOfYear, year);

						System.out.println("\nDu har naa folgende valg:");
						System.out
								.println("f: forrige uke | n: neste uke | q: tilbake til hovedmeny");
						firstOptionChoice = user_input.nextLine();

						if (firstOptionChoice.equalsIgnoreCase("f")) {

							if (weekOfYear != 1) {
								current_user.generateWeeklySchedule(weekOfYear--, year);		// hvorfor ikke printWeeklySchedule?
							} else {
								// Kode for å finne antall uker i forrige year.
								// Skriver java.util siden vi har en klasse som heter Calendar
								java.util.Calendar cal = java.util.Calendar.getInstance();
								cal.set(java.util.Calendar.YEAR, year - 1);
								cal.set(java.util.Calendar.MONTH, java.util.Calendar.DECEMBER);
								cal.set(java.util.Calendar.DAY_OF_MONTH, 31);

								int ordinalDay = cal
										.get(java.util.Calendar.DAY_OF_YEAR);
								int weekDay = cal
										.get(java.util.Calendar.DAY_OF_WEEK) - 1; // Sunday
																					// =
																					// 0
								int numberOfWeeks = (ordinalDay - weekDay + 10) / 7;
					//			current_user.generateWeeklySchedule(numberOfWeeks, year--);		// hvorfor ikke printWeeklySchedule?
								year--;
								weekOfYear = numberOfWeeks;
							}
						} else if (firstOptionChoice.equalsIgnoreCase("n")) {
							// Kode for å finne antall uker i forrige year.
							// Skriver java.util siden vi har en klasse som
							// heter Calendar
							java.util.Calendar cal = java.util.Calendar.getInstance();
							cal.set(java.util.Calendar.YEAR, year);
							cal.set(java.util.Calendar.MONTH, java.util.Calendar.DECEMBER);
							cal.set(java.util.Calendar.DAY_OF_MONTH, 31);

							int ordinalDay = cal.get(java.util.Calendar.DAY_OF_YEAR);
							int weekDay = cal.get(java.util.Calendar.DAY_OF_WEEK) - 1; // Sunday
																				// =
																				// 0
							int numberOfWeeks = (ordinalDay - weekDay + 10) / 7;

							if (weekOfYear != numberOfWeeks) {
					//			current_user.generateWeeklySchedule(weekOfYear++, year);		// hvorfor ikke printWeeklySchedule?
								weekOfYear++;
							} else {
					//			current_user.generateWeeklySchedule(1, year++);			// hvorfor ikke printWeeklySchedule?
								year++;
								weekOfYear = 1;
							}
						} else if (firstOptionChoice.equalsIgnoreCase("q")) {
							break;
						}
					}
				} else if (option == 2) { //funker
					getEventInput(current_user);// eventet blir opprettet gjennom kall current_user.createEvent(), her blir b�de event og eventdeltakelse skrevet til databasen

				} else if (option == 3) {
					if (current_user.getInbox().size() > 0) {
						current_user.printInbox();
						while(option != -1){
							System.out.println("Hvilken melding vil du aapne? [-1 for quit]");
							option = Integer.parseInt(user_input.nextLine());
							if(option == -1){
								break;
							}
							System.out.println(current_user.getInbox().get(option).readMessage());
							ctd.UpdateMessage(con, current_user.getInbox().get(option));
							System.out.println("\nVil du aapne flere meldinger?");
							current_user.printInbox();
						}

					} else {
						System.out.println("\nIngen meldinger aa vise\n");
					}
				} else if (option == 4) {
					List<Event> myEvents = new ArrayList<Event>();
					for (int i = 0; i < events.size(); i++) {
						if (events.get(i).getCreator() == current_user) {
							myEvents.add(events.get(i));
							System.out.println(i + ": " + events.get(i));
						}
					}
					if (myEvents.size() == 0) {
						System.out.println("\nDu har ikke opprettet noen events enda.\n");
					} else {
						String firstOptionChoice = "";
						String secondOptionChoice = "";
						String thirdOptionChoice = "";
						String fourthOptionChoice = "";			// endret her tester. var inni lokka for
						System.out.println("Hvilken av disse vil du endre?");
						firstOptionChoice = user_input.nextLine();
						while (!firstOptionChoice.equals("q")) {
							Event chosen_event = events.get(Integer.parseInt(firstOptionChoice));
							if(secondOptionChoice.equals("q")){
								break;
							}
							while (!secondOptionChoice.equals("q")) {
								System.out.println("Hva vil du gjore? ['q' for aa quite]");
								System.out.println("1: se detaljer (peopleGoing, peopleDeclined og peopleInvited; lokasjon) | 2: endre event)");
								secondOptionChoice = user_input.nextLine();
								if (secondOptionChoice.equals("1")) {
									System.out.println("Du ser naa paa " + chosen_event + ".");
									System.out.println("Dette arrangementet har folgende deltakerstatus: ");
									System.out.println("peopleInvited: "+ chosen_event.getPeopleInvited());
									System.out.println("peopleGoing: "	+ chosen_event.getPeopleGoing());
									System.out.println("peopleDeclind: " + chosen_event.getPeopleDeclined());
									if (chosen_event.getRoom() != null){
										System.out.println("Rom: " + chosen_event.getRoom().getName());
									}
									else{
										System.out.println("Sted: " + chosen_event.getPlace());
									}
								} else if (secondOptionChoice.equals("2")) {
									System.out.println("Hva vil du endre?");
									System.out.println("1: avlys event | 2: trekk invitasjon | 3: inviter deltakere | 4: endre rom | 5: endring av tidspunkt");
									thirdOptionChoice = user_input.nextLine();
									if (thirdOptionChoice.equals("1")) {
										System.out.println("Hva er grunnen til avlysningen?");
										String reason = user_input.nextLine();
										//db-metode for aa slette event, fredrik
										ctd.deleteEvent(con, chosen_event);
										current_user.cancelEvent(chosen_event, reason);
										
										System.out.println("Eventen er slettet.");
										break;
									} else if (thirdOptionChoice.equals("2")) {
										System.out.println("Hvem vil du trekke invitasjonen til?");
										for (int i = 0; i < chosen_event.getPeopleInvited().size(); i++) {
											System.out.println(i+ ""+ chosen_event.getPeopleInvited().get(i));
										}

										fourthOptionChoice = user_input.nextLine();
										Employee empRemove = events.get(Integer.parseInt(firstOptionChoice)).getPeopleInvited().get(Integer.parseInt(fourthOptionChoice));
										current_user.withdrawInvitation(events.get(Integer.parseInt(firstOptionChoice)).getPeopleInvited().get(Integer.parseInt(fourthOptionChoice)),events.get(Integer.parseInt(firstOptionChoice)));
										ctd.updateEventDeltakelsesStatus(con, chosen_event, empRemove);
										//fjern eventdeltakelse(employee, event) fra databasen, Fredrik // DETTE ER ORDNET
										
									} else if (thirdOptionChoice.equals("3")) {
										// legge til deltakere.
										Scanner user_input = new Scanner(System.in);
										List<Employee> availableEmployees = getAvailableEmployees(chosen_event.getStartTime(),chosen_event.getEndTime());
										for (int i = 0; i < getAvailableEmployees(chosen_event.getStartTime(), chosen_event.getEndTime()).size(); i++) {
											System.out.println(""+ i + ": "	+ availableEmployees.get(i));
										}

										System.out.println("Hvem vil du invitere til dette arrangementet[tom streng for aa avslutte]?");
										String input = user_input.nextLine();

										List<Employee> peopleInvited = new ArrayList<Employee>();

										int num_people = chosen_event.getPeopleGoing().size() + chosen_event.getPeopleInvited().size(); 
										while (isInteger(input, 10)){	//  && (num_people < chosen_event.getRoom().getCapacity() || !chosen_event.getPlace().isEmpty())) { // dette kan bli problematisk hvis chosen_event.getRoom() == null
											if (current_user.inviteEmployeeToEvent(availableEmployees.get(Integer.parseInt(input)),chosen_event)) {
												peopleInvited.add(availableEmployees.get(Integer.parseInt(input)));
												ctd.WriteMessageToDatabase(con,availableEmployees.get(Integer.parseInt(input)));

												num_people += 1;
												if (num_people == chosen_event.getCapacity()) {
													System.out.println("Du har naa invitert ");
												}
											} else {
												System.out.println("Personen er allerede invitert til dette arrangementet.");
											}
											System.out.println("Noen flere[tom streng for aa avslutte]?");
											input = user_input.nextLine();
										}
										ctd.WriteEventDeltakelseToDatabase(con,chosen_event, peopleInvited);
										user_input.close();
									} else if (thirdOptionChoice.equals("4")) {
										System.out.println("\nEventet: " + chosen_event.getTitle() + ", er satt til aa avholdes paa: ");
										if(chosen_event.getPlace() == null){
								//			System.out.println("printer rom");	// skal vekk
											System.out.println(chosen_event.getRoom().getName());

										}
										else{
								//			System.out.println("printer place");	// skal vekk
											System.out.println(chosen_event.getPlace() + "\n");
										}
										//databasefix for aa endre rom
										List<Room> availableRoomsChange = new ArrayList<Room>();
										availableRoomsChange = findLocation(chosen_event.getStartTime(), chosen_event.getEndTime(), chosen_event.getCapacity());

										for (int i = 0; i < availableRoomsChange.size(); i++) {
											int nr = i + 1;
											System.out.println("" + nr + ". Navn: "
													+ availableRoomsChange.get(i).getName() + " | Romspec: "
													+ availableRoomsChange.get(i).getDescription() + " | Kapasitet: "
													+ availableRoomsChange.get(i).getCapacity());
										}
											System.out.println("Velg nytt rom eller press enter for aa skrive inn egendefinert sted");
											String 	changeRoomInput = user_input.nextLine();
											if(changeRoomInput.isEmpty()){
												System.out.println("Skriv inn navn paa stedet du �nsker � arrangere eventet: ");
												String place1 = user_input.nextLine();
												chosen_event.getRoom().removeEvent(chosen_event);
												chosen_event.setRoomToNull();
												chosen_event.SetPlace(place1);
											}
											else{
												if(chosen_event.getRoom() != null){
													chosen_event.getRoom().removeEvent(chosen_event);
												}
												int chosenInput = Integer.parseInt(changeRoomInput);
												chosen_event.SetPlace(null);
												chosen_event.setRoom(availableRoomsChange.get(chosenInput-1));	
												availableRoomsChange.get(chosenInput-1).addEventToRoom(chosen_event);
											}
											
											ctd.UpdateEventRoom(con, chosen_event);
											System.out.println("\nStedet hvor eventet avholdes er naa endret\n");
											
									} else if (thirdOptionChoice.equals("5")) {
										//databasefix for aa endre tidspunkt
										Date newStartTime = new Date();
										Date newEndTime = new Date();
										System.out.println("Eventet: " + chosen_event.getTitle() + ", har folgende start-tidspunkt: " + chosen_event.getStartTime().toString() + ", og slutter: " + chosen_event.getEndTime().toString());
										
										System.out.println("\nSkriv inn ny startTime [dd/mm/yyyy HH:MM:SS]");
										String startTimeString = user_input.nextLine();
										System.out.println("\nSkriv inn ny endTime [dd/mm/yyyy HH:MM:SS]");
										String endTimeString = user_input.nextLine();
										
										SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy H:m:s");

										try {
											newStartTime = formatter.parse(startTimeString);
											newEndTime = formatter.parse(endTimeString);
										} catch (ParseException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
										
											chosen_event.setTime(newStartTime, newEndTime);										}
										
									}
								}
							}
						}

				} else if (option == 5 && current_user.isAdmin()) {
					int choice = 0;

					while (choice == 0) {
						Boolean exists = true;
						int brukerChoice = 0;
						System.out.println("1. Administrer bruker | 2. Legg til ny bruker | 3. Tilbake til hovedmeny");
						choice = Integer.parseInt(user_input.nextLine());
						if (choice == 1) {
							System.out.println("Skriv inn brukernavn til brukeren du oensker aa endre:");
							String userName = (user_input.nextLine());
							if (ctd.checkUserName(con, userName) == true) {
								Employee tempEmployee = null;
								for (Employee employee : employees) {
									if (employee.getUsername().equalsIgnoreCase(userName)) {
										tempEmployee = employee;
									}
								}

								while (brukerChoice == 0) {
									System.out.println("1. Endre tlf | 2. Endre position | 3. Endre admin-rettigheter | 4. Slett bruker | 5. Exit");
									brukerChoice = Integer.parseInt(user_input.nextLine());
									if (brukerChoice == 1) {
										System.out.println("Nytt tlfnr:");
										int tlf = Integer.parseInt(user_input.nextLine());
										String s = "UPDATE Employee SET telnum = ? WHERE username = ?";
										ctd.updateEmployeeTelnum(con, s, tlf,tempEmployee);
										tempEmployee.setTelnum(tlf);
										System.out.println("Oppdateringen var vellykket\n");
									} else if (brukerChoice == 2) {
										System.out.println("Ny position:");
										String pos = user_input.nextLine();
										String s = "UPDATE Employee SET position = ? WHERE username = ?";
										ctd.updateEmployeePos(con, s, pos,tempEmployee);
										tempEmployee.setPosition(pos);
										System.out.println("Oppdateringen var vellykket\n");
									} else if (brukerChoice == 3) {
										if (tempEmployee.isAdmin() == true) {
											System.out.println(tempEmployee.getName() + " har admin-rettigheter, vil du fjerne disse?\n\n");
											if (user_input.nextLine().equalsIgnoreCase("ja")) {
												String sql = "UPDATE Employee SET admin = ? WHERE username = ?";
												String adm = "false";
												ctd.updateEmployeeAdmin(con, sql, adm, tempEmployee);
												tempEmployee.setAdmin(false);
												System.out.println("Oppdateringen var vellykket\n\n");
											} else {
												System.out.println("\nIngen endringer ble utf�rt\n");
											}
										} else if (tempEmployee.isAdmin() == false) {
											System.out.println(tempEmployee.getName()+ " har ikke admin-rettigheter, vil du gi han admin-rettigheter?\n\n");
											if (user_input.nextLine().equalsIgnoreCase("ja")) {
												String sql = "UPDATE Employee SET admin = ? WHERE username = ?";
												String adm = "true";
												ctd.updateEmployeeAdmin(con, sql, adm, tempEmployee);
												tempEmployee.setAdmin(true);
												System.out.println("Oppdateringen var vellykket\n\n");
											} else {
												System.out.println("\nIngen endringer ble utf�rt\n");
											}
										}
									} else if (brukerChoice == 4) {

										System.out.println("\nSikker paa at du vil slette: '"+ tempEmployee.getName()+ "', fra databasen?");
										if (user_input.nextLine().equalsIgnoreCase("ja")) {
											String sql = "DELETE FROM Employee WHERE username = ?";
											try {
												ctd.deleteUser(con, sql,tempEmployee);
											} catch (SQLException se) {
												se.printStackTrace();
											}
											System.out.println("Oppdateringen var vellykket\n"+ tempEmployee.getName()+ ", ble slettet fra databasen!\n");
											employees.remove(tempEmployee);
											exists = false;
										} else {
											System.out.println("\n"+ tempEmployee.getName()+ ", ble ikke slettet fra databasen\n");
										}
									}
									if (exists == true) {
										System.out.println("1. Rediger, "+ tempEmployee
																.getName()
														+ ", ytterligere | 2. Administrer ny bruker | 3. Tilbake til hovedmeny");
										String checkOption = user_input
												.nextLine();
										if (Integer.parseInt(checkOption) == 1) {
											brukerChoice = 0;
										} else if (Integer
												.parseInt(checkOption) == 2) {
											choice = 0;
										}
									}
								}
							} else {
								System.out.println("Brukernavnet: '"+ userName+ "', eksisterer ikke i databasen.\nVennligst skriv inn et gyldig brukernavn!\n");
								choice = 0;
							}
						} else if (choice == 2) {
							createNewUser();
						}
					}
				}else if (option == 6) {
					System.out.println("Hva onsker du aa gjore?");
					System.out.println("1: Svare paa invitasjon | 2: kansellere deltakelse");
					String initOptionChoice = user_input.nextLine();
					if(initOptionChoice.equals("1")){
						if(current_user.getUpcomingEvents().size() != 0){
							System.out.println("");
							for (int i = 0; i < current_user.getUpcomingEvents().size(); i++) {
								System.out.println("" + i + ": " + current_user.getUpcomingEvents().get(i));
							}
							System.out.println("Hvilken event vil du svare paa? [-1 for aa quite]");
							String firstOptionChoice = user_input.nextLine();
							String secondOptionChoice = "";
							if(! firstOptionChoice.equals("-1") && Integer.parseInt(firstOptionChoice) < current_user.getUpcomingEvents().size()){
								System.out.println("Vil du delta paa denne eventen? ['ja'/'nei']");
								secondOptionChoice = user_input.nextLine();
								if(secondOptionChoice.equalsIgnoreCase("ja")){
									Event e = current_user.getUpcomingEvents().get(Integer.parseInt(firstOptionChoice));
									current_user.acceptInvitation(e);
									ctd.updateEventDeltakelsesStatus(con, e, current_user);
									System.out.println("\nInvitasjonen er akseptert");
								}else{
									Event e = current_user.getUpcomingEvents().get(Integer.parseInt(firstOptionChoice));
									current_user.declineInvitation(e);
									ctd.updateEventDeltakelsesStatus(con, e, current_user);
									System.out.println("\nInvitasjonen er avslått");
								}
								System.out.println("\n");
							}
						} else{
							System.out.println("Du har ingen upcomingEvents\n");
						}
					} else if(initOptionChoice.equals("2")){
						if(current_user.getEventsAttending().size() > 0){
							System.out.println("");
							for (int i = 0; i < current_user.getEventsAttending().size(); i++) {
								System.out.println("" + i + ": " + current_user.getEventsAttending().get(i));
							}
							System.out.println("\nHvilken event onsker du aa kansellere?");
							String firstOptionChoice = user_input.nextLine();
							Event e = current_user.getEventsAttending().get(Integer.parseInt(firstOptionChoice));
							current_user.changeYourMind(e);
							ctd.updateEventDeltakelsesStatus(con, e, current_user, "d");
							System.out.println("\n" + e + " kansellert.\n");
						}else{
							System.out.println("Du har ikke akseptert noen invitasjoner enda");
						}
						
					}
					
				} else if (option == 9) {
					current_user = null;
					System.out.println("Du er naa logget ut.\n\n");

					// metode for aa skrive tilbake til server mangler her
					main(null);
				}
			}
		}
	}

	private void init2() {

		Room r1 = new Room(1, "R1", 500, "Fint rom1");
		Room r2 = new Room(2, "R2", 400, "Fint rom2");
		Room r3 = new Room(3, "R3", 300, "Fint rom3");
		Room r4 = new Room(4, "R4", 200, "Fint rom4");
		Room r5 = new Room(5, "R5", 100, "Fint rom5");
		Room r6 = new Room(6, "R6", 50, "Fint rom6");
		Room r7 = new Room(7, "R7", 60, "Fint rom7");
		rooms = new ArrayList<Room>();
		addRoom(r1);
		addRoom(r2);
		addRoom(r3);
		addRoom(r4);
		addRoom(r5);
		addRoom(r6);
		addRoom(r7);

	}

	public void run2() {
		Date dato1 = new Date(115, 2, 19, 19, 0, 0);
		Date dato2 = new Date(115, 2, 19, 21, 0, 0);
		Date dato3 = new Date(116, 2, 19, 18, 30, 0);
		Date dato4 = new Date(116, 2, 19, 20, 30, 0);
		Date dato5 = new Date(116, 3, 18, 20, 30, 0);
		Date dato6 = new Date(116, 3, 19, 21, 30, 0);
		Date dato7 = new Date(116, 3, 19, 19, 30, 0);
		Date dato8 = new Date(116, 3, 19, 21, 00, 0);

		Employee biti = new Employee(1, "Bendik", "Junior", "biti", "bata",
				123, false);
		Employee sverre = new Employee(2, "Sverre", "Senior", "sverrak",
				"heiia", 45884408, false);
		Employee yolo = new Employee(3, "Jola", "Junior+", "bata", "biti", 123,
				false);
		current_user = null;

		employees = new ArrayList<Employee>();
		addEmployee(biti);
		addEmployee(sverre);
		addEmployee(yolo);

		// Event birthday = biti.createEvent("Bursdag", dato1, dato2,
		// "halla paarae");
		// Event birthdayAgain = biti.createEvent("Bursdag igjen", dato3, dato4,
		// "halla paasan");

		/*
		 * Event party = biti.createEvent("party", dato5, dato6,
		 * "kom paa party!"); Event party2 = biti.createEvent("partyOnSameDay",
		 * dato7, dato8, "kom paa party!"); //disse to skal kollidere. Ber ikke
		 * om feilmelding
		 */

		Event sverresEvent = sverre.createEvent("sverresEvent", dato1, dato2,
				"eventet til sverre som kolliderer med bursdag");
		sverre.inviteEmployeeToEvent(biti, sverresEvent);
		System.out.println("Bendiks events: " + "\n- Events invited to: "
				+ biti.getUpcomingEvents() + "\n- Events attending: "
				+ biti.getEventsAttending());

		/*
		 * System.out.println(birthday.getPeopleInvited());
		 * biti.inviteEmployeeToEvent(sverre, birthday);
		 * System.out.println("Invitasjon gitt: " +
		 * birthday.getPeopleInvited()); sverre.declineInvitation(birthday);
		 * System.out.println("Etter Sverre decliner: ");
		 * System.out.println("sverres upcoming: " + sverre.getUpcomingEvents()
		 * + "sverres attending: " + sverre.getEventsAttending());
		 * System.out.println("Invitert til bursdag: " +
		 * birthday.getPeopleInvited()); System.out.println("Going to bursdag: "
		 * + birthday.getPeopleGoing());
		 * 
		 * biti.inviteEmployeeToEvent(sverre, birthdayAgain);
		 * 
		 * System.out.println(sverre.getInbox().get(0));
		 * 
		 * 
		 * System.out.println(birthdayAgain.getPeopleGoing() + "" +
		 * birthdayAgain.getPeopleInvited());
		 * System.out.println(biti.getUpcomingEvents());
		 * 
		 * System.out.println(); System.out.println();
		 * biti.printWeeklySchedule(12, 2015);
		 */
		/*
		 * sverre.cancelEvent(sverresEvent, "dette er lame event uansett");
		 * biti.cancelEvent(birthday, "vet ikike. Er spontan");
		 * System.out.println("Bendiks events: " + "\n- Events invited to: " +
		 * biti.getUpcomingEvents() + "\n- Events attending: " +
		 * biti.getEventsAttending()); biti.printWeeklySchedule(12, 2015);
		 * 
		 * System.out.println("Biti's declined events: " +
		 * biti.getDeclinedEvents());
		 */
		Group gruppe51 = biti.createGroup("gruppe51", "den kegeste gjengen");
		biti.addEmployeeToGroup(sverre, gruppe51);
		biti.createGroupEvent("Scrum meeting", dato1, dato2,
				"gaar gjennom ukas oppgaver", gruppe51);
		biti.printGroupSchedule(gruppe51, 12, 2015);
	}

	public static boolean isInteger(String s) {
		return isInteger(s, 10);
	}

	public static boolean isInteger(String s, int radix) {
		if (s.isEmpty())
			return false;
		for (int i = 0; i < s.length(); i++) {
			if (i == 0 && s.charAt(i) == '-') {
				if (s.length() == 1)
					return false;
				else
					continue;
			}
			if (Character.digit(s.charAt(i), radix) < 0)
				return false;
		}
		return true;
	}
}
