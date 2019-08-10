package ontology.elements;

public class SlotInfo {
	private Integer slotID;
	private Event event;
	private boolean unacceptable;
	private boolean checked = false; 
	
	public SlotInfo() { }
	
	public SlotInfo(Integer slotID, Event event, boolean unacceptable) {
		this.slotID = slotID;
		this.event = event;
		this.unacceptable = unacceptable;
	}
	
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

	public boolean isChecked() {
		return checked;
	}

	public void setHasBeenChecked(boolean checked) {
		this.checked = checked;
	}
	
}
