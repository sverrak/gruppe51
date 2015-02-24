package Code;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;

public class Event implements Comparable<Event>{
	private String title;
	private Date startTime;
	private Date endTime;
	private String description;
	private Room room;
	private Collection<Employee> peopleInvited;
	private Collection<Employee> peopleGoing;
	private Collection<Employee> peopleDeclined;
	private Employee creator;
	
	
	public Event(String title, Date startTime, Date endTime,
			String description, Employee creator) {
		super();
		this.title = title;
		this.startTime = startTime;
		this.endTime = endTime;
		this.description = description;
		this.room = null;
		this.peopleInvited = new ArrayList<Employee>();
		this.peopleGoing = new ArrayList<Employee>();
		this.peopleDeclined = new ArrayList<Employee>();
		this.creator = creator;
	}

	public void fireChange(){
		
	}
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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
	
	// Skal vi ha støtte for dette?
	public void setRoom(Room room) {
		this.room = room;
	}

	public void employeeAcceptedInvitation(Employee employee){
		peopleGoing.add(employee);
	}
	
	public void employeeDeclinedInvitation(Employee employee){
		peopleDeclined.add(employee);
	}
	
	public Room findLocation(){
		
		return null;
	}
	
	public boolean inviteEmployee(Employee sender, Employee receiver){
		
		Message msg = new Message(sender, receiver);
		
	}
	// Ikke implementert
	public boolean removeEmployee(){
		return true;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return this.title;
	}
	
	
	// Metodene nedenfor brukes i compareTo()-metoden
	public String getYear(){
		return getStartTime().toString().substring(0, 4);
	}
	public String getMonth(){
		return getStartTime().toString().substring(5, 7);
	}
	public String getDay(){
		return getStartTime().toString().substring(8, 10);
	}
	public String getHour(){
		return getStartTime().toString().substring(11, 13);
	}
	public String getMinute(){
		return getStartTime().toString().substring(14, 16);
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