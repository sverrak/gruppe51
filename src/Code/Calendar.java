package Code;

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
		Room r1 = new Room("R1", "500", "Fint rom1");
		Room r2 = new Room("R2", "400", "Fint rom2");
		Room r3 = new Room("R3", "300", "Fint rom3");
		Room r4 = new Room("R4", "200", "Fint rom4");
		Room r5 = new Room("R5", "100", "Fint rom5");
		Room r6 = new Room("R6", "50", "Fint rom6");
		Room r7 = new Room("R7", "60", "Fint rom7");
		Employee biti = new Employee("Bendik", "Junior", "biti", "bata", "123");
		Employee sverre = new Employee("Sverre", "Senior", "sverrak", "heiia", "45884408");
		Employee yolo = new Employee("Jola", "Junior+", "bata", "biti", "123");
		Event birthday = new Event("Bursdag", "15/05/2015 19:00:00", "15/05/2015 23:00:00", "halla påræ");
		biti.addEvent(birthday);
	}
}
