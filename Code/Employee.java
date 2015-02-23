package Code;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Scanner;

public class Employee {
	private String name;
	private String position;
	private String username;
	private String password;
	private Collection<Group> groups;
	private Collection<Event> upcomingEvents;
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
	
	
	//Returnerer true hvis event ble lagt til i timeplanen til den ansatte
	public Boolean addEvent(Event event){
		try{
			Scanner user_input = new Scanner(System.in);
			String title = user_input.nextLine();
			String description = user_input.nextLine();
			
			//formatering av datogreier
			String startTimeString = user_input.nextLine();
			String endTimeString = user_input.nextLine();
			Date startTime = null;
			Date endTime = null;
			SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy H:m:s");
			try { 
				startTime = formatter.parse(startTimeString);
				endTime = formatter.parse(startTimeString);
				System.out.println(startTimeString);
				
		 
			} catch (ParseException e) {
				e.printStackTrace();
			}
			
			Event newEvent = new Event(title, startTime, endTime, description, this);
			
			Room room = event.findLocation();
			
			
			
			
		}
	}
	// Answer er true hvis personen takker ja til invite, nei hvis ikke. Returnerer  false hvis  hva?
	public Boolean answerRequest(Event event, Boolean answer) {
		if(answer){
			event.employeeAcceptedInvitation(this);
		} else{
			event.employeeDeclinedInvitation(this);
		}
		return true;
	}
	
	
	public Boolean cancelEvent(Event event, String reason){
		return true;
	}
	
	//vet ikke om dette er lurt, men prøver
	public void reactOnUpdate(Event event, String change){
		
	}



	public Collection<Message> getInbox() {
		return inbox;
	}
	
	
	
	
	
}
