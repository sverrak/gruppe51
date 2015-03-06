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

public class CalendarProgram {
	/*	private Employee biti;
	private Employee sverre;
	private Employee yolo; 
	private Room r1;
	private Room r2;
	private Room r3;
	private Room r4;
	private Room r5;
	private Room r6;
	private Room r7;*/
	private int employeeCounter;
	private Connection con = null;
	ConnectionToDatabase ctd;
	private Boolean admin = false;
	private List<Room> rooms;
	private	List<Employee> employees;
	private Employee current_user;
	
	//login-felter
	private String login_option;
	private String username;
	private String password;
	
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
			room.getDescription();
			if(room.isAvailable(startTime, endTime) && room.getCapacity() >= capacity){
				availableRooms.add(room);
			}
		}
		return availableRooms;
	}

	public Event getEventInput(Employee employee){
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
		
		//oppretter event
		Event newEvent = new Event(title, startTime, endTime, description, employee);
		
		//finner ledige rom
		List<Room> availableRooms = findLocation(startTime, endTime, capacity);
		String print = "";
		
		
		for (int i = 0; i < availableRooms.size(); i++) {
			print = i + ": ";
			print += availableRooms.get(i);
			System.out.println(print);
		}
		
		//bruker velger rom
		System.out.println("Skriv nummer på rommet du vil ha");
		String input = user_input.nextLine();
		newEvent.setRoom(availableRooms.get(Integer.parseInt(input)));
		
		//print roomSchedulen til Room
		//System.out.println(newEvent.getRoom().getRoomSchedule().toString());
		
		//legge til deltakere
		List<Employee> availableEmployees = getAvailableEmployees(startTime, endTime);
		for (int i = 0; i < getAvailableEmployees(startTime, endTime).size(); i++) {
			System.out.println("" + i + ": " + availableEmployees.get(i));
		}
		
		System.out.println("Hvem vil du invitere til dette arrangementet[tom streng for å avslutte]?");
		input = user_input.nextLine();
		
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
		      try{
		         if(con == null)
		            con.close();
		      }catch(SQLException se){
		         se.printStackTrace();
		      }//end finally try
		   }//end try
	}
	
	private void init() {
		r1 = new Room("R1", 500, "Fint rom1");
		r2 = new Room("R2", 400, "Fint rom2");
		r3 = new Room("R3", 300, "Fint rom3");
		r4 = new Room("R4", 200, "Fint rom4");
		r5 = new Room("R5", 100, "Fint rom5");
		r6 = new Room("R6", 50, "Fint rom6");
		r7 = new Room("R7", 60, "Fint rom7");
		rooms = new ArrayList<Room>();
		addRoom(r1);
		addRoom(r2);
		addRoom(r3);
		addRoom(r4);
		addRoom(r5);
		addRoom(r6);
		addRoom(r7);
		
		biti = new Employee("Bendik", "Junior", "biti", "bata", "123");
		sverre = new Employee("Sverre", "Senior", "sverrak", "heiia", "45884408");
		yolo = new Employee("Jola", "Junior+", "bata", "biti", "123");
		current_user = null;
		
		employees = new ArrayList<Employee>();
		addEmployee(biti);
		addEmployee(sverre);
		addEmployee(yolo);

		Date dato1 = new Date(115, 2, 19, 19, 0, 0);
		Date dato2 = new Date(115, 2, 19, 21, 0, 0);
		Date dato3 = new Date(116, 2, 19, 18, 30, 0);
		Date dato4 = new Date(116, 2, 19, 20, 30, 0);

		events = new ArrayList<Event>();
		Event birthday = new Event("Bursdag", dato1, dato2, "halla paarae", biti);
		Event birthdayAgain = new Event("Bursdag", dato3, dato4, "halla paasan", biti);
		biti.addEvent(birthday);
		biti.addEvent(birthdayAgain);
	}

	
	public Employee login() throws SQLException{
		
		String sporring = "SELECT * FROM Employee";
		employees = ctd.Sporring(con, sporring);
		
		user_input = new Scanner(System.in);
		username = "";
		System.out.println("Hei\n");
		
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
	
	public void createUser() throws SQLException{
	
		employeeCounter = employees.size();
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
			System.out.println("Make admin? (yes/no)");
			admin = Boolean.parseBoolean(user_input.nextLine());
			int employeeID = employeeCounter + 1;
			
			Employee employee = new Employee(employeeID, name, position, username, password, tlf, admin);
			employees.add(employee);
			
			ctd.NewEmployee(con, employee);
			
			System.out.println("Du er nå lagt til i databasen");
			
		}
		
	private void run() throws SQLException {
		current_user = login();
		System.out.println("\nDu er nå logget inn. Skriv quit for å logge ut");
		System.out.println("Hei, " + current_user.getName() + "!");
		
		System.out.println("Du har " + current_user.countUnreadMessages() + " uleste meldinger i innboksen din\n");
		
		while(current_user != null){
			System.out.println("Hva vil du gjøre?");
			if(current_user.isAdmin() == true){ //Sjekker om current_user har adminrettigheter
				
				System.out.println("1: Se alle upcoming events[goingTo] | 2: Legg til nytt event | 3: åpne innboks | 4: Se dine events |5: Quit | 6: Administrer brukere");
			}
			else 
				System.out.println("1: Se alle upcoming events[goingTo] | 2: Legg til nytt event | 3: åpne innboks | 4: Se dine events |5: Quit");
			
			int option = 0;
			
			while((current_user.isAdmin() == false && (option < 1 || option > 5)) || (current_user.isAdmin() == true && option < 1 || option > 6)){
				option = Integer.parseInt(user_input.nextLine());
				if(option == 1){
					System.out.println(current_user.generateWeeklySchedule()); //her m� "SELECT * FROM Event" sp�rring kj�res
				} else if(option == 2){ 
					Event event = getEventInput(current_user); //Her m� event opprettes i databasen med rom og creator
					current_user.addEvent(event);
					
				
				} else if(option == 3){ //Her m� en sp�rring kj�res for � hente alle meldinger knyttet til en person 
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
						System.out.println("Ingen meldinger å vise\n");
					}
				} else if(option == 4){
					
					
					
				} else if(option == 5 && current_user.isAdmin() == false){
					current_user = null;
					System.out.println("Du er nå logget ut.\n\n");
					
					//metode for å skrive tilbake til server mangler her
					main(null);
				} else if(option == 5 && current_user.isAdmin() == true){
					
				}
					
				}
				
			}
		}
	
	private void init2() {
	
		r1 = new Room(1, "R1", 500, "Fint rom1");
		r2 = new Room(2, "R2", 400, "Fint rom2");
		r3 = new Room(3, "R3", 300, "Fint rom3");
		r4 = new Room(4, "R4", 200, "Fint rom4");
		r5 = new Room(5, "R5", 100, "Fint rom5");
		r6 = new Room(6, "R6", 50, "Fint rom6");
		r7 = new Room(7, "R7", 60, "Fint rom7");
		rooms = new ArrayList<Room>();
		addRoom(r1);
		addRoom(r2);
		addRoom(r3);
		addRoom(r4);
		addRoom(r5);
		addRoom(r6);
		addRoom(r7);
		
		biti = new Employee(1, "Bendik", "Junior", "biti", "bata", "123");
		sverre = new Employee(2, "Sverre", "Senior", "sverrak", "heiia", "45884408");
		yolo = new Employee(3, "Jola", "Junior+", "bata", "biti", "123");
		current_user = null;
		
		employees = new ArrayList<Employee>();
		addEmployee(biti);
		addEmployee(sverre);
		addEmployee(yolo); 
		
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
		
		Event birthday = biti.createEvent("Bursdag", dato1, dato2, "halla paarae");
		Event birthdayAgain = biti.createEvent("Bursdag igjen", dato3, dato4, "halla paasan");
		
		Event party = biti.createEvent("party", dato5, dato6, "kom paa party!");
		Event party2 = biti.createEvent("partyOnSameDay", dato7, dato8, "kom paa party!");	//disse to skal kollidere. Ber ikke om feilmelding
		
		
		System.out.println("Bendiks events: " + "\n- Events invited to: " + biti.getUpcomingEvents() + "\n- Events attending: " + biti.getEventsAttending());
		
		System.out.println(birthday.getPeopleInvited()); // printer ekstra '[]'?
		biti.inviteEmployeeToEvent(sverre, birthday);
		System.out.println(birthday.getPeopleInvited());
		
		biti.inviteEmployeeToEvent(sverre, birthdayAgain);
		
		
		System.out.println(birthdayAgain.getPeopleGoing() + "" + birthdayAgain.getPeopleInvited());
		biti.cancelEvent(birthday, "Ingen ville komme :(");
		System.out.println(biti.getUpcomingEvents());
	}

}
