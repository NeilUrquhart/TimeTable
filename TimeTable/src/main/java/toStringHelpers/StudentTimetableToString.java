package toStringHelpers;

import ontology.elements.StudentTimetable;
import ontology.elements.TTSlot;

public class StudentTimetableToString implements ObjectToString 
{
	private StudentTimetable timeTable;
	
	public StudentTimetableToString(StudentTimetable timeTable)
	{
		this.timeTable = timeTable;
	}
	
	@Override
	public String stringify() 
	{
		String result = String.format("\tStudent Timetable"
				+ "\nStudent Matric:\t%1$s", timeTable.getStudent().getMatric());
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
