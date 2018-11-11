package entities;

import java.util.ArrayList;
import java.util.List;
import toStringHelpers.ObjectToString;
import toStringHelpers.ProgrammeToString;

public class Programme
{
	private String name;
	private List<Module> modules;
	private ObjectToString progToString;
	
	public String getName()
	{
		return name;
	}
	public List<Module> getModules()
	{
		return modules;
	}
	
	public void setName(String name)
	{
		this.name = name;
	}
	public void setModules(List<Module> modules)
	{
		this.modules = modules;
	}
	
	public Programme()
	{
		this("");
	}
	public Programme(String name)
	{
		setName(name);
		setModules(new ArrayList<Module>());
		progToString = new ProgrammeToString(this);
	}
	
	@Override
	public String toString()
	{
		return progToString.stringify();
	}
}
