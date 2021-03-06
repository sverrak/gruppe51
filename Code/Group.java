package Code;

import java.util.ArrayList;
import java.util.Collection;

public class Group {

	private String groupName;
	private String description;
	private Employee responsible;
	private Collection<Employee> participants;
	private Collection<Group> subgroups;
	private Collection<Group> supergroups;
	
	
	public Group(String groupName, String description, Employee responsible) {
		super();
		this.groupName = groupName;
		this.description = description;
		this.responsible = responsible;
		participants = new ArrayList<Employee>();
		subgroups = new ArrayList<Group>();
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

	public Collection<Employee> getParticipants() {
		return participants;
	}


	public void setResponsible(Employee responsible) {
		this.responsible = responsible;
	}
	
// "Grupper administreres utenfor systemet..." - kravlista fra DB.
	public Boolean addEmployee(Employee employee){
		return true;
	}
	public Boolean removeEmployee(Employee employee){
		return true;
	}
	
	public ArrayList<Employee> printParticipants(){
		return (ArrayList<Employee>) this.participants;
	}
	
	public void addSubgroupToGroup(Group group){
		this.subgroups.add(group);
		group.addSupergroupToGroup(this);
	}
	
	// Skal bare kalles fra funksjonen over
	private void addSupergroupToGroup(Group group){
		this.supergroups.add(group);
	}
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return this.groupName;
	}
	
	
}
