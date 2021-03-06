package Code;

import java.util.Date;
import java.sql.Timestamp;


public class Message {
	private int messageID;
	private Employee sender;
	private Employee receiver;
	private Boolean read;
	private String content;
	private String subject;
	private final java.sql.Timestamp timestamp;
	
	public Message(Employee sender, Employee receiver, java.sql.Timestamp dateTime,
			String content, String subject) {
		super();
		this.sender = sender;
		this.receiver = receiver;
		this.read = false;
		this.content = content;
		this.subject = subject;
		this.timestamp = dateTime;

	}
	
	public Message(Employee sender, Employee receiver, String content, String subject){
		super();
		this.sender = sender;
		this.receiver = receiver;
		this.read = false;
		this.content = content;
		this.subject = subject;
		
		java.util.Date date = new java.util.Date();
		this.timestamp = new java.sql.Timestamp(date.getTime());
		
	}
	
	public Message(int messageID, Employee sender, Employee receiver, java.util.Date timestamp, String content, String subject, Boolean isRead) {
		this.messageID = messageID;
		this.sender = sender;
		this.receiver = receiver;
		this.read = isRead;
		this.content = content;
		this.subject = subject;
		java.util.Date date = timestamp;
		this.timestamp = new java.sql.Timestamp(date.getTime());
	}

	public Timestamp getTimeStamp(){
		return timestamp;
	}
	
	public int getMessageID(){
		return messageID;
	}
	public Employee getSender() {
		return sender;
	}
	public Employee getReceiver() {
		return receiver;
	}
	public Boolean isRead() {
		return read;
	}
	public String getContent() {
		return content;
	}
	public String getSubject() {
		return subject;
	}
		
	public String readMessage(){
		this.read = true;
		//db-fix fredrik? vil at isRead-feltet i databasen skal endres til true
		return toString();
	}
	
	public void sendMessage(){
		this.receiver.addMessageToInbox(this);
	}
	
	@Override
	public String toString() {
		this.read = true;
		String str = timestamp + "\n";
		str += "FROM: " +  this.sender + "\n";
		str += "TO: " + this.receiver + "\n";
		str += "SUBJECT: " + this.subject + "\n";
		str += "CONTENT:" + this.content + "\n";
		return str;	
	}
	public void read() {
		this.read = true;
		
	}
	
	
	
}
