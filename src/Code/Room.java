package Code;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

public class Room {
	private int roomID;
	private String name;
	private int capacity;
	private String description;
	public List<Event> roomSchedule = new ArrayList<Event>();
	
	public Room(int roomID, String name, int capacity, String description) {
		super();
		this.roomID = roomID;
		this.name = name;
		this.capacity = capacity;
		this.description = description;
		this.roomSchedule = new ArrayList<Event>();
	}
	
	public int getRoomID(){
		return roomID;
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
	
	public List<Event> getRoomSchedule() {
		return roomSchedule;
	}
	
	// returnerer true hvis det gikk bra. Ikke implementert ferdig
	public Boolean addEventToRoom(Event event){
		if (isAvailable(event.getStartTime(), event.getEndTime())){
			roomSchedule.add(event);
		//	System.out.println(event.getTitle() + ", was added to: " + this.getName());
			//Hvordan sortere roomSchedule på en hensiktsmessig måte?
			//roomSchedule.sort(c);
			return true;
		}
		return false;	
	}
	
	public boolean removeEvent(Event event){
		if (roomSchedule.contains(event)){
			roomSchedule.remove(event);
			return true;
		}
		return false;
	}
	
	public Boolean isAvailable(Date startTime, Date endTime){
		
		if(this.roomSchedule.size() == 0){
			return true;
		}
		for (int i = 0; i < roomSchedule.size() - 1; i++) {
			if(roomSchedule.get(i).getEndTime().compareTo(startTime) < 0 && endTime.compareTo(roomSchedule.get(i+1).getStartTime()) < 0){
				return true;
			}
		}
		return false;	
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return this.name;
	}
	
}
