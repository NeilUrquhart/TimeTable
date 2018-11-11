package dao;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;

import entities.Module;
import entities.Programme;

public class ProgrammeCsvReader
{
	private String filePath;
	private String line;
	private String csvSplitOn;
	private String moduleSplitOn;
	
	public ProgrammeCsvReader()
	{
		filePath = "programmes.csv";
		line = "";
		csvSplitOn = ",";
		moduleSplitOn = ";";
	}
	
	public Map<String, Programme> read()
	{
		Map<String, Programme> result  = new HashMap<String, Programme>();
		ClassLoader classLoader = getClass().getClassLoader();
		File progFile = new File(classLoader.getResource(filePath).getFile());
		try
		{
			progFile.createNewFile();
			Reader fileReader = new FileReader(progFile);
			BufferedReader br = new BufferedReader(fileReader);
			while((line = br.readLine()) != null)
			{
				String[] progData = line.split(csvSplitOn);
				String[] moduleData = progData[1].split(moduleSplitOn);
				Programme prog = new Programme(progData[0].trim());
				for(int i = 0; i < moduleData.length; i++)
				{
					Module module = new Module();
					module.setName(moduleData[i]);
					prog.getModules().add(module);
				}
				result.put(prog.getName(), prog);
			}
			br.close();
			return result;
		}
		catch(Exception e)
		{
			throw new RuntimeException("\nCannot read file."
					+ "\nClass: " + getClass().getName()
					+ "\nMethod: read"
					+ "\nException: " + e.getMessage());
		}
	}
}
