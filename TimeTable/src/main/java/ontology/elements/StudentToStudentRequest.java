package ontology.elements;

import jade.content.AgentAction;
import jade.core.AID;
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

public class StudentToStudentRequest implements AgentAction {
	// Which event am I interested
	private int targetEvent;
	// Which Event the Student is part of
	private int leaveSlot;
	// Is the slot unacceptable or just awkward
	private boolean unacceptable;
	// AID of the consenting student
	private String consentingStudentMatric;
	
	
	public String getConsentingStudentMatric() {
		return consentingStudentMatric;
	}
	public void setConsentingStudentMatric(String consentingStudentMatric) {
		this.consentingStudentMatric = consentingStudentMatric;
	}
	public int getUtility() {
		return utility;
	}
	public void setUtility(int utility) {
		this.utility = utility;
	}
	// Utility of the consenting student
	private int utility;
	
	

	public int getTargetEvent() {
		return targetEvent;
	}
	public void setTargetEvent(int targetEvent) {
		this.targetEvent = targetEvent;
	}
	public int getLeaveSlot() {
		return leaveSlot;
	}
	public void setLeaveSlot(int leaveSlot) {
		this.leaveSlot = leaveSlot;
	}
	public boolean isUnacceptable() {
		return unacceptable;
	}
	public void setUnacceptable(boolean unacceptable) {
		this.unacceptable = unacceptable;
	}
}
