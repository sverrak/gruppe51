package Code;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import Code.Event;

public class Event implements Comparable<Event>{
	private int eventID;
	private String title;
	private Date startTime;
	private Date endTime;
	private String description;
	private Room room;
	private Employee creator;
	private List<Employee> peopleInvited;
	private List<Employee> peopleGoing;
	private List<Employee> peopleDeclined;
	
	private static ArrayList<String> months = new ArrayList(Arrays.asList("jan","feb","mar", "apr", "may", "jun", "jul", "aug", "sep", "oct", "nov", "dec"));
	
	
	public Event(int eventID, String title, Date startTime, Date endTime,
			String description, Employee creator) {
		super();
		setTitle(title);
		this.creator = creator;
		this.eventID = eventID;
		//setTime(startTime, endTime);
		this.startTime = startTime;
		this.endTime = endTime;
		setDescription(description);
		this.room = null;
		this.peopleInvited = new ArrayList<Employee>();
		this.peopleGoing = new ArrayList<Employee>();
		this.peopleDeclined = new ArrayList<Employee>();
		
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
	public int getEventID(){
		return eventID;
	}
	
	public List<Employee> getPeopleInvited() {
		return peopleInvited;
	}
	
	public List<Employee> getPeopleDeclined() {
		return peopleDeclined;
	}
	
	public List<Employee> getPeopleGoing() {
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
			Message msg = new Message(employee, this.creator, "Endringen av eventen har gjort at jeg dessverre ikke kan delta", "Varsel om at jeg ikke kan delta");
			msg.sendMessage();
			return peopleInvited.remove(employee);
		}
		return false;
	}
	
	@Override
	public String toString() {
		String str = this.title + " har foelgende deltakere " + peopleGoing.toString() + "og avholdes kl: " + this.getHour() + ":";
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
		return (this.startTime.getTime() < event.getStartTime().getTime() ? -1 : 1);
	}
	
}