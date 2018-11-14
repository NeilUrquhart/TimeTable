package main;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.List;
import java.util.Map;

import controllers.FileLoadController;
import controllers.QueryController;
import dao.DataReaderSetting;
import dao.ProgrammeCsvReader;
import dao.TimetableData;
import dao.TimetableExcelDataReader;
import entities.Event;
import entities.Module;
import entities.Programme;
import entities.Room;
import entities.Slot;
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
		Programme p = queryCont.getProgrammeByName("bsccomputing");
		Module m = queryCont.getModuleByName("csn07101");
		Room r = queryCont.getRoomByName("mer_a17");
		Staff s = queryCont.getStaffByName("sybill");
		List<Slot> slotsRoom = queryCont.getSlotsFreeForRoom("mer_a17");
		List<Slot> slotsStaff = queryCont.getSlotsFreeForStaff("sybill");
		for(Slot slot : slotsStaff)
		{
			System.out.println(slot.toString());
		}
	}
}
