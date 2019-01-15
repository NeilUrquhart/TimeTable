package controllers;

import entities.Event;
import entities.Student;
import entities.StudentInEvent;
import entities.TTSlot;
import returnCodes.ResponseMove;
import returnCodes.StudentMoveCode;

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
	
	public StudentMoveCode swapStudents(Student studentOne, Event eventOne, Student studentTwo, Event eventTwo) 
	{
		// If students are already in the other Event can't move into it
		for(StudentInEvent temp : studentOne.getEvents()) 
		{
			if(temp.getEvent().getId() == eventTwo.getId())
			{
				return StudentMoveCode.NO_MOVE;
			}
		}
		
		for(StudentInEvent temp : studentTwo.getEvents())
		{
			if(temp.getEvent().getId() == eventOne.getId())
			{
				return StudentMoveCode.NO_MOVE;
			}
		}
		
		return StudentMoveCode.OK;
	}
}
