package Code;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.Collection;



public class ConnectionToDatabase {

	  private ResultSet resultSet = null; 
	  private ArrayList<ResultSetMetaData> metaData = new ArrayList<ResultSetMetaData>();
	  private ArrayList<ResultSet> resultData = new ArrayList<ResultSet>();	
	  private List<Employee> employees = new ArrayList<Employee>();
	  private ArrayList<Room> rooms = new ArrayList<Room>();
	  private List<Group> groups = new ArrayList<Group>();
	  private ArrayList<Event> events = new ArrayList<Event>();
	  private ArrayList<Message> messages = new ArrayList<Message>();
	

	// Denne metoden brukes i initialiseringen for aa hente ut og sjekke om brukeren eksisterer i databasen
	public List<Employee> SporringEmployees(Connection con, String sporring) throws SQLException{
		Statement stmt = null;
		stmt = con.createStatement();
		ResultSet employeeSet = stmt.executeQuery(sporring);
		ResultSetMetaData employeesmd = employeeSet.getMetaData();
		metaData.add(employeesmd);
		resultData.add(employeeSet);
		InitFetchEmployees(metaData, resultData);
		return employees;
	}
	
	// Hjelpemetode for SporringEmployees
	public void InitFetchEmployees (ArrayList<ResultSetMetaData> metaData1, ArrayList<ResultSet> resultData) throws SQLException{
		
		int counter = 0;
		
		while (counter < metaData.size()) {
			
			int numberOfColumns = metaData.get(counter).getColumnCount();
			
			 int employeeID = 0;
			 String name = "";
			 String password = "";
			 String position = "";
			 String username = "";
			 int telnum = 0;
			  
			  while (resultData.get(counter).next()) {
				  Boolean admin = false;
			        for (int i = 1; i <= numberOfColumns; i++) {
			          String columnValue = resultData.get(counter).getString(i);
			          if (i==1){
			        	  employeeID = Integer.parseInt(columnValue);
			          }
			          if (i==2){
			        	  name = columnValue;
			          }
			          if (i==3){
			        	  password = columnValue;
			          }
			          if (i==4){
			        	  position = columnValue;
			          }
			          if (i==5){
			        	  username = columnValue;
			          }
			          if (i==6){
			        	  telnum = Integer.parseInt(columnValue);
			          }
			          if (i==7 && columnValue.equalsIgnoreCase("true")){
			        	  
			        	  admin = true;
			          }
			        }
			        	Employee i = new Employee(employeeID, name, position, username, password, telnum, admin);
			        	employees.add(i);//Maa sorge for at nyEmployee-stringen har samme format som inn-parameterene til new Employee
			      } 
			  counter++;
		}
	}
	
	// Oppdaterer databasen n�r 
	public void updateEmployeeTelnum(Connection con, String sql, int telnum, Employee e) throws SQLException{
		
		PreparedStatement preparedStatement = null;
		preparedStatement = con.prepareStatement(sql);
		
		preparedStatement.setInt(1, telnum);
		preparedStatement.setString(2, e.getUsername());
		
		preparedStatement.executeUpdate();	
		
	}
	public void updateEmployeePos(Connection con, String sql, String pos, Employee e) throws SQLException{
		
		PreparedStatement preparedStatement = null;
		preparedStatement = con.prepareStatement(sql);
		
		preparedStatement.setString(1, pos);
		preparedStatement.setString(2, e.getUsername());
		
		preparedStatement.executeUpdate();	
		
	}
	
	public void updateEmployeeAdmin(Connection con, String sql, String adm, Employee e) throws SQLException{
		
		PreparedStatement preparedStatement = null;
		preparedStatement = con.prepareStatement(sql);
		
		preparedStatement.setString(1, adm);
		preparedStatement.setString(2, e.getUsername());
		
		preparedStatement.executeUpdate();	
		
	}
	
	public void updateEventDeltakelsesStatus(Connection con, Event e, Employee emp) throws SQLException{
		
		PreparedStatement preparedStatement = null;
		String sql = "UPDATE Eventdeltakelse SET status = ? WHERE employeeID = ?, eventID = ?";
		preparedStatement = con.prepareStatement(sql);
		
		if(emp.getEventsAttending().contains(e)){
			preparedStatement.setString(1, "a");
		} else if(emp.getDeclinedEvents().contains(e)){
			preparedStatement.setString(1, "d");
		} else{
			preparedStatement.setString(1, "i");
		}
		preparedStatement.setInt(2, emp.getEmployeeID());
		preparedStatement.setInt(3, e.getEventID());
		
		preparedStatement.executeUpdate();	
	}
	
