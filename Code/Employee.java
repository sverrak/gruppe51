package Code;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

public class Employee {
	private String name;
	private String position;
	private String username;
	private String password;
	private Collection<Group> groups;
	private List<Event> upcomingEvents;
	private Collection<Event> declinedEvents;
	private String telnum;
	private Collection<Message> inbox;
	
	
	public Employee(String name, String position, String username,
			String password, String telnum) {
		super();
		this.name = name;
		this.position = position;
		this.username = username;
		this.password = password;
		this.telnum = telnum;
		groups = new ArrayList<Group>();
		upcomingEvents = new ArrayList<Event>();
		declinedEvents = new ArrayList<Event>();
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
	//	try{
			groups.add(group);
			group.addEmployee(this);
	//	} catch {
	//		throw new IllegalStateException("Noe gikk galt i joinGroup()");
	//	}
	//	return true;
	}
	
	//Det er her man lager en ny Event
	//Returnerer true hvis event ble lagt til i timeplanen til den ansatte
/*	public void addEvent(Event event){
		String title = "";
		String description = "";
		Date startTime;
		Date endTime;
		
		try{
			Scanner user_input = new Scanner(System.in);
			title = user_input.nextLine();
			description = user_input.nextLine();
			
			//formatering av datogreier
			String startTimeString = user_input.nextLine();			// formatet pï¿½ disse mï¿½ vi ha orden pï¿½
			String endTimeString = user_input.nextLine();
	
			SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy H:m:s");
			try { 
				startTime = formatter.parse(startTimeString);
				endTime = formatter.parse(endTimeString);
				System.out.println(startTimeString);
				
		 
			} catch (ParseException e) {
				e.printStackTrace();
			}
			
		} finally{
			
			Event newEvent = new Event(title, startTime, endTime, description, this);
			
			Room room = event.findLocation();
		}
	}
	*/
	
	public void addEvent(Event event){
		upcomingEvents.add(event);
		event.getPeopleInvited().add(this);
	}
	
	public void acceptInvitation(Event event){
		if(isAvailable(event.getStartTime(), event.getEndTime())){
			event.getPeopleGoing().add(this);
		}else{
			System.out.println("Du er opptatt paa dette tidspunktet");
		}
	}
		
	public void declineInvitation(Event event){
		if (upcomingEvents.contains(event)){
			event.getPeopleDeclined().add(this);		// skal vi fjerne fra peopleInvited også??
			upcomingEvents.remove(event);
		}
	}
	
	public Boolean isAvailable(Date startTime, Date endTime){
		for (int i = 0; i < upcomingEvents.size() - 1; i++) {
			if(upcomingEvents.get(i).getEndTime().compareTo(startTime) < 0 && endTime.compareTo(upcomingEvents.get(i+1).getStartTime()) < 0){
				return true;
			}
		}
		
		return false;	
	}
	
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
		event.getRoom().getRoomSchedule().remove(event);
		
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
	
	public Collection<Message> getInbox() {
		return inbox;
	}
	
	// Dette fjerner employeens deltakelse paa eventen
	private boolean removeEvent(Event event){
		if (upcomingEvents.contains(event)){
			upcomingEvents.remove(event);
			event.removeEmployee(this);
			return true;
		}
		return false;
	}
		
	public boolean inviteEmployeeToEvent(Employee employee, Event event){
		if (event.getCreator() != this){
			return false;
		}else if(! employee.isAvailable(event.getStartTime(), event.getEndTime())){
			return false;
		}
		
		Message msg = new Message(this, employee, false, "Jeg har invitert deg til eventen " + event.toString(), "Invitasjon til " + event.toString());
		msg.sendMessage();
		event.addEmployee(employee);
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
	
	@Override
	public String toString() {
		return this.name;
	}
	
}
