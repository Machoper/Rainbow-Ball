package edu.virginia.engine.display;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.sound.sampled.UnsupportedAudioFileException;

import edu.virginia.engine.events.CollideEvent;
import edu.virginia.engine.events.StatusEvent;
import edu.virginia.engine.tween.Tween;
import edu.virginia.engine.tween.TweenEvent;
import edu.virginia.engine.util.SoundManager;
import edu.virginia.engine.display.Player;

public class Enemy extends Sprite {

	private int MaxHP;
	private HealthBar healthBar;
	private double angle;
	private double shootAngle;
	private int roll;

	private int enemySpeed;
	private int enemyMaxSpeed;
	private int enemyAttack;
	private boolean keepChasing;
	private boolean keepShooting;
	private boolean accelerated;
	private boolean isAlive;
	private List<Bullet> bList;
	
	private long shootThreshold = 800;
	long lastShoot;
	
	final long hdThreshold = 100;
	long lastDeduct = System.currentTimeMillis();

	private Tween dead;

	public Enemy(String id, String fileName) {
		super(id, fileName);
		healthBar = new HealthBar("enemyHealthBar");
		healthBar.setBarWidth(this.getUnscaledWidth());
		healthBar.setBarHeight(30);
		healthBar.setDrawHealth(this.getUnscaledWidth() - 4);
		healthBar.setxPosition(0);
		healthBar.setyPosition(this.getUnscaledHeight());
		this.addChild(healthBar);
		enemyMaxSpeed = 6;
		enemySpeed = enemyMaxSpeed;
		enemyAttack = 4;
		keepChasing = true;
		keepShooting = true;
		isAlive = true;
		accelerated = false;
		bList = new ArrayList<Bullet>();
		lastShoot = System.currentTimeMillis();
	}
	
	public void resetEnemy(int xpos, int ypos, int hp)
	{
		this.setxPosition(xpos);
		this.setyPosition(ypos);
		this.angle = 0.0;
		this.setHp(hp);
		healthBar.setBarWidth(this.getUnscaledWidth());
		healthBar.setBarHeight(30);
		healthBar.setDrawHealth(this.getUnscaledWidth() - 4);
		healthBar.setxPosition(0);
		healthBar.setyPosition(this.getUnscaledHeight());
		this.addChild(healthBar);
		//enemyMaxSpeed = 6;
		enemySpeed = enemyMaxSpeed;
		//enemyAttack = 5;
		keepChasing = true;
		keepShooting = true;
		isAlive = true;
		accelerated = false;
		bList.clear(); 
		lastShoot = System.currentTimeMillis();
	}

	public int getMaxHP() {
		return MaxHP;
	}

	public void setMaxHP(int maxHP) {
		MaxHP = maxHP;
	}

	/*
	 * public int getHp() { return hp; }
	 * 
	 * public void setHp(int hp) { this.hp = hp; }
	 */

	public HealthBar getHealthBar() {
		return healthBar;
	}

	public void setHealthBar(HealthBar healthBar) {
		this.healthBar = healthBar;
	}
	
	public boolean isKeepChasing() {
		return keepChasing;
	}

	public void setKeepChasing(boolean keepChasing) {
		this.keepChasing = keepChasing;
	}

	public void chase(Player target) {
		angle = Math.atan2(
				(target.getyPosition() + target.getUnscaledHeight() / 2)
						- (this.getyPosition() + this.getParent().getyPosition() + this.getUnscaledHeight() / 2),
				(target.getxPosition() + target.getUnscaledWidth() / 2)
						- (this.getxPosition() + this.getParent().getxPosition() + this.getUnscaledWidth() / 2));
		this.setxPosition(this.getxPosition() + (int) (enemySpeed * Math.cos(angle)));
		this.setyPosition(this.getyPosition() + (int) (enemySpeed * Math.sin(angle)));
	}

