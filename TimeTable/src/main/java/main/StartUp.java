package main;
/*
 * This is a test comment.
 * 
 */

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
		
		Student s1 = facade.getStudentByMatric("30");
		Student s2 = facade.getStudentByMatric("31");
		
		Event e1 = facade.getEventById(16);
		Event e2 = facade.getEventById(17);
		
		for (TTSlot slot : e1.getSlots()) {
			System.out.println(slot.getId());
		}
		
		for (StudentInEvent e : s1.getEvents()) {
			System.out.println(e.getEvent().getModule());
		}
		
		for (StudentInEvent e : s2.getEvents()) {
			System.out.println(e.getEvent().getModule());
		}
	}
}
