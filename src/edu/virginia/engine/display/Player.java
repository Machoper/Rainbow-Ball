package edu.virginia.engine.display;

import java.util.ArrayList;

import edu.virginia.engine.events.StatusEvent;

public class Player extends Sprite{
	

	private int health;
	private int maxHealth;
	private int speed;
	private int maxSpeed;
	private int attack;

	
	private int basicWeaponAttack;
	private int upgradedWeaponAttack;
	private int freezeWeaponAttack;
	private int ultimateWeaponAttack;
	
	private boolean upgradedWeaponUnlocked;
	private boolean freezeWeaponUnlocked;
	private boolean ultimateWeaponUnlocked;
	
	private boolean isAlive;
	
	private String currentWeapon;
	
	private int currentRoomId; //always start in room 0 for simplicity
	
	private MapSprite map;
	
	private ArrayList<RoomSprite> visitedRooms;

	
	public Player(String id, String fileName, MapSprite map) {
		super(id, fileName);
		this.map = map;
		this.visitedRooms = new ArrayList<>();
		visitedRooms.add(map.getRoom(0));
		initRoomCollisions();
		
		health = 100;
		maxHealth = 100;
		speed = 10;
		maxSpeed = 10;
		attack = 5;
		
		basicWeaponAttack = 5;
		upgradedWeaponAttack = 10;
		freezeWeaponAttack = 5;
		ultimateWeaponAttack = 50;
		
		upgradedWeaponUnlocked = false;
		freezeWeaponUnlocked = false;
		ultimateWeaponUnlocked = false;
		
		isAlive = true;
		
		currentWeapon = "basic";
		
		currentRoomId = 0; //always start in room 0 for simplicity
	}
	
	public void resetPlayerNew(String img, int hp)
	{
		this.setImage(img);
		this.visitedRooms.clear();
		visitedRooms.add(map.getRoom(0));
		health = 100;
		maxHealth = 100;
		speed = 10;
		maxSpeed = 10;
		attack = 5;
		basicWeaponAttack = 5;
		upgradedWeaponAttack = 10;
		freezeWeaponAttack = 5;
		ultimateWeaponAttack = 50;
		
		this.setHp(hp);

		upgradedWeaponUnlocked = false;
		freezeWeaponUnlocked = false;
		ultimateWeaponUnlocked = false;
		
		isAlive = true;
		
		currentWeapon = "basic";
		
		currentRoomId = 0; //always start in room 0 for simplicity
	}
	
	private void initRoomCollisions() 
	{
		for(RoomSprite room : map.getRooms())
		{
			for(DisplayObject roomObj: room.getChildren())
			{
				this.addCollisionObject(roomObj);
			}
		}
	}
	
	@Override
	public void update(ArrayList<String> pressedKeys) {
		super.update(pressedKeys);

		// TODO: update
		for (DisplayObject objectCol : collidableObjects) {
			if(objectCol.getParent() != null)
			{
				
				if(objectCol.getParent().getClass().getName().equals("edu.virginia.engine.display.RoomSprite"))
				{
					int id = ((RoomSprite)(objectCol.getParent())).getIdNum();
					if(map.roomIsUnlocked(id) && id != currentRoomId)
					{
					//	System.out.println("collidable room id is " + id + " but current room id is " + currentRoomId);
						continue;
					}
					else
					{
						int x = 5; // COLLISION
						x = 6; //for debug purposes
					}
				}
	
				if (this.collideWithTop(objectCol)) {
					map.setyPosition(map.getyPosition()-this.getSpeed());
					
					//this.setyPosition(this.getyPosition()+5);
				}
				if (this.collideWithBottom(objectCol)) {
					map.setyPosition(map.getyPosition()+this.getSpeed());
					//this.setyPosition(this.getyPosition()-5);
				}
				if (this.collideWithLeft(objectCol)) {
					map.setxPosition(map.getxPosition()-this.getSpeed());
					//this.setxPosition(this.getxPosition()+5);
				}
				if (this.collideWithRight(objectCol)) {
					map.setxPosition(map.getxPosition()+this.getSpeed());
					//this.setxPosition(this.getxPosition()-5);
				}
				
			}
				
			//check if room switched
			updateCurrentRoom();
		}
		
		if (this.getHealth() == 0 && this.isAlive) {
			this.dispatchEvent(new StatusEvent(StatusEvent.VANISH, this));
			this.setAlive(false);
		}
	
	}
	
