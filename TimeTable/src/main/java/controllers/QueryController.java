package controllers;

import java.util.ArrayList;
import java.util.List;

import dao.TimetableData;
import entities.Event;
import entities.EventType;
import entities.Module;
import entities.Programme;
import entities.Room;
import entities.TTSlot;
import entities.Staff;
import entities.Student;

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
	
	public TTSlot getSlotById(int id)
	{
		TTSlot result = null;
		for(TTSlot slot : data.getSlots().values())
		{
			if(slot.getId() == id)
			{
				result = slot;
				break;
			}
		}
		return result;
	}
	
	public Student getStudentByMatric(String matric)
	{
		Student result = null;
		for(Student student : data.getStudents().values())
		{
			if(student.getMatric().toUpperCase().equals(matric.toUpperCase()))
			{
				result = student;
			}
		}
		return result;
	}
	
	public Event getEventById(int id)
	{
		Event result = null;
		for(Event event : data.getEvents().values())
		{
			if(event.getId() == id)
			{
				result = event;
			}
		}
		return result;
	}
	
	public List<Event> getEventByType(EventType type)
	{
		List<Event> result = new ArrayList<Event>();
		for(Event event : data.getEvents().values())
		{
			if(event.getType() == type)
			{
				result.add(event);
			}
		}
		return result;
	}
}
