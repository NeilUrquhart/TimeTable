package entities;

import java.util.ArrayList;
import java.util.List;

import jade.content.Concept;
import toStringHelpers.ObjectToString;
import toStringHelpers.StaffTimetableToString;

public class StaffTimetable implements Concept
{
	private Staff staff;
	private List<TTSlot> slots;
	private ObjectToString objectToString;
	
	public Staff getStaff()
	{
		return staff;
	}
	public List<TTSlot> getSlots()
	{
		return slots;
	}
	
	public void setStaff(Staff staff)
	{
		this.staff = staff;
	}
	public void setSlots(List<TTSlot> slots)
	{
		this.slots = slots;
	}
	
	public StaffTimetable()
	{
		this(new Staff(), new ArrayList<TTSlot>());
	}
	public StaffTimetable(Staff staff, List<TTSlot> slots)
	{
		setStaff(staff);
		setSlots(slots);
		objectToString = new StaffTimetableToString(this);
	}
	
	@Override
	public String toString()
	{
		return objectToString.stringify();
	}
}
