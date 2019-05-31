package ontology.elements;

public class SlotInfo {
	private Integer slotID;
	private Event event;
	private boolean unacceptable;
	
	public boolean isUnacceptable() {
		return unacceptable;
	}
	public void setUnacceptable(boolean unacceptable) {
		this.unacceptable = unacceptable;
	}
	public Integer getSlotID() {
		return slotID;
	}
	public void setSlotID(Integer slotID) {
		this.slotID = slotID;
	}
	public Event getEvent() {
		return event;
	}
	public void setEvent(Event event) {
		this.event = event;
	}
	
}
