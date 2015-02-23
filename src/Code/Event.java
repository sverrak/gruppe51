package Code;

import java.util.Collection;
import java.util.Date;

public class Event {
	private String title;
	private Date startTime;
	private Date endTime;
	private String description;
	private String image;
	private Room room;
	
	public Event(String title, Date startTime, Date endTime,
			String description, String image) {
		super();
		this.title = title;
		this.startTime = startTime;
		this.endTime = endTime;
		this.description = description;
		this.image = image;
		this.room = null;
	}

	public void fireChange(){
		
	}
	
	public Collection<Room> findLocation(){
		
		return null;
	}
	
	public boolean addEmployee(){
		return true;
	}
	
	public boolean removeEmployee(){
		return true;
	}
	
}