	public void UpdateMessage(Connection con, Message m) throws SQLException{
		PreparedStatement preparedStatement = null;
		String sql = "UPDATE Message SET isRead = ? WHERE messageID = ?";
		preparedStatement = con.prepareStatement(sql);
		
		preparedStatement.setString(1, m.isRead().toString());
		preparedStatement.setInt(2, m.getMessageID());
	}
	// Ikke implementert
	public Boolean checkUserName(Connection con, String s) throws SQLException{
		//Trenger en for-lokke som itererer gjennom alle eksisterende employees i selskapet og skriver dem til databasen
		//Hvis vedkommende allerede eksisterer,  ignorer oppdatering

		Statement stmt = null;
		stmt = con.createStatement();
		ResultSet rs = stmt.executeQuery("SELECT username FROM Employee");
		
		while (rs.next()){
			if (rs.getString("username").equalsIgnoreCase(s)){
				return true;
			}
		}
		return false;	
	}
	
	//Denne metoden sletter en bruker n�r admin velger � slette en bruker
	public void deleteUser(Connection con, String sql, Employee e) throws SQLException{
		
		PreparedStatement preparedStatement = null;
		
		preparedStatement = con.prepareStatement(sql);
		preparedStatement.setString(1,e.getUsername());
		preparedStatement.executeUpdate();
	}
	
	//Denne metoden oppretter en ny Employee i databasen n�r admin har opprettet en ny bruker
	public void NewEmployee(Connection con, Employee e) throws SQLException{
		
		PreparedStatement preparedStatement = null;

		String sql = "INSERT INTO Employee (name, password, position, username, telnum, admin)" + " VALUES (?, ?, ?, ?, ?, ?)";

		preparedStatement = con.prepareStatement(sql);

		preparedStatement.setString(1, e.getName()); //Her m� Employee.getEmployeeID() benyttes for hver enkelt employee
		preparedStatement.setString(2, e.getPassword()); //Her m� Employee.getName() benyttes
		preparedStatement.setString(3, e.getPosition()); //Her m� Employee.getPassword() benyttes
		preparedStatement.setString(4, e.getUsername()); //Her m� Employee.getPosition() benyttes
		preparedStatement.setInt(5, e.getTelnum()); // Her m� Employee.getUsername() benyttes
		preparedStatement.setString(6, e.isAdmin().toString());

		
		preparedStatement.executeUpdate(); //Her oppdateres databasen	
		
	}
	

	//Denne metoden henter Messages fra databasen, og lagrer de i en 
	public void getMessages(Connection con) throws SQLException{

		Statement stmt = null;
		stmt = con.createStatement();
		
		String sql = "SELECT * FROM Message";
		ResultSet rs = stmt.executeQuery(sql);
		
		int messageID = 0;
		String subject = "";
		String content = "";
		java.sql.Timestamp dateTime = null;
		int senderID = -1;
		int receiverID = -1;
		List<Employee> emps = new ArrayList<Employee>();
		emps = SporringEmployees(con, "SELECT * FROM Employee");
		Employee sender = null;
		Employee receiver = null;
		
		while(rs.next()){
			subject = rs.getString("subject");
			content = rs.getString("content");
			dateTime = (rs.getTimestamp("timeStamp"));
			senderID = rs.getInt("sender_ID");
			receiverID = rs.getInt("receiver_ID");
			for (Employee e : emps){
				if (e.getEmployeeID() == senderID){
					sender = e;
				}
				else if(e.getEmployeeID() == receiverID){
					receiver = e;
				}
			}
			
			Message msg = new Message(sender, receiver, dateTime, subject, content);
			messages.add(msg);
			receiver.addMessageToInbox(msg);
		}

	}
	
	//Denne metoden benyttes for � hente Room fra databasen, og lagrer de i den lokale listen rooms
	public void fetchRooms(Connection con) throws SQLException{
		Statement stmt = null;
		stmt = con.createStatement();
		
		String sql = "SELECT * FROM Room";
		ResultSet rs = stmt.executeQuery(sql);
		
		int roomID = 0;
		String roomDescription = "";
		int capacity = 0;
		String name = "";
		
		while (rs.next()){
			
			roomID = rs.getInt("roomID");
			roomDescription = rs.getString("roomDescription");
			capacity = rs.getInt("capacity");
			name = rs.getString("name");
			
			Room tempRoom = new Room(roomID, name, capacity, roomDescription);
			rooms.add(tempRoom);
		}
	}
	
