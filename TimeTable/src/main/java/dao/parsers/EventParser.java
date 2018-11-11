package dao.parsers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Row;

import entities.Event;
import entities.EventType;
import entities.Module;
import entities.Room;
import entities.Slot;
import entities.Staff;
import entities.Student;
import entities.StudentInEvent;

public class EventParser
{
	private Row row;
	private ModuleParser moduleParser;
	private RoomParser roomParser;
	private StaffParser staffParser;
	private StudentParser studentParser;
	
	public void setRow(Row row)
	{
		this.row = row;
	}
	
	public EventParser()
	{

	}
	public EventParser(Row row)
	{
		this.row = row;
		this.moduleParser = new ModuleParser(row);
		this.roomParser = new RoomParser(row);
		this.staffParser = new StaffParser(row);
		this.studentParser = new StudentParser(row);
	}
	
	public Event createEventFromRow()
	{
		Event result = new Event();
		result.setModule(getModuleFromCell());
		result.setType(getEventTypeFromCell());
		result.setInstance(getEventInstanceFromCell());
		result.setRoom(getRoomFromCell());
		result.setStaff(getStaffFromCell());
		result.setStudents(getStudentsFromCell(result));
		result.setSlots(getAllSlotsForEvent(result));
		return result;
	}
	
	private Module getModuleFromCell()
	{
		return moduleParser.createModuleFromRow();
	}
	
	private EventType getEventTypeFromCell()
	{
		String result = getTypeOrInstanceFromCell(0).trim();
		return getEventTypeFromString(result);
	}
	
	private String getEventInstanceFromCell()
	{
		String result = getTypeOrInstanceFromCell(1).trim();
		return removeExtraFromInstance(result);
	}
	
	// Splits the String of the Cell on the /
	// index of 0 is the Type, 1 is the instance
	private String getTypeOrInstanceFromCell(int index)
	{
		String cellData = removeModuleIdFromCell();
		String[] result = cellData.split("/");
		return result[index].trim();
	}
	
	private String removeModuleIdFromCell()
	{
		String cellData = row.getCell(0).toString();
		String[] result = cellData.split("\\\\");
		return result[1].trim();
	}
	
	private EventType getEventTypeFromString(String type)
	{
		if(type.equals("LEC"))
			return EventType.LECTURE;
		else if(type.equals("TUT"))
			return EventType.TUTORIAL;
		else if(type.equals("PRA"))
			return EventType.PRACTICAL;
		else
			return EventType.DEFAULT;
	}
	
	private String removeExtraFromInstance(String info)
	{
		if(!info.contains(" "))
		{
			return info;
		}
		
		String[] split = info.split(" ");
		return split[0];
	}
	
	private Room getRoomFromCell()
	{
		return roomParser.createRoomFromRow();
	}
	
	private List<Staff> getStaffFromCell()
	{
		Map<String, Staff> staffMap = staffParser.createStaffFromRow();
		return new ArrayList<Staff>(staffMap.values());
	}
	
	private List<StudentInEvent> getStudentsFromCell(Event event)
	{
		Map<String, Student> studentMap = studentParser.createStudentsFromRow();
		List<StudentInEvent> result = new ArrayList<StudentInEvent>();
		for(Student s : studentMap.values())
		{
			StudentInEvent sie = new StudentInEvent();
			sie.setStudent(s);
			sie.setEvent(event);
			result.add(sie);
		}
		return result;
	}
	
	private List<Slot> getAllSlotsForEvent(Event event)
	{
		SlotParser slotParser = new SlotParser(row, event);
		Map<Integer, Slot> slotMap = slotParser.createSlotFromRow();
		return new ArrayList<Slot>(slotMap.values());
	}
}
