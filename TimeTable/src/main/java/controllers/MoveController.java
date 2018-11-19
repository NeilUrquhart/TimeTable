package controllers;

import java.util.ArrayList;
import java.util.List;
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
	
	public boolean moveEventToNewSlots(Event event, Slot moveTo)
	{
		if(!canEventMoveToSlot(event, moveTo))
			return false;
		
		List<Slot> slots = getAllSlotsForMove(moveTo.getId(), event.getSlots().size());
		event.getSlots().clear();
		event.setSlots(slots);
		updateDataWithNewEvent(event);
		return true;
	}
	
	public boolean canEventMoveToSlot(Event event, Slot moveTo) 
	{
		Room room = getRoomForEvent(event);
		if(isRoomInUse(room, moveTo))
			return false;
		
		int numOfSlots = event.getSlots().size();
		
		if(numOfSlots == 1)
			return true;
		
		if((moveTo.getId() + numOfSlots) - 1 <= 45)
		{
			List<Slot> slots = getAllSlotsForMove(moveTo.getId(), numOfSlots);
			
			if(!areAllSlotsFreeForRoom(room, slots))
				return false;
		}
		
		return true;
	}
	
	private Room getRoomForEvent(Event event)
	{
		return query.getRoomByName(event.getRoom().getName());
	}
	
	private boolean isRoomInUse(Room room, Slot slot)
	{
		for(Event eventInRoom : room.getEvents())
		{
			for(Slot slotInEvent : eventInRoom.getSlots())
			{
				if(slotInEvent.getId() == slot.getId())
					return true;
			}
		}
		return false;
	}
	
	private List<Slot> getAllSlotsForMove(int startSlotId, int numOfSlots)
	{
		List<Slot> result = new ArrayList<Slot>();
		for(int i = startSlotId; i < startSlotId + numOfSlots; i++)
		{
			result.add(query.getSlotById(i));
		}
		return result;
	}
	
	private boolean areAllSlotsFreeForRoom(Room room, List<Slot> slots)
	{
		for(Event eventInRoom : room.getEvents())
		{
			for(Slot slotInEvent : eventInRoom.getSlots())
			{
				for(Slot slot : slots)
				{
					if(slotInEvent.getId() == slot.getId())
						return false;
				}
			}
		}
		return true;
	}
	
	private void updateDataWithNewEvent(Event event)
	{
		for(Event eventInData : data.getEvents().values())
		{
			if(eventInData.getId() == event.getId())
			{
				eventInData = event;
			}
		}
		query.setTimetableData(data);
	}
}
