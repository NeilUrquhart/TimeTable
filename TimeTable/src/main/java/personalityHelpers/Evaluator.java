package personalityHelpers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ontology.elements.StudentInEvent;
import ontology.elements.TTSlot;
import ontology.elements.Event;
import ontology.elements.Personality;
import ontology.elements.SlotInfo;

public class Evaluator {

	private ArrayList<SlotInfo> unacceptable = new ArrayList<>(); 
	private ArrayList<SlotInfo> awkward = new ArrayList<>(); 

	public boolean hasUnnacceptable() {
		if (unacceptable.size() > 0)
			return true;
		else
			return false;
	}
	
	public boolean hasAwkard() {
		if (awkward.size() > 0)
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
			for (StudentInEvent e : events) {
				for (TTSlot ttslot : e.getEvent().getSlots())
				if (slot  == ttslot.getId()) {
					System.out.println("Unacceptable slot int use - slot "+slot);
					SlotInfo sl = new SlotInfo();
					sl.setSlotID(slot);
					sl.setEvent(e.getEvent());
					unacceptable.add(sl);
				}	
			}
		}

		for (int slot : p.getAwkwardSlots()) {
			for (StudentInEvent e : events) {
				for (TTSlot ttslot : e.getEvent().getSlots()) {
					if (slot  == ttslot.getId()  && !unacceptable.contains(slot)) {
						System.out.println("Awkward slot int use - slot "+slot);
						SlotInfo sl = new SlotInfo();
						sl.setSlotID(slot);
						sl.setEvent(e.getEvent());
						awkward.add(sl);
					}	
				}
			}
		}

	}
	
	public SlotInfo getUnacceptable(){
		SlotInfo slot = unacceptable.remove(0);
		return  slot;
	}

	public SlotInfo getAwkward() {
		SlotInfo slot = awkward.remove(0);
		return  slot;
	}
	
	public ArrayList<SlotInfo> getUnacceptableList(){
		return  unacceptable;
	}

	public ArrayList<SlotInfo> getAwkwardList() {
		return  awkward;
	}

	public void setUnacceptable(ArrayList<SlotInfo> unacceptable) {
		this.unacceptable = unacceptable;
	}

	public void setAwkward(ArrayList<SlotInfo> awkward) {
		this.awkward = awkward;
	}
}