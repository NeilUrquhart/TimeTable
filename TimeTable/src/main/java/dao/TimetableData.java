package dao;

import java.util.HashMap;
import java.util.Map;

import entities.Event;
import entities.Module;
import entities.Programme;
import entities.Room;
import entities.TTSlot;
import entities.Staff;
import entities.Student;

public class TimetableData
{
	private Map<String, Module> modules;
	private Map<Integer, Event> events;
	private Map<String, Room> rooms;
	private Map<String, Student> students;
	private Map<String, Staff> staff;
	private Map<Integer, TTSlot> slots;
	private Map<String, Programme> programmes;
	
	public Map<String, Module> getModules()
	{
		return modules;
	}
	public Map<Integer, Event> getEvents()
	{
		return events;
	}
	public Map<String, Room> getRooms()
	{
		return rooms;
	}
	public Map<String, Student> getStudents()
	{
		return students;
	}
	public Map<String, Staff> getStaff()
	{
		return staff;
	}
	public Map<Integer, TTSlot> getSlots()
	{
		return slots;
	}
	public Map<String, Programme> getProgrammes()
	{
		return programmes;
	}
	
	public void setModules(Map<String, Module> modules)
	{
		this.modules = modules;
	}
	public void setEvents(Map<Integer, Event> events)
	{
		this.events = events;
	}
	public void setRooms(Map<String, Room> rooms)
	{
		this.rooms = rooms;
	}
	public void setStudents(Map<String, Student> students)
	{
		this.students = students;
	}
	public void setStaff(Map<String, Staff> staff)
	{
		this.staff = staff;
	}
	public void setSlots(Map<Integer, TTSlot> slots)
	{
		this.slots = slots;
	}
	public void setProgrammes(Map<String, Programme> programmes)
	{
		this.programmes = programmes;
	}
	
	public TimetableData()
	{
		this(new HashMap<String, Module>(), new HashMap<Integer, Event>(), 
				new HashMap<String, Room>(), new HashMap<String, Student>(), 
				new HashMap<String, Staff>(), new HashMap<Integer, TTSlot>());
	}
	public TimetableData(Map<String, Module> modules, Map<Integer, Event> events, 
			Map<String, Room> rooms, Map<String, Student> students, Map<String, Staff> staff, 
			Map<Integer, TTSlot> slots)
	{
		this.modules = modules;
		this.events = events;
		this.rooms = rooms;
		this.students = students;
		this.staff = staff;
		this.slots = slots;
		programmes = new HashMap<String, Programme>();
	}
}
