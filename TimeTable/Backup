package mainAgent;

import java.util.ArrayList;
import java.util.HashMap;

import controllers.FacadeController;
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
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import ontology.TimeTableOntology;
import ontology.elements.Event;
import ontology.elements.Student;
import ontology.elements.StudentInEvent;
import ontology.elements.StudentToTimetableRequest;
import ontology.elements.TTSlot;
import ontology.elements.TimeTableMessage;
import ontology.elements.TimetableToStudentRequest;
import returnCodes.ResponseMove;


public class TimeTablingSystem extends Agent {
	private Codec codec = new SLCodec();
	private Ontology ontology = TimeTableOntology.getInstance();
	private FacadeController facade;
	private HashMap<String, AID> studentList = new HashMap<String, AID>();

	protected void setup() {
		DFAgentDescription dfd = new DFAgentDescription();
		dfd.setName(getAID());
		ServiceDescription sd = new ServiceDescription();
		sd.setType("timetable");
		sd.setName(getLocalName());
		dfd.addServices(sd);
		try {
			DFService.register(this, dfd);
		}
		catch (FIPAException fe) {
			fe.printStackTrace();
		}
		facade = (FacadeController) getArguments()[0];

		// Register language and Ontology
		getContentManager().registerLanguage(codec);
		getContentManager().registerOntology(ontology);

		doWait(500);
		this.addBehaviour(new FindStudents(this));
		this.addBehaviour(new ReceiveRequests(this));
	}
	
	
	private class FindStudents extends OneShotBehaviour {

		public FindStudents(Agent a) {
			super(a);
		}

		@Override
		public void action() {
			DFAgentDescription template = new DFAgentDescription();
			ServiceDescription sd = new ServiceDescription();
			sd.setType("student");
			template.addServices(sd);
			try{
				DFAgentDescription[] agentsType1  = DFService.search(myAgent,template); 
				for (DFAgentDescription agent : agentsType1) {
					int index = agent.getName().getName().indexOf('@');
					studentList.put(agent.getName().getName().substring(0, index), agent.getName());
				}
			}
			catch(FIPAException e) {
				e.printStackTrace();
			}
		}

	}

