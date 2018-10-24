package dao;

import java.io.File;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import dao.parsers.DayOfWeekParser;
import dao.parsers.TimeParser;
import entities.DayOfWeek;
import entities.Slot;

public class SlotExcelDataReader
{
	private final String FILE_PATH = "Slots.xlsx";
	
	public SlotExcelDataReader()
	{
		
	}
	
	public Map<Integer, Slot> readAll()
	{
		XSSFWorkbook workbook = openWorkbook(getFile());
		if(workbook == null)
		{
			return Collections.emptyMap();
		}
		
		XSSFSheet sheet = getSheetFromWorkbook(workbook);
		if(sheet == null)
		{
			return Collections.emptyMap();
		}
		closeWorkbook(workbook);
		return getAll(sheet);
	}
	
	private File getFile()
	{
		ClassLoader classLoader = getClass().getClassLoader();
		try
		{
			return new File(classLoader.getResource(FILE_PATH).getFile());
		}
		catch(Exception e)
		{
			throw new RuntimeException("\nNo file exists."
					+ "\nClass: " + getClass().getName()
					+ "\nMethod: getFile"
					+ "\nException: " + e.getMessage());
		}
	}
	
	private XSSFWorkbook openWorkbook(File file)
	{
		try
		{
			return new XSSFWorkbook(file);
		}
		catch(Exception e)
		{
			throw new RuntimeException("\nCannot read file."
					+ "\nClass: " + getClass().getName()
					+ "\nMethod: openWorkbook"
					+ "\nException: " + e.getMessage());
		}
	}
	
	private void closeWorkbook(XSSFWorkbook workbook)
	{
		try
		{
			workbook.close();
		}
		catch(Exception e)
		{
			throw new RuntimeException("\nCannot Close Workbook"
					+ "\nClass: " + getClass().getName()
					+ "\nMethod: closeWorkbook"
					+ "\nException: " + e.getMessage());
		}
	}
	
	private XSSFSheet getSheetFromWorkbook(XSSFWorkbook workbook)
	{
		return workbook.getSheetAt(0);
	}
	
	private Map<Integer, Slot> getAll(XSSFSheet sheet)
	{
		Map<Integer, Slot> result = new HashMap<Integer, Slot>();
		Iterator<Row> rowIt = sheet.iterator();
		while(rowIt.hasNext())
		{
			Row row = rowIt.next();
			Slot slot = new Slot();
			slot.setId(getId(row.getCell(0).toString()));
			slot.setDay(getDayOfWeek(row.getCell(1).toString()));
			slot.setStartTime(getTime(row.getCell(2)));
			slot.setEndTime(getTime(row.getCell(3)));
			slot.setUsed(false);
			if(!result.containsKey(slot.getId()))
			{
				result.put(slot.getId(), slot);
			}
		}
		return result;
	}
	
	private int getId(String cellData)
	{
		float value = Float.parseFloat(cellData);
		return Math.round(value);
	}
	
	private DayOfWeek getDayOfWeek(String cellData)
	{
		DayOfWeekParser parser = new DayOfWeekParser();
		return parser.getDayOfTheWeek(cellData);
	}
	
	private String getTime(Cell cellData)
	{
		TimeParser parser = new TimeParser();
		return parser.getTimeFromCell(cellData);
	}
}
