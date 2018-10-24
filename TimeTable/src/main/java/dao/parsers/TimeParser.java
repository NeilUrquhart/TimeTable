package dao.parsers;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.poi.ss.usermodel.Cell;

public class TimeParser
{
	private final String TIME_FORMAT = "HH:mm";
	
	public TimeParser() 
	{
		
	}
	
	public String getTimeFromCell(Cell cellData)
	{
		Date time = cellData.getDateCellValue();
		DateFormat formatter = new SimpleDateFormat(TIME_FORMAT);
		return formatter.format(time);
	}
}
