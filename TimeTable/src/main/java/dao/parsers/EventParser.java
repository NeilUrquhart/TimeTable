package dao.parsers;

import org.apache.poi.ss.usermodel.Row;

import entities.Event;
import entities.EventType;
import entities.Module;

public class EventParser
{
	private Row row;
	private Module module;
	
	public EventParser(Row row, Module module)
	{
		this.row = row;
		this.module = module;
	}
	
	public Event createEventFromRow()
	{
		Event result = new Event();
		result.setModule(module);
		result.setType(getEventTypeFromCell());
		result.setInstance(getEventInstanceFromCell());
		return result;
	}
	
	private EventType getEventTypeFromCell()
	{
		String cellData = removeModuleIdFromCell();
		String[] result = cellData.split("/");
		return getEventTypeFromString(result[0]);
	}
	
	private String getEventInstanceFromCell()
	{
		String cellData = removeModuleIdFromCell();
		String[] result = cellData.split("/");
		return removeExtraFromInstance(result[1]);
	}
	
	private String removeModuleIdFromCell()
	{
		String cellData = row.getCell(0).toString();
		String[] result = cellData.split("\\\\");
		return result[1];
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
}
