package entities;

import java.util.ArrayList;
import java.util.List;

import toStringHelpers.ObjectToString;
import toStringHelpers.RoomToString;

public class Room
{
	private String name;
	private int capacity;
	private List<Event> events;
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
	
	public Room()
	{
		this("", 0);
	}
	public Room(String name, int capacity)
	{
		setName(name);
		setCapacity(capacity);
		setEvents(new ArrayList<Event>());
		roomToString = new RoomToString(this);
	}
	
	@Override
	public String toString()
	{
		return roomToString.stringify();
	}
}
