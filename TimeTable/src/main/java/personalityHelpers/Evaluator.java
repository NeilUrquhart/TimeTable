package personalityHelpers;

import java.util.ArrayList;
import java.util.List;

import ontology.elements.StudentInEvent;
import ontology.elements.Personality;

public class Evaluator {

	private ArrayList<Integer> unacceptable = new ArrayList<Integer>(); 
	private ArrayList<Integer> awkward = new ArrayList<Integer>(); 

	public boolean hasUnnacceptable() {
		if (unacceptable.size() > 0)
			return true;
		else
			return false;
	}

	public void evaluate(Personality p, List<StudentInEvent> events) {

		unacceptable.clear();
		awkward.clear();

		System.out.println("Timetable evaluation");
		//Check for unacceptable slots
		for (int slot : p.getUnnaceptableSlots()) {
			for (int i = 0; i < events.size(); i++) {
				if (slot  == events.get(i).getEvent().getId()) {
					System.out.println("Unacceptable slot int use - slot "+slot);
					unacceptable.add(slot);
				}	
			}
		}

		for (int slot : p.getAwkwardSlots()) {
			for (int i = 0; i < events.size(); i++) {
				if (slot  == events.get(i).getEvent().getId()) {
					System.out.println("Awkward slot int use - slot "+slot);
					awkward.add(slot);
				}
			}
		}

	}
}