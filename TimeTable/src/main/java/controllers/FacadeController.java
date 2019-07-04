package controllers;

import java.util.ArrayList;
import java.util.List;

import dao.TimetableData;
import ontology.elements.Event;
import ontology.elements.Staff;
import ontology.elements.StaffTimetable;
import ontology.elements.Student;
import ontology.elements.StudentInEvent;
import ontology.elements.StudentTimetable;
import ontology.elements.TTSlot;
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

	public List<Event> getAllEvents()
	{
		queryController = new QueryController(data);
		return queryController.getAllEvents();
	}

	public List<Student> getAllStudents()
	{
		queryController = new QueryController(data);
		return queryController.getAllStudents();
	}

	public Event getEventById(int id)
	{
		queryController = new QueryController(data);
		return queryController.getEventById(id);
	}

	public Student getStudentByMatric(String matric)
	{
		queryController = new QueryController(data);
		return queryController.getStudentByMatric(matric);
	}

	public Staff getStaffByName(String name)
	{
		queryController = new QueryController(data);
		return queryController.getStaffByName(name);
	}

	public StudentMoveCode swapStudents(Student studentOne, Event eventOne, Student studentTwo, Event eventTwo)
	{
		StudentMoveCode result = StudentMoveCode.NO_MOVE;
		studentMove.setTimetableData(data);
		ResponseMove resultOne = studentMove.canStudentMoveToEvent(studentOne, eventTwo, eventOne, true);
		ResponseMove resultTwo = studentMove.canStudentMoveToEvent(studentTwo, eventOne, eventTwo, true);
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

	public ResponseMove moveStudentToNewEvent(Student student, Event event, Event oldEvent)
	{

		createNewStudentMoveController();
		ResponseMove canStudentMove = studentMove.canStudentMoveToEvent(student, event, oldEvent, false);
		if(canStudentMove != ResponseMove.OK)
		{
			return canStudentMove;
		}
		studentMove.moveStudentToEvent(student, event, oldEvent);



		data = studentMove.getTimetableData();
		return ResponseMove.OK;
	}

	public ResponseMove swapStudentsBetweenEvents(Student studentOne, Event eventOne, Student studentTwo, Event eventTwo)
	{
		createNewStudentMoveController();
		ResponseMove studentOneResult = studentMove.canStudentMoveToEvent(studentOne, eventTwo, eventOne, true);
		ResponseMove studentTwoResult = studentMove.canStudentMoveToEvent(studentTwo, eventOne, eventTwo, true);
		
		if(studentOneResult == ResponseMove.NOT_IN_OLD_EVENT || studentTwoResult == ResponseMove.NOT_IN_OLD_EVENT) {
			return ResponseMove.NOT_IN_OLD_EVENT;
		}
		else if(studentOneResult != ResponseMove.OK && studentTwoResult != ResponseMove.OK)
		{
			return ResponseMove.NO_ROOM_AVAILABLE;
		}

		StudentMoveCode swapResult = studentMove.swapStudents(studentOne, eventOne, studentTwo, eventTwo);
		if(swapResult != StudentMoveCode.OK)
		{
			throw new RuntimeException("Students Unable to swap: " + swapResult);
		}

		data = studentMove.getTimetableData();
		return ResponseMove.OK;
	}

	public ResponseMove moveStaffToNewEvent(Staff staff, Event event)
	{
		StaffMoveController staffMove = new StaffMoveController(data);
		ResponseMove canStaffMove = staffMove.canStaffMoveEvent(staff, event);
		if(canStaffMove != ResponseMove.OK)
		{
			return canStaffMove;
		}

		staffMove.moveStaffToEvent(staff, event);
		data = staffMove.getTimetableData();
		return ResponseMove.OK;
	}


	private void createNewStudentMoveController()
	{
		studentMove = new StudentMoveController();
		studentMove.setTimetableData(data);
	}

	public ArrayList<Event> getCompatibleEvents(Event event, List<StudentInEvent> studentEvents) {
		queryController = new QueryController(data);
		ArrayList<Event> compatibleEvents = new ArrayList<>();
		for (Event e : queryController.getAllEvents()) {
			if  (event.getModule().getDescription().equals(e.getModule().getDescription()) && event.getType() == e.getType() &&
					event.getId() != e.getId()) {
				// Now check if the student already doesn't have that event
				boolean addEvent = true;
				for (StudentInEvent sie : studentEvents) {
					if (sie.getEvent().getId() == e.getId())
						addEvent = false;

				}
				if (addEvent)
					compatibleEvents.add(e);

			}
		}
		return compatibleEvents;
	}

	public void pruneEvents(ArrayList<Event> events, int slotID) {
		ArrayList<Integer> elementsToRemove = new ArrayList<Integer>();
		for (int i = 0; i < events.size(); i++)
			for (TTSlot sl : events.get(i).getSlots()) {
				if (sl.getId() == slotID) {
					elementsToRemove.add(i);
				}
			}
		for (Integer index : elementsToRemove) {
			events.remove((int) index);
		}
	}
}

