package controllers;

import dao.TimetableData;
import entities.Event;
import entities.Room;
import entities.Slot;

public class MoveController
{
	private TimetableData data;
	
	public TimetableData getData()
	{
		return data;
	}
	
	public MoveController(TimetableData data)
	{
		this.data = data;
	}
	
	public boolean canEventMoveToSlot(Event event, Slot moveTo) {
		Room room = event.getRoom();
		room.getSlots().clear();
		for(Event e : room.getEvents()) {
			for(Slot s : e.getSlots()) {
				room.getSlots().add(s);
			}
		}
		
		if(room.getSlots().contains(moveTo))
			return false;
		
		int slotCountOfEvent = event.getSlots().size();
		
		if(slotCountOfEvent > 1) {
			
		}
		
		return true;
	}
}
