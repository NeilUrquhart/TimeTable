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
	
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((event == null) ? 0 : event.hashCode());
		result = prime * result + ((status == null) ? 0 : status.hashCode());
		result = prime * result + ((student == null) ? 0 : student.hashCode());
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
		StudentInEvent other = (StudentInEvent) obj;
		if (event == null)
		{
			if (other.event != null)
				return false;
		} else if (!event.equals(other.event))
			return false;
		if (status != other.status)
			return false;
		if (student == null)
		{
			if (other.student != null)
				return false;
		} else if (!student.equals(other.student))
			return false;
		return true;
	}
}
