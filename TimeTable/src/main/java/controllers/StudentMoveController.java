package controllers;

import entities.Event;
import entities.Student;
import entities.StudentInEvent;
import returnCodes.ResponseMove;

public class StudentMoveController 
{	
	public StudentMoveController()
	{
	}
	
	public ResponseMove canStudentMoveToEvent(Student student, Event event)
	{
		for(StudentInEvent temp : student.getEvents())
		{
			if(temp.getEvent().getId() == event.getId())
			{
				return ResponseMove.OK;
			}
		}
		
		if(event.getStudents().size() < event.getRoom().getCapacity())
			return ResponseMove.OK;
		
		return ResponseMove.NO_ROOM_AVAILABLE;
	}
}
