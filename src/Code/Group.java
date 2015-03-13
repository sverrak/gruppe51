package Code;

import java.util.ArrayList;
import java.util.Collection;

public class Group {
	private int groupID;
	private String groupName;
	private String description;
	private Employee responsible;
	private Collection<Employee> participants;
	private Collection<Group> subgroups;
	private Collection<Group> supergroups;
	
	
	public Group(String groupName, String description, Employee responsible) {
		this.groupName = groupName;
		this.description = description;
		this.responsible = responsible;
		participants = new ArrayList<Employee>();
		subgroups = new ArrayList<Group>();
	}
	
	public Collection<Group> getSubgroups() {
		return subgroups;
	}public Collection<Group> getSupergroups() {
		return supergroups;
	}
	
	public int getGroupID() {
		return groupID;
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
	public void addEmployee(Employee employee){
		participants.add(employee);
	}
	public void removeEmployee(Employee employee){
		participants.remove(employee);
	}
	
	//hva er hensikten med denne metoden??
	public ArrayList<Employee> printParticipants(){
		return (ArrayList<Employee>) this.participants;
	}
	
	public void addSubgroupToGroup(Group group){
		this.subgroups.add(group);
		group.addGroupToSupergroup(this);
	}
	
	// Skal bare kalles fra funksjonen over
	private void addGroupToSupergroup(Group group){
		this.supergroups.add(group);
		group.addSubgroupToGroup(this);
	}
	@Override
	public String toString() {
		return this.groupName;
	}
	
	
}
