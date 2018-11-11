package dao.parsers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Row;

import entities.Room;

// Need to read the Room.CSV for the size of the roms 
// This will be done in here cause I said so.
public class RoomParser
{
	private Row row;
	private List<Room> rooms;
	private String roomCsvFile;
	private String line;
	private String splitOn;
	
	public void setRow(Row row)
	{
		this.row = row;
	}
	
	public RoomParser()
	{
		
	}
	public RoomParser(Row row)
	{
		this.row = row;
	}
	
	public Room createRoomFromRow()
	{
		initialise();
		Room result = new Room();
		result.setName(getRoomNameFromCell().trim());
		for(Room room : rooms)
		{
			if(room.getName().contains(result.getName()))
			{
				result.setCapacity(room.getCapacity());
			}
		}
		return result;
	}
	
	private void initialise()
	{
		rooms = new ArrayList<Room>();
		roomCsvFile = "rooms.csv";
		line = "";
		splitOn = ",";
		populateRoomsFromCsv();
	}

	private String getRoomNameFromCell()
	{
		return row.getCell(11).toString();
	}
	
	// Read the rooms from the CSV file and populate the list
	// Then when each room from the row gets pulled out 
	// find in the list and add the room size to it
	private void populateRoomsFromCsv()
	{
		ClassLoader classLoader = getClass().getClassLoader();
		File csvFile = new File(classLoader.getResource(roomCsvFile).getFile());
		int lineCount = 0;
		try
		{
			csvFile.createNewFile();
			Reader fileReader = new FileReader(csvFile);
			BufferedReader br = new BufferedReader(fileReader);
			while((line = br.readLine()) != null)
			{
				// Since first line is the column header this will skip it
				if(lineCount > 0)
				{
					String[] roomData = line.split(splitOn);
					Room room = new Room(roomData[0].trim(), Integer.parseInt(roomData[1].trim()));
					rooms.add(room);
				}
				lineCount++;
			}
			br.close();
		}
		catch(Exception e)
		{
			throw new RuntimeException("\nCannot read file."
					+ "\nClass: " + getClass().getName()
					+ "\nMethod: populateRoomsFromCsv"
					+ "\nException: " + e.getMessage());
		}
	}
}
