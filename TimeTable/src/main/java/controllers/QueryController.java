package controllers;

import dao.TimetableData;
import entities.Module;
import entities.Programme;
import entities.Room;
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
}
