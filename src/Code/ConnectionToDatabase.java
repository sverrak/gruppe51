package Code;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Collection;

public class ConnectionToDatabase {
 
	  private ArrayList<ResultSetMetaData> metaData = new ArrayList<ResultSetMetaData>();
	  private ArrayList<ResultSet> resultData = new ArrayList<ResultSet>();	
	  private List<Employee> employees = new ArrayList<Employee>();
	

	
	public List<Employee> Sporring(Connection con, String sporring) throws SQLException{
		
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
			 Boolean admin = false;
			  
			  while (resultData.get(counter).next()) {
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
	
	
	public void NewEmployee(Connection con, Employee e) throws SQLException{
			
		PreparedStatement preparedStatement = null;
		String sql = "INSERT INTO Employee (employeeID, name, password, position, username, telnum, admin)" + " VALUES (?, ?, ?, ?, ?, ?, ?)";
		preparedStatement = con.prepareStatement(sql);
		preparedStatement.setInt(1, e.getEmployeeID()); //Her mŒ Employee.getEmployeeID() benyttes for hver enkelt employee
		preparedStatement.setString(2, e.getName()); //Her mŒ Employee.getEmployeeID() benyttes for hver enkelt employee
		preparedStatement.setString(3, e.getPassword()); //Her mŒ Employee.getName() benyttes
		preparedStatement.setString(4, e.getPosition()); //Her mŒ Employee.getPassword() benyttes
		preparedStatement.setString(5, e.getUsername()); //Her mŒ Employee.getPosition() benyttes
		preparedStatement.setInt(6, e.getTelnum()); // Her mŒ Employee.getUsername() benyttes
		preparedStatement.setString(7, e.isAdmin().toString()); // Her mŒ Employee.getUsername() benyttes
		
		preparedStatement.executeUpdate(); //Her oppdateres databasen	
		
	}
	
	public void WriteEventToDatabase(Connection con, Event e) throws SQLException{
		
		//Trenger en for-l¿kke som itererer gjennom alle eksisterende events i selskapet og skriver de til databasen
		//Hvis eventet allerede eksisterer,  ignorer oppdatering
		
		PreparedStatement preparedStatement = null;
		String sql = "INSERT INTO Event (eventID, title, startTime, endTime, description, roomID, creator)" + "VALUES (?, ?, ?, ?, ?, ?, ?)";
		preparedStatement = con.prepareStatement(sql);
		preparedStatement.setInt(1, e.getEventID()); //Her mŒ Event.getEventID() benyttes for hvert enkelt event
		preparedStatement.setString(2, e.getTitle()); //Her mŒ Event.getTitle() benyttes
		preparedStatement.setTimeStamp(3, e.getStartTime()); //Her mŒ Event.getStartTime() benyttes
		preparedStatement.set(4, e.getEndTime()); //Her mŒ Event.getEndTime() benyttes
		preparedStatement.setString(5, "Klientmote"); // Her mŒ Event.getDescription() benyttes
		preparedStatement.setString(6, "Event123"); // Her mŒ Event.getRoomID() benyttes
		
		preparedStatement.executeUpdate(); //Her oppdateres databasen	
		
	}	
	
	public void WriteRoomToDatabase() throws SQLException{
		
		//Trenger en for-l¿kke som itererer gjennom alle eksisterende rom i selskapet og skriver de til databasen
		//Hvis rommet allerede eksisterer,  ignorer oppdatering
		
		String sql = "INSERT INTO Room (roomID, name, capacity, eventID)" + "VALUES (?, ?, ?, ?)";
		preparedStatement = con.prepareStatement(sql);
		preparedStatement.setInt(1, roomID); //Her mŒ Room.getRoomID() benyttes for hvert enkelt rom
		preparedStatement.setString(2, "name"); //Her mŒ Room.getName() benyttes
		preparedStatement.setInt(3, capacity); //Her mŒ Room.getCapacity() benyttes
		preparedStatement.setInt(4, eventID); //Her mŒ Room.getEventID() benyttes
		
		preparedStatement.executeUpdate(); //Her oppdateres databasen	
		
	}	
	
	public void WriteMessageToDatabase() throws SQLException{
		
		//Trenger en for-l¿kke som itererer gjennom alle eksisterende events i selskapet og skriver de til databasen
		//Hvis eventet allerede eksisterer,  ignorer oppdatering
		
		String sql = "INSERT INTO Message (messageID, type, message)" + "VALUES (?, ?, ?)";
		preparedStatement = con.prepareStatement(sql);
		preparedStatement.setInt(1, messageID); //Her mŒ Message.getMessageID() benyttes for hver enkel message
		preparedStatement.setString(2, "type"); //Her mŒ Message.getType() benyttes
		preparedStatement.setString(3, "message"); //Her mŒ message.getMessage() benyttes
		
		preparedStatement.executeUpdate(); //Her oppdateres databasen	
		
	}	
	
	public void WriteGruppeToDatabase() throws SQLException{
		
		//Trenger en for-l¿kke som itererer gjennom alle eksisterende grupper selskapet og skriver de til databasen
		//Hvis gruppen allerede eksisterer,  ignorer oppdatering
		
		String sql = "INSERT INTO Gruppe (gruppeID, navn, ansvarlig, beskrivelse)" + "VALUES (?, ?, ?, ?)";
		preparedStatement = con.prepareStatement(sql);
		preparedStatement.setInt(1, 0003); //Her mŒ Gruppe.getgruppeID() benyttes for hver enkelt gruppe
		preparedStatement.setString(2, "Mat"); //Her mŒ Gruppe.getName() benyttes for hver gruppe
		preparedStatement.setString(3, "Kantinedama"); //Her mŒ Gruppe.getResponsible() benyttes for hver gruppe
		preparedStatement.setString(4, "Digg mat"); //Her mŒ Gruppe.getDescription() benyttes for hver gruppe

		preparedStatement.executeUpdate(); //Her oppdateres databasen
			
	}
	
	public void WriteDatabaseToJava(ArrayList<ResultSetMetaData> metaData, ArrayList<ResultSet> resultData) throws SQLException{
		
		int counter = 0;
		int decider = 0;
		
		while (counter < metaData.size()) {
			
			int numberOfColumns = metaData.get(counter).getColumnCount();
			  
		      for (int i = 1; i <= numberOfColumns; i++) { //Er en for-l¿kke n¿dvendig her?
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
			  
			  while (resultData.get(counter).next()) {
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
			        }
			        	Employee i = new Employee(name, password, position, username, telnum);//Maa sorge for at nyEmployee-stringen har samme format som inn-parameterene til new Employee
			            employees.add(i);
			      } 
			  return employees;
			  
		  }
	/*	  else if (decider == 2){
			  
			  int roomID = 0;
			  String name = "";
			  int capacity = 0;
			  String description = "";
			  
			  while (resultData.get(counter).next()) {
				  	for (int i = 1; i <= numberOfColumns; i++) {
				  	
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
			          if (i==5){ //Her mŒ roomSchedule ordnes?
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
				          if (i==6){  //hvordan fŒ inn en Employee her?
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
		  else if (decider == 4){ // for message, denne mŒ ses n¾rmere pŒ
			  
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
			          if (i==5){ //Her mŒ roomSchedule ordnes
			        	  roomSchedule = columnValue;
			          }
				  }
			        Message addMessage = new Message(sender, reciever, isRead, content, subject);   //Maa sorge for at formatet her er det samme som i konstruktoren til Group-klassen      
			  } 
			  
		  }
		  else if (decider == 5){
			  
			  int groupID = 0; // mŒ kanskje legges til i Group
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
			          if (i==4){ //Her mŒ participants legges til
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
	}*/
	
	public void PrintTables(ArrayList<ResultSetMetaData> metaData, ArrayList<ResultSet> resultData) throws SQLException{
		
		int counter = 0;
		
		while (counter < metaData.size()) {
			
			int numberOfColumns = metaData.get(counter).getColumnCount();
			  
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

