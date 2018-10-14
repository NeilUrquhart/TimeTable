package entities;

import toStringHelpers.ObjectToString;
import toStringHelpers.SlotToString;

public class Slot
{
	private int id;
	private DayOfWeek day;
	private String startTime;
	private String endTime;
	private boolean isUsed;
	private Event event;
	private ObjectToString slotToString;
	
	public int getId()
	{
		return id;
	}
	public DayOfWeek getDay()
	{
		return day;
	}
	public String getStartTime()
	{
		return startTime;
	}
	public String getEndTime()
	{
		return endTime;
	}
	public boolean isUsed()
	{
		return isUsed;
	}
	public Event getEvent()
	{
		return event;
	}
	
	public void setId(int id)
	{
		this.id = id;
	}
	public void setDay(DayOfWeek day)
	{
		this.day = day;
	}
	public void setStartTime(String startTime)
	{
		this.startTime = startTime;
	}
	public void setEndTime(String endTime)
	{
		this.endTime = endTime;
	}
	public void setUsed(boolean isUsed)
	{
		this.isUsed = isUsed;
	}
	public void setEvent(Event event)
	{
		this.event = event;
	}
	
	public Slot()
	{
		this(0, DayOfWeek.DEFAULT, "", "", false);
	}
	public Slot(int id, DayOfWeek day, String startTime, String endTime, boolean isUsed)
	{
		setId(id);
		setDay(day);
		setStartTime(startTime);
		setEndTime(endTime);
		setUsed(isUsed);
		setEvent(new Event());
		slotToString = new SlotToString(this);
	}
	
	@Override
	public String toString()
	{
		return slotToString.stringify();
	}
}
