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

public class TimetableToStudentRequest implements AgentAction {
	// Which event am I interested
	private int targetEvent;
	// Which Event the Student is part of
	private int leaveSlot;
	// Is the slot unacceptable or just awkward
	private boolean unacceptable;
	
	

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
