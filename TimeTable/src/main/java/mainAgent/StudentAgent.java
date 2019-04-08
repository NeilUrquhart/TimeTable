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

public class StudentAgent extends Agent {
	private Codec codec = new SLCodec();
	private Ontology ontology = TimeTableOntology.getInstance();
	private AID timeTableAID;
	private Student studentInfo;
	
	protected void setup() {

		// Register the auction service
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
		Student student = (Student) getArguments()[0];
		studentInfo = student;

		// Register language and Ontology
		getContentManager().registerLanguage(codec);
		getContentManager().registerOntology(ontology);

	}

}
