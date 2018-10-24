package dao.parsers;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import dao.SlotExcelDataReader;
import entities.DayOfWeek;
import entities.Event;
import entities.Slot;

public class SlotParser
{
	private Row row;
	private Map<Integer, Slot> slots;
	private Event event;
	
	public SlotParser(Row row, Event event)
	{
		this.row = row;
		this.slots = getSlotsFromExcel();
		this.event = event;
	}
	
	public Map<Integer, Slot> createSlotFromRow()
	{
		Map<Integer, Slot> result = new HashMap<Integer, Slot>();
		DayOfWeek day = parseDayOfTheWeek(row.getCell(4).toString());
		Slot start = getStartSlot(day, row.getCell(5));
		Slot end = getEndSlot(day, row.getCell(6));
		result.put(start.getId(), start);
		if(!result.containsKey(end.getId()))
		{
			result.put(end.getId(), end);
			result = getAllSlotsInBetween(result);
			result = addEventToSlots(result);
		}
		return result;
	}
	
	private Map<Integer, Slot> getSlotsFromExcel()
	{
		SlotExcelDataReader dataReader = new SlotExcelDataReader();
		return dataReader.readAll();
	}
	
	private DayOfWeek parseDayOfTheWeek(String day)
	{
		DayOfWeekParser parser = new DayOfWeekParser();
		return parser.getDayOfTheWeek(day);
	}
	
	private Slot getStartSlot(DayOfWeek day, Cell cellData)
	{
		Slot result = new Slot();
		result.setDay(DayOfWeek.DEFAULT);
		String startTime = getTimeAsStringFromCell(cellData);
		for(Slot slot : slots.values())
		{
			if(isDayTheSame(slot.getDay(), day))
			{
				if(isTimeTheSame(slot.getStartTime(), startTime))
				{
					result = slot;
				}
			}
		}
		return result;
	}
	
	private Slot getEndSlot(DayOfWeek day, Cell cellData)
	{
		Slot result = new Slot();
		result.setDay(DayOfWeek.DEFAULT);
		String endTime = getTimeAsStringFromCell(cellData);
		for(Slot slot : slots.values())
		{
			if(isDayTheSame(slot.getDay(), day))
			{
				if(isTimeTheSame(slot.getEndTime(), endTime))
				{
					result = slot;
				}
			}
		}
		return result;
	}
	
	private String getTimeAsStringFromCell(Cell cellData)
	{
		TimeParser parser = new TimeParser();
		return parser.getTimeFromCell(cellData);
	}
	
	private boolean isDayTheSame(DayOfWeek day, DayOfWeek dayToCompare)
	{
		return day.equals(dayToCompare);
	}
	
	private boolean isTimeTheSame(String time, String timeToCompare)
	{
		return time.contains(timeToCompare);
	}
	
	private Map<Integer, Slot> getAllSlotsInBetween(Map<Integer, Slot> input)
	{
		if(input.size() <= 2)
		{
			return input;
		}
		
		TreeMap<Integer, Slot> result = new TreeMap<Integer, Slot>(slots);
		for(int i = result.firstKey(); i <= result.lastKey(); i++)
		{
			Slot slot = this.slots.get(i);
			if(!result.containsKey(slot.getId()))
			{
				result.put(slot.getId(), slot);
			}
		}
		return result;
	}
	
	private Map<Integer, Slot> addEventToSlots(Map<Integer, Slot> input)
	{
		for(Slot slot : input.values())
		{
			slot.setEvent(event);
			if(slot.getDay() != DayOfWeek.DEFAULT)
			{
				slot.setUsed(true);
			}
		}
		return input;
	}
}
