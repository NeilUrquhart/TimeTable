package controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import dao.TimetableData;
import entities.Event;
import entities.Room;
import entities.Slot;

public class MoveController
{
	private TimetableData data;
	private QueryController query;
	
	public TimetableData getData()
	{
		return data;
	}
	
	public MoveController(TimetableData data)
	{
		this.data = data;
		query = new QueryController(data);
	}
	
	public boolean canEventMoveToSlot(Event event, Slot moveTo) 
	{
		Room room = query.getRoomByName(event.getRoom().getName());
		for(Event eventInRoom : room.getEvents())
		{
			for(Slot slot : eventInRoom.getSlots())
			{
				if(slot.getId() == moveTo.getId())
					return false;
			}
		}
		return true;
	}
}
