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
import ontology.elements.StudentToTimetableRequestSwap;
import ontology.elements.TTSlot;
import ontology.elements.TimeTableMessage;
import ontology.elements.StudentToStudentRequest;
import returnCodes.ResponseMove;


public class TimeTablingSystem extends Agent {
	private Codec codec = new SLCodec();
	private Ontology ontology = TimeTableOntology.getInstance();
	private FacadeController facade;
	private HashMap<String, AID> studentList = new HashMap<String, AID>();
	private boolean verbose = true;

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
					studentList.put(agent.getName().getLocalName(), agent.getName());
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
						//This is the request after the consent for a swap has happened
						if (ac.getAction() instanceof StudentToTimetableRequestSwap) {
							StudentToTimetableRequestSwap request = (StudentToTimetableRequestSwap) ac.getAction();
							ResponseMove result = facade.swapStudentsBetweenEvents(facade.getStudentByMatric(request.getLeaveStudentMatric())
									, facade.getEventById(request.getLeaveEvent()), facade.getStudentByMatric(request.getTargetStudentMatric())
									, facade.getEventById(request.getTargetEvent()));
							if (result == ResponseMove.OK) {
								// The target event is matched with the leaving student because of the event swap
								sendResponse(result, "Success Swap", msg, request.getLeaveEvent(), request.getTargetEvent(), null, null, studentList.get(request.getTargetStudentMatric()));
								// Also update the other student about the swap
								sendResponse(result, "Success Swap", msg, request.getTargetEvent(), request.getLeaveEvent(), null, null, studentList.get(request.getLeaveStudentMatric()));
								if (verbose) {
									System.out.println("Timetable successfully swapped student " + 
											msg.getSender().getLocalName() + 
											" with student " + request.getTargetStudentMatric());
								}
							}
							else if (result == ResponseMove.NOT_IN_OLD_EVENT) {
								sendResponse(result, "Not In Old Event", msg, null, null, null, null, null);
								if (verbose) {
									System.out.println("Timetable unsuccessfully swapped student " + 
											msg.getSender().getLocalName() + 
											" with student " + request.getTargetStudentMatric() + " because of " + result);
								}
							}
							else {
								sendResponse(result, "Failed Swap", msg, null, null, null, null, null);
								if (verbose) {
									System.out.println("Timetable unsuccessfully swapped student " + 
											msg.getSender().getLocalName() + 
											" with student " + request.getTargetStudentMatric() + " because of " + result);
								}
							}
						}
						// This is the standard request
						else if (ac.getAction() instanceof StudentToTimetableRequest) {
							StudentToTimetableRequest request = (StudentToTimetableRequest) ac.getAction();
							Student student = facade.getStudentByMatric(request.getStudent());
							Event LeaveEvent = facade.getEventById(request.getLeaveEvent());
							ArrayList<Event> compatibleEvents = facade.getCompatibleEvents(LeaveEvent, student.getEvents());
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
										sendResponse(result, "Success", msg, null, null, null, null, null);
										if (verbose)
											System.out.println("Moved Student " + msg.getSender().getLocalName() +
													" from event " + LeaveEvent.getId() + " to event " + e.getId());
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
											if (!s.getStudent().getMatric().equals(student.getMatric())) {
												listOfStudentsToContact.add(studentList.get(s.getStudent().getMatric()));
												listOfEvents.add(e.getId());
											}
										}
									}
									// Send Reply
									if (verbose)
										System.out.println("Could not move student " + msg.getSender().getLocalName() +
												" sending list of potential students to contact.");
									sendResponse(result, "No Room", msg, null, null, listOfStudentsToContact, listOfEvents, null);
									return;
								}
								else if (result == ResponseMove.NOT_IN_OLD_EVENT) {
									sendResponse(result, "Not In Old Event", msg, null, null, null, null, null);
									return;
								}
								else {
									// For some reason it has failed, inform the student
									// This shouldn't ever trigger but I'll leave it just in case
									sendResponse(result, "Critically Failed", msg, null, null, null, null, null);
									return;
								}
							}
							// If the student is leaving an unacceptable event and the only option left is awkward do the transfer
							// This is a very rare case of it happening so there will be a binary decision of moving only if there is space
							else if (request.getPersonality().getAwkwardSlots().contains(request.getSlot()) 
									&& compatibleEventsPrunedFull.isEmpty() && !compatibleEventsPrunedUnacceptable.isEmpty()) {
								for (Event e : compatibleEventsPrunedUnacceptable) {
									result = facade.moveStudentToNewEvent(student, e, LeaveEvent);
									if (result == ResponseMove.OK) {
										sendResponse(result, "Success Caveat", msg, e.getId(), null, null, null, null);
										return;
									}
									else {
										// Inform that the change has failed
										sendResponse(result, "Failed", msg, null, null, null, null, null);
										return;
									}
	
								}
								
							}
							// Otherwise just say that there is no option to switch
							else {
								// Inform that the change has failed
								sendResponse(result, "Failed", msg, null, null, null, null, null);
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
	
	private void sendResponse(ResponseMove result, String value, ACLMessage msg, Integer caveatEvent, Integer caveatEvent2, ArrayList<AID> students, ArrayList<Integer> events, AID targetStudent) {
		// Create response
		TimeTableMessage ttm = new TimeTableMessage();
		ttm.setResponse(result);
		// Only if the student has to move to an event that is awkward for him
		if (caveatEvent != null) {
			ttm.setCaveatEvent(caveatEvent);
		}
		
		if (caveatEvent2 != null) {
			ttm.setCaveatEvent2(caveatEvent2);
		}
		// Only if there are no rooms available
		if (students != null && events != null) {
			// Transform into arrays and send them back
			ttm.setStudentList(students.toArray());
			ttm.setTargetEvents(events.toArray());
		}
		ACLMessage msg2 = msg.createReply();
		if (targetStudent != null) {
			ttm.setTargetStudent(targetStudent);
			msg2.addReceiver(targetStudent);
		}
		
		msg2.setPerformative(ACLMessage.INFORM);
		Result rs = new Result();
		rs.setAction(ttm);
		rs.setValue(value);
		try {
			getContentManager().fillContent(msg2, rs);
		} catch (CodecException | OntologyException e) {
			e.printStackTrace();
		}
		send(msg2);
	}
}