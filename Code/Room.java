package Code;

import java.util.ArrayList;
import java.util.Collection;

public class Room {
	private String name;
	private int capacity;
	private String description;
	private Collection<Event> roomSchedule;
	
	public Room(String name, int capacity, String description) {
		super();
		this.name = name;
		this.capacity = capacity;
		this.description = description;
		this.roomSchedule = new ArrayList<Event>();
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getCapacity() {
		return capacity;
	}
	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	
}
