package controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import dao.TimetableData;
import entities.Event;
import entities.Module;
import entities.Programme;
import entities.Room;
import entities.Slot;
import entities.Staff;

public class QueryController
{
	private TimetableData data;
	
	public void setTimetableData(TimetableData data)
	{
		this.data = data;
	}
	
	public QueryController(TimetableData data)
	{
		setTimetableData(data);
	}
	
	public Programme getProgrammeByName(String name)
	{
		Programme result = null;
		for(Programme programme : data.getProgrammes().values())
		{
			if(programme.getName().toUpperCase().equals(name.toUpperCase()))
			{
				result = programme;
				break;
			}
		}
		return result;
	}
	
	public Module getModuleByName(String name)
	{
		Module result = null;
		for(Module module : data.getModules().values())
		{
			if(module.getName().toUpperCase().contains(name.toUpperCase()))
			{
				result = module;
				break;
			}
		}
		return result;
	}
	
	public Room getRoomByName(String name)
	{
		Room result = null;
		for(Room room : data.getRooms().values())
		{
			if(room.getName().toUpperCase().equals(name.toUpperCase()))
			{
				result = room;
				break;
			}
		}
		return result;
	}
	
	public Staff getStaffByName(String name)
	{
		Staff result = null;
		for(Staff staff : data.getStaff().values())
		{
			if(staff.getName().toUpperCase().equals(name.toUpperCase()))
			{
				result = staff;
				break;
			}
		}
		return result;
	}
	
	public List<Slot> getSlotsFreeForRoom(String roomName)
	{
		Map<Integer, Slot> result = data.getSlots();
		Room room = null;
		for(Room r : data.getRooms().values())
		{
			if(r.getName().toUpperCase().equals(roomName.toUpperCase()))
			{
				room = r;
				break;
			}
		}
		
		if(room == null)
			return new ArrayList<Slot>();
		
		for(Event event : room.getEvents())
		{
			for(Slot slot : event.getSlots())
			{
				result.remove(slot.getId());
			}
		}
		return new ArrayList<Slot>(result.values());
	}
	
	public List<Slot> getSlotsFreeForStaff(String name)
	{
		Map<Integer, Slot> result = data.getSlots();
		Staff staff = null;
		for(Staff s : data.getStaff().values())
		{
			if(s.getName().toUpperCase().equals(name.toUpperCase()))
			{
				staff = s;
				break;
			}
		}
		
		if(staff == null)
			return new ArrayList<Slot>();
		
		for(Event event : staff.getEvents())
		{
			for(Slot slot : event.getSlots())
			{
				result.remove(slot.getId());
			}
		}
		
		return new ArrayList<Slot>(result.values());
	}
}
