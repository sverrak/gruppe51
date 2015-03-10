package Code;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Collection;
import java.util.Date;

public class ConnectionToDatabase {

	  private ResultSet resultSet = null; 
	  private ArrayList<ResultSetMetaData> metaData = new ArrayList<ResultSetMetaData>();
	  private ArrayList<ResultSet> resultData = new ArrayList<ResultSet>();	
	  private List<Employee> employees = new ArrayList<Employee>();
	  private List<Room> rooms = new ArrayList<Room>();
	

	
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
			        	Employee i = new Employee(name, position, username, password, telnum, admin);
			        	employees.add(i);//Maa sorge for at nyEmployee-stringen har samme format som inn-parameterene til new Employee
			      } 
			  counter++;
		}
	}
	
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
	// Ikke implementert
	public Boolean checkUserName(Connection con, String s) throws SQLException{
		
		//Trenger en for-l�kke som itererer gjennom alle eksisterende employees i selskapet og skriver de til databasen
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
	
	public void deleteUser(Connection con, String sql, Employee e) throws SQLException{
		
		PreparedStatement preparedStatement = null;
		
		preparedStatement = con.prepareStatement(sql);
		preparedStatement.setInt(1,e.getEmployeeID());
		preparedStatement.executeUpdate();
	}
	
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
	
	public void fetchRooms(Connection con, String requestedStartTime, String requestedEndTime, int requestedCapacity) throws SQLException{
		
		java.util.Date requestedStart = new java.util.Date();
		java.util.Date requestedEnd = new java.util.Date();
		
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy H:m:s");
		try {
			requestedStart = formatter.parse(requestedStartTime);
			requestedEnd = formatter.parse(requestedEndTime);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Statement stmt = null;
		stmt = con.createStatement();

		String sql = "SELECT R.name, R.capacity, E.startTime, E.endTime, E.roomID FROM Room AS R INNER JOIN Event AS E ON  R.roomID = E.roomID";
		ResultSet rs = stmt.executeQuery(sql);
		ResultSetMetaData rsmd = rs.getMetaData();
		int numberOfCol = rsmd.getColumnCount();
		
		ArrayList<ResultSet> tempRS = new ArrayList<ResultSet>();
		ArrayList<ResultSetMetaData> tempRSMD = new ArrayList<ResultSetMetaData>();
		int counter = 0;
		
		while (counter < metaData.size()) {
			
			int numberOfColumns = metaData.get(counter).getColumnCount();
				
			      for (int i = 1; i <= numberOfColumns; i++) {
				   //     if (i > 1) System.out.print(",  ");
				   //     String columnName = metaData.get(counter).getColumnName(i);

				  }
			      while (resultData.get(counter).next()) {
				        for (int i = 1; i <= numberOfColumns; i++) {
				  //      if (i > 1) System.out.print(",  "); 
				          java.util.Date startDato = new java.util.Date();
				          java.util.Date endDato = new java.util.Date();
				          int roomID = 0;
				          
				          startDato = convertDateTimeToDate(resultData.get(counter).getString("startDate"));
				          endDato = convertDateTimeToDate(resultData.get(counter).getString("endDate"));
				          roomID = resultData.get(counter).getInt("roomID");
				          
				          if
				          
				          System.out.print(columnValue);
				        }      
				  } 
		}
		
	} // midlertidlig, husk � returnere List<Room>
		
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
	}
	

	public void WriteEventToDatabase(Connection con, Event e) throws SQLException{
		
		//Trenger en for-l�kke som itererer gjennom alle eksisterende events i selskapet og skriver de til databasen
		//Hvis eventet allerede eksisterer,  ignorer oppdatering
		
		PreparedStatement preparedStatement = null;
		
		String sql = "INSERT INTO Event (eventID, tittel, startTime, endTime, description, roomID)" + "VALUES (?, ?, ?, ?, ?, ?)";
		preparedStatement = con.prepareStatement(sql);

		preparedStatement.setInt(1, 1); //Her m� Event.getEventID() benyttes for hvert enkelt event
		preparedStatement.setString(2, "Mote"); //Her m� Event.getTitle() benyttes
		preparedStatement.setTime(3, 10:30); //Her m� Event.getStartTime() benyttes
		preparedStatement.setTime(4, 11:30); //Her m� Event.getEndTime() benyttes
		preparedStatement.setString(5, "Klientmote"); // Her m� Event.getDescription() benyttes
		preparedStatement.setString(6, "Event123"); // Her m� Event.getRoomID() benyttes
		
		preparedStatement.executeUpdate(); //Her oppdateres databasen	
	}	
	
	public void WriteRoomToDatabase(Connection con, Room r) throws SQLException{
		
		//Trenger en for-l�kke som itererer gjennom alle eksisterende rom i selskapet og skriver de til databasen
		//Hvis rommet allerede eksisterer,  ignorer oppdatering
		PreparedStatement preparedStatement =  null;
		
		String sql = "INSERT INTO Room (roomID, name, capacity, eventID)" + "VALUES (?, ?, ?, ?)";
		preparedStatement = con.prepareStatement(sql);

		preparedStatement.setInt(1, roomID); //Her m� Room.getRoomID() benyttes for hvert enkelt rom
		preparedStatement.setString(2, "name"); //Her m� Room.getName() benyttes
		preparedStatement.setInt(3, capacity); //Her m� Room.getCapacity() benyttes
		preparedStatement.setInt(4, eventID); //Her m� Room.getEventID() benyttes
		
		preparedStatement.executeUpdate(); //Her oppdateres databasen	
		
	}	
	
	public void WriteMessageToDatabase(Connection con, Message m) throws SQLException{
		
		//Trenger en for-l�kke som itererer gjennom alle eksisterende events i selskapet og skriver de til databasen
		//Hvis eventet allerede eksisterer,  ignorer oppdatering
		PreparedStatement preparedStatement = null;
		
		String sql = "INSERT INTO Message (messageID, type, message)" + "VALUES (?, ?, ?)";
		preparedStatement = con.prepareStatement(sql);
		preparedStatement.setInt(1, messageID); //Her m� Message.getMessageID() benyttes for hver enkel message
		preparedStatement.setString(2, "type"); //Her m� Message.getType() benyttes
		preparedStatement.setString(3, "message"); //Her m� message.getMessage() benyttes
		
		preparedStatement.executeUpdate(); //Her oppdateres databasen	
		
	}	
	
	public void WriteGruppeToDatabase(Connection con, Group g) throws SQLException{
		
		//Trenger en for-l�kke som itererer gjennom alle eksisterende grupper selskapet og skriver de til databasen
		//Hvis gruppen allerede eksisterer,  ignorer oppdatering
		PreparedStatement preparedStatement = null;
		
		String sql = "INSERT INTO Gruppe (gruppeID, navn, ansvarlig, beskrivelse)" + "VALUES (?, ?, ?, ?)";
		preparedStatement = con.prepareStatement(sql);
		preparedStatement.setInt(1, 0003); //Her m� Gruppe.getgruppeID() benyttes for hver enkelt gruppe
		preparedStatement.setString(2, "Mat"); //Her m� Gruppe.getName() benyttes for hver gruppe
		preparedStatement.setString(3, "Kantinedama"); //Her m� Gruppe.getResponsible() benyttes for hver gruppe
		preparedStatement.setString(4, "Digg mat"); //Her m� Gruppe.getDescription() benyttes for hver gruppe

		preparedStatement.executeUpdate(); //Her oppdateres databasen
			
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
			  Boolean isAdmin;
			  
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
			        	Employee i = new Employee(name, password, position, username, telnum, isAdmin);//Maa sorge for at nyEmployee-stringen har samme format som inn-parameterene til new Employee
			            employees.add(i);
			      } 
			  
		  }
		  else if (decider == 2){
			  
			  int roomID = 0;
			  String name = "";
			  int capacity = 0;
			  String description = "";
			  List<Event> roomSchedule = new ArrayList<Room>();
			  
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
			        	  roomSchedule = columnValue;
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
				        	  for (i = 0; i < employees.length(); i++){
				        		
				        		  if (employees.get(i).getName.equalsIgnoreCase(columnValue)) {
				        			  
						        	  creator = employees(i);
				        			  
				        		  }	  
				        	  }
				          }
				  }
			        Event addEvent = new Event(eventID, title, startTime, endTime, description, creator);   //Maa sorge for at nyttEvent-stringen har samme format som inn-parameterene til new Event       
			  } 
			  
		  }
		  else if (decider == 4){ // for message, denne m� ses n�rmere p�
			  
			  int messageID = 0;
			  String type = "";
			  String content = "";
			  
			  while (resultData.get(counter).next()) {
				  	for (int i = 1; i <= numberOfColumns; i++) {
					  String columnValue = resultData.get(counter).getString(i);
			          if (i==1){
			        	  messageID = Integer.parseInt(columnValue);
			          }
			          if (i==2){
			        	  type = columnValue;
			          }
			          if (i==3){
			        	  content = (columnValue);
			          }
			          if (i==4){
			        	  description = columnValue;
			          }
			          if (i==5){ //Her m� roomSchedule ordnes
			        	  roomSchedule = columnValue;
			          }
				  }
			        Message addMessage = new Message(sender, reciever, isRead, content, subject);   //Maa sorge for at formatet her er det samme som i konstruktoren til Group-klassen      
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
			        	 for (i = 0; i <= employees.length(); i++){
			        		 
			        		 if(employees.get(i).getName.EqualsIgnoreCase(columnValue)){
			        			 
			        			 responsible = employees(i);
			        			 
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

			        Group addGroup = new Group(groupID, description, responsible);   //Maa sorge for at nyGruppe-stringen har samme format som inn-parameterene til new Gruppe        
			  }  
			  
		  } 
		      counter++;
		}
	} */
	
	public java.util.Date convertDateTimeToDate(String dateTime) throws SQLException{
		java.util.Date dateObject = new java.util.Date();
		String date = "";
		
		date = dateTime.substring(8,9);
		date.concat("/" + dateTime.substring(5,6) + "/" + dateTime.substring(0,3) + " " + dateTime.substring(11, 18));
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy H:m:s");
		try {
			dateObject = formatter.parse(date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return dateObject;
	}
	
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

