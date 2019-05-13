package dao.parsers;

import ontology.elements.DayOfWeek;

public class DayOfWeekParser
{
	public DayOfWeekParser()
	{
		
	}
	
	public DayOfWeek getDayOfTheWeek(String day)
	{
		if(day.contains("Monday"))
			return DayOfWeek.MONDAY;
		else if(day.contains("Tuesday"))
			return DayOfWeek.TUESDAY;
		else if(day.contains("Wednesday"))
			return DayOfWeek.WEDNESDAY;
		else if(day.contains("Thursday"))
			return DayOfWeek.THURSDAY;
		else if(day.contains("Friday"))
			return DayOfWeek.FRIDAY;
		else
			return DayOfWeek.DEFAULT;
	}
}
