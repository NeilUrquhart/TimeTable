package toStringHelpers;

import entities.Module;

public class ModuleToString implements ObjectToString
{
	private Module module;
	
	public ModuleToString(Module module)
	{
		this.module = module;
	}
	
	public String stringify()
	{
		String result = String.format("\tModule Details"
				+ "\nName:\t\t%1$s"
				+ "\nDescription:\t%2$s"
				+ "\n\n\tEvents In Module"
				+ "\n%3$s", 
				module.getName(), module.getDescription(), getEventsAsString());
		return result;
	}
	
	private String getEventsAsString()
	{
		String result = EventToString.getEventsAsString(module.getEvents());
		if(result == "")
		{
			result = "No Events In Module";
		}
		return result;
	}

}
