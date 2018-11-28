package entities;

import java.util.ArrayList;
import java.util.List;

public class RoomTimetable 
{
	private List<TTSlot> slots;
	
	public List<TTSlot> getRoomTimetable()
	{
		return slots;
	}
	
	public RoomTimetable()
	{
		slots = new ArrayList<TTSlot>();
	}
	
}
