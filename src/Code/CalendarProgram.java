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
	private	List<Employee> employees;
	private Employee current_user;
	
	private Employee biti;
	private Employee sverre;
	private Employee yolo;
	
	//login-felter. Legger det til her siden de brukes i både createNewUser() og i login()
	private String username;
	private String password;
	private Boolean admin;
	
	private Scanner user_input;
	
	private List<Event> events;
	
	public void addEmployee(Employee e){
		employees.add(e);
	}
	public void addRoom(Room room){
		rooms.add(room);
	}
	public void addEvent(Event e){
		events.add(e);
	}
	
	public List<Room> findLocation(Date startTime, Date endTime, int capacity){
		List<Room> availableRooms = new ArrayList<Room>();
		for (Room room : rooms) {
	//		System.out.println(room.getName());
	//		System.out.println("" + room.roomSchedule.size());
			if(room.isAvailable(startTime, endTime) && room.getCapacity() >= capacity){
				availableRooms.add(room);
				System.out.println(room.getName() + ", was added to available rooms");
			}
		}
		return availableRooms;
	}

	public Event getEventInput(Employee employee) throws SQLException{
		//initalisering
		System.out.println("");
		String title = "";
		String description = "";
		Date startTime = new Date();
		Date endTime = new Date();
		
		//input begynnelse
		@SuppressWarnings("resource")
		Scanner user_input = new Scanner(System.in);
		System.out.println("Tittel: ");
		title = user_input.nextLine();
		System.out.println("Beskrivelse: ");
		description = user_input.nextLine();

		System.out.println("Starttidspunkt[16/03/2015 12:00:00]: ");
		String startTimeString = user_input.nextLine();			// formatet p� disse m� vi ha orden p�
		System.out.println("Sluttidspunkt[16/03/2015 12:00:00]: ");
		String endTimeString = user_input.nextLine();
		System.out.println("Kapasitet: ");
		int capacity = Integer.parseInt(user_input.nextLine());
		//input slutt
		//formatering av datogreier
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy H:m:s");
	 
		try {
			startTime = formatter.parse(startTimeString);
			endTime = formatter.parse(endTimeString);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/*finner strste eventID. Hvis eventlisten er tom, settes den til 1. 
		int eventID = 1;
		if(events.size() > 0 ){
			eventID = events.get(events.size()-1).getEventID() + 1;			
		}*/
		//oppretter event
		
		Event newEvent = new Event(title, startTime, endTime, description, employee);
		
		
		//finner ledige rom
		ctd.fetchRooms(con);
		this.rooms = ctd.checkRoomEvents(con);

		List<Room> availableRooms = new ArrayList<Room>();
		availableRooms = findLocation(startTime, endTime, capacity);
		String print = "";
		
		
		for (int i = 0; i < availableRooms.size(); i++) {
			print = i + ": ";
			print += availableRooms.get(i);
			System.out.println(print);
		}
		
		//bruker velger rom
		System.out.println("Skriv nummer på rommet du vil ha");
		if (user_input.nextLine().equals("")){
			
			newEvent.setRoom(null);
		}
		else {

			String input = user_input.nextLine();
			newEvent.setRoom(availableRooms.get(Integer.parseInt(input)));
		}
		//print roomSchedulen til Room
		//System.out.println(newEvent.getRoom().getRoomSchedule().toString());
		
		//legge til deltakere
		List<Employee> availableEmployees = getAvailableEmployees(startTime, endTime);
		for (int i = 0; i < getAvailableEmployees(startTime, endTime).size(); i++) {
			System.out.println("" + i + ": " + availableEmployees.get(i));
		}
		
		System.out.println("Hvem vil du invitere til dette arrangementet[tom streng for å avslutte]?");
		String input = user_input.nextLine();
		
		int counter = 1;
		while((! input.equals("")) && counter  < capacity){
			if(current_user.inviteEmployeeToEvent(availableEmployees.get(Integer.parseInt(input)), newEvent)){
				counter += 1;
			} else{
				System.out.println("Personen er allerede invitert til dette arrangementet.");
			}
			/*newEvent.addEmployee(availableEmployees.get(Integer.parseInt(input)));
			availableEmployees.get(Integer.parseInt(input)).addEvent(newEvent);*/
			System.out.println("Noen flere[tom streng for å avslutte]?");
			input = user_input.nextLine();
		}
		
		current_user.addEvent(newEvent);
		newEvent.addEmployee(current_user);
		
		
		
		return newEvent;
	}
	private List<Employee> getAvailableEmployees(Date startTime, Date endTime) {
		List<Employee> availableEmployees = new ArrayList<Employee>();
		for (Employee employee : employees) {
			if(employee.isAvailable(startTime, endTime)){
				availableEmployees.add(employee);
			}
		}
		return availableEmployees;
	}
	
	
	public static void main(String[] args) throws SQLException {
		CalendarProgram cp = new CalendarProgram();
		cp.initialize();
		cp.run();
	//	cp.init2();
	//	cp.run2();
	}
	
	public static void connection(){
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	
	private void initialize(){
		ctd = new ConnectionToDatabase();
		connection();
		String host = "jdbc:mysql://mysql.stud.ntnu.no:3306/fredrwit_kalender";
		String username = "fredrwit_admin";
		String password ="12345";
		System.out.println("Connecting to database...");
		System.out.println("");
		System.out.println("Fetching database tables...");
		System.out.println("");
		System.out.println("");
		try {
			con = DriverManager.getConnection(host, username, password);
			
			System.out.println("Connection to database successful!\n");
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch(Exception e){
		      //Handle errors for Class.forName
		      e.printStackTrace();
		} finally{
		      //finally block used to close resources
		 /*     try{
		         if(stmt1 != null)
		            con.close();
		      }catch(SQLException se){
		      }// do nothing */
			current_user = null;
			employees = new ArrayList<Employee>();
			events = new ArrayList<Event>();
			rooms = new ArrayList<Room>();
			try{
				if(con == null){
					con.close();					
				}
			}catch(SQLException se){
				se.printStackTrace();
				}//end finally try
			
		}//end try
	}
	
	public Employee login() throws SQLException{
		
		String sporring = "SELECT * FROM Employee";
		this.employees = ctd.SporringEmployees(con, sporring);
		
		user_input = new Scanner(System.in);
		username = "";
		password = null;
		
		while(current_user == null){
			System.out.println("Brukernavn: ");
			username = user_input.nextLine();
			System.out.println("Passord: ");
			password = user_input.nextLine();
			
			for (Employee employee : employees) {
				if(employee.getUsername().equalsIgnoreCase(username) && employee.getPassword().equals(password)){
					current_user = employee;
					break;
				}
			}
			
			if(current_user == null){
				System.out.println("Feil brukernavn/passord. Prøv igjen");
			} 
		}
		return current_user;
		
			
	}
	private void createNewUser() throws SQLException{
		System.out.println("Fyll inn feltene til den nye brukeren!\n");
		String sporring = "SELECT * FROM Employee";
		employees = ctd.SporringEmployees(con, sporring);
		int employeeID = employees.get(employees.size()-1).getEmployeeID() + 1;
		username = "";
		while(username == null || username.equals("")){
			System.out.println("Ønsket brukernavn: ");
			username = user_input.nextLine();
			if(employees.size() > 0){
				for (Employee emp : employees) {
					if(emp.getUsername().equals(username)){
						username = null;
						System.out.println("Brukernavn er opptatt.");
						break;
					}
				}					
			}
		}
		
		System.out.println("Ønsket passord:");
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

		
		Employee employee = new Employee(getGreatestEmployeeID(), name, position, username, password, tlf, admin);
		employees.add(employee);
		
		ctd.NewEmployee(con, employee);
		
		System.out.println("Du er nå lagt til i databasen");
	}
	
		
	private String getGreatestEmployeeID() {
		int greatestID = 0;
		for (Employee employee : employees) {
			if(employee.getEmployeeID() > greatestID){
				greatestID = employee.getEmployeeID();
			}
		}
		return greatestID + "";
	}
	
	private void run() throws SQLException {
		current_user = login();
		System.out.println("\nDu er nå logget inn. Skriv quit for å logge ut");
		System.out.println("Hei, " + current_user.getName() + "!");
		
		System.out.println("Du har " + current_user.countUnreadMessages() + " uleste meldinger i innboksen din\n");
		while(current_user != null){
			System.out.println("Hva vil du gjøre?");
			if(current_user.isAdmin() == true){
				System.out.println("1: Se alle upcoming events[goingTo] | 2: Legg til ny event | 3: Apne innboks | 4: Administrer dine events | 5: Administrer brukere | 9: quit");				
			} 
			else{
				System.out.println("1: Se alle upcoming events[goingTo] | 2: Legg til ny event | 3: Apne innboks | 4: Administrer dine events | 9: quit");
			}
			
			int option = 0;
			
			while(option < 1 || option > 9){
				option = Integer.parseInt(user_input.nextLine());
				if(option == 1){
					int weekOfYear = 1;
					int year = 2015;
					String firstOptionChoice = "";
				
					while(! firstOptionChoice.equalsIgnoreCase("q")){
						if (firstOptionChoice.equals("") || firstOptionChoice.equals("x")){
							System.out.println("Skriv inn ukenummer: ");
							weekOfYear = Integer.parseInt(user_input.nextLine());
							System.out.println("Skriv inn year: ");
							year = Integer.parseInt(user_input.nextLine());							
						}
						
						current_user.printWeeklySchedule(weekOfYear, year);
						
						System.out.println("\nDu har nå folgende valg:");
						System.out.println("f: forrige uke | n: neste uke | q: tilbake til hovedmeny");
						firstOptionChoice = user_input.nextLine();
						
					   
						if(firstOptionChoice.equalsIgnoreCase("f")){
						    
							if(weekOfYear != 1){
								current_user.generateWeeklySchedule(weekOfYear--, year);							
							} else{
								//Kode for å finne antall uker i forrige year. Skriver java.util siden vi har en klasse som heter Calendar
								java.util.Calendar cal = java.util.Calendar.getInstance();
								cal.set(java.util.Calendar.YEAR, year - 1);
								cal.set(java.util.Calendar.MONTH, java.util.Calendar.DECEMBER);
								cal.set(java.util.Calendar.DAY_OF_MONTH, 31);
								
								int ordinalDay = cal.get(java.util.Calendar.DAY_OF_YEAR);
								int weekDay = cal.get(java.util.Calendar.DAY_OF_WEEK) - 1; // Sunday = 0
								int numberOfWeeks = (ordinalDay - weekDay + 10) / 7;
								current_user.generateWeeklySchedule(numberOfWeeks, year--);
							}
						} else if(firstOptionChoice.equalsIgnoreCase("n")){
							//Kode for å finne antall uker i forrige year. Skriver java.util siden vi har en klasse som heter Calendar
							java.util.Calendar cal = java.util.Calendar.getInstance();
							cal.set(java.util.Calendar.YEAR, year);
							cal.set(java.util.Calendar.MONTH, java.util.Calendar.DECEMBER);
							cal.set(java.util.Calendar.DAY_OF_MONTH, 31);
							
							int ordinalDay = cal.get(java.util.Calendar.DAY_OF_YEAR);
							int weekDay = cal.get(java.util.Calendar.DAY_OF_WEEK) - 1; // Sunday = 0
							int numberOfWeeks = (ordinalDay - weekDay + 10) / 7;
							
							if(weekOfYear != numberOfWeeks){
								current_user.generateWeeklySchedule(weekOfYear++, year);							
							} else{
								current_user.generateWeeklySchedule(1, year++);
							}
						} else if(firstOptionChoice.equalsIgnoreCase("q")){
							break;
					}
					}
				} else if(option == 2){
					Event event = getEventInput(current_user);
					current_user.addEvent(event);
					
				
				} else if(option == 3){
					if(current_user.getInbox().size() > 0){
						current_user.printInbox();
						while(option != -1){
							System.out.println("Hvilken melding vil du åpne?");
							option = Integer.parseInt(user_input.nextLine());
							System.out.println(current_user.getInbox().get(option).toString());
							System.out.println("\nVil du åpne flere meldinger?");
							current_user.printInbox();						
						}
					
					} else{
						System.out.println("Ingen meldinger aa vise\n");
					}
				} else if(option == 4){
					List<Event> myEvents = new ArrayList<Event>();
					for (int i = 0; i < events.size(); i++) {
						if(events.get(i).getCreator() == current_user){
							myEvents.add(events.get(i));
							System.out.println(i + ": " + events.get(i));
						}
					}
					if(myEvents.size() == 0 ){
						System.out.println("\nDu har ikke opprettet noen events enda.\n");
					} else{
						String firstOptionChoice = "";
						String secondOptionChoice = "";
						String thirdOptionChoice = "";
						String fourthOptionChoice = "";
						while(! firstOptionChoice.equals("q")){
							System.out.println("Hvilken av disse vil du endre?");
							firstOptionChoice = user_input.nextLine();
							while(! secondOptionChoice.equals("q")){
								System.out.println("Hva vil du gjore?");
								System.out.println("1: se peopleGoing, peopleDeclined og peopleInvited | 2: endre event)");
								secondOptionChoice = user_input.nextLine();
								if(secondOptionChoice.equals("1")){
									System.out.println("Du ser nå på " + events.get(Integer.parseInt(firstOptionChoice)) + ".");
									System.out.println("Dette arrangementet har folgende deltakerstatus: ");
									System.out.println("peopleInvited: " + events.get(Integer.parseInt(firstOptionChoice)).getPeopleInvited());
									System.out.println("peopleGoing: " + events.get(Integer.parseInt(firstOptionChoice)).getPeopleGoing());
									System.out.println("peopleDeclind: " + events.get(Integer.parseInt(firstOptionChoice)).getPeopleDeclined());
								} else if(secondOptionChoice.equals("2")){									
									System.out.println("Hva vil du endre?");
									System.out.println("1: avlys event | 2: trekk invitasjon | 3: inviter deltakere | 4: endre rom | 4: annen endring");
									thirdOptionChoice = user_input.nextLine();
									if(thirdOptionChoice.equals("1")){
										System.out.println("Hva er grunnen til avlysningen?");
										String reason = user_input.nextLine();
										current_user.cancelEvent(events.get(Integer.parseInt(firstOptionChoice)), reason);
										System.out.println("Eventen er slettet.");
									} else if(thirdOptionChoice.equals("2")){
										System.out.println("Hvem vil du trekke invitasjonen til?");
										for (int i = 0; i < events.get(Integer.parseInt(firstOptionChoice)).getPeopleInvited().size(); i++) {
											System.out.println(i + "" + events.get(Integer.parseInt(firstOptionChoice)).getPeopleInvited().get(i));
										}
										
										fourthOptionChoice = user_input.nextLine();
										
										current_user.withdrawInvitation(events.get(Integer.parseInt(firstOptionChoice)).getPeopleInvited().get(Integer.parseInt(fourthOptionChoice)), events.get(Integer.parseInt(firstOptionChoice)));
										
									} else if(thirdOptionChoice.equals("3")){
										
									} else if(thirdOptionChoice.equals("4")){
										
									}
								}
								
							}
							
					}
					
					
					}	
				} else if(option == 5 && current_user.isAdmin()){
					int choice = 0;
					int brukerChoice = 0;
					while (choice < 1 || choice > 2){
						System.out.println("1. Administrer bruker | 2. Legg til ny bruker");
						choice = Integer.parseInt(user_input.nextLine());
						if(choice == 1){
							
							System.out.println("Skriv inn brukernavn til brukeren du �nsker � endre:");
							String userName = (user_input.nextLine());
							if(ctd.checkUserName(con, userName) == true){
								Employee tempEmployee = null;
								for (Employee employee : employees){
									if(employee.getUsername().equalsIgnoreCase(userName)){
										tempEmployee = employee;
									}
								}

								while(brukerChoice < 1 || brukerChoice > 3){
									System.out.println("1. Endre tlf | 2. Endre position | 3. Endre admin-rettigheter | 4. Slett bruker");
									brukerChoice = Integer.parseInt(user_input.nextLine());
									if (brukerChoice == 1){
										System.out.println("Nytt tlfnr:");
										int tlf = Integer.parseInt(user_input.nextLine());
										String s = "UPDATE Employee SET telnum = ? WHERE username = ?";
										ctd.updateEmployeeTelnum(con, s, tlf, tempEmployee);
										tempEmployee.setTelnum(tlf);
										System.out.println("Oppdateringen var vellykket\n");
									}
									else if (brukerChoice == 2){
										System.out.println("Ny position:");
										String pos = user_input.nextLine();
										String s = "UPDATE Employee SET position = ? WHERE username = ?";
										ctd.updateEmployeePos(con, s, pos, tempEmployee);
										tempEmployee.setPosition(pos);
										System.out.println("Oppdateringen var vellykket\n");
									}
									else if (brukerChoice == 3){
										if (tempEmployee.isAdmin() == true){
											System.out.println(tempEmployee.getName() + " har admin-rettigheter, vil du fjerne disse?\n\n");
											if (user_input.nextLine().equalsIgnoreCase("ja")){
												String sql = "UPDATE Employee SET admin = ? WHERE username = ?";
												String adm = "nei";
												ctd.updateEmployeeAdmin(con, sql, adm, tempEmployee);
												tempEmployee.setAdmin(false);
												System.out.println("Oppdateringen var vellykket\n\n");
											}
										}
										else if (tempEmployee.isAdmin() == false){
											System.out.println(tempEmployee.getName() + " har ikke admin-rettigheter, vil du gi han admin-rettigheter?\n\n");
											if (user_input.nextLine().equalsIgnoreCase("ja")){	
												String sql = "UPDATE Employee SET admin = ? WHERE username = ?";
												String adm = "ja";
												ctd.updateEmployeeAdmin(con, sql, adm, tempEmployee);
												tempEmployee.setAdmin(true);
												System.out.println("Oppdateringen var vellykket\n\n");
												
											}
										}
									else if (brukerChoice == 4){
										
										System.out.println("\nSikker p� at du vil slette: '" + tempEmployee.getName() + "', fra databasen?");
										if (user_input.nextLine().equalsIgnoreCase("ja")){
											String sql = "DELETE FROM Employee WHERE username = ?";
											ctd.deleteUser(con, sql, tempEmployee);
											System.out.println("Oppdateringen var vellykket\n\n");
										}
									}
									}
									System.out.println("1. Rediger, " + tempEmployee.getName() + ", ytterligere | 2. Administrer ny bruker | 3. Tilbake til hovedmeny");
									if (Integer.parseInt(user_input.nextLine()) == 1){
										brukerChoice = 0;
									}
									else if(Integer.parseInt(user_input.nextLine()) == 2){
										choice = 0;
									}
									else if(Integer.parseInt(user_input.nextLine()) == 3){
										option = 3;
									}
								}
							}
							else{
								System.out.println("Brukernavnet: '" + userName + "', eksisterer ikke i databasen.\nVennligst skriv inn et gyldig brukernavn!\n");
								choice = 0;
							}
						}
						else if(choice == 2){
							createNewUser();
						}
					}
				} else if(option == 9){
					current_user = null;
					System.out.println("Du er naa logget ut.\n\n");
					
					//metode for aa skrive tilbake til server mangler her
					main(null);
				}
					
				}
				
			}
		}
	private void init2() {
	
		Room r1 = new Room("R1", 500, "Fint rom1");
		Room r2 = new Room("R2", 400, "Fint rom2");
		Room r3 = new Room("R3", 300, "Fint rom3");
		Room r4 = new Room("R4", 200, "Fint rom4");
		Room r5 = new Room("R5", 100, "Fint rom5");
		Room r6 = new Room("R6", 50, "Fint rom6");
		Room r7 = new Room("R7", 60, "Fint rom7");
		rooms = new ArrayList<Room>();
		addRoom(r1);
		addRoom(r2);
		addRoom(r3);
		addRoom(r4);
		addRoom(r5);
		addRoom(r6);
		addRoom(r7);
		
		
	}
	
	public void run2(){
		Date dato1 = new Date(115, 2, 19, 19, 0, 0);
		Date dato2 = new Date(115, 2, 19, 21, 0, 0);
		Date dato3 = new Date(116, 2, 19, 18, 30, 0);
		Date dato4 = new Date(116, 2, 19, 20, 30, 0);
		Date dato5 = new Date(116, 3, 18, 20, 30, 0);
		Date dato6 = new Date(116, 3, 19, 21, 30, 0);
		Date dato7 = new Date(116, 3, 19, 19, 30, 0);
		Date dato8 = new Date(116, 3, 19, 21, 00, 0);
		
		Employee biti = new Employee(1, "Bendik", "Junior", "biti", "bata", 123, false);
		Employee sverre = new Employee(2, "Sverre", "Senior", "sverrak", "heiia", 45884408, false);
		Employee yolo = new Employee(3, "Jola", "Junior+", "bata", "biti", 123, false);
		current_user = null;
		
		employees = new ArrayList<Employee>();
		addEmployee(biti);
		addEmployee(sverre);
		addEmployee(yolo); 
		
		Event birthday = biti.createEvent("Bursdag", dato1, dato2, "halla paarae");
		Event birthdayAgain = biti.createEvent("Bursdag igjen", dato3, dato4, "halla paasan");
		
		/*
		Event party = biti.createEvent("party", dato5, dato6, "kom paa party!");
		Event party2 = biti.createEvent("partyOnSameDay", dato7, dato8, "kom paa party!");	//disse to skal kollidere. Ber ikke om feilmelding
		*/
		/*
		Event sverresEvent = sverre.createEvent("sverresEvent", dato1, dato2, "eventet til sverre som kolliderer med bursdag");
		sverre.inviteEmployeeToEvent(biti, sverresEvent);
		System.out.println("Bendiks events: " + "\n- Events invited to: " + biti.getUpcomingEvents() + "\n- Events attending: " + biti.getEventsAttending());
		*/
		
		System.out.println(birthday.getPeopleInvited());
		biti.inviteEmployeeToEvent(sverre, birthday);
		System.out.println("Invitasjon gitt: " + birthday.getPeopleInvited());
		sverre.declineInvitation(birthday);
		System.out.println("Etter Sverre decliner: ");
		System.out.println("sverres upcoming: " + sverre.getUpcomingEvents() + "sverres attending: " + sverre.getEventsAttending());
		System.out.println("Invitert til bursdag: " + birthday.getPeopleInvited());
		System.out.println("Going to bursdag: " + birthday.getPeopleGoing());
		
		biti.inviteEmployeeToEvent(sverre, birthdayAgain);
		
	/*	
		System.out.println(birthdayAgain.getPeopleGoing() + "" + birthdayAgain.getPeopleInvited());
		biti.cancelEvent(birthday, "Ingen ville komme :(");
		System.out.println(biti.getUpcomingEvents());
		
		System.out.println();
		System.out.println();
		biti.printWeeklySchedule(12, 2015);
		*/
	}

}
