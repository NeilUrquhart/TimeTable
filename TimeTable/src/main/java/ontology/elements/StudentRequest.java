package ontology.elements;

import jade.content.AgentAction;
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

public class StudentRequest implements AgentAction {
	// Which Event the Student is leaving
	private int leaveEvent;
	// What is the target Event
	private int targetEvent;
	// Which student is asking for the request
	private String student;
	
	public int getLeaveEvent() {
		return leaveEvent;
	}
	public void setLeaveEvent(int leaveEvent) {
		this.leaveEvent = leaveEvent;
	}
	public int getTargetEvent() {
		return targetEvent;
	}
	public void setTargetEvent(int targetEvent) {
		this.targetEvent = targetEvent;
	}
	public String getStudent() {
		return student;
	}
	public void setStudent(String student) {
		this.student = student;
	}
}
