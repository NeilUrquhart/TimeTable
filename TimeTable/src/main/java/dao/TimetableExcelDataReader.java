package dao;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import dao.parsers.EventParser;
import dao.parsers.ModuleParser;
import dao.parsers.RoomParser;
import dao.parsers.StaffParser;
import dao.parsers.StudentParser;
import entities.Event;
import entities.Module;
import entities.Room;
import entities.Staff;
import entities.Student;
import entities.StudentInEvent;

public class TimetableExcelDataReader
{
	private DataReaderSetting setting;
	private ModuleParser moduleParser;
	private EventParser eventParser;
	private RoomParser roomParser;
	private StaffParser staffParser;
	private StudentParser studentParser;
	private SlotExcelDataReader slotDataReader;
	
	private Map<String, Module> modules;
	private Map<Integer, Event> events;
	private Map<String, Room> rooms;
	private Map<String, Student> students;
	private Map<String, Staff> staff;
	 
	public TimetableExcelDataReader()
	{
		
	}
	public TimetableExcelDataReader(DataReaderSetting setting)
	{
		this.setting = setting;
	}
	
	public TimetableData getData(String filePath)
	{
		XSSFWorkbook workbook = openWorkbook(getFile(filePath));
		XSSFSheet sheet = getSheetInWorkbook(workbook);
		closeWorkbook(workbook);
		return readSheet(sheet);
	}
	
	private File getFile(String filePath)
	{
		if(setting.equals(DataReaderSetting.LIVE))
		{
			return getLiveFile(filePath);
		}
		else
		{
			return getTestFile();
		}
	}
	
	private File getLiveFile(String filePath)
	{
		try
		{
			return new File(filePath);
		}
		catch(Exception e)
		{
			throw new RuntimeException("\nCannot read file."
					+ "\nClass: " + getClass().getName()
					+ "\nMethod: getLiveFile"
					+ "\nException: " + e.getMessage());
		}
	}
	
	private File getTestFile()
	{
		try
		{
			ClassLoader classLoader = getClass().getClassLoader();
			return new File(classLoader.getResource("TT-Data.xlsx").getFile());
		}
		catch(Exception e)
		{
			throw new RuntimeException("\nCannot read file."
					+ "\nClass: " + getClass().getName()
					+ "\nMethod: getTestFile"
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
			throw new RuntimeException("\nCannot open Workbook."
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
			throw new RuntimeException("\nCannot close Workbook."
					+ "\nClass: " + getClass().getName()
					+ "\nMethod: closeWorkbook"
					+ "\nException: " + e.getMessage());
		}
	}
	
	private XSSFSheet getSheetInWorkbook(XSSFWorkbook workbook)
	{
		return workbook.getSheetAt(0);
	}
	
	private TimetableData readSheet(XSSFSheet sheet)
	{
		int eventId = 1;
		initialiseMaps();
		Iterator<Row> rowIt = sheet.rowIterator();
		while(rowIt.hasNext())
		{
			Row row = rowIt.next();
			if(row.getRowNum() != 0)
			{
				initialiseParsers(row);
				Module module = getModuleFromRow(row);
				addModuleToMap(module);
				
				Event event = getEventFromRow(row);
				event = createEventId(eventId, event);
				eventId++;
				addEventToMap(event);
				addEventToModule(event);
				
				Room room = getRoomFromRow(row);
				addRoomToMap(room);
				addEventToRoom(event);
				
				addStaffToMap(row);
				addEventToStaff(event);
				
				addStudentToMap(row);
				addEventToStudent(event);
			}
		}
		
		TimetableData result = new TimetableData();
		result.setModules(modules);
		result.setEvents(events);
		result.setRooms(rooms);
		result.setStaff(staff);
		result.setStudents(students);
		result.setSlots(slotDataReader.readAll());
		return result;
	}
	
	private void initialiseMaps()
	{
		modules = new HashMap<String, Module>();
		events = new HashMap<Integer, Event>();
		rooms = new HashMap<String, Room>();
		students = new HashMap<String, Student>();
		staff = new HashMap<String, Staff>();
	}
	
	private void initialiseParsers(Row row)
	{
		moduleParser = new ModuleParser(row);
		eventParser = new EventParser(row);
		roomParser = new RoomParser(row);
		staffParser = new StaffParser(row);
		studentParser = new StudentParser(row);
		slotDataReader = new SlotExcelDataReader();
	}
	
	private Module getModuleFromRow(Row row)
	{
		moduleParser.setRow(row);
		return moduleParser.createModuleFromRow();
	}
	
	private void addModuleToMap(Module module)
	{
		if(!modules.containsKey(module.getName()))
		{
			modules.put(module.getName(), module);
		}
	}
	
	private Event getEventFromRow(Row row)
	{
		eventParser.setRow(row);
		return eventParser.createEventFromRow();
	}
	
	private void addEventToMap(Event event)
	{
		if(!events.containsKey(event.getId()))
		{
			events.put(event.getId(), event);
		}
	}
	
	private Event createEventId(int id, Event event)
	{
		event.setId(id);
		return event;
	}
	
	private void addEventToModule(Event event)
	{
		for(Map.Entry<String, Module> entry : modules.entrySet())
		{
			if(entry.getKey().equals(event.getModule().getName()))
			{
				entry.getValue().getEvents().add(event);
			}
		}
	}
	
	private Room getRoomFromRow(Row row)
	{
		roomParser.setRow(row);
		return roomParser.createRoomFromRow();
	}
	
	private void addRoomToMap(Room room)
	{
		if(!rooms.containsKey(room.getName()))
		{
			rooms.put(room.getName(), room);
		}
	}
	
	private void addEventToRoom(Event event)
	{
		for(Map.Entry<String, Room> room : rooms.entrySet())
		{
			if(room.getKey().equals(event.getRoom().getName()))
			{
				room.getValue().getEvents().add(event);
			}
		}
	}
	
	private void addStaffToMap(Row row)
	{
		staffParser.setRow(row);
		Map<String, Staff> tempStaff = staffParser.createStaffFromRow();
		tempStaff.keySet().removeAll(staff.keySet());
		staff.putAll(tempStaff);
	}
	
	private void addEventToStaff(Event event)
	{
		for(Map.Entry<String, Staff> entry : staff.entrySet())
		{
			for(Staff member : event.getStaff())
			{
				if(entry.getKey().equals(member.getName()))
				{
					entry.getValue().getEvents().add(event);
				}
			}
		}
	}
	
	private void addStudentToMap(Row row)
	{
		studentParser.setRow(row);
		Map<String, Student> tempStudent = studentParser.createStudentsFromRow();
		tempStudent.keySet().removeAll(students.keySet());
		students.putAll(tempStudent);
	}
	
	private void addEventToStudent(Event event)
	{
		for(Map.Entry<String, Student> entry : students.entrySet())
		{
			for(StudentInEvent sie : event.getStudents())
			{
				if(entry.getKey().equals(sie.getStudent().getMatric()))
				{
					entry.getValue().getEvents().add(sie);
				}
			}
		}
	}
	
}
