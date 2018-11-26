package entities;

import toStringHelpers.ObjectToString;
import toStringHelpers.SlotToString;

public class TTSlot
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
	
	public TTSlot()
	{
		this(0, DayOfWeek.DEFAULT, "", "", false);
	}
	public TTSlot(int id, DayOfWeek day, String startTime, String endTime, boolean isUsed)
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
	
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((day == null) ? 0 : day.hashCode());
		result = prime * result + ((endTime == null) ? 0 : endTime.hashCode());
		result = prime * result + ((event == null) ? 0 : event.hashCode());
		result = prime * result + id;
		result = prime * result + (isUsed ? 1231 : 1237);
		result = prime * result + ((startTime == null) ? 0 : startTime.hashCode());
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
		TTSlot other = (TTSlot) obj;
		if (day != other.day)
			return false;
		if (endTime == null)
		{
			if (other.endTime != null)
				return false;
		} else if (!endTime.equals(other.endTime))
			return false;
		if (event == null)
		{
			if (other.event != null)
				return false;
		} else if (!event.equals(other.event))
			return false;
		if (id != other.id)
			return false;
		if (isUsed != other.isUsed)
			return false;
		if (startTime == null)
		{
			if (other.startTime != null)
				return false;
		} else if (!startTime.equals(other.startTime))
			return false;
		return true;
	}
}
