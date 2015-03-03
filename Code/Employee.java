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
	private String name;
	private String position;
	private String username;
	private String password;
	private Collection<Group> groups;
	private List<Event> upcomingEvents;		//sortert paa startTime
	private Collection<Event> declinedEvents;	//boer ogs� sorteres paa startTime
	private List<Event> eventsAttending;		// sortert paa startTime. Maa gaa over alt og kanskje endre fra upcomingEvents til eventsAttending
	private String telnum;
	private List<Message> inbox;
	private Object myCalendar;	// vet ikke helt typen p� dette objektet enn�, men alle ansatte skal ha en egen kalender knyttet til seg
								// en mulighet er en liste over WeeklySchedul-objekter
	
	public Employee(String name, String position, String username, String password, String telnum) {
		super();
		this.name = name;
		this.position = position;
		this.username = username;
		this.password = password;
		this.telnum = telnum;
		this.inbox = new ArrayList<Message>();
		groups = new ArrayList<Group>();
		upcomingEvents = new ArrayList<Event>();
		declinedEvents = new ArrayList<Event>();
		eventsAttending = new ArrayList<Event>();
	}

	public List<Event> getEventsAttending() {
		return eventsAttending;
	}
	public void setTelnum(String telnum) {
		this.telnum = telnum;
	}
	public Collection<Event> getDeclinedEvents() {
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
	public String getTelnum(){
		return telnum;
	}
		
	//Returnerer true hvis ansatt ble lagt til i gruppen
	public void joinGroup(Group group){
			groups.add(group);
			group.addEmployee(this);
	}
		
	public void addEvent(Event event){
		System.out.println(event);
		event.getPeopleGoing().add(this);
		if(eventsAttending != null){
			if (eventsAttending.size() == 0){
				eventsAttending.add(event);
			}else{
				for (int i = 0; i < eventsAttending.size(); i++) {		// holder upcomingEvents sortert p� startTime	// ser ut til � feile her
					if (eventsAttending.get(i).getStartTime().compareTo(event.getStartTime()) > 0){	// fortegn her virker galt. Motsatt tegn f�r hele dritten til � henge, men det skyldes kanskje feil i compareTo
						eventsAttending.add(i, event);
					}
				}
			}
			
		}
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
		}
		for (int i = 0; i < upcomingEvents.size()-1; i++) {
			if(upcomingEvents.get(i).getEndTime().compareTo(startTime) < 0 && endTime.compareTo(upcomingEvents.get(i+1).getStartTime()) < 0){
				return true;
			}
		}
		if (upcomingEvents.get(upcomingEvents.size()-1).getEndTime().compareTo(startTime) < 0){
			return true;
		}
		return false;	
	}
	// Ikkke ferdig
	public boolean cancelEvent(Event event, String reason){
		
		if (event.getCreator() != this){
			return false;
		}		
		for (Employee employee : event.getPeopleInvited()) {
			employee.removeEvent(event);
		}	
		for (Employee employee : event.getPeopleDeclined()) {
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
	
	public List<Message> getInbox() {
		return inbox;
	}
	
	// Dette fjerner employeens deltakelse paa eventen
	private void removeEvent(Event event){
		if (upcomingEvents.contains(event)){
			upcomingEvents.remove(event);
		} if (eventsAttending.contains(event)){	// dersom feil oppst�r, kan vi gj�re denne til ren 'if'
			eventsAttending.remove(event);
		}
		event.removeEmployee(this);
	}
		
	public boolean inviteEmployeeToEvent(Employee employee, Event event){
		if (event.getCreator() != this){
			return false;
		}else if(! employee.isAvailable(event.getStartTime(), event.getEndTime())){
			return false;
		}
		
		Message msg = new Message(this, employee, false, "Jeg har invitert deg til eventen " + event, "Invitasjon til " + event.toString());
		msg.sendMessage();
		
		// hvis eventen er upcoming
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
		
	// UFERDIG! itererer over matrisa og fyller inn event-navn der employee er opptatt. Alle andre felter forblir 0
	private WeeklySchedule generateWeeklySchedule(){
		WeeklySchedule weeklySchedule = new WeeklySchedule();	// tom matrise for timeplan opprettes hvor nummer p� uke i �ret er kjent
		
		
	//	Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
	//	calendar.set(200, 2, 19, 18, 30);		
		
		for (Event event : eventsAttending) {
			// hvis event.startTidspunkt er denne uka.		Kunne v�rt event.getStartTime().get(Calendar.WEEK_OF_YEAR) == weeklySchedule.getWeekOfYear()	dersom startTidspunkt er CALENDAR-type
				// col = event.getDaytOfWeek -1 							(index til kolonne i matrix)
				// firstRow = (event.getStartTime().getHour() - 8)*0.5 		(index til rad i matrix)
				// lastRow = (event.getEndTime().getHour() - 8)*0.5 		(index til rad i matrix
				// for alle slots fra firstRow til lastRow
					// matrix[rad i ][col] = event.getName() + "A"		// A'en er for attending
		}
		for (Event event : upcomingEvents) {
			// hvis event.startTidspunkt er denne uka
				// col = event.getDaytOfWeek -1 							(index til kolonne i matrix)
				// firstRow = (event.getStartTime().getHour() - 8)*0.5 		(index til rad i matrix)
				// lastRow = (event.getEndTime().getHour() - 8)*0.5 		(index til rad i matrix
				// for alle slots fra firstRow til lastRow
					// if matrix[rad i ][col] != 0													(if slot not filled)
						// matrix[rad i ][col] = event.getName() + "U"		// U'en er for upcoming/unanswered
		}
		return weeklySchedule;		// m� kanskje returnere hvilken uke i �ret det er ogs�
	}
	
	//UFERDIG! skal hente evente't som spenner seg over et tidspunkt.
	private Event getEventAt(Date time){
		
		return event;
	}
	
	// UFERDIG! skal hente denne ukas timeplan
		public WeeklySchedule getWeeklySchedule(){
			// return thisWeeksSchedule;
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
