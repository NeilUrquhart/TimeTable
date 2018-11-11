package toStringHelpers;

import entities.Programme;

public class ProgrammeToString implements ObjectToString
{
	private Programme programme;
	
	public ProgrammeToString(Programme programme)
	{
		this.programme = programme;
	}
	
	@Override
	public String stringify()
	{
		String result = String.format("\tProgramme Details"
				+ "\nName:\t\t%1$s"
				+ "\n%2$s", 
				programme.getName(), getModulesAsString());
		return result;
	}
	
	private String getModulesAsString()
	{
		String result = ModuleToString.getModuleAsString(programme.getModules());
		if(result.equals(""))
		{
			result = "No Modules in Programme";
		}
		return result;
	}

}