	//Sjekker hvilke rom som er ledige for et spesifisert event, m� derfor hente allerede eksisterende event og sjekke om rom eksisterer i noen av disse og i tilfelle til hvilken tid
	public List<Room> checkRoomEvents(Connection con) throws SQLException{
		
		Statement stmt = null;
		stmt = con.createStatement();

		String sql1 = "SELECT * FROM Event";
		ResultSet rs = stmt.executeQuery(sql1);
			
	          java.util.Date startDato = new java.util.Date();
	          java.util.Date endDato = new java.util.Date();
	          String eventDescription = "";
	          int creatorID = 0;
	          int roomID = 0;
	          String title = "";
	          Employee tempEmployee = null;
			      while (rs.next()) {
				    	  roomID = rs.getInt("roomID");
				    	  title = rs.getString("title");
				          startDato = convertDateTimeToDate(rs.getString("startTime"));
				          endDato = convertDateTimeToDate(rs.getString("endTime"));
				          eventDescription = rs.getString("eventDescription");
				          creatorID = rs.getInt("creator_ID");
				          
					          for (Employee emp : employees){
					        	  if (emp.getEmployeeID() == creatorID){
					        		  tempEmployee = emp;
					        	  }
					          }
	
					          Event tempEvent = new Event(title, startDato, endDato, eventDescription, tempEmployee);
					          for (Room room : rooms){
					        	  if(room.getRoomID() == roomID){
					        		  tempEvent.setRoom(room);
					        	  	  room.addEventToRoom(tempEvent);
					        	  }
					          }
			      }
			      return rooms;
	}
		


// midlertidlig, husk � returnere List<Room>
// midlertidlig, husk � returnere List<Room>
		
/*		int counter = 0;
		int capacity = 0;
		int roomID = 0;
		String name = "";
		String description = "";
		
		  
		  while (resultData.get(counter).next()) {
			  for (int i = 1; i <= numberOfCol; i++) {
			  	
				  String columnValue = resultData.get(counter).getString(i);
		          if (i==1){
		        	  roomID = Integer.parseInt(columnValue);
		          }
		          if (i==2){
		        	  name = columnValue;
		          }
		          if (i==3){
		        	  capacity = Integer.parseInt(columnValue);
		          }
		          if (i==4){
		        	  description = columnValue;
		          }
		          if (i==5){ //Her m� roomSchedule ordnes?
		        	  roomSchedule = columnValue;
		          }
			  }

		        Room addRoom = new Room(roomID, name, capacity, description);   //Maa sorge for at nyttRoom-stringen har samme format som inn-parameterene til new Room 
		  }
		
		return rooms;
	} */
	
	
	//Viktig for � f� riktig format p� Date-objektene i javakoden s� de kan skrives til databasen
	private java.sql.Timestamp convertDateToDateTime(java.util.Date date) throws SQLException{
		
		int year = date.getYear();
		int month = date.getMonth();
		String dateTime = ("" + year + "-" + date.getMonth() + "-" + date.getDate() + " " + date.getHours() + ":" + date.getMinutes() + date.getMinutes() + ":" + date.getSeconds());
		java.sql.Timestamp timeS = new Timestamp(year, month, date.getDate(), date.getHours(), date.getMinutes(), date.getSeconds(), 0);
		//YYYY-MM-DD HH:MI:SS
		return timeS;
	}

	//S�rger for at event oppdateres etter at et nytt event er oppdatert
	public void WriteEventToDatabase(Connection con, Event e) throws SQLException{
		
		PreparedStatement preparedStatement = null;
		java.sql.Timestamp startT = convertDateToDateTime(e.getStartTime());
		java.sql.Timestamp endT = convertDateToDateTime(e.getEndTime());
		
		
		String sql = "INSERT INTO Event (eventID, title, startTime, endTime, eventDescription, roomID, creator_ID )" + "VALUES (?, ?, ?, ?, ?, ?, ?)";
		preparedStatement = con.prepareStatement(sql);

		preparedStatement.setInt(1, e.getEventID());
		preparedStatement.setString(2, e.getTitle());
		preparedStatement.setTimestamp(3, startT);
		preparedStatement.setTimestamp(4, endT);
		preparedStatement.setString(5, e.getDescription()); 
		preparedStatement.setInt(6, e.getRoom().getRoomID()); 		
		preparedStatement.setInt(7, e.getCreator().getEmployeeID()); 

		
		try{
		preparedStatement.executeUpdate(); //Her oppdateres databasen
		System.out.println("" + e.getTitle() + ", ble opprettet som et event i databasen\n");
		}
		catch(SQLException el){
			el.printStackTrace();
		}
	}	
	
	//S�rger for � skrive rom til databasen, er denne n�dvendig?
	public void WriteRoomToDatabase(Connection con, Room r) throws SQLException{
		
		PreparedStatement preparedStatement =  null;
		
		String sql = "INSERT INTO Room (roomID, name, capacity, description)" + "VALUES (?, ?, ?, ?)";
		preparedStatement = con.prepareStatement(sql);

		preparedStatement.setInt(1, r.getRoomID()); 
		preparedStatement.setString(2, r.getName()); 
		preparedStatement.setInt(3, r.getCapacity()); 
		preparedStatement.setString(4, r.getDescription()); 
		
		preparedStatement.executeUpdate(); //Her oppdateres databasen	
		
	}	
	
