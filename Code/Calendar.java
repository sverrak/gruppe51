package Code;

import java.util.Date;

public class Calendar {
	
	public static void main(String[] args) {
		Calendar calendar = new Calendar();
		calendar.init();
		calendar.run();
		
		
	}

	private void run() {
		// TODO Auto-generated method stub
		
	}

	private void init() {
		Room r1 = new Room("R1", 500, "Fint rom1");
		Room r2 = new Room("R2", 400, "Fint rom2");
		Room r3 = new Room("R3", 300, "Fint rom3");
		Room r4 = new Room("R4", 200, "Fint rom4");
		Room r5 = new Room("R5", 100, "Fint rom5");
		Room r6 = new Room("R6", 50, "Fint rom6");
		Room r7 = new Room("R7", 60, "Fint rom7");
		Employee biti = new Employee("Bendik", "Junior", "biti", "bata", "123");
		Employee sverre = new Employee("Sverre", "Senior", "sverrak", "heiia", "45884408");
		Employee yolo = new Employee("Jola", "Junior+", "bata", "biti", "123");
		Date dato1 = new Date(115, 2, 19, 19, 0, 0);
		Date dato2 = new Date(115, 2, 19, 19, 0, 0);
	//	Employee martin = new Employee("Martin", "Konsernsjef", "martiboy","passord", "12345678");
		Event birthday = new Event("Bursdag", dato1, dato2, "halla paarae", biti);
		biti.addEvent(birthday);
		System.out.println(birthday);
		Date dato3 = new Date(116, 2, 19, 18, 30, 0);
		Date dato4 = new Date(116, 2, 19, 18, 30, 0);
		Event birthdayAgain = new Event("Bursdag", dato3, dato4, "halla paasan", biti);
		biti.addEvent(birthdayAgain);
		System.out.println(biti.getUpcomingEvents());
		
		biti.cancelEvent(birthday, "Ingen ville komme :(");		// oppstår feil her. 
		System.out.println(biti.getUpcomingEvents());
		
	}
	
}
