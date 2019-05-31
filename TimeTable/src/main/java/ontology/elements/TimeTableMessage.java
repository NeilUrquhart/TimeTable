package ontology.elements;

import jade.content.AgentAction;
import jade.core.AID;
import jade.util.leap.ArrayList;
import returnCodes.ResponseMove;

public class TimeTableMessage implements AgentAction {
	ResponseMove response;
	Event caveatEvent;
	Object[] studentList;
	Object[] targetEvents; 

	

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

	public Event getCaveatEvent() {
		return caveatEvent;
	}

	public void setCaveatEvent(Event caveatEvent) {
		this.caveatEvent = caveatEvent;
	}

	public ResponseMove getResponse() {
		return response;
	}

	public void setResponse(ResponseMove response) {
		this.response = response;
	}
}
