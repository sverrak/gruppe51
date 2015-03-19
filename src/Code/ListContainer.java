package Code;

import java.util.ArrayList;
import java.util.List;

public class ListContainer {
	private List<Event> events;
	private List<Employee> employees;
	
	public ListContainer(List<Event> events, List<Employee> employees) {
		this.events = events;
		this.employees = employees;
	}
	
	public List<Employee> getEmployees() {
		return employees;
	}
	public List<Event> getEvents() {
		return events;
	}

}
