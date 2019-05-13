package mainAgent;

import java.util.List;

import controllers.FacadeController;
import jade.core.*;
import jade.core.Runtime;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import ontology.elements.Event;
import ontology.elements.EventType;
import ontology.elements.Module;
import ontology.elements.Programme;
import ontology.elements.Room;
import ontology.elements.Staff;
import ontology.elements.StaffTimetable;
import ontology.elements.Student;
import ontology.elements.StudentTimetable;
import ontology.elements.TTSlot;
import controllers.FacadeController;
import controllers.FileLoadController;
import controllers.QueryController;
import dao.DataReaderSetting;
import dao.ProgrammeCsvReader;
import dao.TimetableData;
import dao.TimetableExcelDataReader;
import returnCodes.ResponseMove;

public class AgentStartUp {

	public static void main(String[] args) {
		Profile myProfile = new ProfileImpl();
		Runtime myRuntime = Runtime.instance();
		try{
			ContainerController myContainer = myRuntime.createMainContainer(myProfile);	
			AgentController rma = myContainer.createNewAgent("rma", "jade.tools.rma.rma", null);
			rma.start();
			
			FacadeController facade = FacadeController.getInstance(""); //loads tt data

			Object[] fc = {facade};
			AgentController timeTablingAgent = myContainer.createNewAgent("time-table", TimeTablingSystem.class.getCanonicalName(), fc);
			timeTablingAgent.start();
			
			AgentController Student;
			List<Student> students = facade.getAllStudents();
			for(int i = 0; i < students.size(); i++) {
				Object[] stud = {students.get(i)}; // To pass in the student info create a student object
				Student = myContainer.createNewAgent("Student ID: " + students.get(i).getMatric(), StudentAgent.class.getCanonicalName(), stud);

				Student.start();
			}
		}
		catch(Exception e){
			System.out.println("Exception starting agent: " + e.toString());
		}


	}

}
