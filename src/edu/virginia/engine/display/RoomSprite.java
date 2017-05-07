package edu.virginia.engine.display;

import java.util.List;

public class RoomSprite extends Sprite { // can change to extending AnimatedSprite if needed
	
	int gridX; // room's relative x position. Example: leftmost uppermost room is gridX of 0 and gridY of 0
	int gridY; // room's relative y position
	RoomWallType wallType; // default wall type is MIDDLE since that would be most common in a large grid
	int idNum; // number of room in order generated

	public RoomSprite(String id) {
		super(id);
		wallType = RoomWallType.MIDDLE;
		// TODO Auto-generated constructor stub
	}
	
	public RoomSprite(String id, String imageFileName) {
		super(id, imageFileName);
		wallType = RoomWallType.MIDDLE;
	} 
	
	public RoomSprite(String id, String imageFileName, RoomWallType wallType) {
		super(id, imageFileName);
		this.wallType = wallType;
	}
	
	// not sure if gridX and gridY needed but never hurts
	public RoomSprite(String id, String imageFileName, RoomWallType wallType, int gridX, int gridY, int idNum) {
		super(id, imageFileName);
		this.wallType = wallType;
		this.gridX = gridX;
		this.gridY = gridY;
		this.idNum = idNum;
	}
	
	
	
	
	public int getIdNum() {
		return idNum;
	}

	public void setIdNum(int idNum) {
		this.idNum = idNum;
	}

