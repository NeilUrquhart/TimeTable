package ontology.elements;

import jade.content.AgentAction;
import returnCodes.ResponseMove;

public class TimeTableMessage implements AgentAction {
	ResponseMove response;

	public ResponseMove getResponse() {
		return response;
	}

	public void setResponse(ResponseMove response) {
		this.response = response;
	}
}
