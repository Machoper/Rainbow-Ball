package edu.virginia.engine.display;

import java.util.ArrayList;

public class Bullet extends Sprite {
	
	private int bulletSpeedX = 20;
	private int bulletSpeedY = 20;
	private boolean shoot;
	
	private int damage;
	private String type;
	
	public Bullet(String id, String fileName) {
		super(id, fileName);
		this.setVisible(false);
		shoot = false;
		//bulletSpeedX = 20;
		//bulletSpeedY = 20;
	}
	
	public Bullet(String id, String fileName, int damage, String type) {
		super(id, fileName);
		this.setVisible(false);
		this.damage = damage;
		this.type = type;
	}
	
	public void launchBullet() {
		this.setxPosition(this.getxPosition()+bulletSpeedX);
		this.setyPosition(this.getyPosition()+bulletSpeedY);
	}
	
	public int getBulletSpeedX() {
		return bulletSpeedX;
	}
	
	public void setBulletSpeedX(int bulletSpeedX) {
		this.bulletSpeedX = bulletSpeedX;
	}
	
	public int getBulletSpeedY() {
		return bulletSpeedY;
	}
	
	public void setBulletSpeedY(int bulletSpeedY) {
		this.bulletSpeedY = bulletSpeedY;
	}
	
	public void setShoot(boolean shoot) {
		this.shoot = shoot;
	}
	
	public int getDamage() {
		return damage;
	}

	public void setDamage(int damage) {
		this.damage = damage;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Override
	public void update(ArrayList<String> pressedKeys) {
		super.update(pressedKeys);
		
		if (shoot == true) {
			this.launchBullet();
		}
		
		/*for (DisplayObject object : this.collidableObjects) {
			if (this.collideWith(object)) {
				this.setImage("b_explode.png");
			}*/
		}

}
