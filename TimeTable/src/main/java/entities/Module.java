package entities;

import java.util.ArrayList;
import java.util.List;

import toStringHelpers.ModuleToString;
import toStringHelpers.ObjectToString;

public class Module
{
	private String name;
	private String description;
	private List<Event> events;
	private ObjectToString moduleToString;
	
	public String getName()
	{
		return name;
	}
	public String getDescription()
	{
		return description;
	}
	public List<Event> getEvents()
	{
		return events;
	}
	
	public void setName(String name)
	{
		this.name = name;
	}
	public void setDescription(String description)
	{
		this.description = description;
	}
	public void setEvents(List<Event> events)
	{
		this.events = events;
	}
	
	public Module()
	{
		this("", "");
	}
	public Module(String name, String description)
	{
		setName(name);
		setDescription(description);
		setEvents(new ArrayList<Event>());
		moduleToString = new ModuleToString(this);
	}
	
	@Override
	public String toString()
	{
		return moduleToString.stringify();
	}
}
