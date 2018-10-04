package entities;

public class StudentInEvent
{
	private Student student;
	private Event event;
	private EventStatus status;
	
	public Student getStudent()
	{
		return student;
	}
	public Event getEvent()
	{
		return event;
	}
	public EventStatus getStatus()
	{
		return status;
	}
	
	public void setStudent(Student student)
	{
		this.student = student;
	}
	public void setEvent(Event event)
	{
		this.event = event;
	}
	public void setStatus(EventStatus status)
	{
		this.status = status;
	}
	
	public StudentInEvent()
	{
		this(EventStatus.WHITE);
	}
	public StudentInEvent(EventStatus status)
	{
		setStudent(new Student());
		setEvent(new Event());
		setStatus(status);
	}
}
