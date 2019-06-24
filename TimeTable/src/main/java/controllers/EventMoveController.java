package controllers;

import java.util.ArrayList;
import java.util.List;

import dao.TimetableData;
import ontology.elements.Event;
import ontology.elements.Room;
import ontology.elements.TTSlot;
import returnCodes.ResponseMove;

public class EventMoveController
{
	private TimetableData data;
	
	public void setTimetableData(TimetableData data)
	{
		this.data = data;
	}
	
	public TimetableData getTimetableData()
	{
		return data;
	}
	
	public EventMoveController()
	{
		data = new TimetableData();
	}
	
	public ResponseMove moveEventRooms(Event event, Room room)
	{
		ResponseMove move = canEventMoveRooms(event, room);
		if(move != ResponseMove.OK)
			return move;
		
		// Set the room to the events room and then set the event to the room
		event.setRoom(room);
		room.getEvents().add(event);
		
		// Update the data
		for(Event temp : data.getEvents().values())
		{
			if(temp.getId() == event.getId())
			{
				temp = event;
			}
		}
		
		for(Room temp : data.getRooms().values())
		{
			if(temp.getName() == room.getName())
			{
				temp = room;
			}
		}
		
		return ResponseMove.OK;
	}
	
	public ResponseMove canEventMoveRooms(Event event, Room room)
	{
		// If the event is already in the room it cannot move into it
		if(event.getRoom().getName() == room.getName())
			return ResponseMove.NO_ROOM_AVAILABLE;
		
		// Check to see if the events time clash with the rest of the events in the rooms times
		for(TTSlot eventSlot : event.getSlots())
		{
			for(Event temp : room.getEvents())
			{
				for(TTSlot roomSlot : temp.getSlots())
				{
					if(eventSlot.getId() == roomSlot.getId())
						return ResponseMove.NO_ROOM_AVAILABLE;
				}
			}
		}
		return ResponseMove.OK;
	}
	
	// TODO: Finish this
	public ResponseMove canEventMoveToNewSlot(Event event, TTSlot startSlot)
	{
		// Since any slots that are divisible by 9 are the final ones of the day
		// If the event takes more than 1 slot know back
		if(startSlot.getId() % 9 == 0 && event.getSlots().size() > 1)
			return ResponseMove.NO_ROOM_AVAILABLE;
		
		// Will hold all the slots that this event spans
		List<TTSlot> newSlots = new ArrayList<TTSlot>();
		
		// Get the amount of slots its current takes and -1
		
		for(TTSlot slot : event.getRoom().getSlots())
		{
			if(slot.getId() == startSlot.getId())
				return ResponseMove.NO_ROOM_AVAILABLE;
		}
		
		return ResponseMove.OK;
	}
}
