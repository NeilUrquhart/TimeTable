package entities;

import java.util.ArrayList;
import java.util.List;

import jade.content.Concept;
import toStringHelpers.ObjectToString;
import toStringHelpers.StudentTimetableToString;

public class StudentTimetable 
{
	private Student student;
	private List<TTSlot> slots;
	private ObjectToString objectToString;
	
	public Student getStudent()
	{
		return student;
	}
	public List<TTSlot> getSlots()
	{
		return slots;
	}
	
	public void setStudent(Student student)
	{
		this.student = student;
	}
	public void setSlots(List<TTSlot> slots)
	{
		this.slots = slots;
	}
	
	public StudentTimetable()
	{
		this(new Student(), new ArrayList<TTSlot>());
	}
	public StudentTimetable(Student student, List<TTSlot> slots)
	{
		setStudent(student);
		setSlots(slots);
		objectToString = new StudentTimetableToString(this);
	}
	
	@Override
	public String toString()
	{
		return objectToString.stringify();
	}
}
