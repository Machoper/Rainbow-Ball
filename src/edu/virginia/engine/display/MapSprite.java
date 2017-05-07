package edu.virginia.engine.display;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;

public class MapSprite extends Sprite {

	int gridWidth; // number of rooms in the x direction that this map has
	int gridHeight; // number of rooms in the y direction that this map has
	ArrayList<RoomSprite> rooms;
	ArrayList<RoomSprite> unlockedRooms;
	
	public MapSprite(String id) {
		super(id);
		// TODO Auto-generated constructor stub
	}
	
	public MapSprite(String id, String imageFileName) {
		super(id, imageFileName);
	}
	
	public MapSprite(String id, int gridWidth, int gridHeight) {
		super(id);
		this.getChildren().clear();
		rooms = new ArrayList<>();
		unlockedRooms = new ArrayList();
		this.gridWidth = gridWidth;
		this.gridHeight = gridHeight;
		createRooms();
	}
	
	private void createRooms()
	{
		RoomWallType type;
		for (int i = 0; i < gridHeight; i++)
		{
			for (int j = 0; j < gridWidth; j++)
			{
				//bunch of if statements to determine wall type
				if (i == gridHeight-1 && j == 0) // hardcoded
				{
					type = RoomWallType.BOSS_LEFT;
				}
				else if (i == gridHeight-1 && j == 1) //hardcoded
				{
					type = RoomWallType.BOSS_RIGHT;
				}
				else if (i == 0 && j == 0)
				{
					type = RoomWallType.UPPER_LEFT;
				}
				else if (i == 0 && j == gridWidth-1)
				{
					type = RoomWallType.UPPER_RIGHT;
				}
				else if (i == 0)
				{
					type = RoomWallType.UPPER_MIDDLE;
				}
				else if (j == 0 && i == gridHeight-1)
				{
					type = RoomWallType.BOTTOM_LEFT;
				}
				else if (j == 0)
				{
					type = RoomWallType.MIDDLE_LEFT;
				}
				else if (j == gridWidth-1 && i == gridHeight-1)
				{
					type = RoomWallType.BOTTOM_RIGHT;
				}
				else if (j == gridWidth-1)
				{
					type = RoomWallType.MIDDLE_RIGHT;
				}
				else if (i == gridHeight-1)
				{
					type = RoomWallType.BOTTOM_MIDDLE;
				}
				else
				{
					type = RoomWallType.MIDDLE;
				}
		
				
				// create the room and set it all up
				int idNum = (i*gridWidth)+j;
				RoomSprite room = new RoomSprite(Integer.toString(idNum), "angular_grey_background_single.png", type, j, i, idNum);
				room.setxPosition(j*room.getUnscaledWidth());
				room.setyPosition(i*room.getUnscaledHeight());
				room.generateWalls();
				room.setParent(this);
				this.addChild(room);
				rooms.add(room);
				if (i == 0)
				{
					unlockedRooms.add(room); // make first two rooms unlocked
					room.unlockRoom();
				}
				//System.out.println("room wall type and i and j are: " + type + " " + i + " " + j);
			}
		}
	}
	
	public void resetMap()
	{
		//TODO: set ball getnumvisited rooms to initial value
		unlockedRooms.clear();
		//todo lock all rooms, except room0
		for(int i = 0; i < this.rooms.size(); i++)
		{
			this.rooms.get(i).resetRoom();
			if(i == 0 || i == 1)
			{
				unlockedRooms.add(rooms.get(i));
			}
		}
		
		this.setxPosition(0);
		this.setyPosition(0);
		this.setRespondToKeys(true);
		
		//this.getChildren().clear();
		
		/*for(DisplayObject child: this.getChildren())
		{
			if(child.getId().contains("boss") || child.getId().contains("cloud") || child.getId().contains("Boss") || child.getId().contains("Ball") || child.getId().contains("bullet"))
			{
				//this.removeChild(child);
				child.setParent(null);
				child = null;
			}
		}*/
		
	}
	
	

	public ArrayList<RoomSprite> getUnlockedRooms() {
		return unlockedRooms;
	}
	
	public boolean roomIsUnlocked(int id)
	{
		for(RoomSprite room : unlockedRooms)
		{
			if(Integer.parseInt(room.getId()) == id)
			{
				return true;
			}
		}
		return false;
	}
	
	public void unlockNext(int numRoomsVisited)
	{
		switch(numRoomsVisited)
		{
			case 2:
				rooms.get(2).unlockRoom();
				rooms.get(3).unlockRoom();
				unlockedRooms.add(rooms.get(2));
				unlockedRooms.add(rooms.get(3));
				break;
			case 4:
				rooms.get(4).unlockRoom();
				rooms.get(5).unlockRoom();
				unlockedRooms.add(rooms.get(4));
				unlockedRooms.add(rooms.get(5));
				break;
			case 6:
				rooms.get(6).unlockRoom();
				rooms.get(7).unlockRoom();
				unlockedRooms.add(rooms.get(6));
				unlockedRooms.add(rooms.get(7));
				break;
			case 8:
				rooms.get(8).unlockRoom();
				rooms.get(9).unlockRoom();
				unlockedRooms.add(rooms.get(8));
				unlockedRooms.add(rooms.get(9));
				break;
			case 10:
				rooms.get(10).unlockRoom();
				rooms.get(11).unlockRoom();
				unlockedRooms.add(rooms.get(10));
				unlockedRooms.add(rooms.get(11));
				break;
			case 12:
				rooms.get(12).unlockRoom();
				rooms.get(13).unlockRoom();
				unlockedRooms.add(rooms.get(12));
				unlockedRooms.add(rooms.get(13));
				break;
			case 14:
				rooms.get(14).unlockRoom();
				rooms.get(15).unlockRoom();
				unlockedRooms.add(rooms.get(14));
				unlockedRooms.add(rooms.get(15));
				break;
			case 16:
				break;
			default:
				//not good if in here
				System.out.println("Error with unlocking correct rooms");
				break;
		}
	}

	public ArrayList<RoomSprite> getRooms() {
		return rooms;
	}
	
	public int getNumRooms()
	{
		return rooms.size();
	}

	public void setRooms(ArrayList<RoomSprite> rooms) {
		this.rooms = rooms;
	}

	public RoomSprite getRoom(int id)
	{
		return this.rooms.get(id);
	}

	public int getGridWidth() {
		return gridWidth;
	}

	public int getGridHeight() {
		return gridHeight;
	}

	

}
