package main;

import java.util.ArrayList;

import controllers.FacadeController;
import entities.StaffTimetable;
import entities.StudentTimetable;

public class NBUTest {
	
	public static void main(String[] args) {
		String filePath = "H:\\docs\\TimeTables\\BigTimetableData.xlsx";

		FacadeController facade = FacadeController.getInstance(filePath);
//		for (String staffID : facade.getStaffIDs()) {
//			StaffTimetable staffTT = facade.getTimetableForStaff(staffID);
//			System.out.println(staffTT.toString());
//		}
		
		for (String studentID : facade.getStudentIDs()) {
			StudentTimetable studentTT = facade.getTimetableForStudent(studentID);
			System.out.println(studentTT.toString());
		}
	}
}
