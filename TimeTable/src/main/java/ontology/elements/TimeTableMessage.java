package ontology.elements;

import jade.content.AgentAction;
import jade.core.AID;
import jade.util.leap.ArrayList;
import returnCodes.ResponseMove;

public class TimeTableMessage implements AgentAction {
	ResponseMove response;
	int caveatEvent;
	int caveatEvent2;
	Object[] studentList;
	Object[] targetEvents; 
	AID targetStudent;
	
	public AID getTargetStudent() {
		return targetStudent;
	}

	public void setTargetStudent(AID targetStudent) {
		this.targetStudent = targetStudent;
	}

	public Object[] getStudentList() {
		return studentList;
	}

	public void setStudentList(Object[] studentList) {
		this.studentList = studentList;
	}

	public Object[] getTargetEvents() {
		return targetEvents;
	}

	public void setTargetEvents(Object[] targetEvents) {
		this.targetEvents = targetEvents;
	}

	public int getCaveatEvent() {
		return caveatEvent;
	}

	public void setCaveatEvent(int caveatEvent) {
		this.caveatEvent = caveatEvent;
	}

	public int getCaveatEvent2() {
		return caveatEvent2;
	}

	public void setCaveatEvent2(int caveatEvent2) {
		this.caveatEvent2 = caveatEvent2;
	}
	
	public ResponseMove getResponse() {
		return response;
	}

	public void setResponse(ResponseMove response) {
		this.response = response;
	}
}
