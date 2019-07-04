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

	public void evaluate(Personality p, List<StudentInEvent> events, boolean verbose, String matric) {

		unacceptable.clear();
		awkward.clear();
		if (verbose)
			System.out.println("Student " + matric + " is evaluating his/her timetable.");
		//Check for unacceptable slots
		for (int slot : p.getUnnaceptableSlots()) {
			for (StudentInEvent e : events) {
				for (TTSlot ttslot : e.getEvent().getSlots())
				if (slot  == ttslot.getId()) {
					if (verbose)
						System.out.println("Unacceptable slot in use - slot " + slot + " for student " + matric);
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
						if (verbose)
							System.out.println("Awkward slot in use - slot " + slot + " for student " + matric);
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
		SlotInfo slot = unacceptable.get(0);
		slot.setUnacceptable(true);
		return  slot;
	}

	public SlotInfo getAwkward() {
		SlotInfo slot = awkward.get(0);
		slot.setUnacceptable(false);
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
	
	public ArrayList<SlotInfo> getFullList() {
		ArrayList<SlotInfo> temp = unacceptable;
		temp.addAll(awkward);
		return temp;
	}
	
	public void reAddSlot(SlotInfo currentSlot) {
		// First remove the slot then re-add to the end of the list
		removeSlotInfo(currentSlot);
		if (currentSlot.isUnacceptable()) {
			unacceptable.add(currentSlot);
		} else {
			awkward.add(currentSlot);
		}		
	}

	public void removeSlotInfo(SlotInfo si) {
		for (int i = 0; i < unacceptable.size(); i++) {
			if (unacceptable.get(i).getSlotID() == si.getSlotID() && unacceptable.get(i).getEvent().getId() == si.getEvent().getId()) {
				unacceptable.remove(i);
			}
		}
		
		for (int i = 0; i < awkward.size(); i++) {
			if (awkward.get(i).getSlotID() == si.getSlotID() && awkward.get(i).getEvent().getId() == si.getEvent().getId()) {
				awkward.remove(i);
			}
		}
		
	}
}