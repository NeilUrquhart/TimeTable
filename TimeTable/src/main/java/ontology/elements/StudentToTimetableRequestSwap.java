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

public class StudentToTimetableRequestSwap implements AgentAction {
	// Which Slot the Student wishes to leave
	private int targetEvent;
	// Which Event the Student is part of
	private int LeaveEvent;
	// Which student is asking for the request
	private String leaveStudentMatric;
	// Which student is getting 
	private String targetStudentMatric;
	
	public int getTargetEvent() {
		return targetEvent;
	}
	public void setTargetEvent(int targetEvent) {
		this.targetEvent = targetEvent;
	}
	public int getLeaveEvent() {
		return LeaveEvent;
	}
	public void setLeaveEvent(int leaveEvent) {
		LeaveEvent = leaveEvent;
	}
	public String getLeaveStudentMatric() {
		return leaveStudentMatric;
	}
	public void setLeaveStudentMatric(String leaveStudentMatric) {
		this.leaveStudentMatric = leaveStudentMatric;
	}
	public String getTargetStudentMatric() {
		return targetStudentMatric;
	}
	public void setTargetStudentMatric(String targetStudentMatric) {
		this.targetStudentMatric = targetStudentMatric;
	}
	
	
}
