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
			throw new RuntimeException("No Matric Number Supplied"
					+ "\nClass: " + getClass().getSimpleName()
					+ "\nMethod: getTimetableForStudent");
		
		queryController = new QueryController(data);
		
		Student student = queryController.getStudentByMatric(matric);
		
		if(student == null)
			throw new RuntimeException("No Student found with that Matric"
					+ "\nClass: " + getClass().getSimpleName()
					+ "\nMethod: getTimetableForStudent");
		
		List<TTSlot> slots = new ArrayList<TTSlot>();
		for(StudentInEvent event : student.getEvents())
		{
			for(TTSlot slot : event.getEvent().getSlots())
			{
				slots.add(slot);
			}
		}
		
		for(TTSlot slot : data.getSlots().values())
		{
			if(!slots.contains(slot))
			{
				slots.add(slot);
			}
		}
		
		return new StudentTimetable(student, slots);
	}
	
	public StaffTimetable getTimetableForStaff(String name)
	{
		if(name == null || name == "")
			throw new RuntimeException("No Staff Name Supplied"
					+ "\nClass: " + getClass().getSimpleName()
					+ "\nMethod: getTimetableForStaff");
		
		queryController = new QueryController(data);
		
		Staff staff = queryController.getStaffByName(name);
		if(staff == null)
			throw new RuntimeException("No Staff found with that Name"
					+ "\nClass: " + getClass().getSimpleName()
					+ "\nMethod: getTimetableForStaff");
		
		List<TTSlot> slots = new ArrayList<TTSlot>();
		for(Event event : staff.getEvents())
		{
			for(TTSlot slot : event.getSlots())
			{
				slots.add(slot);
			}
		}
		
		for(TTSlot slot : data.getSlots().values())
		{
			if(!slots.contains(slot))
			{
				slots.add(slot);
			}
		}
		
		return new StaffTimetable(staff, slots);
	}
}
