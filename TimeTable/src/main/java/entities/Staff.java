package entities;

import java.util.ArrayList;
import java.util.List;

import toStringHelpers.ObjectToString;
import toStringHelpers.StaffToString;

public class Staff
{
	private String name;
	private List<Event> events;
	private ObjectToString staffToString;
	
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
}
