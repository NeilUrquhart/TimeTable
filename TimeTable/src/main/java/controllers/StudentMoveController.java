package controllers;

import dao.TimetableData;
import ontology.elements.Event;
import ontology.elements.Student;
import ontology.elements.StudentInEvent;
import ontology.elements.TTSlot;
import returnCodes.ResponseMove;
import returnCodes.StudentMoveCode;

public class StudentMoveController 
{	
	private TimetableData data;
	
	public void setTimetableData(TimetableData data)
	{
		this.data = data;
	}
	
	public TimetableData getTimetableData()
	{
		return data;
	}
	
	public StudentMoveController()
	{
		data = new TimetableData();
	}
	
	public ResponseMove canStudentMoveToEvent(Student student, Event event, boolean swapEvent)
	{
		if(!canStudentMove(student, event))
			return ResponseMove.NO_ROOM_AVAILABLE;
		
		int increaseCapacity = 0;
		if (swapEvent)
			increaseCapacity = 1;
		
		if(event.getStudents().size() < event.getRoom().getCapacity() + increaseCapacity)
			return ResponseMove.OK;
		
		return ResponseMove.NO_ROOM_AVAILABLE;
	}
	
	public StudentMoveCode swapStudents(Student studentOne, Event eventOne, Student studentTwo, Event eventTwo) 
	{
		// If students are already in the other Event can't move into it
		if(isStudentAlreadyInEvent(studentOne, eventTwo))
		{
			return StudentMoveCode.NO_MOVE;
		}
		if(isStudentAlreadyInEvent(studentTwo, eventOne))
		{
			return StudentMoveCode.NO_MOVE;
		}
		
		// Add event to student
		studentOne = addNewEventToStudent(studentOne, eventTwo);
		studentTwo = addNewEventToStudent(studentTwo, eventOne);
		
		// Add student to event
		eventOne = addNewStudentToEvent(eventOne, studentTwo);
		eventTwo = addNewStudentToEvent(eventTwo, studentOne);
		
		// Remove old event from students
		studentOne = removeEventFromStudent(studentOne, eventOne);
		studentTwo = removeEventFromStudent(studentTwo, eventTwo);
		
		// Remove old student from the events
		eventOne = removeStudentFromEvent(eventOne, studentOne);
		eventTwo = removeStudentFromEvent(eventTwo, studentTwo);
		
		// Update student timetable data
		updateStudentTimetableData(studentOne);
		updateStudentTimetableData(studentTwo);
		
		// Update event timetable data
		updateEventTimetableData(eventOne);
		updateEventTimetableData(eventTwo);
		
		return StudentMoveCode.OK;
	}
	
	public void moveStudentToEvent(Student student, Event event, Event oldEvent)
	{
		student = addNewEventToStudent(student, event);
		event = addNewStudentToEvent(event, student);
		//Remove old event?
		//Remove student from old event
		// Remove old event from students
		removeEventFromStudent(student, oldEvent);
		
		// Remove old student from the events
		removeStudentFromEvent(oldEvent, student);
		//
		updateStudentTimetableData(student);
		updateEventTimetableData(event);
	}
	
	// Check to make sure there is no slot collisions with the new events
	private boolean canStudentMove(Student student, Event event)
	{
		for(StudentInEvent temp : student.getEvents())
		{
			for(TTSlot slotOne : temp.getEvent().getSlots())
			{
				for(TTSlot slotTwo : event.getSlots())
				{
					if(slotTwo.getId() == slotOne.getId())
					{
						return false;
					}
				}
			}
		}
		
		return true;
	}
	
	private boolean isStudentAlreadyInEvent(Student student, Event event)
	{
		for(StudentInEvent temp : student.getEvents())
		{
			if(temp.getEvent().getId() == event.getId())
			{
				return true;
			}
		}
		return false;
	}
	
	private Student addNewEventToStudent(Student student, Event event)
	{
		student.getEvents().add(moveStudent(student, event));
		return student;
	}
	
	private Event addNewStudentToEvent(Event event, Student student)
	{
		event.getStudents().add(moveStudent(student, event));
		return event;
	}
	
	private StudentInEvent moveStudent(Student student, Event event)
	{
		StudentInEvent result = new StudentInEvent();
		result.setEvent(event);
		result.setStudent(student);
		return result;
	}
	
	private Student removeEventFromStudent(Student student, Event event)
	{
		int index = 0;
		for(StudentInEvent temp : student.getEvents())
		{
			if(temp.getEvent().getId() == event.getId())
			{
				index = student.getEvents().indexOf(temp);
			}
		}
		student.getEvents().remove(index);
		return student;
	}
	
	private Event removeStudentFromEvent(Event event, Student student)
	{
		int index = 0;
		for(StudentInEvent temp : event.getStudents())
		{
			if(temp.getStudent().getMatric() == student.getMatric())
			{
				index = event.getStudents().indexOf(temp);
			}
		}
		event.getStudents().remove(index);
		return event;
	}
	
	private void updateStudentTimetableData(Student student)
	{
		for(Student temp : data.getStudents().values())
		{
			if(temp.getMatric() == student.getMatric())
			{
				temp = student;
			}
		}
	}
	
	private void updateEventTimetableData(Event event)
	{
		for(Event temp : data.getEvents().values())
		{
			if(temp.getId() == event.getId())
			{
				temp = event;
			}
		}
	}
}
