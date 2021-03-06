package Code;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.Calendar;

public class Employee {
	private Boolean admin; // la til adminobjekt
	private int employeeID;
	private String name;
	private String position;
	private String username;
	private String password;
	private Collection<Group> groups;
	private List<Event> upcomingEvents;		//sortert paa startTime
	private List<Event> declinedEvents;	//boer ogs� sorteres paa startTime
	private List<Event> eventsAttending;		// sortert paa startTime. Maa gaa over alt og kanskje endre fra upcomingEvents til eventsAttending
	private int telnum;
	private List<Message> inbox;
	private List<Event> hiddenEvents;	// events skjult i kalendervisningen. De befinner seg ogs� i declinedEvents
	private ConnectionToDatabase ctd = new ConnectionToDatabase();
	
	// GreatestEmployeeID er String for at vi skal skille mellom de to konstruktørene.
	public Employee(String greatestEmployeeID, String name, String position, String username, 
			String password, int telnum, Boolean admin) { // endret konstruktoren til aa ta in admin(true/false)
		super();
		this.employeeID = Integer.parseInt(greatestEmployeeID);
		this.name = name;
		this.position = position;
		this.username = username;
		this.password = password;
		this.telnum = telnum;
		this.admin = admin;
		this.inbox = new ArrayList<Message>();
		groups = new ArrayList<Group>();
		upcomingEvents = new ArrayList<Event>();
		declinedEvents = new ArrayList<Event>();
		eventsAttending = new ArrayList<Event>();
		hiddenEvents = new ArrayList<Event>();
	}
	
	public Employee(int employeeID, String name, String position, String username, 
			String password, int telnum, Boolean admin) { // endret konstruktoren til aa ta in admin(true/false)
		super();
		this.employeeID = employeeID;
		this.name = name;
		this.position = position;
		this.username = username;
		this.password = password;
		this.telnum = telnum;
		this.admin = admin;
		this.inbox = new ArrayList<Message>();
		groups = new ArrayList<Group>();
		upcomingEvents = new ArrayList<Event>();
		declinedEvents = new ArrayList<Event>();
		eventsAttending = new ArrayList<Event>();
	}
	
