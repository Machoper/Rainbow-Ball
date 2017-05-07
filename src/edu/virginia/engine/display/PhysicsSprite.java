package edu.virginia.engine.display;

import java.util.ArrayList;
import java.util.List;

public class PhysicsSprite extends AnimatedSprite {
	
	private int gravity;
	private boolean havingG;
	private int fallingSpeed;
	private int jumpPower;

	public PhysicsSprite(String id, List<String> fileNames) {
		super(id, fileNames);
		setHavingG(false);
		gravity = 1;
		fallingSpeed = 0;
		jumpPower = -20;
	}


	public int getGravity() {
		return gravity;
	}

	public void setGravity(int gravity) {
		this.gravity = gravity;
	}

	public boolean isHavingG() {
		return havingG;
	}

	public void setHavingG(boolean havingG) {
		this.havingG = havingG;
	}
	
	public void fall() {
		setyPosition(getyPosition()+fallingSpeed);
		fallingSpeed += gravity;
		if (fallingSpeed > 15) {
			fallingSpeed = 15;
		}
	}
	
	public void jump() {
		fallingSpeed = jumpPower;
		fall();
	}
	
	public boolean isOnGround() {
		boolean landed = false;
		for (DisplayObject object : collidableObjects) {
			if (this.collideWithBottom(object)) { 
				this.setyPosition(object.getyPosition()-this.getUnscaledHeight()+1);
				landed = true; 
				}
			}
		return landed;
		}
	
	@Override
	public void update(ArrayList<String> pressedKeys) {
		super.update(pressedKeys);
		
		if (this != null && this.isRespondToKeys()) {
			if (isOnGround()) {
				this.animate("walk");
				if (pressedKeys.contains("A")) {
					
					jump();
				}
			}
			else {
				this.animate("jump");
				fall();
				if (this.getyPosition() >= 736) {
					this.setyPosition(736);
					}
				}
				
			
		}
		
		for (DisplayObject object : collidableObjects) {
			if (this.collideWithTop(object)) {
				this.setyPosition(object.getyPosition()+object.getUnscaledHeight());
			}
			if (this.collideWithLeft(object)) {
				this.setxPosition(object.getxPosition()+object.getUnscaledWidth());
			}
			if (this.collideWithRight(object)) {
				this.setxPosition(object.getxPosition()-this.getUnscaledWidth());
			}
			
		}
	}
}