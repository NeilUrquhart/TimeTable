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
		
		try
		{
			FileInputStream fs = new FileInputStream(f);
			XSSFWorkbook wb = new XSSFWorkbook(fs);
			XSSFSheet sheet = wb.getSheetAt(0);
			System.out.println("WOOHOO");
		}
		catch(Exception e)
		{
			System.out.println("Error: " + e.getMessage());
		}
	}
}