	public List<Event> getEventsAttending() {
		return eventsAttending;
	}
	public List<Event> getDeclinedEvents() {
		return declinedEvents;
	}
	public Collection<Group> getGroups() {
		return groups;
	}
	public List<Event> getUpcomingEvents() {
		return upcomingEvents;
	}
	public List<Event> getHiddenEvents() {
		return hiddenEvents;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getEmployeeID(){
		return employeeID;
	}
	public String getPosition() {
		return position;
	}
	public void setPosition(String position) {
		this.position = position;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public void setTelnum(int telnum) {
		this.telnum = telnum;
	}
	public int getTelnum(){
		return telnum;
	}
	public Boolean isAdmin(){
		return admin;
	}
	public void setAdmin(Boolean admin){
		this.admin = admin;
	}
	
		
	//Returnerer true hvis ansatt ble lagt til i gruppen
	public void joinGroup(Group group){
			groups.add(group);
			group.addEmployee(this);
	}

// skal opprette nytt event med personen som inviterer som attending
// hva brukes den til?
	/*
	public void addEvent(Event event){
		event.getPeopleGoing().add(this);
/*		if(eventsAttending != null){		// hva er dette godt for?

			if (eventsAttending.size() == 0){
				eventsAttending.add(event);
			}else{ 
				if(isAvailable(event.getStartTime(), event.getEndTime())){
					eventsAttending.add(event);	
				}
	}
*/
	
	public Event createEvent(String title, Date startTime, Date endTime, String description){
		if (eventsAttending.size() == 0 || this.isAvailable(startTime, endTime)){
			Event event = new Event(title, startTime, endTime, description, this);
			eventsAttending.add(event);
			Collections.sort(eventsAttending);
			event.getPeopleGoing().add(this);
			return event;
		}
		System.out.println("Du er opptatt paa tidspunktet. " + title + " ble ikke opprettet.");
		return null;
	}
	
	// returnerer true hvis ja-svar ble sendt, false ellers
	public boolean acceptInvitation(Event event){
		if (! upcomingEvents.contains(event)){
			return false;
		}
		if(! this.isAvailable(event.getStartTime(), event.getEndTime())){
			System.out.println("Du er opptatt paa dette tidspunktet");		// kan vurderes fjernes. Faar se hva vi trenger	
			return false;
		}
		event.getPeopleGoing().add(this);
		eventsAttending.add(event);
		Collections.sort(eventsAttending);
		upcomingEvents.remove(event);
		Message msg = new Message(this, event.getCreator(),"I accept your invitation to event " + event.getTitle(), "Invitation Accept");
		msg.sendMessage();
		return true;
	}
	
	// returnerer true hvis event ble fjernet, false dersom event ikke i upcomingEvents
	public boolean declineInvitation(Event event){
		if (upcomingEvents.contains(event)){
			event.getPeopleDeclined().add(this);
			event.getPeopleInvited().remove(this);
			this.upcomingEvents.remove(event);
			this.declinedEvents.add(event);
			Message msg = new Message(this, event.getCreator(),"I decline your invitation to event " + event.getTitle(), "Invitation Decline");
			msg.sendMessage();
			return true;
		}
		return false;
	}
// returnerer true hvis employee-objektet er tilgjengelig i tidsrommet
	public boolean isAvailable(Date startTime, Date endTime){
		if(this.eventsAttending.size() == 0){
			return true;
		} else if(eventsAttending.get(0).getStartTime().compareTo(endTime) > 0){
			return true;
		} 		
		for (int i = 0; i < eventsAttending.size()-1; i++) {
			if(eventsAttending.get(i).getEndTime().compareTo(startTime) < 0 && endTime.compareTo(eventsAttending.get(i+1).getStartTime()) < 0){ // fortegn her virker galt. Motsatt tegn f�r hele dritten til � henge, men det skyldes kanskje feil i compareTo
				return true;
			}
		}
		if (eventsAttending.get(eventsAttending.size()-1).getEndTime().compareTo(startTime) < 0){
			return true;
		}
		return false;
	}
	// Ikke ferdig
	public boolean cancelEvent(Event event, String reason){
		if (event.getCreator() != this){
			return false;
		}
		informAboutCancellation(event, reason);
		
		if (event.getPeopleInvited().size()>0){
			for (int i = event.getPeopleInvited().size()-1; i > -1; i--) {
				Employee employee = event.getPeopleInvited().get(i);
				employee.removeEvent(event, false);
			}
		}
		if (event.getPeopleGoing().size()>0){
			for (int i = event.getPeopleGoing().size()-1; i > -1; i--) {
				Employee employee = event.getPeopleGoing().get(i);
				employee.removeEvent(event, false);
			}
		}
		
		if (event.getRoom()!= null){
			event.getRoom().getRoomSchedule().remove(event);
		}
		return true;
	}
	
	// Dette fjerner employeens deltakelse paa eventen. 
		private void removeEvent(Event event, Boolean isOnDemandFromParticipant){
			upcomingEvents.remove(event);	// fjernes hvis event ligger der
			eventsAttending.remove(event);	// fjernes hvis event ligger der
			if(isOnDemandFromParticipant){
				Message msg = new Message(this, event.getCreator(), "Endringen av eventen har gjort at jeg dessverre ikke kan delta", "Varsel om at jeg ikke kan delta");
				msg.sendMessage();	
				declinedEvents.add(event);		//la til dette. Haaper ikke noe f�kkes opp...
				Collections.sort(declinedEvents);
				event.getPeopleDeclined().add(this);
				event.getPeopleInvited().remove(this);
				event.getPeopleGoing().remove(this);
			}
		}
		
	public void reactOnUpdate(Event event, String attribute, String oldField){
		System.out.println("Det har skjedd en endring av ");
		System.out.println(attribute + " i eventen " + event.toString());
		if (attribute.equals("tid")){
			System.out.println("Tidspunkt endret fra: " + oldField +"\ntil: " + event.getStartTime() + " - " + event.getEndTime());
		}else if (attribute.equals("title")){
			System.out.println("Navnet p� eventet ble endret fra: " + oldField +  " til: " + event.getTitle());
		}else if (attribute.equals("description")){
			System.out.println("Beskrivelsen p� eventet " + event + " ble endret fra: " + oldField + " til " + event.getDescription());
		}else{
			System.out.println("Update type not recognized. No update happened to " + event);
			return;
		}
		System.out.println("\n + oensker du aa fjerne eventet paa bakgrunn av dette? (ja/nei)");
		Scanner scanner = new Scanner(System.in);
		String input = scanner.next(); 
		while (! (input.equals("ja") || input.equals("nei"))){
			System.out.println("Vennligst svar [ja] for aa fjerne eller [nei] for aa la det staa.");
			input = scanner.next(); 
		}
		scanner.close();

		if(input.equals("ja")){
			removeEvent(event, true);
			informAboutCantParticipate(event, attribute);
		}
	}
	
	//informerer creator om at vedkommende ikke kan delta paa eventen
	private void informAboutCantParticipate(Event event, String reason){
		Message msg = new Message(this, event.getCreator(), "Jeg kan dessverre ikke delta pga " + reason, "Avmelding paa " + event.getTitle());
		msg.sendMessage();
	}
	
	private void informAboutCancellation(Event event, String reason){
		for (Employee participant : event.getPeopleGoing()) {
			Message msg = new Message(this, participant, "Jeg har sett meg nodt til aa avlyse eventen pga " + reason, event.getTitle() + " er avlyst.");
			msg.sendMessage();
		}
		for (Employee participant : event.getPeopleInvited()) {
			Message msg = new Message(this, participant, "Jeg har sett meg nodt til aa avlyse eventen pga " + reason, event.getTitle() + " er avlyst.");
			msg.sendMessage();
		}
	}
	
	public void printInbox(){
		System.out.println("ID\tLest\t\tFra\t\tEmne");
		for (int i = 0; i < inbox.size(); i++) {
			if(inbox.get(i).isRead()){
				System.out.println("" + i + "\t[X]\t\t" + inbox.get(i).getSender() + "\t\t" + inbox.get(i).getSubject());
			} else{
				System.out.println("" + i + "\t[ ]\t\t" + inbox.get(i).getSender() + "\t\t" + inbox.get(i).getSubject());

			}
		}
	}
	
	
	public List<Message> getInbox() {
		return inbox;
	}
			
	public boolean inviteEmployeeToEvent(Employee employee, Event event){
		if (event.getCreator() != this){
			return false;
		} else if(event.getPeopleDeclined().contains(employee) || event.getPeopleGoing().contains(employee) || event.getPeopleInvited().contains(employee)){
			return false;
		}
		
		Message msg = new Message(this, employee, "Jeg har invitert deg til eventet: " + event.getTitle() + ", som begynner: " + event.getStartTime(), "Invitasjon til " + event.getTitle());
		msg.sendMessage();

//		employee.printInbox();
		
		// "hvis eventen er upcoming"-funksjonalitet mangler her. m.a.o. hvis eventets tidspunkt er passert, er invitasjonen uinteressant 
		event.addEmployee(employee);
		employee.upcomingEvents.add(event);
		Collections.sort(upcomingEvents);
		return true;
	}

	
	public boolean inviteGroupToEvent(Group group, Event event){
		if (event.getCreator() != this){
			return false;
		}
		for (Employee participant : group.getParticipants()) {
			inviteEmployeeToEvent(participant, event);
		}
		return true;
	}
	
	// hva er hensikten med denne? HVorfor skal man sende melding til seg selv?
	public void addMessageToInbox(Message message) {
		this.inbox.add(message);	
	}
	
	
	// Ikke implementert ferdig. Her maa vi finne en annen loesning paa hvordan vi gjoer removeEvent(). Per naa skrives en melding til 
	public boolean withdrawInvitation(Employee employee, Event event){
		if (event.getCreator() != this){
			return false;
		}
		if(! (employee.getUpcomingEvents().contains(event) || employee.getEventsAttending().contains(event))){
			return false;
		}
		employee.removeEvent(event, true);
		return true;
	}
	
	// oppretter "tom" matrise for ukeplan. Alle felter er 0
	public static ArrayList<ArrayList<String>> generateEmptySchedule(){
		ArrayList<ArrayList<String>> matrix= new ArrayList<ArrayList<String>>();
		for (int row = 0; row < 32; row++) {
			matrix.add(new ArrayList<String>());
			for (int col = 0; col < 7; col++) {
				matrix.get(row).add("");
			}
		}
		return matrix;
	}
		
	public ArrayList<ArrayList<String>> generateWeeklySchedule(int weekOfYear, int year){
			ArrayList<ArrayList<String>> schedule = generateEmptySchedule();
			// Get calendar, clear it and set week number and year.
			Calendar calendar = Calendar.getInstance();
			calendar.clear();					// holder dette? Se ovenfor dersom insufficient
			calendar.set(Calendar.WEEK_OF_YEAR, weekOfYear);
			calendar.set(Calendar.YEAR, year);
			long timeStartWeek = calendar.getTimeInMillis();	//returnerer tidspunkt p� starten av uka i millisec
			calendar.add(Calendar.WEEK_OF_YEAR, 1);
			long timeEndWeek = calendar.getTimeInMillis();
			
			// legger til eventsAttending i ukeplanen
			for (Event event : eventsAttending) {
				if (event.getStartTime().getTime() > timeStartWeek && event.getStartTime().getTime() < timeEndWeek){
					int col = event.getStartTime().getDay() -1;					//	(index til kolonne i matrix) ma sjekke hvilken verdi hver dag retiurnerer
					int firstRow = (event.getStartTime().getHours() - 8) * 2; 	//	(index til rad i matrix)
					if (event.getStartTime().getMinutes() >= 30){
						firstRow += 1;
					}
					int lastRow = (event.getEndTime().getHours() - 8) * 2; 	//	(index til rad i matrix
					if (event.getEndTime().getMinutes() >= 30){
						lastRow += 1;
					}
					int num_declined = event.getPeopleDeclined().size();
					for (int i = firstRow; i < lastRow; i++) {		 // for alle slots fra firstRow til lastRow
						schedule.get(i).set(col, event.getTitle() + " (A) [" + num_declined + "]"); // matrix[rad i ][col] = event.getName() + "A"		// A'en er for attending  // [num_declined] for ant. som har declined eventet
					}
				}
			}
			// legger til upcomingEvents i ukeplanen
			for (Event event : upcomingEvents) {
				if (event.getStartTime().getTime() > timeStartWeek && event.getStartTime().getTime() < timeEndWeek){
					int col = event.getStartTime().getDay() -1;					//	(index til kolonne i matrix) ma sjekke hvilken verdi hver dag retiurnerer
					int firstRow = (event.getStartTime().getHours() - 8) * 2; 	//	(index til rad i matrix)
					if (event.getStartTime().getMinutes() >= 30){
						firstRow += 1;
					}
					int lastRow = (event.getEndTime().getHours() - 8) * 2; 	//	(index til rad i matrix
					if (event.getEndTime().getMinutes() >= 30){
						lastRow += 1;
					}
					int num_declined = event.getPeopleDeclined().size();
					for (int i = firstRow; i < lastRow; i++) {		 // for alle slots fra firstRow til lastRow
						if (schedule.get(i).equals("")){
							schedule.get(i).set(col, event.getTitle() + " (U) [" + num_declined + "]");
						}
						else{
						schedule.get(i).set(col, schedule.get(i).get(col) + "#" + event.getTitle() + " (U) [" + num_declined + "]"); // matrix[rad i ][col] = event.getName() + "U"	// U'en er for attending
						}
					}
				}			
			}
			for (Event event : declinedEvents) {
				if(hiddenEvents.contains(event)){
					break;
				}
				if (event.getStartTime().getTime() > timeStartWeek && event.getStartTime().getTime() < timeEndWeek){
					int col = event.getStartTime().getDay() -1;					//	(index til kolonne i matrix) ma sjekke hvilken verdi hver dag retiurnerer
					int firstRow = (event.getStartTime().getHours() - 8) * 2; 	//	(index til rad i matrix)
					if (event.getStartTime().getMinutes() >= 30){
						firstRow += 1;
					}
					int lastRow = (event.getEndTime().getHours() - 8) * 2; 	//	(index til rad i matrix
					if (event.getEndTime().getMinutes() >= 30){
						lastRow += 1;
					}
					int num_declined = event.getPeopleDeclined().size();
					for (int i = firstRow; i < lastRow; i++) {		 // for alle slots fra firstRow til lastRow
						if (schedule.get(i).equals("")){
							schedule.get(i).set(col, event.getTitle() + " (D) [" + num_declined + "]");
						}
						else{
						schedule.get(i).set(col, schedule.get(i).get(col) + "#" + event.getTitle() + " (U) [" + num_declined + "]"); // matrix[rad i ][col] = event.getName() + "U"	// U'en er for attending
						}
					}
				}			
			}
			return schedule;
		}

	
	//skal gi en visning i konsollen av innevaerende ukes plan soen-loer
	//mangler aa vise declined events som ikke er hidden!
	public void printWeeklySchedule(int weekOfYear, int year){
			ArrayList<ArrayList<String>> schedule = generateWeeklySchedule(weekOfYear, year);	
			
			String str = "|08:00|----------SONDAG------------+-----------MANDAG-----------+-----------TIRSDAG----------+----------ONSDAG------------+-------------TORSDAG--------+-----------FREDAG-----------+-----------LORDAG-----------+\n";
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
						int num_of_spaces = 28 - small_entry.length();
						for (int i = 0; i < num_of_spaces; i++) {
							str += " ";
						}
						str += "|";		// legger til noen stolper ekstra til h�yre for kalenderen
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
				
				str += "|----------------------------+----------------------------+----------------------------+----------------------------+----------------------------+----------------------------+----------------------------+\n";
			}
			System.out.println(str);
		}
		
		// returnerer true hvis endring skjedde; false ellers
	public boolean changeEventTime(Event event,Date startTime, Date endTime){		// endre til boolean?
		if (event.getCreator() != this || startTime.getTime() > endTime.getTime()){
			return false;
		}
		List<Event> oldEventsAttending = eventsAttending;
		eventsAttending.remove(event);
		
		if (this.isAvailable(startTime, endTime)){
			event.setTime(startTime, endTime);
			eventsAttending.add(event);
			Collections.sort(eventsAttending);
			return true;
		}else{
			eventsAttending = oldEventsAttending;
			return false;
		}
	}
	
	public boolean postponeEvent(Event event, int minutes){
		Calendar calStart = Calendar.getInstance(); // creates calendar
	    calStart.setTime(event.getStartTime()); // sets calendar time/date
	    calStart.add(Calendar.MINUTE, minutes); // adds minutes to the time
	    
	    Calendar calEnd = Calendar.getInstance(); // creates calendar
	    calEnd.setTime(event.getEndTime()); // sets calendar time/date
	    calEnd.add(Calendar.MINUTE, minutes); // adds minutes to the time
		
	    Date startTime = new Date();
	    Date endTime = new Date();
	    startTime.setTime(calStart.getTimeInMillis());
	    endTime.setTime(calEnd.getTimeInMillis());
	    
		return changeEventTime(event, startTime, endTime);
	}
		
	@Override
	public String toString() {
		return this.name;
	}

	public int countUnreadMessages() {
		int counter = 0;
		for (Message msg : inbox) {
			if (! msg.isRead()){
				counter += 1;
			}
		}
		return counter;
	}
	
	public void hideEvent(Event event){
		if(declinedEvents.contains(event)){
			hiddenEvents.add(event);
			Collections.sort(hiddenEvents);
		}
	}
	public void showDeclinedEvent(Event event){
		if (hiddenEvents.contains(event)){
			hiddenEvents.remove(event);
		}
	}
	
	public Group createGroup(String groupName, String description) {
		return new Group(groupName, description, this);
	}
	public void addEmployeeToGroup(Employee employee, Group group){
		if (group.getResponsible() == this){
			group.getParticipants().add(employee);
		}else{
			System.out.println("you are not authorized to administer participants og group: " + group.getName());
		}
	}
	public void removeEmployeeFromGroup(Employee employee, Group group){
		if (group.getResponsible() == this){
			group.getParticipants().remove(employee);
		}else{
			System.out.println("you are not authorized to administer participants og group: " + group.getName());
		}
	}
	public Event createGroupEvent(String title, Date startTime, Date endTime, String description, Group group){
		if (eventsAttending.size() == 0 || isAvailable(startTime, endTime)){
			Event event = new Event(title, startTime, endTime, description, this);
			eventsAttending.add(event);
			event.getPeopleGoing().add(this);
			Collections.sort(eventsAttending);
			group.getUpcomingEvents().add(event);
			Collections.sort(group.getUpcomingEvents());
			for (Employee employee : group.getParticipants()){
				employee.getUpcomingEvents().add(event);
				Collections.sort(employee.getUpcomingEvents());
			}
			return event;
		}else{
			System.out.println("Du er opptatt paa tidspunktet. " + title + " ble ikke opprettet.");
			return null;
		}
	}
	
	public void printGroupSchedule(Group group, int weekOfYear, int year){
		if (! group.getParticipants().contains(this)){
			System.out.println("You are not in this group!");
			return;
		}else{
			group.printWeeklySchedule(weekOfYear, year);
		}
	}

	public void changeYourMind(Event event) {
		if(this.getEventsAttending().contains(event)){
			event.getPeopleGoing().remove(this);
			event.getPeopleDeclined().add(this);
			this.eventsAttending.remove(event);
			this.declinedEvents.add(event);
		}
	}
}