	public RoomWallType getWallType()
	{
		return this.wallType;
	}
	/*
	 * Uses the RoomWallType to determine what kind of walls to create, i.e. where to put doorways
	 */
	public void generateWalls()
	{
		int thisWidth = this.getUnscaledWidth();
		int thisHeight = this.getUnscaledHeight();
		// will have 0-2 solid walls.
		DisplayObject wall0 = new DisplayObject("wall0", "horizontal_wall.png");
		DisplayObject wall1 = new DisplayObject("wall1", "vertical_wall.png");
		
		int wall0W = wall0.getUnscaledWidth();
		int wall0H = wall0.getUnscaledHeight();
		int wall1W = wall1.getUnscaledWidth();
		int wall1H = wall1.getUnscaledHeight();
		
		DisplayObject door0 = new DisplayObject("door0", "horizontal_door.png");
		DisplayObject door1 = new DisplayObject("door1", "horizontal_door.png");
		//DisplayObject door2 = new DisplayObject("door2", "vertical_door.png");
		
		int doorHW = door0.getUnscaledWidth();
		int doorHH = door0.getUnscaledHeight();
		
		//int doorVW = door2.getUnscaledWidth();
		//int doorVH = door2.getUnscaledHeight();

		// will have between 2-4 gateways, which means having 2-4 partial vertical walls, and 2-4 partial horizontal walls
		DisplayObject wall2 = new DisplayObject("wall2", "vertical_wall_partial.png");
		wall2.setParent(this);
		this.addChild(wall2);
		DisplayObject wall3 = new DisplayObject("wall3", "vertical_wall_partial.png");
		wall3.setParent(this);
		this.addChild(wall3);
		DisplayObject wall4 = new DisplayObject("wall4", "horizontal_wall_partial.png");
		wall4.setParent(this);
		this.addChild(wall4);
		DisplayObject wall5 = new DisplayObject("wall5", "horizontal_wall_partial.png");
		wall5.setParent(this);
		this.addChild(wall5);
		
		int vWallPartW = wall2.getUnscaledWidth();
		int vWallPartH = wall2.getUnscaledHeight();
		
		int hWallPartW = wall4.getUnscaledWidth();
		int hWallPartH = wall4.getUnscaledHeight();
		
		DisplayObject wall6 = new DisplayObject("wall6", "vertical_wall_partial.png");
		DisplayObject wall7 = new DisplayObject("wall7", "vertical_wall_partial.png");
		DisplayObject wall8 = new DisplayObject("wall8", "horizontal_wall_partial.png");
		DisplayObject wall9 = new DisplayObject("wall9", "horizontal_wall_partial.png");
		
		
		switch(wallType)
		{
		
			case UPPER_LEFT:				
				wall0.setParent(this);
				this.addChild(wall0);
				wall1.setParent(this);
				this.addChild(wall1);
				wall2.setxPosition(thisWidth-vWallPartW);
				wall3.setxPosition(thisWidth-vWallPartW);
				wall3.setyPosition(thisHeight-vWallPartH);
				wall4.setyPosition(thisHeight-hWallPartH);
				wall5.setxPosition(thisWidth-hWallPartW);
				wall5.setyPosition(thisHeight-hWallPartH);
				door0.setParent(this);
				this.addChild(door0);
				//door0.setxPosition((thisWidth/2)-(doorHW/2));
				//door0.setyPosition(thisHeight-doorHH);
				//door2.setParent(this);
				//this.addChild(door2);
				//door2.setxPosition(thisWidth-doorVW);
				//door2.setyPosition((thisHeight/2)-(doorVH/2));
				break;
			case UPPER_MIDDLE:
				wall0.setParent(this);
				this.addChild(wall0);
				wall2.setxPosition(thisWidth-vWallPartW);
				wall3.setxPosition(thisWidth-vWallPartW);
				wall3.setyPosition(thisHeight-vWallPartH);
				wall4.setyPosition(thisHeight-hWallPartH);
				wall5.setxPosition(thisWidth-hWallPartW);
				wall5.setyPosition(thisHeight-hWallPartH);
				wall6.setParent(this);
				this.addChild(wall6);
				wall7.setParent(this);
				this.addChild(wall7);
				wall7.setyPosition(thisHeight-vWallPartH);
				break;
			case UPPER_RIGHT:
				wall0.setParent(this);
				this.addChild(wall0);
				wall1.setParent(this);
				this.addChild(wall1);
				wall1.setxPosition(thisWidth-wall1W);
				wall3.setyPosition(thisHeight-vWallPartH);
				wall4.setyPosition(thisHeight-hWallPartH);
				wall5.setxPosition(thisWidth-hWallPartW);
				wall5.setyPosition(thisHeight-hWallPartH);
				door0.setParent(this);
				this.addChild(door0);
				door0.setxPosition((thisWidth/2)-(doorHW/2));
				door0.setyPosition(thisHeight-doorHH);
				//door2.setParent(this);
				//this.addChild(door2);
				//door2.setxPosition(thisWidth-doorVW);
				//door2.setyPosition((thisHeight/2)-(doorVH/2));
				break;
			case MIDDLE_LEFT:
				wall1.setParent(this);
				this.addChild(wall1);
				wall2.setxPosition(thisWidth-vWallPartW);
				wall3.setxPosition(thisWidth-vWallPartW);
				wall3.setyPosition(thisHeight-vWallPartH);
				wall4.setyPosition(thisHeight-hWallPartH);
				wall5.setxPosition(thisWidth-hWallPartW);
				wall5.setyPosition(thisHeight-hWallPartH);
				wall8.setParent(this);
				this.addChild(wall8);
				wall9.setParent(this);
				this.addChild(wall9);
				wall9.setxPosition(thisWidth-hWallPartW);
				door0.setParent(this);
				this.addChild(door0);
				door0.setxPosition((thisWidth/2)-(doorHW/2));
				door0.setyPosition(thisHeight-doorHH);
				//door2.setParent(this);
				//this.addChild(door2);
				//door2.setxPosition(thisWidth-doorVW);
				//door2.setyPosition((thisHeight/2)-(doorVH/2));
				door1.setParent(this);
				this.addChild(door1);
				door1.setxPosition((thisWidth/2)-(doorHW/2));
				break;
			case MIDDLE:
				wall3.setyPosition(thisHeight-vWallPartH);
				wall4.setyPosition(thisHeight-hWallPartH);
				wall5.setxPosition(thisWidth-hWallPartW);
				wall5.setyPosition(thisHeight-hWallPartH);
				
				wall8.setParent(this);
				this.addChild(wall8);
				wall9.setParent(this);
				this.addChild(wall9);
				wall9.setxPosition(thisWidth-hWallPartW);
				
				DisplayObject wall10 = new DisplayObject("wall10", "vertical_wall_partial.png");
				wall10.setParent(this);
				this.addChild(wall10);
				DisplayObject wall11 = new DisplayObject("wall11", "vertical_wall_partial.png");
				wall11.setParent(this);
				this.addChild(wall11);
				wall10.setxPosition(thisWidth-vWallPartW);
				wall11.setxPosition(thisWidth-vWallPartW);
				wall11.setyPosition(thisHeight-vWallPartH);
				break;
			case MIDDLE_RIGHT:
				wall1.setParent(this);
				this.addChild(wall1);
				wall1.setxPosition(thisWidth-wall1W);
				wall3.setyPosition(thisHeight-vWallPartH);
				wall4.setyPosition(thisHeight-hWallPartH);
				wall5.setxPosition(thisWidth-hWallPartW);
				wall5.setyPosition(thisHeight-hWallPartH);
				
				wall8.setParent(this);
				this.addChild(wall8);
				wall9.setParent(this);
				this.addChild(wall9);
				wall9.setxPosition(thisWidth-hWallPartW);
				door0.setParent(this);
				this.addChild(door0);
				door0.setxPosition((thisWidth/2)-(doorHW/2));
				door0.setyPosition(thisHeight-doorHH);
				//door2.setParent(this);
				//this.addChild(door2);
				//door2.setxPosition(thisWidth-doorVW);
				//door2.setyPosition((thisHeight/2)-(doorVH/2));
				door1.setParent(this);
				this.addChild(door1);
				door1.setxPosition((thisWidth/2)-(doorHW/2));
				break;
			case BOTTOM_LEFT:
				wall0.setParent(this);
				this.addChild(wall0);
				wall1.setParent(this);
				this.addChild(wall1);
				wall0.setyPosition(thisHeight-wall0H);
				wall2.setxPosition(thisWidth-vWallPartW);
				wall3.setxPosition(thisWidth-vWallPartW);
				wall3.setyPosition(thisHeight-vWallPartH);
				wall5.setxPosition(thisWidth-hWallPartW);
				break;
			case BOSS_LEFT:
				wall0.setParent(this);
				this.addChild(wall0);
				wall1.setParent(this);
				this.addChild(wall1);
				
				wall0.setyPosition(thisHeight-wall0H);
				//wall2.setxPosition(this.getUnscaledWidth()-wall2.getUnscaledWidth());
				//wall3.setxPosition(this.getUnscaledWidth()-wall3.getUnscaledWidth());
				//wall3.setyPosition(this.getUnscaledHeight()-wall3.getUnscaledHeight());
				wall5.setxPosition(thisWidth-hWallPartW);
				door1.setParent(this);
				this.addChild(door1);
				door1.setxPosition((thisWidth/2)-(doorHW/2));
				break;
			case BOTTOM_MIDDLE:
				wall0.setParent(this);
				this.addChild(wall0);
				wall0.setyPosition(thisHeight-wall0H);
				wall2.setxPosition(thisWidth-vWallPartW);
				wall3.setxPosition(thisWidth-vWallPartW);
				wall3.setyPosition(thisHeight-vWallPartH);
				wall5.setxPosition(thisWidth-hWallPartW);
				wall6.setParent(this);
				this.addChild(wall6);
				wall7.setParent(this);
				this.addChild(wall7);
				wall7.setyPosition(thisHeight-vWallPartH);
				break;
			case BOTTOM_RIGHT:
				wall0.setParent(this);
				this.addChild(wall0);
				wall1.setParent(this);
				this.addChild(wall1);
				wall0.setyPosition(thisHeight-wall0H);
				wall1.setxPosition(thisWidth-wall1W);
				wall3.setyPosition(thisHeight-vWallPartH);
				wall5.setxPosition(thisWidth-hWallPartW);
				break;
			case BOSS_RIGHT:
				wall0.setParent(this);
				this.addChild(wall0);
				wall1.setParent(this);
				this.addChild(wall1);
				
				wall0.setyPosition(thisHeight-wall0H);
				wall1.setxPosition(thisWidth-wall1W);
				wall2.setxPosition(thisWidth-vWallPartW);
				wall3.setxPosition(thisWidth-vWallPartW);
				wall5.setxPosition(thisWidth-hWallPartW);
				door1.setParent(this);
				this.addChild(door1);
				door1.setxPosition((thisWidth/2)-(doorHW/2));
				break;
		}
	}
	
	public void unlockRoom()
	{
		for(int i = 0; i < this.getChildren().size(); i++)
		{
			DisplayObject child = this.getChildren().get(i);
			if(child.getId().contains("door"))
			{
				child.setVisible(false);
				child.setxPosition(0);
				//child = null;
			}
		}
	}
	
	public void resetRoom()
	{
		for(int i = 0; i < this.getChildren().size(); i++)
		{
			DisplayObject child = this.getChildren().get(i);
			if(child.getId().contains("door") && this.wallType != RoomWallType.UPPER_LEFT && this.wallType != RoomWallType.UPPER_RIGHT)
			{
				child.setVisible(true);
				child.setxPosition((this.getUnscaledWidth()/2)-(child.getUnscaledWidth()/2));
			}
		}
	}

}
