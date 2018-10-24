package main;

import dao.DataReaderSetting;
import dao.TimetableData;
import dao.TimetableExcelDataReader;
import entities.Event;
import entities.Module;
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
		TimetableExcelDataReader dataReader = new TimetableExcelDataReader(DataReaderSetting.TESTING);
		TimetableData data = dataReader.getData(filePath);
	}
}
