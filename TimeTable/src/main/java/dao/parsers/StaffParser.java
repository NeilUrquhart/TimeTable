package dao.parsers;

import java.util.HashMap;
import java.util.Map;

import org.apache.poi.ss.usermodel.Row;

import entities.Staff;

public class StaffParser
{
	private Row row;
	
	public StaffParser(Row row)
	{
		this.row = row;
	}
	
	public Map<String, Staff> createStaffFromRow()
	{
		String[] staffArray = getStaffAsStringArray();
		return getStaffFromStringArray(staffArray);
	}
	
	private String[] getStaffAsStringArray()
	{
		String cellData = row.getCell(10).toString();
		return cellData.split(",");
	}
	
	private Map<String, Staff> getStaffFromStringArray(String[] staffArray)
	{
		Map<String, Staff> result = new HashMap<String, Staff>();
		for(int i = 0; i < staffArray.length; i++)
		{
			if(!staffArray[i].trim().isEmpty())
			{
				Staff staff = new Staff(staffArray[i]);
				result.put(staff.getName(), staff);
			}
		}
		return result;
	}
}