	private void updateCurrentRoom()
	{
		if(map != null)
		{
			if(this.collideWith(map.getRoom(this.currentRoomId)))
			{
				//System.out.println("colliding w/ same room");
				return;
			}
			
			// check to the right
			if(this.currentRoomId != (map.getNumRooms()-1) && this.collideWith(map.getRoom(this.currentRoomId+1)) && map.roomIsUnlocked(this.currentRoomId+1))
			{
				this.currentRoomId += 1;
			}
			// check to the left
			else if(this.getCurrentRoomId() != 0 && this.collideWith(map.getRoom(this.currentRoomId-1)) && map.roomIsUnlocked(this.getCurrentRoomId()-1))
			{
				this.currentRoomId -= 1;
			}
			//check below
			else if ((this.currentRoomId + map.getGridWidth()) < map.getNumRooms() && this.collideWith(map.getRoom(this.currentRoomId + map.getGridWidth())) && map.roomIsUnlocked(this.currentRoomId + map.getGridWidth()))
			{
				this.currentRoomId += map.getGridWidth();
			}
			//check above
			else if ((this.currentRoomId - map.getGridWidth()) >= 0 && this.collideWith(map.getRoom(this.currentRoomId - map.getGridWidth())) && map.roomIsUnlocked(this.currentRoomId - map.getGridWidth()))
			{
				this.currentRoomId -= map.getGridWidth();
			}
			else // this is weird if it gets here
			{
				return;
			}
			//only the statements where the room changes will hit this line of code
			//updateRoomCollisions(oldRoomId, currentRoomId);
			
			if(map.roomIsUnlocked(this.currentRoomId) && !this.visitedRooms.contains(map.getRoom(this.currentRoomId)))
			{
				this.visitedRooms.add(map.getRoom(this.currentRoomId));
			}
	
		}
	}
	
	public int getNumVisitedRooms()
	{
		return this.visitedRooms.size();
	}

	public int getMaxHealth() {
		return maxHealth;
	}

	public void setMaxHealth(int maxHealth) {
		this.maxHealth = maxHealth;
	}

	public int getSpeed() {
		return speed;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}

	public int getMaxSpeed() {
		return maxSpeed;
	}

	public void setMaxSpeed(int maxSpeed) {
		this.maxSpeed = maxSpeed;
	}

	public MapSprite getMap() {
		return map;
	}

	public void setMap(MapSprite map) {
		this.map = map;
	}

	public int getCurrentRoomId() {
		return currentRoomId;
	}

	public void setCurrentRoomId(int currentRoomId) {
		this.currentRoomId = currentRoomId;
	}

	public int getBasicWeaponAttack() {
		return basicWeaponAttack;
	}

	public void setBasicWeaponAttack(int basicWeaponAttack) {
		this.basicWeaponAttack = basicWeaponAttack;
	}

	public int getUpgradedWeaponAttack() {
		return upgradedWeaponAttack;
	}

	public void setUpgradedWeaponAttack(int upgradedWeaponAttack) {
		this.upgradedWeaponAttack = upgradedWeaponAttack;
	}

	public int getFreezeWeaponAttack() {
		return freezeWeaponAttack;
	}

	public void setFreezeWeaponAttack(int freezeWeaponAttack) {
		this.freezeWeaponAttack = freezeWeaponAttack;
	}

	public int getUltimateWeaponAttack() {
		return ultimateWeaponAttack;
	}

	public void setUltimateWeaponAttack(int ultimateWeaponAttack) {
		this.ultimateWeaponAttack = ultimateWeaponAttack;
	}

	public boolean isUpgradedWeaponUnlocked() {
		return upgradedWeaponUnlocked;
	}

	public void setUpgradedWeaponUnlocked(boolean upgradedWeaponUnlocked) {
		this.upgradedWeaponUnlocked = upgradedWeaponUnlocked;
	}

	public boolean isFreezeWeaponUnlocked() {
		return freezeWeaponUnlocked;
	}

	public void setFreezeWeaponUnlocked(boolean freezeWeaponUnlocked) {
		this.freezeWeaponUnlocked = freezeWeaponUnlocked;
	}

	public boolean isUltimateWeaponUnlocked() {
		return ultimateWeaponUnlocked;
	}

	public void setUltimateWeaponUnlocked(boolean ultimateWeaponUnlocked) {
		this.ultimateWeaponUnlocked = ultimateWeaponUnlocked;
	}

	public String getCurrentWeapon() {
		return currentWeapon;
	}

	public void setCurrentWeapon(String currentWeapon) {
		this.currentWeapon = currentWeapon;
	}

	public int getHealth() {
		return health;
	}

	public void setHealth(int health) {
		this.health = health;
	}

	public int getAttack() {
		return attack;
	}

	public void setAttack(int attack) {
		this.attack = attack;
	}

	public boolean isAlive() {
		return isAlive;
	}

	public void setAlive(boolean isAlive) {
		this.isAlive = isAlive;
	}
}
