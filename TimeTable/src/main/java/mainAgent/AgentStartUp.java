package mainAgent;

import java.util.List;

import controllers.FacadeController;
import entities.Student;
import jade.core.*;
import jade.core.Runtime;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;

import controllers.FacadeController;
import controllers.FileLoadController;
import controllers.QueryController;
import dao.DataReaderSetting;
import dao.ProgrammeCsvReader;
import dao.TimetableData;
import dao.TimetableExcelDataReader;
import entities.Event;
import entities.EventType;
import entities.Module;
import entities.Programme;
import entities.Room;
import entities.TTSlot;
import returnCodes.ResponseMove;
import entities.Staff;
import entities.StaffTimetable;
import entities.Student;
import entities.StudentTimetable;

public class AgentStartUp {

	public static void main(String[] args) {
		Profile myProfile = new ProfileImpl();
		Runtime myRuntime = Runtime.instance();
		try{
			ContainerController myContainer = myRuntime.createMainContainer(myProfile);	
			AgentController rma = myContainer.createNewAgent("rma", "jade.tools.rma.rma", null);
			rma.start();
			
			FacadeController facade = FacadeController.getInstance(""); //loads tt data

			List<Event> events = facade.getAllEvents();
			Object[] ev = {events};
			AgentController timeTablingAgent = myContainer.createNewAgent("time-table", TimeTablingSystem.class.getCanonicalName(), ev);
			timeTablingAgent.start();
			
			AgentController Student;
			List<Student> students = facade.getAllStudents();
			for(int i = 0; i < students.size(); i++) {
				Object[] stud = {students.get(i)}; // To pass in the student info create a student object
				Student = myContainer.createNewAgent("Student: " + students.get(i).getMatric(), Student.class.getCanonicalName(), stud);

				Student.start();
			}
		}
		catch(Exception e){
			System.out.println("Exception starting agent: " + e.toString());
		}


	}

}
