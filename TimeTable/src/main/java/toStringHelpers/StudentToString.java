package toStringHelpers;

import ontology.elements.Student;
import ontology.elements.StudentInEvent;

public class StudentToString implements ObjectToString
{
	private Student student;
	
	public StudentToString(Student student)
	{
		this.student = student;
	}
	
	public String stringify()
	{
		String result = String.format("\tStudent Details"
				+ "\nMatric:\t\t%1$s"
				+ "\n\n\tStudent In Events"
				+ "\n%2$s", 
				student.getMatric(), getEventsAsString());
		return result;
	}
	
	private String getEventsAsString()
	{
		String result = "";
		for(StudentInEvent e : student.getEvents())
		{
			result += String.format("Module:\t\t%1$s [%2$s]"
					+ "\nEvent Type:\t%3$s"
					+ "\nInstance:\t%4$s"
					+ "\nStatus:\t\t%5$s"
					+ "\n\n", 
					e.getEvent().getModule().getName(), e.getEvent().getModule().getDescription(),
					e.getEvent().getType(), e.getEvent().getInstance(),
					e.getStatus());
		}
		
		if(result == "")
		{
			result = "Student has no Events!";
		}
		return result;
	}
}
