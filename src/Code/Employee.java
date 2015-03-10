package Code;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.Calendar;
import java.util.TimeZone;

public class Employee {
	private Boolean admin; // la til adminobjekt
	private int employeeID;
	private String name;
	private String position;
	private String username;
	private String password;
	private Collection<Group> groups;
	private List<Event> upcomingEvents;		//sortert paa startTime
	private List<Event> declinedEvents;	//boer ogs� sorteres paa startTime
	private List<Event> eventsAttending;		// sortert paa startTime. Maa gaa over alt og kanskje endre fra upcomingEvents til eventsAttending
	private int telnum;
	private List<Message> inbox;
	
	public Employee(String name, String position, String username, 
			String password, int telnum, Boolean admin) { // endret konstruktoren til � ta in admin(true/false)
		super();
	//	this.employeeID = employeeID;
		this.name = name;
		this.position = position;
		this.username = username;
		this.password = password;
		this.telnum = telnum;
		this.admin = admin;
		this.inbox = new ArrayList<Message>();
		groups = new ArrayList<Group>();
		upcomingEvents = new ArrayList<Event>();
		declinedEvents = new ArrayList<Event>();
		eventsAttending = new ArrayList<Event>();
	}

	public List<Event> getEventsAttending() {
		return eventsAttending;
	}
	public List<Event> getDeclinedEvents() {
		return declinedEvents;
	}
	public Collection<Group> getGroups() {
		return groups;
	}
	public List<Event> getUpcomingEvents() {
		return upcomingEvents;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getEmployeeID(){
		return employeeID;
	}
	public String getPosition() {
		return position;
	}
	public void setPosition(String position) {
		this.position = position;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public void setTelnum(int telnum) {
		this.telnum = telnum;
	}
	public int getTelnum(){
		return telnum;
	}
	public Boolean isAdmin(){
		return admin;
	}
	public void setAdmin(Boolean admin){
		this.admin = admin;
	}
	
		
	//Returnerer true hvis ansatt ble lagt til i gruppen
	public void joinGroup(Group group){
			groups.add(group);
			group.addEmployee(this);
	}

// skal opprette nytt event med personen som inviterer som attending

	public void addEvent(Event event){
		event.getPeopleGoing().add(this);
		if(eventsAttending != null){		// hva er dette godt for?
			if(event.getCreator() != this){		// inni her skjer det jo ingenting
				
			}
			if (eventsAttending.size() == 0){
				eventsAttending.add(event);
			}else{
				if(isAvailable(event.getStartTime(), event.getEndTime()))		// holder upcomingEvents sortert p� startTime	// ser ut til � feile her
					eventsAttending.add(event);	
				}
			}
		}
	
	public Event createEvent(String title, Date startTime, Date endTime, String description){
		if (eventsAttending.size() == 0 || isAvailable(startTime, endTime)){
			Event event = new Event(title, startTime, endTime, description, this);
			eventsAttending.add(event);
			event.addEmployee(this);
			return event;
		}
		System.out.println("Du er opptatt p� tidspunktet. " + title + " ble ikke opprettet.");
		return null;
	}
	
	// returnerer true hvis ja-svar ble sendt, false ellers
	public boolean acceptInvitation(Event event){
		if (! upcomingEvents.contains(event)){
			return false;
		}
		if(! this.isAvailable(event.getStartTime(), event.getEndTime())){
			System.out.println("Du er opptatt paa dette tidspunktet");		// kan vurderes fjernes. Faar se hva vi trenger	
			return false;
		}
		event.getPeopleGoing().add(this);
		eventsAttending.add(event);
		upcomingEvents.remove(event);
		return true;
	}
	
	// returnerer true hvis event ble fjernet, false dersom event ikke i upcomingEvents
	public boolean declineInvitation(Event event){
		if (upcomingEvents.contains(event)){
			event.getPeopleDeclined().add(this);		// skal vi fjerne fra peopleInvited ogsaa??
			upcomingEvents.remove(event);
			return true;
		}
		return false;
	}
// returnerer true hvis employee-objektet er tilgjengelig i tidsrommet
	public boolean isAvailable(Date startTime, Date endTime){
		if(this.eventsAttending.size() == 0){		// kommer ikke inn her :(
			return true;
		} else if(eventsAttending.get(0).getStartTime().compareTo(endTime) > 0){
			return true;
		} else if (eventsAttending.get(eventsAttending.size()-1).getEndTime().compareTo(startTime) < 0){
			return true;
		}
		
		for (int i = 0; i < eventsAttending.size()-1; i++) {
			if(eventsAttending.get(i).getEndTime().compareTo(startTime) < 0 && endTime.compareTo(eventsAttending.get(i+1).getStartTime()) < 0){ // fortegn her virker galt. Motsatt tegn f�r hele dritten til � henge, men det skyldes kanskje feil i compareTo
				return true;
			}
		}
		if (eventsAttending.get(eventsAttending.size()-1).getEndTime().compareTo(startTime) < 0){
			return true;
		}
		return false;
	}
	// Ikke ferdig
	public boolean cancelEvent(Event event, String reason){
		if (event.getCreator() != this){
			return false;
		}
		informAboutCancellation(event, reason);
		
		for (int i = event.getPeopleInvited().size(); i < -1; i--) {			
			Employee employee = event.getPeopleInvited().get(i);
			employee.removeEvent(event, false);
		}
		
		for (int i = event.getPeopleGoing().size(); i < -1; i--) {			
			Employee employee = event.getPeopleGoing().get(i);
			employee.removeEvent(event, false);
		}
		
		if (event.getRoom()!= null){
			event.getRoom().getRoomSchedule().remove(event);
		}
		return true;
	}

	//vet ikke om dette er lurt, men proever
	public void reactOnUpdate(Event event, String attribute){
		System.out.println("Det har skjedd en endring av ");
		System.out.println(attribute + " i eventen " + event.toString());
		System.out.println("\n + oensker du aa fjerne eventen paa bakgrunn av dette? (true/false)");
		Scanner user_input = new Scanner(System.in);
		
		String answer = user_input.nextLine();
		if(answer.equals("true")){
			removeEvent(event, true);
			informAboutCantParticipate(event, attribute);
		}
		user_input.close();
	}
	
	//informerer creator om at vedkommende ikke kan delta paa eventen
	private void informAboutCantParticipate(Event event, String reason){
		Message msg = new Message(this, event.getCreator(), "Jeg kan dessverre ikke delta pga " + reason, "Avmelding på " + event.getTitle());
		msg.sendMessage();
	}
	
	private void informAboutCancellation(Event event, String reason){
		for (Employee participant : event.getPeopleGoing()) {
			Message msg = new Message(this, participant, "Jeg har sett meg nodt til å avlyse eventen pga " + reason, event.getTitle() + " er avlyst.");
			msg.sendMessage();
		}
		for (Employee participant : event.getPeopleInvited()) {
			Message msg = new Message(this, participant, "Jeg har sett meg nodt til å avlyse eventen pga " + reason, event.getTitle() + " er avlyst.");
			msg.sendMessage();
		}
	}
	
	public void printInbox(){
		for (int i = 0; i < inbox.size(); i++) {
			if(inbox.get(i).getIsRead()){
				System.out.println("[X]" + inbox.get(i).getSender() + ": " + inbox.get(i).getSubject());
			} else{
				System.out.println("[ ]" + inbox.get(i).getSender() + ": " + inbox.get(i).getSubject());
			}
		}
	}
	
	
	public List<Message> getInbox() {
		return inbox;
	}
	
	// Dette fjerner employeens deltakelse paa eventen. 
	private void removeEvent(Event event, Boolean isOnDemandFromParticipant){
		if (upcomingEvents.contains(event)){
			upcomingEvents.remove(event);
		} 
		if (eventsAttending.contains(event)){	// dersom feil oppst�r, kan vi gj�re denne til ren 'if'
			eventsAttending.remove(event);
		}
		if(isOnDemandFromParticipant){			
			event.removeEmployee(this);
		}
	}
		
	public boolean inviteEmployeeToEvent(Employee employee, Event event){
		if (event.getCreator() != this){
			return false;
		}else if(! employee.isAvailable(event.getStartTime(), event.getEndTime())){
			return false;
		} else if(event.getPeopleDeclined().contains(employee) || event.getPeopleGoing().contains(employee) || event.getPeopleInvited().contains(employee)){
			return false;
		}
		
		Message msg = new Message(this, employee, "Jeg har invitert deg til eventen " + event, "Invitasjon til " + event.getTitle());
		msg.sendMessage();
		employee.printInbox();
		
		// "hvis eventen er upcoming"-funksjonalitet mangler her
		event.addEmployee(employee);
		employee.upcomingEvents.add(event);	
		return true;
	}

	
	public boolean inviteGroupToEvent(Group group, Event event){
		if (event.getCreator() != this){
			return false;
		}
		for (Employee participant : group.getParticipants()) {
			inviteEmployeeToEvent(participant, event);
		}
		return true;
	}
	
	// hva er hensikten med denne? HVorfor skal man sende melding til seg selv?
	public void addMessageToInbox(Message message) {
		this.inbox.add(message);	
	}
	
	
	// Ikke implementert ferdig. Her må vi finne en annen løsning på hvordan vi gjør removeEvent(). Per nå skrives en melding til 
	public boolean withdrawInvitation(Employee employee, Event event){
		if (event.getCreator() != this){
			return false;
		}
		if(! (employee.getUpcomingEvents().contains(event) || employee.getEventsAttending().contains(event))){
			return false;
		}
		employee.removeEvent(event, false);
		return true;
	}
	
	// oppretter "tom" matrise for ukeplan. Alle felter er 0
	private ArrayList<ArrayList<String>> generateEmptySchedule(){
		ArrayList<ArrayList<String>> matrix= new ArrayList<ArrayList<String>>();
		for (int row = 0; row < 32; row++) {
			matrix.add(new ArrayList<String>());
			for (int col = 0; col < 7; col++) {
				matrix.get(row).add("");
			}
		}
		return matrix;
	}
	
	public ArrayList<ArrayList<String>> generateWeeklySchedule(int weekOfYear, int year){
			ArrayList<ArrayList<String>> schedule = generateEmptySchedule();
			// Get calendar, clear it and set week number and year.
			Calendar calendar = Calendar.getInstance();
			calendar.clear();					// holder dette? Se ovenfor dersom insufficient
			calendar.set(Calendar.WEEK_OF_YEAR, weekOfYear);
			calendar.set(Calendar.YEAR, year);
			long timeStartWeek = calendar.getTimeInMillis();	//returnerer tidspunkt p� starten av uka i millisec
			calendar.add(Calendar.WEEK_OF_YEAR, 1);
			long timeEndWeek = calendar.getTimeInMillis();
			
			// legger til eventsAttending i ukeplanen
			for (Event event : eventsAttending) {
				if (event.getStartTime().getTime() > timeStartWeek && event.getStartTime().getTime() < timeEndWeek){
					int col = event.getStartTime().getDay() -1;					//	(index til kolonne i matrix) ma sjekke hvilken verdi hver dag retiurnerer
					int firstRow = (event.getStartTime().getHours() - 8) * 2; 	//	(index til rad i matrix)
					if (event.getStartTime().getMinutes() >= 30){
						firstRow += 1;
					}
					int lastRow = (event.getEndTime().getHours() - 8) * 2; 	//	(index til rad i matrix
					if (event.getEndTime().getMinutes() >= 30){
						lastRow += 1;
					}
					for (int i = firstRow; i < lastRow; i++) {		 // for alle slots fra firstRow til lastRow
						schedule.get(i).set(col, event.getTitle() + " (A)"); // matrix[rad i ][col] = event.getName() + "A"		// A'en er for attending
					}
				}
			}
			// legger til upcomingEvents i ukeplanen
			for (Event event : upcomingEvents) {
				if (event.getStartTime().getTime() > timeStartWeek && event.getStartTime().getTime() < timeEndWeek){
					int col = event.getStartTime().getDay() -1;					//	(index til kolonne i matrix) ma sjekke hvilken verdi hver dag retiurnerer
					int firstRow = (event.getStartTime().getHours() - 8) * 2; 	//	(index til rad i matrix)
					if (event.getStartTime().getMinutes() >= 30){
						firstRow += 1;
					}
					int lastRow = (event.getEndTime().getHours() - 8) * 2; 	//	(index til rad i matrix
					if (event.getEndTime().getMinutes() >= 30){
						lastRow += 1;
					}
					for (int i = firstRow; i < lastRow; i++) {		 // for alle slots fra firstRow til lastRow
						if (schedule.get(i).equals("")){
							schedule.get(i).set(col, event.getTitle() + " (U)");
						}
						else{
						schedule.get(i).set(col, schedule.get(i) + "/n" + event.getTitle() + " (U)"); // matrix[rad i ][col] = event.getName() + "U"	// U'en er for attending
						}
					}
				}			
			}
			return schedule;		// maa kanskje returnere hvilken uke i aaret det er ogsaa
		}

	
	//skal gi en visning i konsollen av innevaerende ukes plan soen-loer. UFERDIG!
		public void printWeeklySchedule(int weekOfYear, int year){
			
			ArrayList<ArrayList<String>> schedule = generateWeeklySchedule(weekOfYear, year);	
			
			String str = "|08:00|--------S�NDAG----------+---------MANDAG---------+---------TIRSDAG--------+--------ONSDAG----------+-----------TORSDAG------+--------FREDAG----------+---------L�RDAG---------+\n";
			for (int row = 0; row < 32; row++) {
			str += "|+++++|";
				for (int col = 0; col < 7; col++){
					String entry = schedule.get(row).get(col);
					str += entry;
					int num_of_spaces = 24 - entry.length();
					for (int i = 0; i < num_of_spaces; i++) {
						str += " ";
					}
					str += "|";
				}
				str += "\n";
				str += "|";
				if (row < 4){
					str += "0";
				}
				if ( row % 2 == 0){
					str += (8 + row/2) + ":00";
					} else{
						str += ((8 + row/2) + ":30");
						}
				
				str += "|------------------------+------------------------+------------------------+------------------------+------------------------+------------------------+------------------------+\n";
			}
			System.out.println(str);
		}
	
	@Override
	public String toString() {
		return this.name;
	}

	public int countUnreadMessages() {
		int counter = 0;
		for (Message msg : inbox) {
			if (! msg.getIsRead()){
				counter += 1;
			}
		}
		return counter;
	}
	
}