	//Denne s�rger for at meldinger blir lagret i databasen etter at disse er opprettet, f.eks n�r folk blir invitert til et event
	public void WriteMessageToDatabase(Connection con, Employee e) throws SQLException{
		PreparedStatement preparedStatement = null;
		Message m = e.getInbox().get(e.getInbox().size()-1);
		
		String sql = "INSERT INTO Message (messageID, subject, content, timeStamp, sender_ID, receiver_ID, isRead)" + "VALUES (?, ?, ?, ?, ?, ?, ?)";
		preparedStatement = con.prepareStatement(sql);
		preparedStatement.setInt(1, m.getMessageID());
		preparedStatement.setString(2, m.getSubject());
		preparedStatement.setString(3, m.getContent());
		preparedStatement.setTimestamp(4, m.getTimeStamp());
		preparedStatement.setInt(5, m.getSender().getEmployeeID());
		preparedStatement.setInt(6, m.getReceiver().getEmployeeID());
		preparedStatement.setString(7, m.isRead().toString());
		
		
		preparedStatement.executeUpdate(); //Her oppdateres databasen	
		
	}	
	
	//Denne metoden skriver gruppe til databasen, s�rger for at en gruppe oppdateres hvis endringer oppst�r
	public void WriteGruppeToDatabase(Connection con, Group g) throws SQLException{
		
		PreparedStatement preparedStatement = null;
		
		String sql = "INSERT INTO Gruppe (gruppeID, navn, ansvarlig, beskrivelse)" + "VALUES (?, ?, ?, ?)";
		preparedStatement = con.prepareStatement(sql);
		preparedStatement.setInt(1, g.getGroupID()); 
		preparedStatement.setString(2, g.getName());
		preparedStatement.setInt(3, g.getResponsible().getEmployeeID());
		preparedStatement.setString(4, g.getDescription());

		preparedStatement.executeUpdate(); //Her oppdateres databasen

	}
	
	//Denne metoden s�rger for at eventDeltakelse oppdateres i databasen, n�r et event har f�tt flere deltakere vil databasen oppdateres
	public void WriteEventDeltakelseToDatabase(Connection con, Event ev, List<Employee> emp) throws SQLException{
		
		for(Employee e : emp){
		
			PreparedStatement preparedStatement = null;
			
			String sql = "INSERT INTO Eventdeltakelse (event_ID, employee_ID, status, isHidden)" + "VALUES (?, ?, ?, ?)";
			preparedStatement = con.prepareStatement(sql);
			
			preparedStatement.setInt(1, ev.getEventID()); 
			preparedStatement.setInt(2, e.getEmployeeID());
			
			//SetInt
			if(e.getEventsAttending().contains(ev)){
				preparedStatement.setString(3, "a");
			} else if(e.getDeclinedEvents().contains(ev)){
				preparedStatement.setString(3, "d");
			} else{
				preparedStatement.setString(3, "i");
			}
			// Ikke implementert
			preparedStatement.setString(4, "false");
	
			preparedStatement.executeUpdate(); //Her oppdateres databasen
			
			preparedStatement.close();
		}
	}
	
	//Ikke implementert.
	public void WriteGruppeDeltakelseToDatabase(Connection con, Group g, Employee emp) throws SQLException{
		
	}
	
