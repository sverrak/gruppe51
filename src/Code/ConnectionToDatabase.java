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
import java.util.Scanner;
import java.util.Collection;

public class ConnectionToDatabase {
	
	  private Connection con = null;
	  private Statement stmt1 = null;
	  private Statement stmt2 = null;
	  private Statement stmt3 = null;
	  private Statement stmt4 = null;
	  private Statement stmt5 = null;
	  private PreparedStatement preparedStatement = null;
	  private ResultSet resultSet = null; 
	  private ArrayList<ResultSetMetaData> metaData = new ArrayList<ResultSetMetaData>();
	  private ArrayList<ResultSet> resultData = new ArrayList<ResultSet>();	
	
	public static void connection(){
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	
	public void ConnectionToMySql(){
		connection();
		String host = "jdbc:mysql://mysql.stud.ntnu.no:3306/fredrwit_kalender";
		String username = "fredrwit_admin";
		String password ="12345";
		System.out.println("Connecting to database...");
		System.out.println("");
		System.out.println("Fetching database tables...");
		System.out.println("");
		System.out.println("");
		try {
			con = DriverManager.getConnection(host, username, password);
			
		/*  System.out.println("Connection Works");
			stmt1 = con.createStatement();
			stmt2 = con.createStatement();
			stmt3 = con.createStatement();
			stmt4 = con.createStatement();
			stmt5 = con.createStatement(); 
			
			
			//String input = Scan();
			String employee = "SELECT * FROM Employee";
			ResultSet employeeSet = stmt1.executeQuery(employee);
			ResultSetMetaData employeesmd = employeeSet.getMetaData();
			metaData.add(employeesmd);
			resultData.add(employeeSet);
			WriteEventToDatabase();
			WriteDatabaseToJava(metaData, resultData);
			
			String room = "SELECT * FROM Room";
			ResultSet roomSet = stmt2.executeQuery(room);
			ResultSetMetaData roomsmd = roomSet.getMetaData();
			metaData.add(roomsmd);
			resultData.add(roomSet);	
			
			String event = "SELECT * FROM Event";
			ResultSet eventSet = stmt3.executeQuery(event);
			ResultSetMetaData eventsmd = eventSet.getMetaData();
			metaData.add(eventsmd);
			resultData.add(eventSet);
			
			String message = "SELECT * FROM Message";
			ResultSet messageSet = stmt4.executeQuery(message);
			ResultSetMetaData messagesmd = messageSet.getMetaData();
			metaData.add(messagesmd);
			resultData.add(messageSet);
			
			
			String group = "SELECT * FROM Gruppe";
			ResultSet groupSet = stmt5.executeQuery(group); 
			ResultSetMetaData groupsmd = groupSet.getMetaData();
			metaData.add(groupsmd);
			resultData.add(groupSet);
			PrintTables(metaData, resultData); 
			
			
			String spm = "Do you wish to write to database?\n Yes/no";
			String input = Scan(spm);
			
			if(input.equalsIgnoreCase("yes")){
				
				WriteEmployeeToDatabase();
				System.out.println("Database was successfully updated");
			} */
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch(Exception e){
		      //Handle errors for Class.forName
		      e.printStackTrace();
		} finally{
		      //finally block used to close resources
		      try{
		         if(stmt1 != null)
		            con.close();
		      }catch(SQLException se){
		      }// do nothing
		      try{
		         if(con != null)
		            con.close();
		      }catch(SQLException se){
		         se.printStackTrace();
		      }//end finally try
		   }//end try
	}
	
	public void WriteEmployeeToDatabase() throws SQLException{
		
		//Trenger en for-løkke som itererer gjennom alle eksisterende employees i selskapet og skriver de til databasen
		//Hvis vedkommende allerede eksisterer,  ignorer oppdatering
		
		String sql = "INSERT INTO Employee (employeeID, name, password, position, username, telnum)" + " VALUES (?, ?, ?, ?, ?, ?)";
		preparedStatement = con.prepareStatement(sql);
		preparedStatement.setInt(1, 6); //Her må Employee.getEmployeeID() benyttes for hver enkelt employee
		preparedStatement.setString(2, "Sandra"); //Her må Employee.getName() benyttes
		preparedStatement.setString(3, "mops"); //Her må Employee.getPassword() benyttes
		preparedStatement.setString(4, "okonomisjef"); //Her må Employee.getPosition() benyttes
		preparedStatement.setString(5, "sandras"); // Her må Employee.getUsername() benyttes
		preparedStatement.setInt(6, 22225555); // Her må Employee.getTelNum() benyttes
		
		preparedStatement.executeUpdate(); //Her oppdateres databasen	
		System.out.println("En ny ansatt ble lagt til i databasen");
		
	}
	
	public void WriteEventToDatabase() throws SQLException{
		
		//Trenger en for-løkke som itererer gjennom alle eksisterende events i selskapet og skriver de til databasen
		//Hvis eventet allerede eksisterer,  ignorer oppdatering
		
		String sql = "INSERT INTO Event (eventID, tittel, startTime, endTime, description, roomID)" + "VALUES (?, ?, ?, ?, ?, ?)";
		preparedStatement = con.prepareStatement(sql);
		preparedStatement.setInt(1, 1); //Her må Event.getEventID() benyttes for hvert enkelt event
		preparedStatement.setString(2, "Mote"); //Her må Event.getTitle() benyttes
		preparedStatement.setTime(3, 10:30); //Her må Event.getStartTime() benyttes
		preparedStatement.setTime(4, 11:30); //Her må Event.getEndTime() benyttes
		preparedStatement.setString(5, "Klientmote"); // Her må Event.getDescription() benyttes
		preparedStatement.setString(6, "Event123"); // Her må Event.getRoomID() benyttes
		
		preparedStatement.executeUpdate(); //Her oppdateres databasen	
		
	}	
	
	public void WriteRoomToDatabase() throws SQLException{
		
		//Trenger en for-løkke som itererer gjennom alle eksisterende rom i selskapet og skriver de til databasen
		//Hvis rommet allerede eksisterer,  ignorer oppdatering
		
		String sql = "INSERT INTO Room (roomID, name, capacity, eventID)" + "VALUES (?, ?, ?, ?)";
		preparedStatement = con.prepareStatement(sql);
		preparedStatement.setInt(1, roomID); //Her må Room.getRoomID() benyttes for hvert enkelt rom
		preparedStatement.setString(2, "name"); //Her må Room.getName() benyttes
		preparedStatement.setInt(3, capacity); //Her må Room.getCapacity() benyttes
		preparedStatement.setInt(4, eventID); //Her må Room.getEventID() benyttes
		
		preparedStatement.executeUpdate(); //Her oppdateres databasen	
		
	}	
	
	public void WriteMessageToDatabase() throws SQLException{
		
		//Trenger en for-løkke som itererer gjennom alle eksisterende events i selskapet og skriver de til databasen
		//Hvis eventet allerede eksisterer,  ignorer oppdatering
		
		String sql = "INSERT INTO Message (messageID, type, message)" + "VALUES (?, ?, ?)";
		preparedStatement = con.prepareStatement(sql);
		preparedStatement.setInt(1, messageID); //Her må Message.getMessageID() benyttes for hver enkel message
		preparedStatement.setString(2, "type"); //Her må Message.getType() benyttes
		preparedStatement.setString(3, "message"); //Her må message.getMessage() benyttes
		
		preparedStatement.executeUpdate(); //Her oppdateres databasen	
		
	}	
	
	public void WriteGruppeToDatabase() throws SQLException{
		
		//Trenger en for-løkke som itererer gjennom alle eksisterende grupper selskapet og skriver de til databasen
		//Hvis gruppen allerede eksisterer,  ignorer oppdatering
		
		String sql = "INSERT INTO Gruppe (gruppeID, navn, ansvarlig, beskrivelse)" + "VALUES (?, ?, ?, ?)";
		preparedStatement = con.prepareStatement(sql);
		preparedStatement.setInt(1, 0003); //Her må Gruppe.getgruppeID() benyttes for hver enkelt gruppe
		preparedStatement.setString(2, "Mat"); //Her må Gruppe.getName() benyttes for hver gruppe
		preparedStatement.setString(3, "Kantinedama"); //Her må Gruppe.getResponsible() benyttes for hver gruppe
		preparedStatement.setString(4, "Digg mat"); //Her må Gruppe.getDescription() benyttes for hver gruppe

		preparedStatement.executeUpdate(); //Her oppdateres databasen
			
	}
	
	public void WriteDatabaseToJava(ArrayList<ResultSetMetaData> metaData, ArrayList<ResultSet> resultData) throws SQLException{
		
		int counter = 0;
		int decider = 0;
		
		while (counter < metaData.size()) {
			
			int numberOfColumns = metaData.get(counter).getColumnCount();
			  
		      for (int i = 1; i <= numberOfColumns; i++) { //Er en for-løkke nødvendig her?
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
			  String telnum ="";
			  
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
			        	  password = columnValue;
			          }
			          if (i==6){
			        	  telnum = columnValue;
			          }
			        }
			        	Employee i = new Employee(employeeID, name, password, position, username, telnum);//Maa sorge for at nyEmployee-stringen har samme format som inn-parameterene til new Employee
			        	String navn = i.getName();
			        	System.out.println(navn);
			      } 
			  
		  }
		  else if (decider == 2){
			  
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
			       /*   if (i==5){ //Her må roomSchedule ordnes?
			        	  roomSchedule = columnValue;
			          }*/
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
				          if (i==6){  //hvordan få inn en Employee her?
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
		  else if (decider == 4){ // for message, denne må ses nærmere på
			  
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
			          if (i==5){ //Her må roomSchedule ordnes
			        	  roomSchedule = columnValue;
			          }
				  }
			        Message addMessage = new Message(sender, reciever, isRead, content, subject);   //Maa sorge for at formatet her er det samme som i konstruktoren til Group-klassen      
			  } 
			  
		  }
		  else if (decider == 5){
			  
			  int groupID = 0; // må kanskje legges til i Group
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
			          if (i==4){ //Her må participants legges til
			        	  description = (columnValue);
			          }
			          if (i==5){
			        	  description = columnValue;
			          }
				  }

			        Group addGroup = new Group(groupID, description, responsible);   //Maa sorge for at nyGruppe-stringen har samme format som inn-parameterene til new Gruppe        
			  }  
			  
		  } */
		      counter++;
		}
	}
	
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
	
	public static class PrintColumnTypes  {

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
		test.ConnectionToMySql();
			
	}

}

