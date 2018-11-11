package main;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Map;

import controllers.FileLoadController;
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
		String filePath = "C:\\Users\\Neil\\Desktop\\Source Control\\TT-Data.xlsx";
		FileLoadController controller = new FileLoadController();
		controller.readTimetableData(filePath);
		
		for(Programme p : controller.getTimetableData().getProgrammes().values())
		{
			System.out.println(p);
		}
	}
}
