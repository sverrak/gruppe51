package Code;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

public class Room {
	private String name;
	private int capacity;
	private String description;
	private List<Event> roomSchedule;
	
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
	
	private Boolean isAvailable(Date startTime, Date endTime){
		for (int i = 0; i < roomSchedule.size() - 1; i++) {
			if(roomSchedule.get(i).getEndTime().compareTo(startTime) < 0 && endTime.compareTo(roomSchedule.get(i+1).getStartTime()) < 0){
				return true;
			}
		}
		
		return false;	
	}
	// returnerer true hvis det gikk bra
	public Boolean addEventToRoom(Event event){
		if (isAvailable(event.getStartTime(), event.getEndTime())){
			roomSchedule.add(event);
			roomSchedule.sort(c);
		} else{
			
		}
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return this.name;
	}
	
}
