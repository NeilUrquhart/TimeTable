/*
 *  The student agent will have internal logic which calculates utility of accepting the swap
 *  It goes as follows:
 *  
 *  Formula for student receiving an offer:
 *  CurrentEvent(Awkward) = +1
 *  CurrentEvent(Unacceptable)  = +2
 *  CurrentEvent(Neutral) = 0
 *  IncomingEvent(Awkward) = -1
 *  IncomingEvent(Unacceptable) = -2
 *  IncomingEvent(Neutral) = 0
 *  IncomingEvent(AwkwardForOtherStudent) = 0
 *  IncomingEvent(UnacceptableForOtherStudent) = +1
 *  
 *  CurrentEvent + IncomingEvent + IncomingEventForOtherStudent >= 0 Then Accept
 */

package mainAgent;

import jade.content.ContentElement;
import jade.content.lang.Codec;
import jade.content.lang.Codec.CodecException;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;
import jade.content.onto.basic.Action;
import jade.content.onto.basic.Result;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.SequentialBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import ontology.TimeTableOntology;
import ontology.elements.*;

import java.util.ArrayList;
import java.util.Hashtable;

import controllers.FacadeController;
import controllers.FileLoadController;
import controllers.QueryController;
import dao.DataReaderSetting;
import dao.ProgrammeCsvReader;
import dao.TimetableData;
import dao.TimetableExcelDataReader;
import returnCodes.ResponseMove;
import personalityHelpers.Evaluator;

public class StudentAgent extends Agent {
	private Codec codec = new SLCodec();
	private Ontology ontology = TimeTableOntology.getInstance();
	private AID timeTableAID;
	private Student studentInfo;
	private StudentToTimetableRequest studentRequest = new StudentToTimetableRequest();
	private Evaluator evaluation = new Evaluator();
	private boolean processingSlot = false;
	private SlotInfo currentSlot;

	protected void setup() {

		DFAgentDescription dfd = new DFAgentDescription();
		dfd.setName(getAID());
		ServiceDescription sd = new ServiceDescription();
		sd.setType("student");
		sd.setName(getLocalName());
		dfd.addServices(sd);
		try {
			DFService.register(this, dfd);
		}
		catch (FIPAException fe) {
			fe.printStackTrace();
		}

		// Find out who you are
		studentInfo = (Student) getArguments()[0];
		studentRequest.setStudent(studentInfo.getMatric());
		studentRequest.setPersonality(studentInfo.getPersonality());
		// Check if you are in a non-compatible event
		// TODO THIS IS WRONG YOU ARE SUPPOSED TO LOOK AT SLOTS NOT EVENTS
		if (studentInfo.getMatric().equals("4"))
			evaluation.evaluate(studentInfo.getPersonality(), studentInfo.getEvents());


		// Register language and Ontology
		getContentManager().registerLanguage(codec);
		getContentManager().registerOntology(ontology);

		this.addBehaviour(new FindTimeTable(this));

		doWait(2000);
		this.addBehaviour(new SendMessage(this));
		this.addBehaviour(new ReceiveResponse(this));
	}

	private class FindTimeTable extends OneShotBehaviour {

		public FindTimeTable(Agent a) {
			super(a);
		}

		@Override
		public void action() {
			DFAgentDescription template = new DFAgentDescription();
			ServiceDescription sd = new ServiceDescription();
			sd.setType("timetable");
			template.addServices(sd);
			try{
				DFAgentDescription[] agentsType1  = DFService.search(myAgent,template); 
				timeTableAID = agentsType1[0].getName();
			}
			catch(FIPAException e) {
				e.printStackTrace();
			}
		}

	}

