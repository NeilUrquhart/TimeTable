package ontology.elements;

import jade.content.AgentAction;
import jade.core.AID;
import jade.util.leap.ArrayList;
import returnCodes.ResponseMove;

public class TimeTableMessage implements AgentAction {
	ResponseMove response;
	Event caveatEvent;

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
