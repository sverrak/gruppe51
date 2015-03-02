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
	private	List<Employee> employees;
	private Employee current_user;
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
			if(room.isAvailable(startTime, endTime) && room.getCapacity() > capacity){
				availableRooms.add(room);
			}
		}
		return availableRooms;
	}

	public Event getEventInput(Employee employee){
		//initalisering
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
		System.out.println(startTimeString);
		
		
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
			newEvent.addEmployee(availableEmployees.get(Integer.parseInt(input)));
			//availableEmployees.get(Integer.parseInt(input)).addEvent(newEvent);
			System.out.println("Noen flere[tom streng for å avslutte]?");
			input = user_input.nextLine();
			counter += 1;
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


		
}

