package controllers;

import java.util.List;

import dao.TimetableData;
import entities.Event;
import entities.Room;
import entities.TTSlot;
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
		if(event.getRoom().getName() != room.getName())
		{
			// Check to see if the new room has slot conflicts with the event
			for(TTSlot eventSlot : event.getSlots())
			{
				for(Event temp : room.getEvents())
				{
					for(TTSlot roomSlot : temp.getSlots())
					{
						if(eventSlot.getId() == roomSlot.getId())
						{
							return ResponseMove.NO_ROOM_AVAILABLE;
						}
					}
				}
			}
		}
		
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
}
