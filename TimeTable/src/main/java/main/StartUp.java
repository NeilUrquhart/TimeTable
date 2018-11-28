package main;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import controllers.FileLoadController;
import controllers.MoveController;
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
import entities.Staff;
import entities.Student;

@SuppressWarnings("unused")
public class StartUp
{
	public static void main(String[] args)
	{		
		String filePath = "H:\\Neil Urquhart\\TT-Data.xlsx";
		FileLoadController controller = FileLoadController.getInstance(filePath);
		controller.readTimetableData();
		QueryController queryCont = new QueryController(controller.getTimetableData());
		MoveController moveController = new MoveController(controller.getTimetableData());
		Programme p = queryCont.getProgrammeByName("bsccomputing");
		Module m = queryCont.getModuleByName("csn07101");
		Room r = queryCont.getRoomByName("mer_a17");
		Staff s = queryCont.getStaffByName("sybill");
		Event e = queryCont.getEventById(1);
		List<Event> events = queryCont.getEventByType(EventType.LECTURE);
		
		for(Event event : events)
		{
			System.out.println(event);
			System.out.println();
		}
	}
}
