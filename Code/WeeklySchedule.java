package Code;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

// denne klassen utgår, men lar den ligge slik at vi kan se den.

public class WeeklySchedule {
	
	private ArrayList<ArrayList<Object>> schedule;
	private int weekOfYear;
	private int year;
	
	// oppretter "tom" matrise for ukeplan. Alle felter er 0
	public WeeklySchedule(){
		schedule= new ArrayList<ArrayList<Object>>();
		for (int row = 0; row < 20; row++) {
			schedule.add(new ArrayList<Object>());
			for (int col = 0; col < 7; col++) {
				schedule.get(row).add(0);
			}
		}
		Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
		weekOfYear = calendar.get(Calendar.WEEK_OF_YEAR);
		weekOfYear = calendar.get(Calendar.YEAR);
	}
	
	public ArrayList<ArrayList<Object>> getSchedule() {
		return schedule;
	}
	public int getWeekOfYear() {
		return weekOfYear;
	}
	public int getYear() {
		return year;
	}
	
	
/*	@Override
	public String toString() {
		Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
		int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);		// ukedagen i dag
		
		System.out.println("\tSoendag  Mandag  Tirsdag  Onsdag  Torsdag   Fredag  Loerdag");
		
		
		for (int i = 0; i < 10; i++) {
			
			String str = (8+i) + ":00";
			str += "\n";
			str += ((8+i) + ":30");
		}
		
		

		return str;
	}
	*/
}

