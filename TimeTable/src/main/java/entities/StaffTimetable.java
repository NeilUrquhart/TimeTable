package entities;

import java.util.ArrayList;
import java.util.List;

public class StaffTimetable 
{
	private Staff staff;
	private List<TTSlot> slots;
	
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
	}
	
	@Override
	public String toString()
	{
		return "";
	}
}
