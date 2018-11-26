package toStringHelpers;

import entities.TTSlot;

public class SlotToString implements ObjectToString
{
	private TTSlot slot;
	
	public SlotToString(TTSlot slot)
	{
		this.slot = slot;
	}

	public String stringify()
	{
		String result = String.format("Slot:\t\t%1$s[Id]"
				+ " %2$s[Day] %3$s[Start] %4$s[End] %5$s[Is Used]\n", 
				slot.getId(), slot.getDay(), slot.getStartTime(), slot.getEndTime(), slot.isUsed());
		return result;
	}
}
