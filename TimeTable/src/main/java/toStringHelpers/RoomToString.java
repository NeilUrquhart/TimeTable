package toStringHelpers;

import ontology.elements.Room;

public class RoomToString implements ObjectToString
{
	private Room room;
	
	public RoomToString(Room room)
	{
		this.room = room;
	}
	
	public String stringify()
	{
		String result = String.format("\tRoom Details"
				+ "\nName:\t\t%1$s"
				+ "\nCapacity:\t%2$s"
				+ "\n\n\tEvents In Room"
				+ "\n%3$s", 
				room.getName(), room.getCapacity(), 
				getEventsAsString());
		return result;
	}
	
	private String getEventsAsString()
	{
		String result = EventToString.getEventsAsString(room.getEvents());
		if(result == "")
		{
			result = "No Events In Room!";
		}
		return result;
	}
}
