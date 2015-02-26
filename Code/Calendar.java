package Code;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Calendar {
	private List<Room> rooms;
	Room r1;
	Room r2;
	Room r3;
	Room r4;
	Room r5;
	Room r6;
	Room r7;
	Employee biti;
	Employee sverre;
	Employee yolo;
	Date dato1;
	Date dato2;
	Date dato3;
	Date dato4;
	Event birthday;
	Event birthdayAgain;
	
	// Ikke implementert
	public Room findLocation(){
		for (Room room : rooms) {
			room.getDescription();
		}
		return rooms.get(0);
	}
	
	public static void main(String[] args) {
		Calendar calendar = new Calendar();
		calendar.init();
		calendar.run();
	}
	

	private void run() {
//		System.out.println(biti);
		
		biti.addEvent(birthday);
		System.out.println(birthday);
	//	biti.inviteEmployeeToEvent(sverre, birthday);
		
		//Dette funker ikke
		//System.out.println(birthday);
	//	System.out.println(dato1.toLocaleString());
		
		biti.addEvent(birthdayAgain);
		System.out.println(biti.getUpcomingEvents());
		/*
		biti.cancelEvent(birthday, "Ingen ville komme :(");		// oppst�r feil her. 
		System.out.println(biti.getUpcomingEvents());
		*/
		
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
		dato1 = new Date(115, 2, 19, 19, 0, 0);
		dato2 = new Date(115, 2, 19, 21, 0, 0);
		dato3 = new Date(116, 2, 19, 18, 30, 0);
		dato4 = new Date(116, 2, 19, 20, 30, 0);
	//	Employee martin = new Employee("Martin", "Konsernsjef", "martiboy","passord", "12345678");
		birthday = new Event("Bursdag", dato1, dato2, "halla paarae", biti);
		birthdayAgain = new Event("Bursdag", dato3, dato4, "halla paasan", biti);
	
	}
	
}
