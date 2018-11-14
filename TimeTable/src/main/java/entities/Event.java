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
	
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		result = prime * result + ((instance == null) ? 0 : instance.hashCode());
		result = prime * result + ((module == null) ? 0 : module.hashCode());
		result = prime * result + ((room == null) ? 0 : room.hashCode());
		result = prime * result + ((slots == null) ? 0 : slots.hashCode());
		result = prime * result + ((staff == null) ? 0 : staff.hashCode());
		result = prime * result + ((students == null) ? 0 : students.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Event other = (Event) obj;
		if (id != other.id)
			return false;
		if (instance == null)
		{
			if (other.instance != null)
				return false;
		} else if (!instance.equals(other.instance))
			return false;
		if (module == null)
		{
			if (other.module != null)
				return false;
		} else if (!module.equals(other.module))
			return false;
		if (room == null)
		{
			if (other.room != null)
				return false;
		} else if (!room.equals(other.room))
			return false;
		if (slots == null)
		{
			if (other.slots != null)
				return false;
		} else if (!slots.equals(other.slots))
			return false;
		if (staff == null)
		{
			if (other.staff != null)
				return false;
		} else if (!staff.equals(other.staff))
			return false;
		if (students == null)
		{
			if (other.students != null)
				return false;
		} else if (!students.equals(other.students))
			return false;
		if (type != other.type)
			return false;
		return true;
	}
}
