package Code;

import com.sun.xml.internal.bind.v2.runtime.unmarshaller.Receiver;

public class Message {
	private Employee sender;
	private Employee receiever;
	private Boolean isRead;
	private String content;
	private String subject;
	
	public Message(Employee sender, Employee receiever, Boolean isRead,
			String content, String subject) {
		super();
		this.sender = sender;
		this.receiever = receiever;
		this.isRead = isRead;
		this.content = content;
		this.subject = subject;
	}
	public Employee getSender() {
		return sender;
	}
	public Employee getReceiever() {
		return receiever;
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
		this.receiever.addMessageToInbox(this);
	}
	
	@Override
	public String toString() {
		String str = "FROM: " +  this.sender + "\n";
		str += "TO: " + this.receiever + "\n";
		str += "SUBJECT: " + this.subject + "\n";
		str += "CONTENT:" + this.content + "\n";
		return str;	
	}
	
	
	
}
