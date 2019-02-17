package controllers;

import java.util.ArrayList;
import java.util.List;

import dao.TimetableData;
import entities.Event;
import entities.Staff;
import entities.StaffTimetable;
import entities.Student;
import entities.StudentInEvent;
import entities.StudentTimetable;
import entities.TTSlot;
import returnCodes.ResponseMove;
import returnCodes.StudentMoveCode;

public class FacadeController 
{
	private TimetableData data;
	private FileLoadController fileLoader;
	private QueryController queryController;
	private StudentMoveController studentMove;
	
	private static FacadeController facade = null;
	
	public static FacadeController getInstance(String filePath)
	{
		if(facade == null)
			facade = new FacadeController(filePath);
		return facade;
	}
	
	private FacadeController(String filePath)
	{
		data = new TimetableData();
		fileLoader = FileLoadController.getInstance(filePath);
		readTimetableData();
	}
	
	private void readTimetableData()
	{
		fileLoader.readTimetableData();
		data = fileLoader.getTimetableData();
	}
	
	public StudentTimetable getTimetableForStudent(String matric)
	{
		if(matric == null || matric == "")
			throw new RuntimeException("\nNo Matric Number Supplied"
					+ "\nClass: " + getClass().getSimpleName()
					+ "\nMethod: getTimetableForStudent");
		
		queryController = new QueryController(data);
		
		Student student = queryController.getStudentByMatric(matric);
		
		if(student == null)
			throw new RuntimeException("\nNo Student found with that Matric"
					+ "\nClass: " + getClass().getSimpleName()
					+ "\nMethod: getTimetableForStudent");
		
		// I DONT LIKE THIS BUT CANT GET IT ANY OTHER WAY
		// WITHOUT CREATING MULTIPLE LISTS CAUSE OF STUPID PASS BY REFERENCE
		// AND .CONTAINS ON A LIST NOT BEING NICE
		List<TTSlot> slots = new ArrayList<TTSlot>(data.getSlots().values());
		for(TTSlot slot : slots)
		{
			slot.setEvent(null);
			slot.setUsed(false);
			for(StudentInEvent event : student.getEvents())
			{
				for(TTSlot slotInEvent : event.getEvent().getSlots())
				{
					if(slotInEvent.getId() == slot.getId())
					{
						slot.setEvent(event.getEvent());
						slot.setUsed(true);
					}
				}
			}
		}
		
		return new StudentTimetable(student, slots);
	}
	
	public StaffTimetable getTimetableForStaff(String name)
	{
		if(name == null || name == "")
			throw new RuntimeException("\nNo Staff Name Supplied"
					+ "\nClass: " + getClass().getSimpleName()
					+ "\nMethod: getTimetableForStaff");
		
		queryController = new QueryController(data);
		
		Staff staff = queryController.getStaffByName(name);
		if(staff == null)
			throw new RuntimeException("\nNo Staff found with that Name"
					+ "\nClass: " + getClass().getSimpleName()
					+ "\nMethod: getTimetableForStaff");
		
		// SEE ABOVE METHOD
		List<TTSlot> slots = new ArrayList<TTSlot>(data.getSlots().values());
		for(TTSlot slot : slots)
		{
			slot.setEvent(null);
			slot.setUsed(false);
			
			for(Event event : staff.getEvents())
			{
				for(TTSlot slotInEvent : event.getSlots())
				{
					if(slot.getId() == slotInEvent.getId())
					{
						slot.setEvent(event);
						slot.setUsed(true);
					}
				}
			}
		}
		
		return new StaffTimetable(staff, slots);
	}
	
	public List<String> getStaffIDs(){
		//Return a list of all staff within the system
		queryController = new QueryController(data);
		return queryController.getStaffIDs();
	}
	
	public List<String> getStudentIDs(){
		//Return a list of all staff within the system
		queryController = new QueryController(data);
		return queryController.getStudentIDs();
	}
	
	public StudentMoveCode swapStudents(Student studentOne, Event eventOne, Student studentTwo, Event eventTwo)
	{
		StudentMoveCode result = StudentMoveCode.NO_MOVE;
		studentMove.setTimetableData(data);
		ResponseMove resultOne = studentMove.canStudentMoveToEvent(studentOne, eventTwo);
		ResponseMove resultTwo = studentMove.canStudentMoveToEvent(studentTwo, eventOne);
		if(resultOne == ResponseMove.OK && resultTwo == ResponseMove.OK)
		{
			result = studentMove.swapStudents(studentOne, eventOne, studentTwo, eventTwo);
		}
		
		if(resultOne != ResponseMove.OK)
		{
			result = StudentMoveCode.NO_MOVE;
		}
		if(resultTwo != ResponseMove.OK)
		{
			result = StudentMoveCode.NO_MOVE;
		}
		
		return result;
	}
}
