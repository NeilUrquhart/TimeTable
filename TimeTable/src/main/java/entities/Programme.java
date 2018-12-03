package entities;

import java.util.ArrayList;
import java.util.List;

import jade.content.Concept;
import toStringHelpers.ObjectToString;
import toStringHelpers.ProgrammeToString;

public class Programme implements Concept
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
	
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((modules == null) ? 0 : modules.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Programme other = (Programme) obj;
		if (modules == null)
		{
			if (other.modules != null)
				return false;
		} else if (!modules.equals(other.modules))
			return false;
		if (name == null)
		{
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
}
