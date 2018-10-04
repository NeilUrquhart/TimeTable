package toStringHelpers;

import java.util.List;

import entities.Staff;

public class StaffToString implements ObjectToString
{

	private Staff staff;
	
	public StaffToString(Staff staff)
	{
		this.staff = staff;
	}
	
	public String stringify()
	{
		String result = String.format("\tStaff Details"
				+ "\nName:\t\t%1$s"
				+ "\n\n\tEvents On Staff"
				+ "\n%2$s", 
				staff.getName(), getEventsAsString());
		return result;
	}
	
	private String getEventsAsString()
	{
		String result = EventToString.getEventsAsString(staff.getEvents());
		if(result == "")
		{
			result = "No Events On Staff!";
		}
		return result;
	}

	public static String getStaffAsString(List<Staff> staffMembers)
	{
		String result = "";
		for(Staff s : staffMembers)
		{
			result += String.format("%1$s, ", s.getName());
		}
		return result.replaceAll(", $", "");
	}
}
