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
	private int telnum;
	private Collection<Group> groups;
	private List<Event> upcomingEvents;		//sortert paa startTime
	private List<Event> declinedEvents;	//boer ogsï¿½ sorteres paa startTime
	private List<Event> eventsAttending;		// sortert paa startTime. Maa gaa over alt og kanskje endre fra upcomingEvents til eventsAttending
	private List<Message> inbox;
	
	public Employee(String name, String position, String username,
			String password, int telnum, Boolean admin) { // endret konstruktoren til Œ ta in admin(true/false)
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
	//	try{
			groups.add(group);
			group.addEmployee(this);
	//	} catch {
	//		throw new IllegalStateException("Noe gikk galt i joinGroup()");
	//	}
	//	return true;
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
				if(isAvailable(event.getStartTime(), event.getEndTime()))		// holder upcomingEvents sortert pï¿½ startTime	// ser ut til ï¿½ feile her
					eventsAttending.add(event);	
				}
			}
		}
	
	public Event createEvent(String title, Date startTime, Date endTime, String description){
		if (eventsAttending.size() == 0 || isAvailable(startTime, endTime)){
			Event event = new Event(title, startTime, endTime, description, this);
			eventsAttending.add(event);
			return event;
		}
		System.out.println("Du er opptatt på tidspunktet. " + title + " ble ikke opprettet.");
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
			if(eventsAttending.get(i).getEndTime().compareTo(startTime) < 0 && endTime.compareTo(eventsAttending.get(i+1).getStartTime()) < 0){ // fortegn her virker galt. Motsatt tegn fï¿½r hele dritten til ï¿½ henge, men det skyldes kanskje feil i compareTo
				return true;
			}
		}
		if (eventsAttending.get(eventsAttending.size()-1).getEndTime().compareTo(startTime) < 0){
			return true;
		}
		return false;
	}
	// Ikkke ferdig
	public boolean cancelEvent(Event event, String reason){
		
		if (event.getCreator() != this){
			return false;
		}		
/*		for (Employee employee : event.getPeopleInvited()) {
			employee.removeEvent(event);
		}
		*/	
		
		for (int i = event.getPeopleInvited().size(); i < -1; i--) {
			Employee employee = event.getPeopleInvited().get(i);
			employee.removeEvent(event);
		}
/*		for (Employee employee : event.getPeopleDeclined()) {
			employee.removeEvent(event);
		}
	*/
		for (int i = event.getPeopleInvited().size(); i < -1; i--) {
			Employee employee = event.getPeopleInvited().get(i);
			employee.removeEvent(event);
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
		
		Boolean answer = user_input.nextBoolean();
		if(answer){
			removeEvent(event);			
		}
		user_input.close();
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
	
	// Dette fjerner employeens deltakelse paa eventen
	private void removeEvent(Event event){
		if (upcomingEvents.contains(event)){
			upcomingEvents.remove(event);
		} if (eventsAttending.contains(event)){	// dersom feil oppstï¿½r, kan vi gjï¿½re denne til ren 'if'
			eventsAttending.remove(event);
		}
		event.removeEmployee(this);
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
	
	public boolean withdrawInvitation(Employee employee, Event event){
		if (event.getCreator() != this){
			return false;
		}
		if(! (employee.getUpcomingEvents().contains(event) || employee.getEventsAttending().contains(event))){
			return false;
		}
		employee.removeEvent(event);
		return true;
	}
	
	// oppretter "tom" matrise for ukeplan. Alle felter er 0
	private ArrayList<ArrayList<Object>> generateEmptySchedule(){
		ArrayList<ArrayList<Object>> matrix= new ArrayList<ArrayList<Object>>();
		for (int row = 0; row < 20; row++) {
			matrix.add(new ArrayList<Object>());
			for (int col = 0; col < 7; col++) {
				matrix.get(row).add(0);
			}
		}
		return matrix;
	}
	
	public ArrayList<ArrayList<Object>> generateWeeklySchedule(int weekOfYear, int year){
		//	WeeklySchedule weeklySchedule = new WeeklySchedule();	// tom matrise for timeplan opprettes hvor nummer paa uke i aaret er kjent
			ArrayList<ArrayList<Object>> schedule = generateEmptySchedule();
			// koden nedenfor fungerer for å generere denne ukas schedule
	/*		Calendar cal = Calendar.getInstance();
			cal.set(Calendar.HOUR_OF_DAY, 0); // ! clear would not reset the hour of day !
			cal.clear(Calendar.MINUTE);
			cal.clear(Calendar.SECOND);
			cal.clear(Calendar.MILLISECOND);

			// get start of this week in milliseconds
			cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek());
//			System.out.println("Start of this week:       " + cal.getTime());
//			System.out.println("... in milliseconds:      " + cal.getTimeInMillis());
			long timeStartWeek = cal.getTimeInMillis();
			// start of the next week
			cal.add(Calendar.WEEK_OF_YEAR, 1);
//			System.out.println("Start of the next week:   " + cal.getTime());
//			System.out.println("... in milliseconds:      " + cal.getTimeInMillis());
			long timeEndWeek = cal.getTimeInMillis();
			
			*/
			// Get calendar, clear it and set week number and year.
			Calendar calendar = Calendar.getInstance();
			calendar.clear();					// holder dette? Se ovenfor dersom insufficient
			calendar.set(Calendar.WEEK_OF_YEAR, weekOfYear);
			calendar.set(Calendar.YEAR, year);
			long timeStartWeek = calendar.getTimeInMillis();
			calendar.add(Calendar.WEEK_OF_YEAR, 1);
			long timeEndWeek = calendar.getTimeInMillis();
			
			for (Event event : eventsAttending) {
				if (event.getStartTime().getTime() > timeStartWeek && event.getStartTime().getTime() < timeEndWeek){
					int col = event.getStartTime().getDay() -1;					//	(index til kolonne i matrix) ma sjekke hvilken verdi hver dag retiurnerer
					int firstRow = (event.getStartTime().getHours() - 8) * 2; 	//	(index til rad i matrix)
					if (event.getStartTime().getMinutes() == 30){
						firstRow += 1;
					}
					int lastRow = (event.getEndTime().getHours() - 8) * 2; 	//	(index til rad i matrix
					if (event.getEndTime().getMinutes() == 30){
						lastRow += 1;
					}
					for (int i = firstRow; i < lastRow; i++) {		 // for alle slots fra firstRow til lastRow
						schedule.get(i).set(col, event.getTitle() + "A"); // matrix[rad i ][col] = event.getName() + "A"		// A'en er for attending
					}
				}
			}
			for (Event event : upcomingEvents) {
				if (event.getStartTime().getTime() > timeStartWeek && event.getStartTime().getTime() < timeEndWeek){
					int col = event.getStartTime().getDay() -1;					//	(index til kolonne i matrix) ma sjekke hvilken verdi hver dag retiurnerer
					int firstRow = (event.getStartTime().getHours() - 8) * 2; 	//	(index til rad i matrix)
					if (event.getStartTime().getMinutes() == 30){
						firstRow += 1;
					}
					int lastRow = (event.getEndTime().getHours() - 8) * 2; 	//	(index til rad i matrix
					if (event.getEndTime().getMinutes() == 30){
						lastRow += 1;
					}
					for (int i = firstRow; i < lastRow; i++) {		 // for alle slots fra firstRow til lastRow
						schedule.get(i).set(col, event.getTitle() + "U"); // matrix[rad i ][col] = event.getName() + "U"	// U'en er for attending
					}
				}			
			}
			return schedule;		// maa kanskje returnere hvilken uke i aaret det er ogsaa
		}

	
	//skal gi en visning i konsollen av innevaerende ukes plan man-sï¿½n. UFERDIG!
		public void printWeeklySchedule(){
			Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
			int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);		// ukedagen i dag
			
			System.out.println("\tMandag  Tirsdag  Onsdag  Torsdag   Fredag  Lï¿½rdag  Sï¿½ndag");
			
			
			for (int i = 0; i < 10; i++) {
				
				String str = (8+i) + ":00";
				str += "\n";
				str += ((8+i) + ":30");
			}
			
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
