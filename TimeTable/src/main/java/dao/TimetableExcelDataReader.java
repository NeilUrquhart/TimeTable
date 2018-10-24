package dao;

import java.io.File;
import java.io.FileInputStream;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class TimetableExcelDataReader
{
	public void run()
	{
		ClassLoader cl = getClass().getClassLoader();
		File f = new File(cl.getResource("TT-Data.xlsx").getFile());
	}
	
	public void initialise(String filePath)
	{
		ClassLoader classLoader = getClass().getClassLoader();
		File file = new File(filePath);
		try
		{
			XSSFWorkbook wb = new XSSFWorkbook(file);
			XSSFSheet sheet = wb.getSheetAt(0);
			System.out.println("Works");
		}
		catch(Exception e)
		{
			throw new RuntimeException("Oh shit!" + e.getMessage());
		}
	}
	
	public void readExcelSheet()
	{
		
	}
}
