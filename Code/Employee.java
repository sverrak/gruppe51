package Code;

import java.util.Collection;

public class Employee {
	private String name;
	private String position;
	private String username;
	private String password;
	private Collection<Group> groups;
	private Collection<Event> upcomingEvents;
	
	
	public Employee(String name, String position, String username,
			String password) {
		super();
		this.name = name;
		this.position = position;
		this.username = username;
		this.password = password;
	}

	//Returnerer true hvis ansatt ble lagt til i gruppen
	public Boolean joinGroup(Group group){
		return true;
	}
	
	
	//Returnerer true hvis event ble lagt til i timeplanen til den ansatte
	public Boolean addEvent(Event event){
		return true;
	}
	// Answer er true hvis personen takker ja til invite, nei hvis ikke. Returnerer  false hvis  hva?
	public Boolean answerRequest(Boolean answer) {
		// TODO Auto-generated method stub
		return true;
	}
	
	
	public Boolean cancelEvent(Event event, String reason){
		return true;
	}
	
	//vet ikke om dette er lurt, men prøver
	public void reactOnUpdate(Event event, String change){
		
	}
	
	
	
}
