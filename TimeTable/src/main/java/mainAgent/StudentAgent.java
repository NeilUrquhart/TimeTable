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
import java.util.Collections;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.List;

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
	private int noOfExpectedResponses;
	private int noOfResponses;
	private ArrayList<StudentToStudentRequest> consentingStudentList;
	private List<Event> allEvents;
	private boolean verbose = true;

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
		allEvents = (List<Event>) getArguments()[1];
		studentRequest.setStudent(studentInfo.getMatric());
		studentRequest.setPersonality(studentInfo.getPersonality());
		// Check if you are in a non-compatible event
		evaluation.evaluate(studentInfo.getPersonality(), studentInfo.getEvents(), verbose, studentInfo.getMatric());


		// Register language and Ontology
		getContentManager().registerLanguage(codec);
		getContentManager().registerOntology(ontology);

		this.addBehaviour(new FindTimeTable(this));

		doWait(1000);
		this.addBehaviour(new SendMessage(this));
		this.addBehaviour(new ReceiveResponse(this));
		this.addBehaviour(new ReceiveRequest(this));
		this.addBehaviour(new ReceiveConsent(this));
		this.addBehaviour(new ReceiveTermination(this));
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
			if (!processingSlot && (evaluation.hasUnnacceptable() || evaluation.hasAwkard())) {
				// Check if all the slots on the list have been checked and if so inform the timetabling system
				try {
					checkSlotsChecked();
				} catch (CodecException | OntologyException e) {
					e.printStackTrace();
				}
				
				if (verbose) {
					System.out.println("Student " + studentInfo.getMatric() + " still has " + evaluation.getFullList().size() + " slot(s) to take care of.");
				}
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
					if (verbose)
						System.out.println("Student " + studentInfo.getMatric() + " sent a message to the Time Tabling System.");
				}
				catch (CodecException ce) {
					ce.printStackTrace();
				}
				catch (OntologyException oe) {
					oe.printStackTrace();
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
							evaluation.removeSlotInfo(currentSlot);
							processingSlot = false;
							if (verbose)
								System.out.println("Student " + studentInfo.getMatric() + " has succesfully dealt with the following slot " + currentSlot.getSlotID());
							checkSlotsChecked();
							return;
						}
						else if  (status.equals("Success Swap")) {
							processingSlot = false;
							System.out.println(message.getTargetStudent());
							// Check if the student has to add an awkward slot
							Event caveatEvent = getEventByID(message.getCaveatEvent());
							for (TTSlot slot : caveatEvent.getSlots())
								if (studentInfo.getPersonality().getAwkwardSlots().contains(slot.getId())) {
									evaluation.reAddSlot(new SlotInfo(slot.getId(), caveatEvent, false));
									if (verbose) {
										System.out.println("Student " + studentInfo.getMatric() + " was moved to another awkward slot.");
									}
									break;
								}

							// Check if the student was moved away from an awkward/unacceptable slot
							for (SlotInfo si : evaluation.getFullList()) {
								if (si.getEvent().getId() == message.getCaveatEvent2()) {
									evaluation.removeSlotInfo(si);
									if (verbose)
										System.out.println("Student " + studentInfo.getMatric() + " left unacceptable/awkward slot " + si.getSlotID());
									checkSlotsChecked();
									break;
								}
							}
						}
						else if (status.equals("No Room")) {
							// Grab the elements
							ArrayList<AID> studentList = new ArrayList<AID>();
							ArrayList<Integer> eventList = new ArrayList<Integer>();
							updateStudentList(studentList, message.getStudentList());
							updateEventList(eventList, message.getTargetEvents());
							noOfExpectedResponses = studentList.size();
							noOfResponses = 0;
							if (consentingStudentList == null)
								consentingStudentList = new ArrayList<StudentToStudentRequest>();
							else 
								consentingStudentList.clear();
							// Send a message to each of the 
							for (int i = 0; i < studentList.size(); i++) {
								// Prepare the REQUEST message
								StudentToStudentRequest stsr = new StudentToStudentRequest();
								stsr.setTargetEvent(eventList.get(i));
								stsr.setLeaveSlot(currentSlot.getSlotID());
								ACLMessage msg2 = new ACLMessage(ACLMessage.PROPOSE);
								msg2.addReceiver(studentList.get(i));
								msg2.setLanguage(codec.getName());
								msg2.setOntology(ontology.getName()); 
								Action request = new Action();
								request.setAction(stsr);
								request.setActor(studentList.get(i));
								getContentManager().fillContent(msg2, request);
								send(msg2);
								if (verbose) {
									int index = studentList.get(i).getName().indexOf('@');
									System.out.println("Student " + studentInfo.getMatric() + " sent a message to the Student with proposal to " + studentList.get(i).getName().substring(0,  index));
								}

							}
						}
						else if (status.equals("Not In Old Event")) {
							processingSlot = false;
						}
						else {
							evaluation.reAddSlot(currentSlot);
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
			MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.PROPOSE);
			ACLMessage msg = receive(mt);
			if(msg != null){
				try {
					ContentElement ce = null;
					// Let JADE convert from String to Java objects
					// Output will be a ContentElement
					ce = getContentManager().extractContent(msg);
					if (ce instanceof Action) {
						Action ac = (Action) ce;
						if (ac.getAction() instanceof StudentToStudentRequest) {
							StudentToStudentRequest message = (StudentToStudentRequest) ac.getAction();
							// Calculate if the swap is good
							int utility;
							int targetEvent = message.getTargetEvent();
							boolean unacceptableForOther = message.isUnacceptable();
							int slot = message.getLeaveSlot();
							utility = calculateUtility(slot, unacceptableForOther, targetEvent);
							if (verbose) {
								int index = msg.getSender().getName().indexOf('@');								
								System.out.println("Student " + studentInfo.getMatric() +
										" has received and calculated event swap utility from Student " + msg.getSender().getName().substring(0, index) +
										" which is: " + utility);
							}
							if (utility >= 0)
							{
								// Consent to the swap
								Result rs = new Result();
								message.setConsentingStudentMatric(studentInfo.getMatric());
								message.setUtility(utility);
								rs.setAction(message);
								rs.setValue("Consent");
								ACLMessage msg2 = msg.createReply();
								msg2.setPerformative(ACLMessage.CONFIRM);
								try {
									getContentManager().fillContent(msg2, rs);
								} catch (CodecException | OntologyException e) {
									e.printStackTrace();
								}
								send(msg2);
								if (verbose) {						
									System.out.println("Student " + studentInfo.getMatric() +
											" consented to event swap from Student " + msg.getSender().getLocalName());
								}
							}
							else
							{
								// Don't Consent to the swap
								Result rs = new Result();
								rs.setAction(message);
								rs.setValue("No Consent");
								ACLMessage msg2 = msg.createReply();
								msg2.setPerformative(ACLMessage.DISCONFIRM);
								try {
									getContentManager().fillContent(msg2, rs);
								} catch (CodecException | OntologyException e) {
									e.printStackTrace();
								}
								send(msg2);
								if (verbose) {
									int index = msg.getSender().getName().indexOf('@');								
									System.out.println("Student " + studentInfo.getMatric() +
											" didn't consent to event swap from Student " + msg.getSender().getName().substring(0, index));
								}
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

	private class ReceiveConsent extends Behaviour {
		public ReceiveConsent (Agent a) {
			super(a);
		}

		@Override
		public void action() {
			MessageTemplate mt = MessageTemplate.or(MessageTemplate.MatchPerformative(ACLMessage.CONFIRM),
					MessageTemplate.MatchPerformative(ACLMessage.DISCONFIRM));
			ACLMessage msg = receive(mt);
			if(msg != null){
				try {
					ContentElement ce = null;
					// Let JADE convert from String to Java objects
					// Output will be a ContentElement
					ce = getContentManager().extractContent(msg);
					if (ce instanceof Result) {
						Result rs = (Result) ce;
						if (rs.getAction() instanceof StudentToStudentRequest) {
							StudentToStudentRequest message = (StudentToStudentRequest) rs.getAction();
							if (rs.getValue().equals("Consent")) {
								// Add to list of responses
								consentingStudentList.add(message);								
							}
							noOfResponses++;
							// If you have received all responses then message the timetabling for the swap
							if (noOfResponses == noOfExpectedResponses) {
								if (consentingStudentList.size() == 0) {
									evaluation.reAddSlot(currentSlot);
									processingSlot = false;
									return;
								}
								else {
									// Sort the list and pick the one with the highest utility
									Collections.sort(consentingStudentList, new Comparator<StudentToStudentRequest>() {
										public int compare(StudentToStudentRequest c1, StudentToStudentRequest c2) {
											if (c1.getUtility() > c2.getUtility()) return -1;
											if (c1.getUtility() < c2.getUtility()) return 1;
											return 0;
										}});
									StudentToStudentRequest bestStudent = consentingStudentList.get(0);
									for (StudentToStudentRequest nextStudent : consentingStudentList) {
										if (nextStudent.getUtility() > bestStudent.getUtility())
											bestStudent =  nextStudent;
									}
									// Send message to timetabling system demanding a swap
									StudentToTimetableRequestSwap stsrs = new StudentToTimetableRequestSwap();
									stsrs.setLeaveEvent(currentSlot.getEvent().getId());
									stsrs.setTargetEvent(bestStudent.getTargetEvent());
									stsrs.setLeaveStudentMatric(studentInfo.getMatric());
									stsrs.setTargetStudentMatric(bestStudent.getConsentingStudentMatric());
									ACLMessage msg2 = new ACLMessage(ACLMessage.REQUEST);
									msg2.addReceiver(timeTableAID);
									msg2.setLanguage(codec.getName());
									msg2.setOntology(ontology.getName()); 
									Action request = new Action();
									request.setAction(stsrs);
									request.setActor(timeTableAID);
									getContentManager().fillContent(msg2, request);
									send(msg2);
									if (verbose)
										System.out.println("Student " + studentInfo.getMatric() + " sent a swap request to the Timetabling System.");
								}
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

	private class ReceiveTermination extends Behaviour {

		public ReceiveTermination(Agent a) {
			super(a);
		}

		@Override
		public void action() {
			MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.CANCEL);
			ACLMessage msg = receive(mt);
			if(msg != null){
				myAgent.doDelete();
			}			
		}

		@Override
		public boolean done() {
			// TODO Auto-generated method stub
			return false;
		}


	}

	
	private Event getEventByID(int id) {
		for (Event e : allEvents) {
			if (e.getId() == id)
				return e;
		}
		return null;
	}

	private void updateStudentList(ArrayList<AID>studentList, Object[] studentObjectList) {
		for (int i = 0; i < studentObjectList.length; i++) {
			studentList.add((AID) studentObjectList[i]);
		}
	}

	private void updateEventList(ArrayList<Integer>studentList, Object[] eventObjectList) {
		for (int i = 0; i < eventObjectList.length; i++) {
			// I DON'T NO WHY BUT IT HAS TO BE CONVERTED TO A LONG BEFORE IT BECOMES A INT??? THANKS JAVA!!!
			long preInt = (long) eventObjectList[i];
			studentList.add((int) preInt);
		}
	}

	private int calculateUtility(int slot, boolean unacceptableForOther, int targetEvent) {
		int utility = 0;
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

		return utility;
	}
	
	private void  checkSlotsChecked() throws CodecException, OntologyException {
		boolean allChecked = true;
		for (SlotInfo si : evaluation.getFullList())
			allChecked = si.isChecked();
		
		if (allChecked) {
			// Message the Timetabling agent that you are done. It is set to request to differentiate this message
			if (verbose) {
				System.out.println("Student " + getLocalName() + " is done.");
			}
			ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
			msg.setLanguage(codec.getName());
			msg.setOntology(ontology.getName()); 
    		msg.addReceiver(timeTableAID);
    		Action request = new Action();
    		// The action here is completely unnecessary but I can't get the timetabling system to read the
    		// message sender without implementing some sort of action
			request.setAction(new StudentToTimetableRequestSwap());
			request.setActor(timeTableAID);
			getContentManager().fillContent(msg, request);
			send(msg);
		}
	}
}
