package Code;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class Calendar {
	private List<Room> rooms;
	private Room r1;
	private Room r2;
	private Room r3;
	private Room r4;
	private Room r5;
	private Room r6;
	private Room r7;
	private	List<Employee> employees;
	private Employee biti;
	private Employee sverre;
	private Employee yolo;
	private Employee current_user;
	private Date dato1;
	private Date dato2;
	private Date dato3;
	private Date dato4;
	private Event birthday;
	private Event birthdayAgain;
	
	
	//login-felter
	private String login_option;
	private String username;
	private String password;
	
	private Scanner user_input;
	
	public List<Room> findLocation(Date startTime, Date endTime, int capacity){
		List<Room> availableRooms = new ArrayList<Room>();
		for (Room room : rooms) {
			room.getDescription();
			if(room.isAvailable(startTime, endTime) && room.getCapacity() > capacity){
				availableRooms.add(room);
			}
		}
		return availableRooms;
	}
	
	public static void main(String[] args) {
		Calendar calendar = new Calendar();
		calendar.init();
		calendar.run();
		calendar.user_input.close();
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
		rooms.add(r1);
		rooms.add(r2);
		rooms.add(r3);
		rooms.add(r4);
		rooms.add(r5);
		rooms.add(r6);
		rooms.add(r7);
		
		biti = new Employee("Bendik", "Junior", "biti", "bata", "123");
		sverre = new Employee("Sverre", "Senior", "sverrak", "heiia", "45884408");
		yolo = new Employee("Jola", "Junior+", "bata", "biti", "123");
		current_user = null;
		
		employees = new ArrayList<Employee>();
		employees.add(biti);
		employees.add(sverre);
		employees.add(yolo);
		
		
		dato1 = new Date(115, 2, 19, 19, 0, 0);
		dato2 = new Date(115, 2, 19, 21, 0, 0);
		dato3 = new Date(116, 2, 19, 18, 30, 0);
		dato4 = new Date(116, 2, 19, 20, 30, 0);
	//	Employee martin = new Employee("Martin", "Konsernsjef", "martiboy","passord", "12345678");
		birthday = new Event("Bursdag", dato1, dato2, "halla paarae", biti);
		birthdayAgain = new Event("Bursdag", dato3, dato4, "halla paasan", biti);
	
	}
	
	
	public Employee login(){
		user_input = new Scanner(System.in);
		username = "";
		System.out.println("Hei");
		System.out.println("Har du bruker?");
		
		login_option = user_input.nextLine();
		if(login_option.equals("ja")){
			username = "";
			password = null;
			
			while(current_user == null){
				System.out.println("Brukernavn: ");
				username = user_input.nextLine();
				System.out.println("Passord: ");
				password = user_input.nextLine();
				System.out.println(username + password);
				for (Employee employee : employees) {
					if(employee.getUsername().equals(username) && employee.getPassword().equals(password)){
						current_user = employee;
						System.out.println(current_user);
						break;
					}
				}
				
				if(current_user == null){
					System.out.println("Feil brukernavn/passord. Prøv igjen");
				} 
			}
			return current_user;
		} else{
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
			
			Employee employee = new Employee(name, position, username, password, telnum);
			employees.add(employee);
			System.out.println("Du er nå lagt til i databasen");
			return employee;
		}
	}
	
	private void printEmployees(){
		System.out.println("Nå har vi følgende brukere:");
		for (int i = 0; i < employees.size(); i++) {
			System.out.println("" + i + ": "+ employees.get(i).toString());
		} 
	}
	public Event getEventInput(Employee employee){
		String title = "";
		String description = "";
		Date startTime;
		Date endTime;
		
	
		Scanner user_input = new Scanner(System.in);
		System.out.println("Tittel: ");
		title = user_input.nextLine();
		System.out.println("Beskrivelse: ");
		description = user_input.nextLine();
		
		//formatering av datogreier
		System.out.println("Starttidspunkt[dd/MM/yyyy H:m:s]: ");
		String startTimeString = user_input.nextLine();			// formatet p� disse m� vi ha orden p�
		System.out.println("Sluttidspunkt[dd/MM/yyyy H:m:s]: ");
		String endTimeString = user_input.nextLine();
		System.out.println("Kapasitet: ");
		int capacity = Integer.parseInt(user_input.nextLine());

		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy H:m:s");
		try { 
			startTime = formatter.parse(startTimeString);
			endTime = formatter.parse(endTimeString);
			System.out.println(startTimeString);
			
			Event newEvent = new Event(title, startTime, endTime, description, employee);
			List<Room> availableRooms = findLocation(startTime, endTime, capacity);
			String print = "";
			for (int i = 0; i < availableRooms.size(); i++) {
				print = i + "";
				print += availableRooms.get(i);
				System.out.println(print);
			}
			
			System.out.println("Skriv nummer på rommet du vil ha");
			String input = user_input.nextLine();
			
			newEvent.setRoom(availableRooms.get(Integer.parseInt(input)));
			availableRooms.get(Integer.parseInt(input)).addEventToRoom(newEvent);
			
			//print roomSchedulen til Room
			//System.out.println(availableRooms.get(Integer.parseInt(input)).getRoomSchedule().toString());
			
			for (int i = 0; i < getAvailableEmployees(startTime, endTime).size(); i++) {
				System.out.println("" + i + ": " + getAvailableEmployees(startTime, endTime).get(i));	
			}
			
			System.out.println("Hvem vil du invitere til dette arrangementet[tom streng for å avslutte]?");
			input = user_input.nextLine();
			while(input != ""){
				newEvent.addEmployee(getAvailableEmployees(startTime, endTime).get(Integer.parseInt(input)));
				System.out.println("Noen flere[tom streng for å avslutte]?");
				input = user_input.nextLine();
			}
			
			
			return newEvent;
			
	 
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		return null;
		
	}
	private List<Employee> getAvailableEmployees(Date startTime, Date endTime) {
		List<Employee> availableEmployees = new ArrayList<Employee>();
		for (Employee employee : employees) {
			if(employee.isAvailable(startTime, endTime)){
				availableEmployees.add(employee);
			}
		}
		return null;
	}

	private void run() {
//		System.out.println(biti);
		current_user = login();
		System.out.println("Du er nå logget inn. Skriv quit for å logge ut");
		System.out.println("Hei, " + current_user.getName() + "!");
		while(current_user != null){
			System.out.println("Hva vil du gjøre?");
			System.out.println("1: se alle upcoming events[goingTo] | 2: legg til ny event | 3: åpne innboks | 4: se dine invitasjoner | 5: quit");
			
			int option = 0;
			
			while(option < 1 || option > 5){
				option = Integer.parseInt(user_input.nextLine());
				if(option == 1){
					current_user.printSchedule();
				} else if(option == 2){
					Event event = getEventInput(current_user);
					current_user.addEvent(event);
				} else if(option == 3){
					
				} else if(option == 4){
					
				} else{
					current_user = null;
					System.out.println("Du er nå logget ut.");
					
					//metode for å skrive tilbake til server mangler her
					main(null);
				}
					
				}
				
			}
		}


	
	
	//	biti.inviteEmployeeToEvent(sverre, birthday);
		
		//Dette funker ikke
		//System.out.println(birthday);
	//	System.out.println(dato1.toLocaleString());
		/*
		biti.addEvent(birthdayAgain);
		System.out.println(biti.getUpcomingEvents());
		biti.inviteEmployeeToEvent(sverre, birthday);
		System.out.println(birthday.getPeopleInvited());
		
		biti.cancelEvent(birthday, "Ingen ville komme :(");		// oppst�r feil her. 
		System.out.println(biti.getUpcomingEvents());
		*/
	
}
