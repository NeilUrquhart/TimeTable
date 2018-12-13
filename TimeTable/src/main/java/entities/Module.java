package entities;

import java.util.ArrayList;
import java.util.List;

import jade.content.Concept;
import jade.content.onto.annotations.AggregateSlot;
import jade.content.onto.annotations.Slot;
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
	
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((description == null) ? 0 : description.hashCode());
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
		Module other = (Module) obj;
		if (description == null)
		{
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
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
		return true;
	}
}
