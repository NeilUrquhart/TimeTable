package entities;

import java.util.ArrayList;
import java.util.List;

import toStringHelpers.ObjectToString;
import toStringHelpers.StudentToString;

public class Student
{
	private String matric;
	private List<StudentInEvent> events;
	private ObjectToString studentToString;
	
	public String getMatric()
	{
		return matric;
	}
	public List<StudentInEvent> getEvents()
	{
		return events;
	}
	
	public void setMatric(String matric)
	{
		this.matric = matric;
	}
	public void setEvents(List<StudentInEvent> events)
	{
		this.events = events;
	}
	
	public Student()
	{
		this("");
	}
	public Student(String matric)
	{
		setMatric(matric);
		setEvents(new ArrayList<StudentInEvent>());
		studentToString = new StudentToString(this);
	}
	
	@Override
	public String toString()
	{
		return studentToString.stringify();
	}
}
