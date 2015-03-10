package Code;

import java.util.Date;


public class Message {
	private int messageID;
	private Employee sender;
	private Employee receiver;
	private Boolean isRead;
	private String content;
	private String subject;
	private final Date timestamp;
	
	public Message(Employee sender, Employee receiver,
			String content, String subject) {
		super();
		this.sender = sender;
		this.receiver = receiver;
		this.isRead = false;
		this.content = content;
		this.subject = subject;
		this.timestamp = new Date();
	}
	
	public Date getTimeStamp(){
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
	public Boolean getIsRead() {
		return isRead;
	}
	public String getContent() {
		return content;
	}
	public String getSubject() {
		return subject;
	}
	
	public String readMessage(){
		this.isRead = true;
		return toString();
	}
	
	public void sendMessage(){
		this.receiver.addMessageToInbox(this);
		System.out.println("Melding sendt til " + this.receiver + ":" + this.subject);
	}
	
	@Override
	public String toString() {
		this.isRead = true;
		String str = "FROM: " +  this.sender + "\n";
		str += "TO: " + this.receiver + "\n";
		str += "SUBJECT: " + this.subject + "\n";
		str += "CONTENT:" + this.content + "\n";
		return str;	
	}
	public void read() {
		this.isRead = true;
		
	}
	
	
	
}
