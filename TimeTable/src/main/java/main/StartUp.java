package main;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Map;

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
		TimetableExcelDataReader dataReader = new TimetableExcelDataReader(DataReaderSetting.TESTING);
		TimetableData data = dataReader.getData(filePath);
		ProgrammeCsvReader programmeReader = new ProgrammeCsvReader();
		Map<String, Programme> programmes = programmeReader.read();
		for(Programme p : programmes.values())
		{
			for(Module m : data.getModules().values())
			{
				for(Module otherM : p.getModules())
				{
					if(m.getName().contains(otherM.getName()))
					{
						otherM.setDescription(m.getDescription());
						otherM.setEvents(m.getEvents());
					}
				}
			}
		}
		
		data.setProgrammes(programmes);
	}
}
