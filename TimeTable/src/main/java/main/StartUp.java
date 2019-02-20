package main;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import controllers.FacadeController;
import controllers.FileLoadController;
import controllers.QueryController;
import dao.DataReaderSetting;
import dao.ProgrammeCsvReader;
import dao.TimetableData;
import dao.TimetableExcelDataReader;
import entities.Event;
import entities.EventType;
import entities.Module;
import entities.Programme;
import entities.Room;
import entities.TTSlot;
import returnCodes.ResponseMove;
import entities.Staff;
import entities.StaffTimetable;
import entities.Student;
import entities.StudentTimetable;

@SuppressWarnings("unused")
public class StartUp
{
	public static void main(String[] args)
	{		
		// Actual file path will access the given file
		String liveFilePath = "H:\\docs\\TimeTables\\BigTimetableData.xlsx";
		
		// blank file path will access the the test mode with built in data set
		String testFilePath = "";
		
		FacadeController facade = FacadeController.getInstance(testFilePath);
		
//		List<Event> events = facade.getAllEvents();
//		for(Event event : events)
//		{
//			System.out.println(event.toString());
//		}
		
//		List<Student> students = facade.getAllStudents();
//		for(Student student : students)
//		{
//			System.out.println(student.toString());
//		}
		
//		Event event = facade.getEventById(4);
//		Student student = facade.getStudentByMatric("30");
//		
//		System.out.println(event.toString());
//		System.out.println();
//		System.out.println(student.toString());
//		System.out.println();
//		
//		ResponseMove result = facade.moveStudentToNewEvent(student, event);
//		System.out.println(result);
//		
//		event = facade.getEventById(4);
//		student = facade.getStudentByMatric("30");
//		
//		System.out.println(event.toString());
//		System.out.println();
//		System.out.println(student.toString());
//		System.out.println();
		
//		Event eventOne = facade.getEventById(1);
//		Student studentOne = facade.getStudentByMatric("1");
//		Event eventTwo = facade.getEventById(9);
//		Student studentTwo = facade.getStudentByMatric("11");
//		
//		ResponseMove swapResult = facade.swapStudentsBetweenEvents(studentOne, eventOne, studentTwo, eventTwo);
//		
//		System.out.println(swapResult);
		
		Staff staff = facade.getStaffByName("sybill");
		System.out.println(staff);
	}
}
