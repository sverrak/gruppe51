package Code;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
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
	public Boolean joinGroup(Group group){
		try{
			groups.add(group);
			group.addEmployee(this);
		} except{
			throw new IllegalStateException("Noe gikk galt i joinGroup()");
		}
		return true;
		
	}
	
	//Det er her man lager en ny Event
	//Returnerer true hvis event ble lagt til i timeplanen til den ansatte
	public Boolean addNewEvent(Event event){
		String title = "";
		String description = "";
		Date startTime;
		Date endTime;
		
		try{
			Scanner user_input = new Scanner(System.in);
			title = user_input.nextLine();
			description = user_input.nextLine();
			
			//formatering av datogreier
			String startTimeString = user_input.nextLine();
			String endTimeString = user_input.nextLine();
			startTime = null;
			endTime = null;
			SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy H:m:s");
			try { 
				startTime = formatter.parse(startTimeString);
				endTime = formatter.parse(startTimeString);
				System.out.println(startTimeString);
				
		 
			} catch (ParseException e) {
				e.printStackTrace();
			}
			
		} finally{
			Event newEvent = new Event(title, startTime, endTime, description, this);
			
			Room room = event.findLocation();
		}
	}
	
	// Answer er true hvis personen takker ja til invite, nei hvis ikke. Returnerer  false hvis  hva?
	//Mangler støtte for utsending av varsel til andre deltakere
	public Boolean answerRequest(Event event, Boolean answer) {
		if(answer){
			// Hvis man har tid pÃ¥ tidspunktet
			if(isAvailable(event.getStartTime(), event.getEndTime())){
				event.employeeAcceptedInvitation(this);				
			} else{
				System.out.println("Du er opptatt pÃ¥ dette tidspunktet");
			}
			
			
		} else{
			event.employeeDeclinedInvitation(this);
		}
		return true;
	}
	
	private Boolean isAvailable(Date startTime, Date endTime){
		for (int i = 0; i < upcomingEvents.size() - 1; i++) {
			if(upcomingEvents.get(i).getEndTime().compareTo(startTime) < 0 && endTime.compareTo(upcomingEvents.get(i+1).getStartTime()) < 0){
				return true;
			}
		}
		
		return false;	
	}
	
	public Boolean cancelEvent(Event event, String reason){
		// her trengs det fyll. Må huske at event må fjernes fra upcomingEvents osv til hvert Employee-objekt som er deltaker
		// samt upcomingEvents til tilknyttet Room-objektet
		event.getPeopleInvited()
		return true;
	}
	
	//vet ikke om dette er lurt, men prÃ¸ver
	public void reactOnUpdate(Event event, String change){
		
	}
	
	public Collection<Message> getInbox() {
		return inbox;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return this.name;
	}
	
	
	
	
	
}