	private class ReceiveRequests extends Behaviour {
		public ReceiveRequests (Agent a) {
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
					if (ce instanceof Action) {
						Action ac = (Action) ce;
						if (ac.getAction() instanceof StudentToTimetableRequest) {
							StudentToTimetableRequest request = (StudentToTimetableRequest) ac.getAction();
							Student student = facade.getStudentByMatric(request.getStudent());
							Event LeaveEvent = facade.getEventById(request.getLeaveEvent());
							ArrayList<Event> compatibleEvents = facade.getCompatibleEvents(LeaveEvent);
							ArrayList<Event> compatibleEventsPrunedUnacceptable = pruneEvent(compatibleEvents, request.getPersonality().getUnnaceptableSlots());
							ArrayList<Event> compatibleEventsPrunedFull = pruneEvent(compatibleEventsPrunedUnacceptable, request.getPersonality().getAwkwardSlots());
							// This list is if all the potential rooms are full and list of events has to be sent back to the student
							ArrayList<Event> noRoomEvents = new ArrayList<Event>();
							/* This next part is crucial the system will do the following
							 * Check what events are available compatible with the preferences of the student
							 * Execute the correct response depending on the pruning on the Event list
							 */
							
							// If the fully pruned list has options check if the Event switch is possible
							ResponseMove result = null;
							if (!compatibleEventsPrunedFull.isEmpty()) {
								for (Event e : compatibleEventsPrunedFull) {
									result = facade.moveStudentToNewEvent(student, e, LeaveEvent);
									if (result == ResponseMove.OK) {
										sendResponse(result, "Success", msg, null);
										return;
									}
									else if (result == ResponseMove.NO_ROOM_AVAILABLE) {
										noRoomEvents.add(e);
									}
								}
								//
								if (!noRoomEvents.isEmpty()) {
									// Send a message to the student with list of students to contact for a swap
									ArrayList<AID> listOfStudentsToContact = new ArrayList<AID>();
									ArrayList<Integer> listOfEvents = new ArrayList<Integer>();
									// Get AIDs
									for (Event e : noRoomEvents) {
										for (StudentInEvent s : e.getStudents()) {
											listOfStudentsToContact.add(studentList.get(s.getStudent().getMatric()));
											listOfEvents.add(e.getId());
										}
									}
									// Send Message to Specific Students
									boolean unacceptable;
									if (request.getPersonality().getUnnaceptableSlots().contains(request.getSlot()))
										unacceptable = true;
									else
										unacceptable = false;
									for (int i = 0; i < listOfStudentsToContact.size(); i++) {
										sendRequest(listOfStudentsToContact.get(i), request.getSlot(), listOfEvents.get(i), unacceptable);
									}
									return;
								}
								else {
									// For some reason it has failed, inform the student
									// This shouldn't ever trigger but I'll leave it just in case
									sendResponse(result, "Critically Failed", msg, null);
									return;
								}
							}
							// If the student is leaving an unacceptable event and the only option left is awkward do the transfer
							// This is a very rare case of it happening so there will be a binary decision of move only if there is space
							else if (request.getPersonality().getAwkwardSlots().contains(request.getSlot()) 
									&& compatibleEventsPrunedFull.isEmpty() && !compatibleEventsPrunedUnacceptable.isEmpty()) {
								for (Event e : compatibleEventsPrunedUnacceptable) {
									result = facade.moveStudentToNewEvent(student, e, LeaveEvent);
									if (result == ResponseMove.OK) {
										sendResponse(result, "Success Caveat", msg, e);
										return;
									}
									else {
										// Inform that the change has failed
										sendResponse(result, "Failed", msg, null);
										return;
									}
	
								}
								
							}
							// Otherwise just say that there is no option to switch
							else {
								// Inform that the change has failed
								sendResponse(result, "Failed", msg, null);
								return;
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
	
	private ArrayList<Event> pruneEvent(ArrayList<Event> toBePruned, ArrayList<Integer> slots) {
		
		for (int slt : slots) {
			facade.pruneEvents(toBePruned, slt);
		}
		return toBePruned;
		
	}
	
	private void sendResponse(ResponseMove result, String value, ACLMessage msg, Event caveatEvent) {
		// Create response
		TimeTableMessage ttm = new TimeTableMessage();
		ttm.setResponse(result);
		// Only if the student has to move to an event that is awkward for him
		if (caveatEvent != null) {
			ttm.setCaveatEvent(caveatEvent);
		}
		Result rs = new Result();
		rs.setAction(ttm);
		rs.setValue(value);
		ACLMessage msg2 = msg.createReply();
		msg2.setPerformative(ACLMessage.INFORM);
		try {
			getContentManager().fillContent(msg2, rs);
		} catch (CodecException | OntologyException e) {
			e.printStackTrace();
		}
		send(msg2);
	}
	
	private void sendRequest(AID student, int leaveSlot, int targetEvent, boolean unacceptable) {
		TimetableToStudentRequest ttsr = new TimetableToStudentRequest();
		ttsr.setLeaveSlot(leaveSlot);
		ttsr.setTargetEvent(targetEvent);
		ttsr.setUnacceptable(unacceptable);
		ACLMessage msg2 = new ACLMessage(ACLMessage.REQUEST);
		msg2.addReceiver(student);
		System.out.println(student);
		msg2.setLanguage(codec.getName());
		msg2.setOntology(ontology.getName()); 
		Action request = new Action();
		request.setAction(ttsr);
		request.setActor(student);

		try {
			// Let JADE convert from Java objects to string
			getContentManager().fillContent(msg2, request);
			send(msg2);
			System.out.println("Sent Message");
		}
		catch (CodecException ce) {
			ce.printStackTrace();
		}
		catch (OntologyException oe) {
			oe.printStackTrace();
		}
	}
}