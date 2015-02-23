package Code;

import java.util.ArrayList;
import java.util.Collection;

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
	
	
	public String getGroupName() {
		return groupName;
	}



	public void setGroupName(String groupName) {
		this.groupName = groupName;
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



	public void setResponsible(Employee responsible) {
		this.responsible = responsible;
	}



	public Boolean addEmployee(Employee employee){
		return true;
	}
	
	public Boolean removeEmployee(Employee employee){
		return true;
	}
	
	public ArrayList<Employee> printParticipants(){
		return (ArrayList<Employee>) this.participants;
	}
	
	
}
