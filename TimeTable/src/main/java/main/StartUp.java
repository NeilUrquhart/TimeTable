package main;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import dao.parsers.EventParser;
import dao.parsers.ModuleParser;
import entities.Event;
import entities.Module;

public class StartUp
{
	public static void main(String[] args)
	{		
		String filePath = "C:\\Users\\Neil\\Desktop\\Source Control\\TT-Data.xlsx";
		HashMap<String,Module> modules = new HashMap<String,Module>();
		List<Event> events = new ArrayList<Event>();
		int i = 0;
		try
		{
			FileInputStream fs = new FileInputStream(filePath);
			XSSFWorkbook wb = new XSSFWorkbook(fs);
			XSSFSheet sheet = wb.getSheetAt(0);
			Iterator<Row> rowIt = sheet.iterator();
			while(rowIt.hasNext())
			{
				Row row = rowIt.next();
				if(row.getRowNum() != 0)
				{
					ModuleParser mp = new ModuleParser(row);
					Module m = mp.createModuleFromRow();
					if(!modules.containsKey(m.getName()))
					{
						modules.put(m.getName(), m);
					}
					EventParser ep = new EventParser(row, m);
					Event e = ep.createEventFromRow();
					i++;
					e.setId(i);
					for(Map.Entry<String,Module> entry : modules.entrySet())
					{
						if(entry.getKey().equals(e.getModule().getName()))
						{
							entry.getValue().getEvents().add(e);
						}
					}
					events.add(e);
				}
			}
			wb.close();
			fs.close();
		}
		catch(IOException ioe)
		{
			System.out.println("OOPS");
		}
		for(Module m : modules.values())
		{
			System.out.println(m.toString());
			System.out.println();
		}
	}
}
