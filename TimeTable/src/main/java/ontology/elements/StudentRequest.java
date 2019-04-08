package ontology.elements;

import jade.content.AgentAction;

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

public class StudentRequest implements AgentAction {
	// Which Event the Student is leaving
	private Event leaveEvent;
	// What is the target Event
	private Event targetEvent;
	// Which student is asking for the request
	private Student student;
}
