package Code;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class Group {
	private int groupID;
	private String name;
	private String description;
	private Employee responsible;
	private Collection<Employee> participants;
	private Collection<Group> subgroups;
	private Collection<Group> supergroups;
	private List<Event> upcomingEvents;
	
	
	public Group(String groupName, String description, Employee responsible) {
		this.name = groupName;
		this.description = description;
		this.responsible = responsible;
		participants = new ArrayList<Employee>();
		participants.add(responsible);
		subgroups = new ArrayList<Group>();
		supergroups = new ArrayList<Group>();
		upcomingEvents = new ArrayList<Event>();
	}
	
	public Group(int groupID, String groupName, String description, Employee responsible) {
		this.groupID = groupID;
		this.name = groupName;
		this.description = description;
		this.responsible = responsible;
		participants = new ArrayList<Employee>();
		participants.add(responsible);
		subgroups = new ArrayList<Group>();
		supergroups = new ArrayList<Group>();
		upcomingEvents = new ArrayList<Event>();
	}
	
	public List<Event> getUpcomingEvents() {
		return upcomingEvents;
	}
	public ArrayList<ArrayList<String>> generateWeeklySchedule(int weekOfYear, int year){
		ArrayList<ArrayList<String>> schedule = Employee.generateEmptySchedule();
		// Get calendar, clear it and set week number and year.
		Calendar calendar = Calendar.getInstance();
		calendar.clear();					// holder dette? Se ovenfor dersom insufficient
		calendar.set(Calendar.WEEK_OF_YEAR, weekOfYear);
		calendar.set(Calendar.YEAR, year);
		long timeStartWeek = calendar.getTimeInMillis();	//returnerer tidspunkt p� starten av uka i millisec
		calendar.add(Calendar.WEEK_OF_YEAR, 1);
		long timeEndWeek = calendar.getTimeInMillis();
		
		// legger til upcomingEvents i ukeplanen
		for (Event event : upcomingEvents) {
			if (event.getStartTime().getTime() > timeStartWeek && event.getStartTime().getTime() < timeEndWeek){
				int col = event.getStartTime().getDay() -1;					//	(index til kolonne i matrix) ma sjekke hvilken verdi hver dag retiurnerer
				int firstRow = (event.getStartTime().getHours() - 8) * 2; 	//	(index til rad i matrix)
				if (event.getStartTime().getMinutes() >= 30){
					firstRow += 1;
				}
				int lastRow = (event.getEndTime().getHours() - 8) * 2; 	//	(index til rad i matrix)
				if (event.getEndTime().getMinutes() >= 30){
					lastRow += 1;
				}
				int num_declined = event.getPeopleDeclined().size();
				for (int i = firstRow; i < lastRow; i++) {		 // for alle slots fra firstRow til lastRow
					if (schedule.get(i).equals("")){
						schedule.get(i).set(col, event.getTitle() + " [" + num_declined + "]");
					}
					else{
					schedule.get(i).set(col, schedule.get(i).get(col) + "#" + event.getTitle() + " [" + num_declined + "]"); // matrix[rad i ][col] = event.getName() + "U"	// U'en er for attending
					}
				}
			}			
		}
		return schedule;
	}
	// her skrives alle events som gruppa er i invitert til ut til konsollen uavhengig om brukeren har declinet eller ikke
	public void printWeeklySchedule(int weekOfYear, int year){
		ArrayList<ArrayList<String>> schedule = generateWeeklySchedule(weekOfYear, year);	
		
		String str = "|08:00|--------S�NDAG----------+----------MANDAG--------+---------TIRSDAG--------+--------ONSDAG----------+-----------TORSDAG------+---------FREDAG---------+---------L�RDAG---------+\n";
		for (int row = 0; row < 32; row++) {
		str += "|+++++|";
			List<ArrayList<String>> rader = new ArrayList<ArrayList<String>>();
			ArrayList<String> first_row = new ArrayList(Arrays.asList("","","", "", "", "", ""));
			rader.add(first_row);
			String[] r = new String[7];
			int max_num_of_events_at_once = 0;				// ant. rader vi trenger til halvtimen
			for (int col = 0; col < 7; col++){
				r[col] = schedule.get(row).get(col);		// gj�r om raden til String[]
				
				
				if (!r[col].equals("")){
					String[] entry = r[col].split("#");			// deler opp events som kr�sjer i timeplanen
					if (entry.length > max_num_of_events_at_once){
						while (entry.length > max_num_of_events_at_once){
							ArrayList<String> new_row = new ArrayList(Arrays.asList("","","", "", "", "", ""));
							max_num_of_events_at_once++;
							rader.add(new_row);
						}
					}
					for (int i = 0; i < max_num_of_events_at_once; i++) {
						rader.get(i).set(col, entry[i]);
					}
				} else{
					rader.get(0).set(col, r[col]);
				}
			}
			for (int rad = 0; rad < rader.size(); rad++) {
				for (int column = 0; column < 7; column++) {
					String small_entry = rader.get(rad).get(column).trim();
					str += small_entry;
					int num_of_spaces = 24 - small_entry.length();
					for (int i = 0; i < num_of_spaces; i++) {
						str += " ";
					}
					str += "|";		// legger til noen stolper ekstra til h�yre for kalenderen, dessverre
				}
				if (rad < rader.size()-2){
					str += "\n|+++++|";
				}
			}
			str += "\n|";
			if (row < 3){
				str += "0";
			}
			if ( row % 2 == 0){
				str += ((8 + row/2) + ":30");
				} else{
					str += (9 + row/2) + ":00";
					}
			
			str += "|------------------------+------------------------+------------------------+------------------------+------------------------+------------------------+------------------------+\n";
		}
		System.out.println(str);
	}
	
	public Collection<Group> getSubgroups() {
		return subgroups;
	}public Collection<Group> getSupergroups() {
		return supergroups;
	}
	
	public int getGroupID() {
		return groupID;
	}
	
	public String getName() {
		return name;
	}

	public void setGroupName(String groupName) {
		this.name = groupName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Employee getResponsible() {
		return responsible;
	}

	public Collection<Employee> getParticipants() {
		return participants;
	}

	public void setResponsible(Employee responsible) {
		this.responsible = responsible;
	}
	
// "Grupper administreres utenfor systemet..." - kravlista fra DB.
	public void addEmployee(Employee employee){
		participants.add(employee);
	}
	public void removeEmployee(Employee employee){
		participants.remove(employee);
	}
	
	//hva er hensikten med denne metoden??
	public ArrayList<Employee> printParticipants(){
		return (ArrayList<Employee>) this.participants;
	}
	
	public void addSubgroupToGroup(Group group){
		this.subgroups.add(group);
		group.addGroupToSupergroup(this);
	}
	
	// Skal bare kalles fra funksjonen over
	private void addGroupToSupergroup(Group group){
		this.supergroups.add(group);
		group.addSubgroupToGroup(this);
	}
	@Override
	public String toString() {
		return this.name;
	}
	
	
}