	public void WriteDatabaseToJava(ArrayList<ResultSetMetaData> metaData, ArrayList<ResultSet> resultData) throws SQLException{
		
		int counter = 0;
		int decider = 0;
		
		while (counter < metaData.size()) {
			
			int numberOfColumns = metaData.get(counter).getColumnCount();
			  
		      for (int i = 1; i <= numberOfColumns; i++) { //Er en for-l�kke n�dvendig her?
		        String columnName = metaData.get(counter).getColumnName(i);
		        
		        if (i == 1 && columnName.equalsIgnoreCase("employeeID")){
		        	
		        	decider = 1;
		        	
		        }
		        else if (i == 1 && columnName.equalsIgnoreCase("roomID")){
		        	
		        	decider = 2;
		        	
		        }
		        else if (i == 1 && columnName.equalsIgnoreCase("eventID")){
		        	
		        	decider = 3;
		        	
		        }
		        else if (i == 1 && columnName.equalsIgnoreCase("messageID")){
		        	
		        	decider = 4;
		        	
		        }
		        else if (i == 1 && columnName.equalsIgnoreCase("gruppeID")){
		        	
		        	decider = 5;
		        	
		        }
		     //   System.out.print(columnName);
		      }
		      System.out.println("");
		      System.out.println("");
		      
		  if (decider == 1){
			  
			  int employeeID = 0;
			  String name = "";
			  String password = "";
			  String position = "";
			  String username = "";
			  int telnum = 0;
			  Boolean isAdmin = false;
			  
			  while (resultData.get(counter).next()) {
			        for (int i = 1; i <= numberOfColumns; i++) {
			          String columnValue = resultData.get(counter).getString(i);
			          if (i==1){
			        	  employeeID = Integer.parseInt(columnValue);
			          } else if (i==2){
			        	  name = columnValue;
			          } else if (i==3){
			        	  password = columnValue;
			          } else if (i==4){
			        	  position = columnValue;
			          } else if (i==5){
			        	  username = columnValue;
			          } else if (i==6){
			        	  telnum = Integer.parseInt(columnValue);
			          } else if (i==7){
			        	  isAdmin = Boolean.parseBoolean(columnValue);
			          }
			        }
			        	Employee i = new Employee(employeeID, name, password, position, username, telnum, isAdmin);//Maa sorge for at nyEmployee-stringen har samme format som inn-parameterene til new Employee
			            employees.add(i);
			      } 
			  
		  }
		  else if (decider == 2){
			  
			  int roomID = 0;
			  String name = "";
			  int capacity = 0;
			  String description = "";
			  List<Event> roomSchedule = new ArrayList<Event>();
			  
			  while (resultData.get(counter).next()) {
				  for (int i = 1; i <= numberOfColumns; i++) {
				  	
					  String columnValue = resultData.get(counter).getString(i);
			          if (i==1){
			        	  roomID = Integer.parseInt(columnValue);
			          } else if (i==2){
			        	  name = columnValue;
			          } else if (i==3){
			        	  capacity = Integer.parseInt(columnValue);
			          } else if (i==4){
			        	  description = columnValue;
			          } else if (i==5){ //Her m� roomSchedule ordnes?
			   //     	  roomSchedule = columnValue;
			          }
				  }

			        Room addRoom = new Room(roomID, name, capacity, description);   //Maa sorge for at nyttRoom-stringen har samme format som inn-parameterene til new Room 
			  } 
			  
		  }
		  else if (decider == 3){
			  
			  int eventID = 0;
			  String title = "";
			  Date startTime = null;
			  Date endTime = null;
			  String description = "";
			  Employee creator = null;
			  
			  while (resultData.get(counter).next()) {
				  	for (int i = 1; i <= numberOfColumns; i++) {

					  String columnValue = resultData.get(counter).getString(i);
					   	  if (i==1){
				        	  eventID = Integer.parseInt(columnValue);
				          }
				          if (i==2){
				        	  title = columnValue;
				          }
				          if (i==3){
				        	  startTime = Date.valueOf(columnValue);
				          }
				          if (i==4){
				        	  endTime = Date.valueOf(columnValue);
				          }
				          if (i==5){
				        	  description = columnValue;
				          }
				          if (i==6){  //hvordan f� inn en Employee her?
				        	  for (i = 0; i < employees.size(); i++){
				        		
				        		  if (employees.get(i).getName().equalsIgnoreCase(columnValue)) {
				        			  
						        	  creator = employees.get(i);
				        			  
				        		  }	  
				        	  }
				          }
				  }
			        Event addEvent = new Event(title, startTime, endTime, description, creator);   //Maa sorge for at nyttEvent-stringen har samme format som inn-parameterene til new Event       
			  } 
			  
		  }
		  else if (decider == 4){ // for message, denne m� ses n�rmere p�
			  
			  int messageID = 0;
			  String subject = "";
			  String content = "";
			  String description = "";
			  List<Event> roomSchedule = new ArrayList<Event>();
			  
			  while (resultData.get(counter).next()) {
				  	for (int i = 1; i <= numberOfColumns; i++) {
					  String columnValue = resultData.get(counter).getString(i);
			          if (i==1){
			        	  messageID = Integer.parseInt(columnValue);
			          }
			          if (i==2){
			        	  subject = columnValue;
			          }
			          if (i==3){
			        	  content = (columnValue);
			          }
			          if (i==4){
			//        	  date = columnValue;
			          }
			          if (i==5){ //Her m� roomSchedule ordnes
			  //      	  roomSchedule = columnValue;
			          }
				  }
			//        Message addMessage = new Message(sender, reciever, isRead, content, subject);   //Maa sorge for at formatet her er det samme som i konstruktoren til Group-klassen      
			  } 
			  
		  }
		  else if (decider == 5){
			  
			  int groupID = 0; // m� kanskje legges til i Group
			  String groupName = "";
			  String description = "";
			  Employee responsible = null;
			  
			  
			  while (resultData.get(counter).next()) {
				  	for (int i = 1; i <= numberOfColumns; i++) {
				  		
					  String columnValue = resultData.get(counter).getString(i);
					  
					  if (i==1){
			        	  groupID = Integer.parseInt(columnValue);
			          }
			          if (i==2){
			        	  groupName = columnValue;
			          }
			          if (i==3){ // hvordan finne Employee som er responsible?
			        	 for (i = 0; i <= employees.size(); i++){
			        		 
			        		 if(employees.get(i).getName().equalsIgnoreCase(columnValue)){
			        			 
			        			 responsible = employees.get(i);
			        			 
			        		 }
			        	 }
			          }
			          if (i==4){ //Her m� participants legges til
			        	  description = (columnValue);
			          }
			          if (i==5){
			        	  description = columnValue;
			          }
				  }

			        Group addGroup = new Group(groupName, description, responsible);   //Maa sorge for at nyGruppe-stringen har samme format som inn-parameterene til new Gruppe        
			  }  
			  
		  } 
		      counter++;
		}
	} 
	
