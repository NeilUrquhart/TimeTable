package controllers;

import dao.TimetableData;

public class FacadeController 
{
	private TimetableData data;
	private FileLoadController fileLoader;
	private QueryController queryController;
	private StudentMoveController studentMove;
	
	private static FacadeController facade = null;
	
	public static FacadeController getInstance(String filePath)
	{
		if(facade == null)
			facade = new FacadeController(filePath);
		return facade;
	}
	
	private FacadeController(String filePath)
	{
		data = new TimetableData();
		fileLoader = FileLoadController.getInstance(filePath);
		readTimetableData();
	}
	
	private void readTimetableData()
	{
		fileLoader.readTimetableData();
		data = fileLoader.getTimetableData();
	}
}
