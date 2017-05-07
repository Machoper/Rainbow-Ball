package edu.virginia.engine.display;

import java.util.ArrayList;

/**
 * Nothing in this class (yet) because there is nothing specific to a Sprite yet that a DisplayObject
 * doesn't already do. Leaving it here for convenience later. you will see!
 * */
public class Sprite extends DisplayObjectContainer {

	public Sprite(String id) {
		super(id);
	}

	public Sprite(String id, String imageFileName) {
		super(id, imageFileName);
	}
	
	@Override
	public void update(ArrayList<String> pressedKeys) {
		super.update(pressedKeys);

		// only do this sort of fix if this is NOT a collision between player and obj
		// IF BALL (PLAYER) ID CHANGES CHANGE THIS ID
		if(!this.getId().equals("ball")) {
			for (DisplayObject object : collidableObjects) {
				if(object.getParent() != null)
				{
					if (this.collideWithTop(object)) {
						this.setyPosition(this.getyPosition()+5);
					}
					if (this.collideWithBottom(object)) {
						this.setyPosition(this.getyPosition()-5);
					}
					if (this.collideWithLeft(object)) {
						this.setxPosition(this.getxPosition()+5);
					}
					if (this.collideWithRight(object)) {
						this.setxPosition(this.getxPosition()-5);
					}
				}

	
			}			
		}
	}
}