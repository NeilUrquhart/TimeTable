package dao.parsers;

import org.apache.poi.ss.usermodel.Row;

import entities.Room;

public class RoomParser
{
	private Row row;
	
	public RoomParser(Row row)
	{
		this.row = row;
	}
	
	public Room createRoomFromRow()
	{
		Room result = new Room();
		result.setName(getRoomNameFromCell());
		return result;
	}
	
	private String getRoomNameFromCell()
	{
		return row.getCell(11).toString();
	}
}
