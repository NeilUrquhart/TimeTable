package controllers;

import java.util.Map;

import dao.DataReaderSetting;
import dao.ProgrammeCsvReader;
import dao.TimetableData;
import dao.TimetableExcelDataReader;
import entities.Module;
import entities.Programme;

public class FileLoadController
{
	private TimetableData timetableData;
	
	public TimetableData getTimetableData()
	{
		return timetableData;
	}
	
	public FileLoadController()
	{
		timetableData = new TimetableData();
	}
	
	public void readTimetableData(String filePath)
	{
		if(filePath.equals(""))
		{
			throw new RuntimeException("\nFile Path not supplied"
					+ "\nClass: " + getClass().getSimpleName() 
					+ "\nMethod: readTimetableData");
		}
		
		TimetableExcelDataReader dataReader = new TimetableExcelDataReader(DataReaderSetting.LIVE);
		timetableData = dataReader.getData(filePath);
		if(timetableData == null)
		{
			throw new RuntimeException("\nError Reading Data"
					+ "\nClass: " + getClass().getSimpleName() 
					+ "\nMethod: readTimetableData");
		}
		
		ProgrammeCsvReader progReader = new ProgrammeCsvReader();
		Map<String, Programme> programmes = progReader.read();
		for(Programme p : programmes.values())
		{
			for(Module m : timetableData.getModules().values())
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
		
		timetableData.setProgrammes(programmes);
	}
}