	private class SendMessage extends Behaviour {
		public SendMessage(Agent a) {
			super(a);
		}		
		public void action() {
			if (studentInfo.getMatric().equals("4")) {
				if (!processingSlot && (evaluation.hasUnnacceptable() || evaluation.hasAwkard())) {
					// Grab a slot
					if (evaluation.hasUnnacceptable()) {
						currentSlot = evaluation.getUnacceptable();
					} else {
						currentSlot = evaluation.getAwkward();
					}
					// We are now in the process of changing a slot
					processingSlot = true;
					// Prepare the REQUEST message
					studentRequest.setLeaveEvent(currentSlot.getEvent().getId());
					studentRequest.setSlot(currentSlot.getSlotID());
					ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
					msg.addReceiver(timeTableAID);
					msg.setLanguage(codec.getName());
					msg.setOntology(ontology.getName()); 
					Action request = new Action();
					request.setAction(studentRequest);
					request.setActor(timeTableAID);

					try {
						// Let JADE convert from Java objects to string
						getContentManager().fillContent(msg, request);
						send(msg);
						System.out.println("Sent a message to the Time Tabling System.");
					}
					catch (CodecException ce) {
						ce.printStackTrace();
					}
					catch (OntologyException oe) {
						oe.printStackTrace();
					} 
				}

			}
		}
		@Override
		public boolean done() {
			// TODO Auto-generated method stub
			return false;
		}
	}

	private class ReceiveResponse extends Behaviour {
		public ReceiveResponse (Agent a) {
			super(a);
		}

		@Override
		public void action() {
			//This behaviour should only respond to REQUEST messages
			MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.INFORM);
			ACLMessage msg = receive(mt);
			if(msg != null){
				try {
					ContentElement ce = null;
					// Let JADE convert from String to Java objects
					// Output will be a ContentElement
					ce = getContentManager().extractContent(msg);				
					if (ce instanceof Result) {
						Result rs = (Result) ce;
						TimeTableMessage message = (TimeTableMessage) rs.getAction();
						String status = (String) rs.getValue();
						// Moved to new event, remove slot/event from personal list
						if (status.equals("Success")) {
							processingSlot = false;
						}
					}
				}


				catch (CodecException ce) {
					ce.printStackTrace();
				}
				catch (OntologyException oe) {
					oe.printStackTrace();
				}

			}
			else{
				block();
			}
		}

		@Override
		public boolean done() {
			// TODO Auto-generated method stub
			return false;
		}

	}
	
	private class ReceiveRequest extends Behaviour {
		public ReceiveRequest (Agent a) {
			super(a);
		}

		@Override
		public void action() {
			//This behaviour should only respond to REQUEST messages
			MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.REQUEST);
			ACLMessage msg = receive(mt);
			if(msg != null){
				try {
					ContentElement ce = null;
					// Let JADE convert from String to Java objects
					// Output will be a ContentElement
					ce = getContentManager().extractContent(msg);				
					if (ce instanceof Result) {
						Result rs = (Result) ce;
						TimetableToStudentRequest message = (TimetableToStudentRequest) rs.getAction();
						// Calculate if the swap is good
						int utility = 0;
						int targetEvent = message.getTargetEvent();
						boolean unacceptableForOther = message.isUnacceptable();
						boolean reluctant = false;
						int slot = message.getLeaveSlot();
						if (studentInfo.getPersonality().getUnnaceptableSlots().contains(slot)) {
							utility -= 2;
						}
						else if (studentInfo.getPersonality().getAwkwardSlots().contains(slot)) {
							utility -= 1;
						}
						
						if (unacceptableForOther)
							utility++;
						
						for (SlotInfo si : evaluation.getAwkwardList()) {
							if (si.getEvent().getId() == targetEvent) {
								utility += 1;
							}
						}
						
						for (SlotInfo si : evaluation.getUnacceptableList()) {
							if (si.getEvent().getId() == targetEvent) {
								utility += 2;
							}
						}
						
					}
				}


				catch (CodecException ce) {
					ce.printStackTrace();
				}
				catch (OntologyException oe) {
					oe.printStackTrace();
				}

			}
			else{
				block();
			}
		}

		@Override
		public boolean done() {
			// TODO Auto-generated method stub
			return false;
		}

	}
	
	private Event getEventByID(int id) {
		for (StudentInEvent e : studentInfo.getEvents()) {
			if (e.getEvent().getId() == id)
				return e.getEvent();
		}
		return null;
	}
}
