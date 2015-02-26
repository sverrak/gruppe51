package Code;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.Scanner;

public class Event implements Comparable<Event>{
	private String title;
	private Date startTime;
	private Date endTime;
	private String description;
	private Room room;
	private Employee creator;
	private Collection<Employee> peopleInvited;
	private Collection<Employee> peopleGoing;
	private Collection<Employee> peopleDeclined;
	public Object getPeopleDeclined;
	
	
	public Event(String title, Date startTime, Date endTime,
			String description, Employee creator) {
		super();
		setTitle(title);
		this.creator = creator;
		//setTime(startTime, endTime);
		this.startTime = startTime;
		this.endTime = endTime;
		setDescription(description);
		this.room = null;
		this.peopleInvited = new ArrayList<Employee>();
		this.peopleGoing = new ArrayList<Employee>();
		this.peopleDeclined = new ArrayList<Employee>();
		
	}
	
	public Object getGetPeopleDeclined() {
		return getPeopleDeclined;
	}
	
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		if(this.title != null){
			fireChange("title");			
		}
		this.title = title;
	}

	public Date getStartTime() {
		return startTime;
	}

	// Må også sjekke om det er ledige rom på tidspunktet
	public void setTime(Date startTime, Date endTime) {
		if(this.creator.isAvailable(startTime, endTime)){
			if(this.startTime == null && this.endTime == null){
				fireChange("tid");				
			}
			if(startTime.compareTo(endTime) < 1){
				this.startTime = startTime;	
				this.endTime = endTime;				
			}
		}
	}

	public Date getEndTime() {
		return endTime;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		// For eksempel. Bare for at man ikke skal være helt dust og sette dritlang description
		if(description.length() < 255){
			if(this.description != null){
				fireChange("description");				
			}
			this.description = description;
		} else{
			System.out.println("Error. Beskrivelsen er for lang");
		}
	}

	public Room getRoom() {
		return room;
	}
	
	public Collection<Employee> getPeopleInvited() {
		return peopleInvited;
	}
	
	public Collection<Employee> getPeopleDeclined() {
		return peopleDeclined;
	}
	
	public Collection<Employee> getPeopleGoing() {
		return peopleGoing;
	}
	
	public Employee getCreator() {
		return creator;
	}
	
	// Skal vi ha støtte for dette? Må bare kalles med rom man VET er ledige, ref findLocation() i kalender-klassen
	public void setRoom(Room room) {
		if(this.room == null){
			this.room = room;			
		}
	}
	
	public void fireChange(String attribute){
		for (Employee employee : peopleInvited) {
			employee.reactOnUpdate(this, attribute);
		}
	}
	
	public void addEmployee(Employee employee) {
		this.peopleInvited.add(employee);
	}
	
	// Dette er en hjelpemetode for employee.removeEvent() og Employee.cancelEvent() og boer ikke kalles andre steder enn der (da faar vi inkonsistens)
	public boolean removeEmployee(Employee employee){
		if (peopleInvited.contains(employee)){
			Message msg = new Message(employee, this.creator, false, "Endringen av eventen har gjort at jeg dessverre ikke kan delta", "Varsel om at jeg ikke kan delta");
			msg.sendMessage();
			return peopleInvited.remove(employee);
		}
		return false;
	}
	
	@Override
	public String toString() {
		String str = this.title + " har foelgende deltakere " + peopleGoing.toString() + "og avholdes kl: " + this.getHour() + ":";
		if (Integer.parseInt(this.getMinute()) < 10){
			str += "0";
		}
		return  str + this.getMinute() + ", den " + this.getDay() + ". " + this.getMonth();		// maaneden blir ikke omgjort til streng som oenskelig :(
	}
	
	// Metodene nedenfor brukes i compareTo()-metoden
	public String getYear(){		// det du har gjort her med loCaleToString() osv fungerer ikke. N� klarer jeg ikke skrive ut Events. F�r NullPointerException :(
		return getStartTime().toLocaleString().substring(7, 11);
	}
	public String getMonth(){
		return getStartTime().toLocaleString().substring(3, 6);
	}
	public String getDay(){
		return getStartTime().toLocaleString().substring(0, 2);
	}
	public String getHour(){
		return getStartTime().toLocaleString().substring(12, 14);
	}
	public String getMinute(){
		return getStartTime().toLocaleString().substring(15, 17);
	}
	
	@Override
	public int compareTo(Event event) {
		if(this.getYear().equals(event.getYear())){
			if(this.getMonth().equals(event.getMonth())){
				if(this.getDay().equals(event.getDay())){
					if(this.getHour().equals(event.getHour())){
						if(this.getMinute().equals(event.getMinute())){
							return 0;
						} else if(Integer.parseInt(this.getMinute()) < Integer.parseInt(event.getMinute())){
							return -1;
						} else{
							return 1;
						}
					} else if(Integer.parseInt(this.getHour()) < Integer.parseInt(event.getHour())){
						return -1;
					} else{
						return 1;
					}
				} else if(Integer.parseInt(this.getDay()) < Integer.parseInt(event.getDay())){
					return -1;
				} else{
					return 1;
				}
			} else if(Integer.parseInt(this.getMonth()) < Integer.parseInt(event.getMonth())){
				return -1;
			} else{
				return 1;
			}
		} else if(Integer.parseInt(this.getYear()) < Integer.parseInt(event.getYear())){
			return -1;
		} else{
			return 1;
		}
	}
	
}