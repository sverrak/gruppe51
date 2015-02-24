package Code;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

public class Event {
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
	
	public void employeeAcceptedInvitation(Employee employee){
		peopleGoing.add(employee);
	}
	
	public void employeeDeclinedInvitation(Employee employee){
		peopleDeclined.add(employee);
	}
	
	public Room findLocation(){
		
		return null;
	}
	
	public boolean inviteEmployee(){
		return true;
	}
	
	public boolean removeEmployee(){
		return true;
	}
	
}