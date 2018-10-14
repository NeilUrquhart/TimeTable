package main;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import dao.parsers.EventParser;
import dao.parsers.ModuleParser;
import dao.parsers.RoomParser;
import dao.parsers.StaffParser;
import dao.parsers.StudentParser;
import entities.Event;
import entities.Module;
import entities.Room;
import entities.Staff;
import entities.Student;
import entities.StudentInEvent;

public class StartUp
{
	public static void main(String[] args)
	{		
		String filePath = "C:\\Users\\Neil\\Desktop\\Source Control\\TT-Data.xlsx";
		Map<String,Module> modules = new HashMap<String,Module>();
		Map<String, Room> rooms = new HashMap<String, Room>();
		Map<String, Staff> staff = new HashMap<String, Staff>();
		Map<String, Student> students = new HashMap<String, Student>();
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
					RoomParser rp = new RoomParser(row);
					StaffParser stp = new StaffParser(row);
					StudentParser sp = new StudentParser(row);
					EventParser ep = new EventParser(row, mp, rp, stp, sp);
					Module m = mp.createModuleFromRow();
					if(!modules.containsKey(m.getName()))
					{
						modules.put(m.getName(), m);
					}
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
					Room r = rp.createRoomFromRow();
					if(!rooms.containsKey(r.getName()))
					{
						rooms.put(r.getName(), r);
					}
					Map<String, Staff> tempStaff = stp.createStaffFromRow();
					tempStaff.keySet().removeAll(staff.keySet());
					staff.putAll(tempStaff);
					
					Map<String, Student> tempStudents = sp.createStudentsFromRow();
					tempStudents.keySet().removeAll(students.keySet());
					students.putAll(tempStudents);
					
					for(Map.Entry<String, Room> entry : rooms.entrySet())
					{
						if(entry.getKey().equals(e.getRoom().getName()))
						{
							entry.getValue().getEvents().add(e);
						}
					}
					for(Map.Entry<String, Staff> entry : staff.entrySet())
					{
						for(Staff s : e.getStaff())
						{
							if(entry.getKey().equals(s.getName()))
							{
								entry.getValue().getEvents().add(e);
							}
						}
					}
					for(Map.Entry<String, Student> entry : students.entrySet())
					{
						for(StudentInEvent s : e.getStudents())
						{
							if(entry.getKey().equals(s.getStudent().getMatric()))
							{
								entry.getValue().getEvents().add(s);
							}
						}
					}
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
//		for(Room r : rooms.values())
//		{
//			System.out.println(r.toString());
//			System.out.println();
//		}
//		for(Staff s : staff.values())
//		{
//			System.out.println(s.toString());
//			System.out.println();
//		}
		for(Student s : students.values())
		{
			System.out.println(s.toString());
			System.out.println();
		}
	}
}
