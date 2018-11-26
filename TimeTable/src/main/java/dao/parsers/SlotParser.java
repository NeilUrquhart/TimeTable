package dao.parsers;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import dao.SlotExcelDataReader;
import entities.DayOfWeek;
import entities.Event;
import entities.TTSlot;

public class SlotParser
{
	private Row row;
	private Map<Integer, TTSlot> slots;
	private Event event;
	
	public SlotParser(Row row, Event event)
	{
		this.row = row;
		this.slots = getSlotsFromExcel();
		this.event = event;
	}
	
	public Map<Integer, TTSlot> createSlotFromRow()
	{
		Map<Integer, TTSlot> result = new HashMap<Integer, TTSlot>();
		DayOfWeek day = parseDayOfTheWeek(row.getCell(4).toString());
		TTSlot start = getStartSlot(day, row.getCell(5));
		TTSlot end = getEndSlot(day, row.getCell(6));
		result.put(start.getId(), start);
		if(!result.containsKey(end.getId()))
		{
			result.put(end.getId(), end);
			result = getAllSlotsInBetween(result);
			result = addEventToSlots(result);
		}
		return result;
	}
	
	private Map<Integer, TTSlot> getSlotsFromExcel()
	{
		SlotExcelDataReader dataReader = new SlotExcelDataReader();
		return dataReader.readAll();
	}
	
	private DayOfWeek parseDayOfTheWeek(String day)
	{
		DayOfWeekParser parser = new DayOfWeekParser();
		return parser.getDayOfTheWeek(day);
	}
	
	private TTSlot getStartSlot(DayOfWeek day, Cell cellData)
	{
		TTSlot result = new TTSlot();
		result.setDay(DayOfWeek.DEFAULT);
		String startTime = getTimeAsStringFromCell(cellData);
		for(TTSlot slot : slots.values())
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
	
	private TTSlot getEndSlot(DayOfWeek day, Cell cellData)
	{
		TTSlot result = new TTSlot();
		result.setDay(DayOfWeek.DEFAULT);
		String endTime = getTimeAsStringFromCell(cellData);
		for(TTSlot slot : slots.values())
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
	
	private Map<Integer, TTSlot> getAllSlotsInBetween(Map<Integer, TTSlot> input)
	{
		if(input.size() <= 2)
		{
			return input;
		}
		
		TreeMap<Integer, TTSlot> result = new TreeMap<Integer, TTSlot>(slots);
		for(int i = result.firstKey(); i <= result.lastKey(); i++)
		{
			TTSlot slot = this.slots.get(i);
			if(!result.containsKey(slot.getId()))
			{
				result.put(slot.getId(), slot);
			}
		}
		return result;
	}
	
	private Map<Integer, TTSlot> addEventToSlots(Map<Integer, TTSlot> input)
	{
		for(TTSlot slot : input.values())
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
