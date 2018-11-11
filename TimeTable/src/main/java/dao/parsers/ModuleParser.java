package dao.parsers;

import org.apache.poi.ss.usermodel.Row;

import entities.Module;

public class ModuleParser
{
	private Row row;
	
	public void setRow(Row row)
	{
		this.row = row;
	}
	
	public ModuleParser()
	{
		
	}
	public ModuleParser(Row row)
	{
		this.row = row;
	}
	
	public Module createModuleFromRow()
	{
		Module result = new Module();
		result.setName(getModuleIdFromCell());
		result.setDescription(getModuleDescriptionFromCell());
		return result;
	}
	
	private String getModuleIdFromCell()
	{
		String cellData = row.getCell(0).toString();
		
		// The Regex recognises the \\\\ as spliting on a singluar \
		String[] results = cellData.split("\\\\"); 
		return results[0].trim();
	}
	
	private String getModuleDescriptionFromCell()
	{
		return row.getCell(1).toString().trim();
	}
}
