package ontology.elements;

import java.util.ArrayList;

public class Personality {
	private ArrayList<Integer> unacceptableSlots = new ArrayList<Integer>();
	private ArrayList<Integer> awkwardSlots = new ArrayList<Integer>();
	
	public void addUnacceptable(int slot) {
		unacceptableSlots.add(slot);
	}

	
	
	public void addAwkward(int slot) {
		awkwardSlots.add(slot);
	}

	public ArrayList<Integer> getUnnaceptableSlots(){
		return unacceptableSlots;
	}
	
	public ArrayList<Integer> getAwkwardSlots(){
		return this.awkwardSlots;
	}
	
}