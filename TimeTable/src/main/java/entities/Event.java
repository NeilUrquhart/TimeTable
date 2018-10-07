package entities;

import java.util.ArrayList;
import java.util.List;

import toStringHelpers.EventToString;
import toStringHelpers.ObjectToString;

public class Event
{
	private int id;
	private EventType type;
	private String instance;
	private Module module;
	private Room room;
	private List<Staff> staff;
	private List<StudentInEvent> students;
	private List<Slot> slots;
	private ObjectToString eventToString;
	
	public int getId()
	{
		return id;
	}
	public EventType getType()
	{
		return type;
	}
	public String getInstance()
	{
		return instance;
	}
	public Module getModule()
	{
		return module;
	}
	public Room getRoom()
	{
		return room;
	}
	public List<Staff> getStaff()
	{
		return staff;
	}
	public List<StudentInEvent> getStudents()
	{
		return students;
	}
	public List<Slot> getSlots()
	{
		return slots;
	}
	
	public void setId(int id)
	{
		this.id = id;
	}
	public void setType(EventType type)
	{
		this.type = type;
	}
	public void setInstance(String instance)
	{
		this.instance = instance;
	}
	public void setModule(Module module)
	{
		this.module = module;
	}
	public void setRoom(Room room)
	{
		this.room = room;
	}
	public void setStaff(List<Staff> staff)
	{
		this.staff = staff;
	}
	public void setStudents(List<StudentInEvent> students)
	{
		this.students = students;
	}
	public void setSlots(List<Slot> slots)
	{
		this.slots = slots;
	}
	
	public Event()
	{
		this(0, EventType.DEFAULT, "");
	}
	public Event(int id, EventType type, String instance)
	{
		setId(id);
		setType(type);
		setInstance(instance);
		setModule(new Module());
		setRoom(new Room());
		setStaff(new ArrayList<Staff>());
		setStudents(new ArrayList<StudentInEvent>());
		setSlots(new ArrayList<Slot>());
		eventToString = new EventToString(this);
	}
	
	@Override
	public String toString()
	{
		return eventToString.stringify();
	}
}
