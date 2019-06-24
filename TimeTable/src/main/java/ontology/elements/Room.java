package ontology.elements;

import java.util.ArrayList;
import java.util.List;

import toStringHelpers.ObjectToString;
import toStringHelpers.RoomToString;
import jade.content.onto.annotations.Slot;

public class Room 
{
	private String name;
	private int capacity;
	private List<Event> events;
	private List<TTSlot> slots;
	private ObjectToString roomToString;
	

	public String getName()
	{
		return name;
	}
	

	public int getCapacity()
	{
		return capacity;
	}
	public List<Event> getEvents()
	{
		return events;
	}
	public List<TTSlot> getSlots()
	{
		return slots;
	}
	
	public void setName(String name)
	{
		this.name = name;
	}
	public void setCapacity(int capacity)
	{
		this.capacity = capacity;
	}
	public void setEvents(List<Event> events)
	{
		this.events = events;
	}
	public void setSlots(List<TTSlot> slots)
	{
		this.slots = slots;
	}
	
	public Room()
	{
		this("", 0);
	}
	public Room(String name, int capacity)
	{
		setName(name);
		setCapacity(capacity);
		setEvents(new ArrayList<Event>());
		setSlots(new ArrayList<TTSlot>());
		roomToString = new RoomToString(this);
	}
	
	@Override
	public String toString()
	{
		return roomToString.stringify();
	}
	
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + capacity;
		result = prime * result + ((events == null) ? 0 : events.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((slots == null) ? 0 : slots.hashCode());
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
		Room other = (Room) obj;
		if (capacity != other.capacity)
			return false;
		if (events == null)
		{
			if (other.events != null)
				return false;
		} else if (!events.equals(other.events))
			return false;
		if (name == null)
		{
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (slots == null)
		{
			if (other.slots != null)
				return false;
		} else if (!slots.equals(other.slots))
			return false;
		return true;
	}
}
