package ontology.elements;

import java.util.ArrayList;
import java.util.List;


public class RoomTimetable 
{
	private Room room;
	private List<TTSlot> slots;
	
	public Room getRoom()
	{
		return room;
	}	
	public List<TTSlot> getSlots()
	{
		return slots;
	}
	
	public void setRoom(Room room)
	{
		this.room = room;
	}
	public void setSlots(List<TTSlot> slots)
	{
		this.slots = slots;
	}
	
	public RoomTimetable()
	{
		this(new Room(), new ArrayList<TTSlot>());
	}
	public RoomTimetable(Room room, List<TTSlot> slots)
	{
		setRoom(room);
		setSlots(slots);
	}
	
	@Override
	public String toString()
	{
		return "";
	}
}