	public void shoot(Player target, SoundManager sm) {
		if (keepShooting == true) {
			shootAngle = Math.atan2(
					target.getyPosition() + target.getUnscaledHeight() / 2
							- (this.getyPosition() + this.getParent().getyPosition() + this.getUnscaledHeight() / 2),
					target.getxPosition() + target.getUnscaledWidth() / 2
							- (this.getxPosition() + this.getParent().getxPosition() + this.getUnscaledWidth() / 2));
			long now = System.currentTimeMillis();
			if (now - lastShoot > shootThreshold) {
				Bullet b = new Bullet("bullet", "beam_bullet.png");
				try {
					sm.PlaySoundEffect("silencer");
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (UnsupportedAudioFileException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				b.setxPosition(this.getUnscaledWidth() / 2 - b.getUnscaledWidth() / 2);
				b.setyPosition(this.getUnscaledHeight() / 2 - b.getUnscaledHeight() / 2);
				b.setBulletSpeedX((int) (b.getBulletSpeedX() * Math.cos(shootAngle)));
				b.setBulletSpeedY((int) (b.getBulletSpeedY() * Math.sin(shootAngle)));
				b.setShoot(true);
				b.setVisible(true);
				// this.addChild(b);
				b.addCollisionObject(target);
				bList.add(b);
				lastShoot = now;
			}
		}
	}
	
	public void speedUp() {
		//long now = System.currentTimeMillis();
		//long threshold = 100;
		if (this.getHp() < this.getMaxHP()/2 && this.accelerated == false) {
			this.enemyMaxSpeed = 12;
			this.enemySpeed = this.enemyMaxSpeed;
			this.accelerated = true;
		}
	}
	
	public void teleport(Player target) {
		roll = (int)(Math.random()*200);
		if (roll < 1) {
			this.setVisible(false);
			this.setxPosition(-this.getParent().getxPosition()+target.getxPosition()+(int)(Math.random()*200-100));
			this.setyPosition(-this.getParent().getyPosition()+target.getyPosition()+(int)(Math.random()*200-100));
			this.setVisible(true);
		}
	}
	
	public int getEnemySpeed() {
		return enemySpeed;
	}

	public void setEnemySpeed(int enemySpeed) {
		this.enemySpeed = enemySpeed;
	}

	public boolean isKeepShooting() {
		return keepShooting;
	}

	public void setKeepShooting(boolean keepShooting) {
		this.keepShooting = keepShooting;
	}

	@Override
	public void update(ArrayList<String> pressedKeys) {
		// TODO Auto-generated method stub
		super.update(pressedKeys);

		this.getHealthBar().setDrawHealth((int) ((this.getUnscaledWidth() - 4) * this.getHp() / this.getMaxHP()));

		if (this.getHp() <= 0 && this.isAlive) {
			this.dispatchEvent(new StatusEvent(StatusEvent.VANISH, this));
			this.setAlive(false);
			this.removeChild(this.getHealthBar());
			this.setKeepShooting(false); // quit shooting
			this.setKeepChasing(false);
			for (DisplayObject object : this.collidableObjects) { // remove
																	// collision
																	// mechanism
																	// when
																	// enemy
																	// dies.
				object.collidableObjects.remove(this);
			}
			this.collidableObjects.clear();
		}

		if (this.isAlive && this.getScaleX() == 0.1f) {
			this.dispatchEvent(new StatusEvent(StatusEvent.APPEAR, this));
		}

		Iterator<Bullet> iter = bList.iterator();
		while (iter.hasNext()) {
			Bullet b = iter.next();
			for (DisplayObject d : this.collidableObjects) {
				b.addCollisionObject(d);
			}
			this.addChild(b);
			for (DisplayObject s : b.collidableObjects) {
				if (s.getParent() != null) {
					if (b.collideWith(s) && s.getAlpha() == 1) {
						this.removeChild(b);

						if (s.getClass().getName() == "edu.virginia.engine.display.Player") {
							((Player)s).setHealth(((Player) s).getHealth() - enemyAttack);
							System.out.println(((Player) s).getHealth());
						}
						
						b.dispatchEvent(new CollideEvent(CollideEvent.COLLIDE_START, s));
						iter.remove();
						break;
					}
				}
			}

		}
	}

	public boolean isAlive() {
		return isAlive;
	}

	public void setAlive(boolean isAlive) {
		this.isAlive = isAlive;
	}

	public Tween getDead() {
		return dead;
	}

	public void setDead(Tween dead) {
		this.dead = dead;
	}

	public int getEnemyAttack() {
		return enemyAttack;
	}

	public void setEnemyAttack(int enemyAttack) {
		this.enemyAttack = enemyAttack;
	}

	public int getEnemyMaxSpeed() {
		return enemyMaxSpeed;
	}

	public void setEnemyMaxSpeed(int enemyMaxSpeed) {
		this.enemyMaxSpeed = enemyMaxSpeed;
	}
	
	public long getShootThreshold() {
		return shootThreshold;
	}

	public void setShootThreshold(long shootThreshold) {
		this.shootThreshold = shootThreshold;
	}

}
