package toStringHelpers;

import entities.StaffTimetable;
import entities.TTSlot;

public class StaffTimetableToString implements ObjectToString 
{
	private StaffTimetable timeTable;
	
	public StaffTimetableToString(StaffTimetable timeTable)
	{
		this.timeTable = timeTable;
	}
	
	@Override
	public String stringify() 
	{
		String result = String.format("\tStaff Timetable"
				+ "\nStaff Name:\t%1$s", timeTable.getStaff().getName());
		for(TTSlot slot : timeTable.getSlots())
		{
			result += String.format("\n\nId: %1$s Day: %2$s Start Time: %3$s End Time: %4$s Is Used: %5$s",
					slot.getId(), slot.getDay(), slot.getStartTime(), slot.getEndTime(), slot.isUsed());
			
			if(slot.isUsed())
			{
				result += String.format("\n\tEVENT: %1$s %2$s %3$s [%4$s]", 
						slot.getEvent().getModule().getName(), 
						slot.getEvent().getType(), 
						slot.getEvent().getInstance(),
						slot.getEvent().getModule().getDescription());
			}
		}
		return result;
	}

}
