package ontology.elements;

import java.util.ArrayList;
import java.util.List;

import toStringHelpers.ObjectToString;
import toStringHelpers.StudentToString;

public class Student 
{
	private String matric;
	private List<StudentInEvent> events;
	private ObjectToString studentToString;
	private Personality personality;
	

	public String getMatric()
	{
		return matric;
	}
	
	public List<StudentInEvent> getEvents()
	{
		return events;
	}
	
	public Personality getPersonality()
	{
		return personality;
	}
	
	public void setMatric(String matric)
	{
		this.matric = matric;
	}
	
	public void setEvents(List<StudentInEvent> events)
	{
		this.events = events;
	}
	
	public void setPersonality(Personality personality)
	{
		this.personality = personality;
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
	
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((events == null) ? 0 : events.hashCode());
		result = prime * result + ((matric == null) ? 0 : matric.hashCode());
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
		Student other = (Student) obj;
		if (events == null)
		{
			if (other.events != null)
				return false;
		} else if (!events.equals(other.events))
			return false;
		if (matric == null)
		{
			if (other.matric != null)
				return false;
		} else if (!matric.equals(other.matric))
			return false;
		return true;
	}
}
