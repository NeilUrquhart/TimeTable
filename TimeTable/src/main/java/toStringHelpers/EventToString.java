package toStringHelpers;

import java.util.List;

import entities.Event;
import entities.StudentInEvent;

public class EventToString implements ObjectToString
{
	private Event event;
	
	public EventToString(Event event)
	{
		this.event = event;
	}
	
	public String stringify()
	{
		String result = String.format("Event:\t\t%1$s"
				+ "\nType:\t\t%2$s"
				+ "\nInstance:\t%3$s"
				+ "\nModule:\t\t%4$s [%5$s]"
				+ "\nRoom:\t\t%6$s [Capacity: %7$s]"
				+ "\nStaff:\t\t%8$s"
				+ "\nStudents:\t%9$s", 
				event.getId(), event.getType(), event.getInstance(), 
				event.getModule().getName(), event.getModule().getDescription(),
				event.getRoom().getName(), event.getRoom().getCapacity(),
				getStaffAsString(), getStudentsAsString());
		return result;
	}
	
	private String getStaffAsString()
	{
		String result = StaffToString.getStaffAsString(event.getStaff());
		if(result == "")
		{
			result = "No Staff On Event!";
		}
		return result;
	}
	
	private String getStudentsAsString()
	{
		String result = "";
		for(StudentInEvent s : event.getStudents())
		{
			result += String.format("%1$s [%2$s], ", 
					s.getStudent().getMatric(), s.getStatus());
		}
		
		if(result == "")
		{
			result = "No Students On Event!";
		}
		return result.replaceAll(", $", "");
	}
	
	public static String getEventsAsString(List<Event> events)
	{
		String result = "";
		for(Event e : events)
		{
			result += e.toString() + "\n\n";
		}
		return result;
	}
}
