package mainAgent;

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
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import ontology.TimeTableOntology;
import ontology.elements.Event;
import ontology.elements.Student;
import ontology.elements.StudentRequest;
import ontology.elements.TimeTableMessage;
import returnCodes.ResponseMove;


public class TimeTablingSystem extends Agent {
	private Codec codec = new SLCodec();
	private Ontology ontology = TimeTableOntology.getInstance();
	private FacadeController facade;

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

		this.addBehaviour(new ReceiveRequests(this));
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
						if (ac.getAction() instanceof StudentRequest) {
							StudentRequest request = (StudentRequest) ac.getAction();
							Student student = facade.getStudentByMatric(request.getStudent());
							Event targetEvent = facade.getEventById(request.getTargetEvent());
							Event LeaveEvent = facade.getEventById(request.getLeaveEvent());
							ResponseMove result = facade.moveStudentToNewEvent(student, targetEvent, LeaveEvent);
							
							// Create response
							TimeTableMessage ttm = new TimeTableMessage();
							ttm.setResponse(result);
							Result rs = new Result();
							rs.setAction(ttm);
							rs.setValue("TimeTable Response");
							ACLMessage msg2 = msg.createReply();
							msg2.setPerformative(ACLMessage.INFORM);
							getContentManager().fillContent(msg2, rs);
							send(msg2);

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
}