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
	private static FileLoadController fileLoadControllerInstance = null;
	private String filePath;
	
	public TimetableData getTimetableData()
	{
		return timetableData;
	}
	
	private FileLoadController(String filePath)
	{
		timetableData = new TimetableData();
		this.filePath = filePath;
	}
	
	public static FileLoadController getInstance(String filePath)
	{
		if(fileLoadControllerInstance == null)
			fileLoadControllerInstance = new FileLoadController(filePath);
		return fileLoadControllerInstance;
	}
	
	public void readTimetableData()
	{
		if(this.filePath.equals(""))
		{
			testMode();
		}
		else 
		{
			liveMode();
		}
	}
	
	public void testMode()
	{
		TimetableExcelDataReader dataReader = new TimetableExcelDataReader(DataReaderSetting.TESTING);
		timetableData = dataReader.getData("");
		if(timetableData == null)
		{
			throw new RuntimeException("\nError Reading Data"
					+ "\nClass: " + getClass().getSimpleName() 
					+ "\nMethod: readTimetableData");
		}
		
		getProgrammeData();
	}
	
	private void liveMode()
	{
		TimetableExcelDataReader dataReader = new TimetableExcelDataReader(DataReaderSetting.LIVE);
		timetableData = dataReader.getData(filePath);
		if(timetableData == null)
		{
			throw new RuntimeException("\nError Reading Data"
					+ "\nClass: " + getClass().getSimpleName() 
					+ "\nMethod: readTimetableData");
		}
		
		getProgrammeData();
	}
	
	private void getProgrammeData()
	{
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
						otherM.setName(m.getName());
						otherM.setDescription(m.getDescription());
						otherM.setEvents(m.getEvents());
					}
				}
			}
		}
		
		timetableData.setProgrammes(programmes);
	}
}
