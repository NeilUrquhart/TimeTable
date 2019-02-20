package controllers;

import dao.TimetableData;
import entities.Event;
import entities.Staff;
import entities.TTSlot;
import returnCodes.ResponseMove;

public class StaffMoveController
{
	private TimetableData data;
	
	public StaffMoveController(TimetableData data)
	{
		this.data = data;
	}
	
	public TimetableData getTimetableData()
	{
		return data;
	}
	
	// Checks to see if the staff member can move to the event
	public ResponseMove canStaffMoveEvent(Staff staff, Event event)
	{
		// If staff is already in event cannot move into it
		for(Event staffEvent : staff.getEvents())
		{
			if(staffEvent.getId() == event.getId())
			{
				return ResponseMove.ALREADY_IN_EVENT;
			}
		}
		
		// Check through all of the staffs events time slots and if there is a conflict kick out
		for(Event staffEvent : staff.getEvents())
		{
			for(TTSlot staffSlot : staffEvent.getSlots())
			{
				for(TTSlot eventSlot : event.getSlots())
				{
					if(eventSlot.getId() == staffSlot.getId())
					{
						return ResponseMove.NO_TIME_AVAILABLE;
					}
				}
			}
		}
		
		return ResponseMove.OK;
	}
	
	// Moves staff into the event
	public void moveStaffToEvent(Staff staff, Event event)
	{
		// Add the event to the staff and vice versa
		staff.getEvents().add(event);
		event.getStaff().add(staff);
		
		// Update data
		updateStaffInData(staff);
		updateEventInData(event);
	}
	
	// Removes an event from the staff member
	public void removeEventFromStaff(Staff staff, Event event)
	{
		int index = -1;
		for(Event staffEvent : staff.getEvents())
		{
			if(staffEvent.getId() == event.getId())
			{
				index = staff.getEvents().indexOf(staffEvent);
			}
		}
		
		if(index != -1)
		{
			staff.getEvents().remove(index);
		}
		
		updateStaffInData(staff);
		updateEventInData(event);
	}
	
	private void updateStaffInData(Staff staff)
	{
		for(Staff temp : data.getStaff().values())
		{
			if(temp.getName() == staff.getName())
			{
				temp = staff;
			}
		}
	}
	
	private void updateEventInData(Event event)
	{
		for(Event temp : data.getEvents().values())
		{
			if(temp.getId() == event.getId())
			{
				temp = event;
			}
		}
	}
}
