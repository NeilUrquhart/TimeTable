package entities;

import java.util.ArrayList;
import java.util.List;

import jade.content.Concept;
import toStringHelpers.ObjectToString;
import toStringHelpers.StaffToString;
import jade.content.onto.annotations.Slot;

public class Staff implements Concept
{
	private String name;
	private List<Event> events;
	private ObjectToString staffToString;
	
	@Slot(mandatory = true)
	public String getName()
	{
		return name;
	}
	
	public List<Event> getEvents()
	{
		return events;
	}
	
	public void setName(String name)
	{
		this.name = name;
	}
	public void setEvents(List<Event> events)
	{
		this.events = events;
	}
	
	public Staff()
	{
		this("");
	}
	public Staff(String name)
	{
		setName(name);
		setEvents(new ArrayList<Event>());
		staffToString = new StaffToString(this);
	}
	
	@Override
	public String toString()
	{
		return staffToString.stringify();
	}
	
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((events == null) ? 0 : events.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		Staff other = (Staff) obj;
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
		return true;
	}
}