	public java.util.Date convertDateTimeToDate(String dateTime) throws SQLException{
		java.util.Date dateObject = new java.util.Date();
		String date = "";
		
		date = dateTime.substring(8,10) + "/" + dateTime.substring(5,7) + "/" + dateTime.substring(0,4) + " " + dateTime.substring(11, 19);
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy H:m:s");
		try {
			dateObject = formatter.parse(date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return dateObject;
	}
	
	
	// brukes ikke lenger - FW
	public void PrintTables(ArrayList<ResultSetMetaData> metaData, ArrayList<ResultSet> resultData, String msg) throws SQLException{
		
		int counter = 0;
		
		while (counter < metaData.size()) {
			
			int numberOfColumns = metaData.get(counter).getColumnCount();
			
			if(msg.equalsIgnoreCase("available rooms")){
				
			      for (int i = 1; i <= numberOfColumns; i++) {
				        if (i > 1) System.out.print(",  ");
				        String columnName = metaData.get(counter).getColumnName(i);
				        
				      }
			      while (resultData.get(counter).next()) {
				        for (int i = 1; i <= numberOfColumns; i++) {
				          if (i > 1) System.out.print(",  ");
				          String columnValue = resultData.get(counter).getString(i);
				          System.out.print(columnValue);
				        }      
				      } 
			}
			else if (msg.equalsIgnoreCase("people invited")){
				
			}
			else if (msg.equalsIgnoreCase("people"))
			  
		      for (int i = 1; i <= numberOfColumns; i++) {
		        if (i > 1) System.out.print(",  ");
		        String columnName = metaData.get(counter).getColumnName(i);
		        System.out.print(columnName);
		      }
		      System.out.println("");
		      System.out.println("");
		  
		      while (resultData.get(counter).next()) {
		        for (int i = 1; i <= numberOfColumns; i++) {
		          if (i > 1) System.out.print(",  ");
		          String columnValue = resultData.get(counter).getString(i);
		          System.out.print(columnValue);
		        }
		        System.out.println("");
		        System.out.println("");       
		      } 
		      counter++;
		}
	}

	public List<Group> SporringGroups(Connection con, String sporring) throws SQLException {
		Statement stmt = null;
		stmt = con.createStatement();
		ResultSet groupSet = stmt.executeQuery(sporring);
		ResultSetMetaData groupsmd = groupSet.getMetaData();
		metaData.add(groupsmd);
		resultData.add(groupSet);
		InitFetchGroups(metaData, resultData);
		return groups;
	}

	private void InitFetchGroups(ArrayList<ResultSetMetaData> metaData, ArrayList<ResultSet> resultData) throws SQLException {
		int counter = 0;
		
		while (counter < metaData.size()) {
			
			int numberOfColumns = metaData.get(counter).getColumnCount();
			
			 int groupID = 0;
			 String name = "";
			 String description = "";
			 int responsible = 0;
			  
			  while (resultData.get(counter).next()) {
			        for (int i = 1; i <= numberOfColumns; i++) {
			          String columnValue = resultData.get(counter).getString(i);
			          if (i==1){
			        	  groupID = Integer.parseInt(columnValue);
			          }
			          if (i==2){
			        	  name = columnValue;
			          }
			          if (i==3){
			        	  responsible = Integer.parseInt(columnValue);
			          }
			          if (i==4){
			        	  description = columnValue;
			          }
			        }
			        Employee e = null;
			        
			        for (Employee employee : employees) {
						if(employee.getEmployeeID() == responsible){
							e = employee;
						}
					}
		        	Group i = new Group(groupID, name, description, e);
		        	groups.add(i);//Maa sorge for at nyGroup-stringen har samme format som inn-parameterene til new Group
			      } 
			  counter++;
		}
	}

	public ArrayList<Event> sporringEvents(Connection con, String sporring, List<Employee> employees) throws SQLException {
		Statement stmt = null;
		stmt = con.createStatement();
		ResultSet eventSet = stmt.executeQuery(sporring);
		ResultSetMetaData eventsmd = eventSet.getMetaData();
		metaData.add(eventsmd);
		resultData.add(eventSet);
		InitFetchEvents(metaData, resultData, (ArrayList<Employee>) employees);
		return events;
	}

	private void InitFetchEvents(ArrayList<ResultSetMetaData> metaData, ArrayList<ResultSet> resultData, ArrayList<Employee> employees) throws SQLException {
		int counter = 0;
		
		while (counter < metaData.size()) {
			
			int numberOfColumns = metaData.get(counter).getColumnCount();
			
			 int eventID = 0;
			 String title = "";
			 java.util.Date startTime = null;
			 java.util.Date endTime = null;
			 String description = "";
			 Room room = null;
			 Employee creator = null;
			 SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy H:m:s");
			  
			  while (resultData.get(counter).next()) {
			        for (int i = 1; i <= numberOfColumns; i++) {
			          String columnValue = resultData.get(counter).getString(i);
			          if (i==1){
			        	  eventID = Integer.parseInt(columnValue);
			          }else if (i==2){
			        	  title = columnValue;
			          }else if (i==3){
			        	  try {
			        		  	startTime = (java.util.Date) formatter.parse(columnValue.substring(8, 10) + "/" + columnValue.substring(5, 7) + "/" + columnValue.substring(0, 4) + " " + columnValue.substring(11, columnValue.length() - 2));
			        		  	
							} catch (ParseException e) {
								e.printStackTrace();
							}
			          }else if (i==4){
			        	  try {
			        		  endTime = (java.util.Date) formatter.parse(columnValue.substring(8, 10) + "/" + columnValue.substring(5, 7) + "/" + columnValue.substring(0, 4) + " " + columnValue.substring(11, columnValue.length() - 2));
			        		  	
							} catch (ParseException e) {
								e.printStackTrace();
							}
			          }else if(i==5){
			        	  description = columnValue;
			          }else if(i==6){
			        	  for (Room r : rooms) {

			        		  //if(r.getRoomID() == Integer.parseInt(columnValue)){
							room = null;
							break;
							//}
						}
			        	room = null;
			          }else if(i==7){
			        	  for (Employee e : employees) {
								if(e.getEmployeeID() == Integer.parseInt(columnValue)){
									creator = e;
									break;
								}
							}
			          }
			        }

		        	Event i = new Event(eventID, title, startTime, endTime, description, room, creator);
		        	events.add(i);//Maa sorge for at nyEvent-stringen har samme format som inn-parameterene til new Group
			      } 
			  counter++;
		}
	}

	public ArrayList<Room> sporringRooms(Connection con, String sporring) throws SQLException {
		Statement stmt = null;
		stmt = con.createStatement();
		ResultSet roomSet = stmt.executeQuery(sporring);
		ResultSetMetaData roomsmd = roomSet.getMetaData();
		metaData.add(roomsmd);
		resultData.add(roomSet);
		InitFetchRooms(metaData, resultData);
		return rooms;
	}

	private void InitFetchRooms(ArrayList<ResultSetMetaData> metaData, ArrayList<ResultSet> resultData) throws SQLException {
		int counter = 0;
		
		while (counter < metaData.size()) {
			
			int numberOfColumns = metaData.get(counter).getColumnCount();
			
			 int roomID = 0;
			 String name = "";
			 int capacity = 0;
			 String description = "";
			 
			 while (resultData.get(counter).next()) {
			        for (int i = 1; i <= numberOfColumns; i++) {
			          String columnValue = resultData.get(counter).getString(i);
			          if (i==1){
			        	  roomID = Integer.parseInt(columnValue);
			          }else if (i==2){
			        	  name = columnValue;
			          }else if (i==3){
			        	  capacity = Integer.parseInt(columnValue);
			          }else if (i==4){
			        	  description = columnValue;
			          }
			        }

		        	Room i = new Room(roomID, name, capacity, description);
		        	rooms.add(i);//Maa sorge for at nyEvent-stringen har samme format som inn-parameterene til new Group
			      } 
			  counter++;
		}
	}

	public ListContainer sporringParticipations(Connection con, String sporring, List<Employee> employees2, List<Event> events2) throws SQLException {
		Statement stmt = null;
		stmt = con.createStatement();
		ResultSet participationSet = stmt.executeQuery(sporring);
		ResultSetMetaData participationsmd = participationSet.getMetaData();
		metaData.add(participationsmd);
		resultData.add(participationSet);
		ListContainer lc = InitFetchParticipation(metaData, resultData, employees2, events2);
		return lc;
	}

	private ListContainer InitFetchParticipation(ArrayList<ResultSetMetaData> metaData, ArrayList<ResultSet> resultData, List<Employee> employees, List<Event> events) throws SQLException {
		int counter = 0;
		
		while (counter < metaData.size()) {
			
			int numberOfColumns = metaData.get(counter).getColumnCount();
			
			 int eventID = 0;
			 int employeeID = 0;
			 String status = "";
			 Boolean isHidden = false; 
			 Employee emp = null;
			 
			 while (resultData.get(counter).next()) {
			        for (int i = 1; i <= numberOfColumns; i++) {
			          String columnValue = resultData.get(counter).getString(i);
			          if (i==1){
			        	  eventID = Integer.parseInt(columnValue);
			          }else if (i==2){
			        	  employeeID = Integer.parseInt(columnValue);
			          }else if (i==3){
			        	  status = columnValue;
			          }else if (i==4){
			        	  isHidden = Boolean.parseBoolean(columnValue);
			          }
			        }
			        
			        for (Employee employee : employees) {
						if(employee.getEmployeeID() == employeeID){
							emp = employee;
							break;
						}
					}
			        for (Event event : events) {
						if(event.getEventID() == eventID){
							//System.out.println(event.toString() + emp.toString() + status);
							// i = invited, a = attending, d = declined
							if (status.equals("i")) {
								event.getPeopleInvited().add(emp);
								emp.getUpcomingEvents().add(event);
							}else if(status.equals("a")){
								event.getPeopleGoing().add(emp);
								emp.getEventsAttending().add(event);
							}else if(status.equals("d")){
								event.getPeopleDeclined().add(emp);
								emp.getDeclinedEvents().add(event);
							}
							break;
						}
					}
			      } 
			  counter++;
		}
		return new ListContainer(events, employees);
		
	}

	public ArrayList<Message> sporringMessages(Connection con, String sporring) throws SQLException {
		Statement stmt = null;
		stmt = con.createStatement();
		ResultSet messageSet = stmt.executeQuery(sporring);
		ResultSetMetaData messagesmd = messageSet.getMetaData();
		metaData.add(messagesmd);
		resultData.add(messageSet);
		InitFetchMessages(metaData, resultData);
		return messages ;
	}

	private void InitFetchMessages(ArrayList<ResultSetMetaData> metaData, ArrayList<ResultSet> resultData) throws SQLException {
		int counter = 0;
		
		while (counter < metaData.size()) {
			
			int numberOfColumns = metaData.get(counter).getColumnCount();
			
			 int messageID = 0;
			 String subject = "";
			 String content = "";
			 java.util.Date timestamp = null;
			 int senderID = 0;
			 int receiverID = 0;
			 boolean isRead = false;
			 SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy H:m:s");
			 Employee sender = null;
			 Employee receiver = null;
			 
			 while (resultData.get(counter).next()) {
			        for (int i = 1; i <= numberOfColumns; i++) {
			          String columnValue = resultData.get(counter).getString(i);
			          if (i==1){
			        	  messageID = Integer.parseInt(columnValue);
			          }else if (i==2){
			        	  subject = columnValue;
			          }else if (i==3){
			        	  content = (columnValue);
			          }else if (i==4){
			        	  try {
			        		  	timestamp = (java.util.Date) formatter.parse(columnValue.substring(8, 10) + "/" + columnValue.substring(5, 7) + "/" + columnValue.substring(0, 4) + " " + columnValue.substring(11, columnValue.length() - 2));
			        		  	
							} catch (ParseException e) {
								e.printStackTrace();
							}
			          }else if (i==5){
			        	  senderID = Integer.parseInt(columnValue);
			          }else if (i==6){
			        	  receiverID = Integer.parseInt(columnValue);
			          }else if (i==7){
			        	  isRead = Boolean.parseBoolean(columnValue);
			          }
			        }
			        
			        
			        for (Employee e : employees) {
						if(senderID == e.getEmployeeID()){
							sender = e;
						}else if(receiverID == e.getEmployeeID()){
							receiver = e;
						}
					}
			        
			        if(employees.contains(receiver)){
			        	
			        	Message i = new Message(messageID, sender, receiver, timestamp, content, subject);
			        	i.sendMessage();
			        	messages.add(i);//Maa sorge for at nyEvent-stringen har samme format som inn-parameterene til new Group			        	
			        }
			      } 
			  counter++;
		}
		
	}
	
/*	public static class PrintColumnTypes  {

		  public static void printColTypes(ResultSetMetaData rsmd) throws SQLException {
		    int columns = rsmd.getColumnCount();
		    for (int i = 1; i <= columns; i++) {
		      int jdbcType = rsmd.getColumnType(i);
		      String name = rsmd.getColumnTypeName(i);
		      System.out.print("Column " + i + " is JDBC type " + jdbcType);
		      System.out.println(", which the DBMS calls " + name);
		    }
		  }
		}

	public static String Scan(String tekst){
		System.out.println(tekst);
		Scanner scan = new Scanner(System.in);
		String input = scan.nextLine();
		return input;
	}
	
	public static void main(String[] args) {
		
		ConnectionToDatabase test = new ConnectionToDatabase();

		
			
	} */

}

