package Code;

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
	
	private Scanner user_input;
	
	public List<Room> findLocation(Date startTime, Date endTime){
		List<Room> availableRooms = new ArrayList<Room>();
		for (Room room : rooms) {
			room.getDescription();
			if(room.isAvailable(startTime, endTime)){
				availableRooms.add(room);
			}
		}
		return availableRooms;
	}
	
	public static void main(String[] args) {
		Calendar calendar = new Calendar();
		calendar.init();
		calendar.run();
	}
	
	public Employee login(){
		user_input = new Scanner(System.in);
		String username = "";
		System.out.println("Hei");
		System.out.println("Har du bruker?");
		
		String login_option = user_input.nextLine();
		if(login_option.equals("ja")){
			username = "";
			String password = null;
			
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
			String password = user_input.nextLine();
			System.out.println("Ditt navn:");
			String name = user_input.nextLine();
			System.out.println("Stilling:");
			String position = user_input.nextLine();
			System.out.println("Telefonnummer:");
			String telnum = user_input.nextLine();
			
			user_input.close();
			Employee employee = new Employee(name, position, username, password, telnum);
			employees.add(employee);
			return employee;
		}
	}
	

	private void run() {
//		System.out.println(biti);
		biti.addEvent(birthday);
		current_user = login();
		
		System.out.println("Nå har vi følgende brukere:");
		for (Employee employee : employees) {
			System.out.println(employee);
		}
		
		
		
		current_user.addEvent(birthday);
		System.out.println(birthday);
		
	//	System.out.println(dato1.toLocaleString());
		
		biti.addEvent(birthdayAgain);
		System.out.println(birthdayAgain);
		
		System.out.println("Bendiks events: " + "\n- Events invited to: " + biti.getUpcomingEvents() + "\n- Events attending: " + biti.getEventsAttending());
		System.out.println(birthday.getPeopleInvited());
		biti.inviteEmployeeToEvent(sverre, birthday);
		System.out.println(birthday.getPeopleInvited());
		sverre.acceptInvitation(birthday);
		System.out.println(sverre.getUpcomingEvents());
		
		System.out.println(birthday.getPeopleGoing());
		biti.cancelEvent(birthday, "Ingen ville komme :(");
		System.out.println("Bendiks events: " + "\n- Events invited to: " + biti.getUpcomingEvents() + "\n- Events attending: " + biti.getEventsAttending() + "\n");
		
		System.out.println(birthdayAgain.getPeopleGoing() + "" + birthdayAgain.getPeopleInvited());
		
		
		
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
	
}
