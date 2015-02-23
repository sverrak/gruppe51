package Code;

import java.util.ArrayList;

public class Group {

	private String groupName;
	private String description;
	private Employee responsible;
	private Collection<Employee> participants;
	
	
	public Group(String groupName, String description, Employee responsible) {
		super();
		this.groupName = groupName;
		this.description = description;
		this.responsible = responsible;
	}
	
	public Boolean addEmployee(Employee employee){
		return true;
	}
	
	public Boolean removeEmployee(Employee employee){
		return true;
	}
	
	public ArrayList<Employee> printParticipants(){
		return this.participants;
	}
	
	
}
