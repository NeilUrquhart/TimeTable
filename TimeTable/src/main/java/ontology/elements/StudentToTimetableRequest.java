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

public class StudentToTimetableRequest implements AgentAction {
	// Which Slot the Student wishes to leave
	private int slot;
	// Which Event the Student is part of
	private int LeaveEvent;
	// Which student is asking for the request
	private String student;
	// Personality
	private Personality personality;
	
	public int getLeaveEvent() {
		return LeaveEvent;
	}
	public void setLeaveEvent(int event) {
		this.LeaveEvent = event;
	}
	public int getSlot() {
		return slot;
	}
	public void setSlot(int slot) {
		this.slot = slot;
	}
	public String getStudent() {
		return student;
	}
	public void setStudent(String student) {
		this.student = student;
	}
	public Personality getPersonality() {
		return personality;
	}
	public void setPersonality(Personality personality) {
		this.personality = personality;
	}
}
