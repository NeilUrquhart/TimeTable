package main;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

import controllers.FacadeController;
import controllers.FileLoadController;
import controllers.QueryController;
import dao.DataReaderSetting;
import dao.ProgrammeCsvReader;
import dao.TimetableData;
import dao.TimetableExcelDataReader;
import ontology.elements.Event;
import ontology.elements.EventType;
import ontology.elements.Module;
import ontology.elements.Programme;
import ontology.elements.Room;
import ontology.elements.Staff;
import ontology.elements.StaffTimetable;
import ontology.elements.Student;
import ontology.elements.StudentInEvent;
import ontology.elements.StudentTimetable;
import ontology.elements.TTSlot;
import returnCodes.ResponseMove;

@SuppressWarnings("unused")
public class StartUp
{
	public static void main(String[] args)
	{		
		// Actual file path will access the given file
		String liveFilePath = "C:\\Users\\Michal Lange\\Documents\\TimeTable\\TimeTable\\src\\main\\resources\\rooms.xlxs";
		
		// blank file path will access the the test mode with built in data set
		String testFilePath = "";
		
		FacadeController facade = FacadeController.getInstance(""); //loads tt data
		
		Event event = facade.getEventById(1);
		Event event2 = facade.getEventById(5);
		Student student = facade.getStudentByMatric("30");
		for (StudentInEvent e : student.getEvents()) {
			//System.out.println(e.getEvent().getId());
		}
		
		ResponseMove result = facade.moveStudentToNewEvent(student, event2, event);
		
		//System.out.println(result);
		for (Event e : facade.getAllEvents()) {
			System.out.println(e.getId());
		}
		
		for (int i = 1; i < facade.getAllEvents().size() - 1; i++) {
			for (int j = i + 1; j < facade.getAllEvents().size(); j++) {
				if (facade.getEventById(i).getModule().getDescription().equals(facade.getEventById(j).getModule().getDescription())
						&& facade.getEventById(i).getType().equals(facade.getEventById(i).getType()))
					System.out.println("hello");
			}
		}
		
		//event = facade.getEventById(4);
		//student = facade.getStudentByMatric("30");
		//System.out.println(facade.getTimetableForStudent("30"));
		
		
		//System.out.println(event.toString());
		//System.out.println();
		//System.out.println(student.toString());
		//System.out.println();
		
		//Event eventOne = facade.getEventById(1);
		//Student studentOne = facade.getStudentByMatric("1");
		//Event eventTwo = facade.getEventById(9);
		//Student studentTwo = facade.getStudentByMatric("11");
		
		//System.out.println(facade.getTimetableForStudent("1"));
		//System.out.println(facade.getTimetableForStudent("11"));
		
		//ResponseMove swapResult = facade.swapStudentsBetweenEvents(studentOne, eventOne, studentTwo, eventTwo);
		
		//System.out.println(swapResult);
		//System.out.println(facade.getTimetableForStudent("1"));
		//System.out.println(facade.getTimetableForStudent("11"));
		
		
		//Staff staff = facade.getStaffByName("sybill");
		//Event staffEvent = facade.getEventById(1);
		//System.out.println(staff);
//		// Returns NO_TIME_AVAILABLE as they are already working in same slot
//		// In this case Event ID: 12 has the same slot as Event ID: 1
		//ResponseMove canStaffMove = facade.moveStaffToNewEvent(staff, staffEvent);
		//System.out.println(canStaffMove);
	}
}
