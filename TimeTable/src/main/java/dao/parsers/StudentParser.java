package dao.parsers;

import java.util.HashMap;
import java.util.Map;

import org.apache.poi.ss.usermodel.Row;

import entities.Student;

public class StudentParser
{
	private Row row;
	
	public StudentParser(Row row)
	{
		this.row = row;
	}
	
	public Map<String, Student> createStudentsFromRow()
	{
		String[] studentArray = getStudentsAsStringArray();
		return getStudentsFromStringArray(studentArray);
	}
	
	private String[] getStudentsAsStringArray()
	{
		String result = row.getCell(3).toString();
		result = result.replace(", $", "");
		return result.split(",");
	}
	
	private Map<String, Student> getStudentsFromStringArray(String[] studentArray)
	{
		Map<String, Student> result = new HashMap<String, Student>();
		for(int i = 0; i < studentArray.length; i++)
		{
			if(!studentArray[i].trim().isEmpty())
			{
				Student student = new Student(studentArray[i]);
				result.put(student.getMatric(), student);
			}
		}
		return result;
	}
}
