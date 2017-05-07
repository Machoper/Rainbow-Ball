package edu.virginia.prototype;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JOptionPane;

import edu.virginia.engine.display.Bullet;
import edu.virginia.engine.display.ColorBall;
import edu.virginia.engine.display.DisplayObject;
import edu.virginia.engine.display.DisplayObjectContainer;
import edu.virginia.engine.display.Enemy;
import edu.virginia.engine.display.Game;
import edu.virginia.engine.display.GameState;
import edu.virginia.engine.display.HealthBar;
import edu.virginia.engine.display.MapSprite;
import edu.virginia.engine.display.Player;
import edu.virginia.engine.display.RoomSprite;
import edu.virginia.engine.display.Sprite;
import edu.virginia.engine.events.CollideEvent;
import edu.virginia.engine.events.Event;
import edu.virginia.engine.events.IEventListener;
import edu.virginia.engine.events.PickedUpEvent;
import edu.virginia.engine.events.StatusEvent;
import edu.virginia.engine.tween.EaseOut;
import edu.virginia.engine.tween.Tween;
import edu.virginia.engine.tween.TweenEvent;
import edu.virginia.engine.tween.TweenJuggler;
import edu.virginia.engine.tween.TweenableParams;
import edu.virginia.engine.util.SoundManager;

public class prototype extends Game implements IEventListener {

	BufferedImage background = this.readImage("background.png");

	private GameState gameState = GameState.START;

	// main elements setup
	private DisplayObject gameParent;
	private MapSprite map;
	private Player ball;
	private List<Bullet> bList;
	private Sprite ballCenter;
	private HealthBar h;
	private Sprite whiteLight;
	private Sprite note;

	// colored ball sprites
	private Sprite redBall;
	private Sprite orangeBall;
	private Sprite blueBall;
	private Sprite greenBall;
	private Sprite yellowBall;
	private Sprite purpleBall;

	// instruction sprites
	private Sprite wasd;
	private Sprite mouse;
	private Sprite r;

	// enemy sprites
	private List<Enemy> eList;
	private Enemy boss1;
	private Enemy boss2;
	private Enemy boss3;
	private Enemy boss4;
	private Enemy sb1;
	private Enemy sb2;
	private Enemy FinalBoss;
	
	// bricks
	//private Sprite brick;
	private Sprite brick1;
	//private Sprite brick3;
	//private Sprite brick5;
	
	
	private SoundManager soundManager;

	// parameters
	private List<Boolean> roomVisited;
	double angle;
	final long threshold = 300;
	long lastShoot;

	private boolean rReleased;

	private DisplayObjectContainer startScreen;

	private Sprite gg;
	final long hdThreshold = 500;
	long lastDeduct = System.currentTimeMillis();
	long lastFreeze = System.currentTimeMillis();


	DisplayObject win;
	DisplayObject rainbow;

	static int VIEWPORT_SIZE_X = 1440;
	static int VIEWPORT_SIZE_Y = 900; // TODO: change

	public prototype() {
		super("prototype", VIEWPORT_SIZE_X, VIEWPORT_SIZE_Y);

		initializeGame();

		// Background music
		InputStream in;
		try {
//			in = new FileInputStream(new File("resources" + File.separator + "BGM2.wav"));
//			AudioStream audios = new AudioStream(in);
//			AudioPlayer.player.start(audios);
//			;
			AudioInputStream as1 = AudioSystem.getAudioInputStream(new BufferedInputStream(new java.io.FileInputStream("resources" + File.separator + "BGM2.wav")));
            AudioFormat af = as1.getFormat();
            Clip clip1 = AudioSystem.getClip();
            DataLine.Info info = new DataLine.Info(Clip.class, af);
            
            clip1.open(as1);
            clip1.loop(Clip.LOOP_CONTINUOUSLY);
            clip1.start();
            
		} catch (Exception e) {
			System.out.println("ERROR: BGM Fault (prototype.java)");
			JOptionPane.showMessageDialog(null, e);
		}

	}

	public void initializeGame() {
		
		roomVisited = new ArrayList<Boolean>();
		
		soundManager = new SoundManager();
		soundManager.LoadSoundEffect("ice", "resources//shootingstar.wav");
		soundManager.LoadSoundEffect("laser", "resources//lasergun.wav");
		soundManager.LoadSoundEffect("fire", "resources//punch.wav");
		soundManager.LoadSoundEffect("silencer", "resources//silencer.wav");
		soundManager.LoadSoundEffect("gotit", "resources//coincollect.wav");

		// I have added in things that were initialized before the constructor,
		// so they can be reset during restart game
		bList = new ArrayList<Bullet>();
		eList = new ArrayList<Enemy>();
		//coloredBallList = new ArrayList<Sprite>();
		rReleased = false;
		lastShoot = System.currentTimeMillis();
		startScreen = new DisplayObjectContainer("startScreen", "startScreen.png");

		// now for the rest of the stuff that was originally initialized in the
		// constructor
		gg = new Sprite("gameOver", "gameOver.png");

		this.getScenePanel().addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				//whiteLight.dispatchEvent(new StatusEvent(StatusEvent.CLEAR, whiteLight));
				angle = Math.atan2(e.getY() - (ball.getyPosition() + ball.getUnscaledHeight() / 2),
						e.getX() - (ball.getxPosition() + ball.getUnscaledWidth() / 2));
				long now = System.currentTimeMillis();
				if (now - lastShoot > threshold) {
					String currWeapon = ball.getCurrentWeapon();

					Bullet b = null;
					

					if (currWeapon.equals("basic")) {
						b = new Bullet("bullet", "bullet0.png", ball.getBasicWeaponAttack(), "basic");
						try {
							soundManager.PlaySoundEffect("laser");
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						} catch (UnsupportedAudioFileException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					} else if (currWeapon.equals("upgraded")) {
						b = new Bullet("bullet", "bullet2.png", ball.getUpgradedWeaponAttack(), "upgraded");
						try {
							soundManager.PlaySoundEffect("fire");
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						} catch (UnsupportedAudioFileException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					} else if (currWeapon.equals("freeze")) {
						b = new Bullet("bullet", "bullet3.png", ball.getFreezeWeaponAttack(), "freeze");
						try {
							soundManager.PlaySoundEffect("ice");
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						} catch (UnsupportedAudioFileException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					} else {
						System.out.println("ERROR: Unknown current weapon. (prototype.java)");
						System.exit(0);
					}

					b.setxPosition(ball.getxPosition() + ball.getUnscaledWidth() / 2 - b.getUnscaledWidth() / 2
							- map.getxPosition());
					b.setyPosition(ball.getyPosition() + ball.getUnscaledHeight() / 2 - b.getUnscaledHeight() / 2
							- map.getyPosition());
					b.setBulletSpeedX((int) (b.getBulletSpeedX() * Math.cos(angle)));
					b.setBulletSpeedY((int) (b.getBulletSpeedY() * Math.sin(angle)));
					b.setShoot(true);
					b.setVisible(true);
					//b.addCollisionObject(brick);
					b.addCollisionObject(brick1);
					//b.addCollisionObject(brick3);
					//b.addCollisionObject(brick5);
					for (Enemy enemy : eList) {
						if (enemy.getParent() != null)
							b.addCollisionObject(enemy);
					}
					for (RoomSprite room : map.getRooms()) {
						for (DisplayObject roomObj : room.getChildren()) {
							b.addCollisionObject(roomObj);
						}
					}
					bList.add(b);
					lastShoot = now;
				}

			}
		});
		gameParent = new DisplayObject("gameParent");

		// All objects that are not the player should be children of the map so
		// they move appropriately
		map = new MapSprite("map", 2, 8);
		map.setParent(gameParent);
		map.setxPosition(0);
		map.setyPosition(0);
		map.setRespondToKeys(true);
		this.addChild(map);
		for (DisplayObject child : map.getChildren()) {
			System.out.println(child.getId());
		}

		wasd = new Sprite("wasd", "wasd_keys.png");
		wasd.setxPosition(200);
		wasd.setyPosition(200);
		wasd.setScaleX(0.5f);
		wasd.setScaleY(0.5f);
		wasd.addEventListener(this, StatusEvent.VANISH);
		map.addChild(wasd);
		mouse = new Sprite("mouse", "mouse_left_click.png");
		mouse.setxPosition(1000);
		mouse.setyPosition(400);
		mouse.setScaleX(0.5f);
		mouse.setScaleY(0.5f);
		mouse.addEventListener(this, StatusEvent.VANISH);
		map.addChild(mouse);
		r = new Sprite("r", "key_r.png");
		r.setxPosition(200);
		r.setyPosition(600);
		r.setScaleX(0.5f);
		r.setScaleY(0.5f);
		r.addEventListener(this, StatusEvent.VANISH);
		map.addChild(r);

		// NOTE: if you change the id of the ball, update it in "Sprite" class's
		// collision code
		ball = new Player("ball", "whiteBall.png", map); // declare sprite
															// before adding it
															// as collision
															// object
		ball.setParent(gameParent);
		ball.setMap(map);
		ball.setxPosition(620);
		ball.setyPosition(350);
		ball.setHp(100);
		ball.setPivotPoint(new Point(ball.getxPosition() + ball.getUnscaledWidth() / 2,
				ball.getyPosition() + ball.getUnscaledHeight() / 2));
		ball.addEventListener(this, CollideEvent.COLLIDE_START);
		ball.addEventListener(this, StatusEvent.VANISH);
		this.addChild(ball);

		ballCenter = new Sprite("ballCenter", null);
		ballCenter.setxPosition(ball.getxPosition() + ball.getUnscaledWidth() / 2);
		ballCenter.setyPosition(ball.getyPosition() + ball.getUnscaledHeight() / 2);
		this.addChild(ballCenter);

		// Set up ColorBalls
		redBall = new ColorBall("redBall", "red_ball.png", "red");
		redBall.setxPosition(700);
		redBall.setyPosition(700);
		redBall.addEventListener(this, PickedUpEvent.RED_BALL_PICKED_UP);
		map.addChild(redBall);
		note = new Sprite("note", "double damage.png");
		note.setxPosition(redBall.getxPosition()+redBall.getUnscaledWidth()/2-note.getUnscaledWidth()/2);
		note.setyPosition(redBall.getyPosition()+100);
		note.addEventListener(this, StatusEvent.APPEAR);
		note.addEventListener(this, StatusEvent.VANISH);
		map.addChild(note);

		orangeBall = new ColorBall("orangeBall", "orange_ball.png", "orange");
		orangeBall.setScaleX(0.01f);
		orangeBall.setScaleY(0.01f);
		orangeBall.addEventListener(this, PickedUpEvent.ORANGE_BALL_PICKED_UP);

		blueBall = new ColorBall("blueBall", "blue_ball.png", "blue");
		blueBall.setScaleX(0.01f);
		blueBall.setScaleY(0.01f);
		blueBall.addEventListener(this, PickedUpEvent.BLUE_BALL_PICKED_UP);

		greenBall = new ColorBall("greenBall", "green_ball.png", "green");
		greenBall.setScaleX(0.01f);
		greenBall.setScaleY(0.01f);
		greenBall.addEventListener(this, PickedUpEvent.GREEN_BALL_PICKED_UP);

		yellowBall = new ColorBall("yellowBall", "yellow_ball.png", "yellow");
		yellowBall.setScaleX(0.01f);
		yellowBall.setScaleY(0.01f);
		yellowBall.addEventListener(this, PickedUpEvent.YELLOW_BALL_PICKED_UP);
		
		purpleBall = new ColorBall("purpleBall", "purple_ball.png", "purple");
		purpleBall.setScaleX(0.01f);
		purpleBall.setScaleY(0.01f);
		purpleBall.addEventListener(this, PickedUpEvent.PURPLE_BALL_PICKED_UP);
		
		
		// whiteLight
		whiteLight = new Sprite("whiteLight", "whiteLight.png");
		whiteLight.setxPosition(1500);
		whiteLight.setyPosition(7500);
		whiteLight.setAlpha(0);
		whiteLight.addEventListener(this, StatusEvent.CLEAR);
		map.addChild(whiteLight);
		

		// Set up enemies
		boss1 = new Enemy("boss1", "smog_cloud.png");
		boss1.setxPosition(1000);
		boss1.setyPosition(2500);
		boss1.setScaleX(0.1f);
		boss1.setScaleY(0.1f);
		boss1.setHp(150);
		boss1.setMaxHP(150);
		boss1.addEventListener(this, StatusEvent.VANISH);

		boss2 = new Enemy("boss2", "lighting_cloud.png");
		boss2.setxPosition(2000);
		boss2.setyPosition(1500);
		boss2.setScaleX(0.1f);
		boss2.setScaleY(0.1f);
		boss2.setHp(150);
		boss2.setMaxHP(150);
		boss2.addEventListener(this, StatusEvent.VANISH);
		
		boss3 = new Enemy("boss3", "weird_cloud.png");
		boss3.setxPosition(1000);
		boss3.setyPosition(4500);
		boss3.setScaleX(0.1f);
		boss3.setScaleY(0.1f);
		boss3.setHp(200);
		boss3.setMaxHP(200);
		boss3.addEventListener(this, StatusEvent.VANISH);
		
		boss4 = new Enemy("boss4", "angry_cloud.png");
		boss4.setxPosition(2000);
		boss4.setyPosition(5500);
		boss4.setScaleX(0.1f);
		boss4.setScaleY(0.1f);
		boss4.setHp(200);
		boss4.setMaxHP(200);
		boss4.addEventListener(this, StatusEvent.VANISH);
		
		sb1 = new Enemy("smallBoss4", "angry_cloud_2.png");
		//sb1.setxPosition(boss4.getxPosition()+150);
		//sb1.setyPosition(boss4.getyPosition());
		sb1.setScaleX(0.1f);
		sb1.setScaleY(0.1f);
		sb1.setHp(100);
		sb1.setMaxHP(100);
		sb1.addEventListener(this, StatusEvent.VANISH);
		sb1.addEventListener(this, StatusEvent.APPEAR);
		
		sb2 = new Enemy("smallBoss4", "angry_cloud_2.png");
		//sb2.setxPosition(boss4.getxPosition()-150);
		//sb2.setyPosition(boss4.getyPosition());
		sb2.setScaleX(0.1f);
		sb2.setScaleY(0.1f);
		sb2.setHp(100);
		sb2.setMaxHP(100);
		sb2.addEventListener(this, StatusEvent.VANISH);
		sb2.addEventListener(this, StatusEvent.APPEAR);
		
		FinalBoss = new Enemy("finalBoss", "FinalBoss.png");
		FinalBoss.setxPosition(2000);
		FinalBoss.setyPosition(7500);
		FinalBoss.setScaleX(0.1f);
		FinalBoss.setScaleY(0.1f);
		FinalBoss.setHp(250);
		FinalBoss.setMaxHP(250);
		FinalBoss.setShootThreshold(500);
		FinalBoss.addEventListener(this, StatusEvent.VANISH);
		
		
		// roomVisited setup
		for (int i = 0; i < 16; i++) {
			roomVisited.add(false);
		}
		
		// bricks
		/*brick = new Sprite("brick", "block.png");
		brick.setxPosition(2100);
		brick.setyPosition(400);
		ball.addCollisionObject(brick);
		boss1.addCollisionObject(brick);
		boss2.addCollisionObject(brick);
		map.addChild(brick);*/
		
		brick1 = new Sprite("brick1", "block.png");
		brick1.setxPosition(700);
		brick1.setyPosition(2300);
		ball.addCollisionObject(brick1);
		boss1.addCollisionObject(brick1);
		boss2.addCollisionObject(brick1);
		boss3.addCollisionObject(brick1);
		boss4.addCollisionObject(brick1);
		sb1.addCollisionObject(brick1);
		sb2.addCollisionObject(brick1);
		FinalBoss.addCollisionObject(brick1);
		map.addChild(brick1);
		
		/*brick3 = new Sprite("brick3", "block.png");
		brick3.setxPosition(2100);
		brick3.setyPosition(4400);
		ball.addCollisionObject(brick3);
		boss3.addCollisionObject(brick3);
		boss4.addCollisionObject(brick3);
		sb1.addCollisionObject(brick3);
		sb2.addCollisionObject(brick3);
		map.addChild(brick3);
		
		brick5 = new Sprite("brick5", "block.png");
		brick5.setxPosition(2100);
		brick5.setyPosition(6300);
		ball.addCollisionObject(brick5);
		FinalBoss.addCollisionObject(brick5);
		map.addChild(brick5);*/

		h = new HealthBar("ball_healthBar");
		h.setBarWidth(1440);
		h.setBarHeight(30);
		h.setDrawHealth(1436);
		h.setxPosition(-ball.getxPosition() - ball.getUnscaledWidth() / 2);
		h.setyPosition(-ball.getyPosition() - ball.getUnscaledHeight() / 2);
		ballCenter.addChild(h);

		gg.setAlpha(0);
		gg.setxPosition(-gg.getUnscaledWidth() / 2);
		gg.setyPosition(-gg.getUnscaledHeight() / 2);
		ballCenter.addChild(gg);

		for (RoomSprite room : map.getRooms()) { // add walls as enemy's
													// collidable objects
			for (DisplayObject roomObj : room.getChildren()) {
				boss1.addCollisionObject(roomObj);
				boss2.addCollisionObject(roomObj);
				boss3.addCollisionObject(roomObj);
				boss4.addCollisionObject(roomObj);
				sb1.addCollisionObject(roomObj);
				sb2.addCollisionObject(roomObj);
				FinalBoss.addCollisionObject(roomObj);
			}
		}

		
		this.addChild(startScreen);	
		
		rainbow = new DisplayObject("rainbow", "rainbow_background.jpg");
		rainbow.setAlpha(0);
		this.addChild(rainbow);
		
		win = new DisplayObject("winimage", "winning.png");
		this.addChild(win);
		win.setVisible(false);
		
	}
	
	//make everything null before restart
	public void removeEventListeners(DisplayObjectContainer doc)
	{
		for(DisplayObject child : doc.getChildren())
		{
			if(child instanceof DisplayObjectContainer && ((DisplayObjectContainer)child).getChildren() != null)
			{
				removeEventListeners((DisplayObjectContainer)child);
			}
			else
			{
				child.clearEventListener();
			}
		}
		
	}
	
	public void restartGame()
	{
		removeEventListeners(this);
		roomVisited.clear();
		
		
		for(DisplayObject child: ballCenter.getChildren())
		{
			if(!child.getId().equals("ball_healthBar"))
			{
				child.setVisible(false);
			}
		}
		
		/*soundManager = new SoundManager();
		soundManager.LoadSoundEffect("ice", "resources//shootingstar.wav");
		soundManager.LoadSoundEffect("laser", "resources//lasergun.wav");
		soundManager.LoadSoundEffect("fire", "resources//punch.wav");
		soundManager.LoadSoundEffect("silencer", "resources//silencer.wav");
		soundManager.LoadSoundEffect("gotit", "resources//coincollect.wav");*/

		// I have added in things that were initialized before the constructor,
		// so they can be reset during restart game
		bList.clear();// = new ArrayList<Bullet>();
		eList.clear();// = new ArrayList<Enemy>();
//		/coloredBallList.clear();// = new ArrayList<Sprite>();
		rReleased = false;
		lastShoot = System.currentTimeMillis();
		//startScreen = new DisplayObjectContainer("startScreen", "startScreen.png");

		// now for the rest of the stuff that was originally initialized in the
		// constructor
		//gg = new Sprite("gameOver", "gameOver.png");
		

		/*this.getScenePanel().addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				angle = Math.atan2(e.getY() - (ball.getyPosition() + ball.getUnscaledHeight() / 2),
						e.getX() - (ball.getxPosition() + ball.getUnscaledWidth() / 2));
				long now = System.currentTimeMillis();
				if (now - lastShoot > threshold) {
					String currWeapon = ball.getCurrentWeapon();

					Bullet b = null;
					

					if (currWeapon.equals("basic")) {
						b = new Bullet("bullet", "bullet0.png", ball.getBasicWeaponAttack(), "basic");
						try {
							soundManager.PlaySoundEffect("laser");
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						} catch (UnsupportedAudioFileException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					} else if (currWeapon.equals("upgraded")) {
						b = new Bullet("bullet", "bullet2.png", ball.getUpgradedWeaponAttack(), "upgraded");
						try {
							soundManager.PlaySoundEffect("fire");
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						} catch (UnsupportedAudioFileException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					} else if (currWeapon.equals("freeze")) {
						b = new Bullet("bullet", "bullet3.png", ball.getFreezeWeaponAttack(), "freeze");
						try {
							soundManager.PlaySoundEffect("ice");
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						} catch (UnsupportedAudioFileException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					} else {
						System.out.println("ERROR: Unknown current weapon. (prototype.java)");
						System.exit(0);
					}

					b.setxPosition(ball.getxPosition() + ball.getUnscaledWidth() / 2 - b.getUnscaledWidth() / 2
							- map.getxPosition());
					b.setyPosition(ball.getyPosition() + ball.getUnscaledHeight() / 2 - b.getUnscaledHeight() / 2
							- map.getyPosition());
					b.setBulletSpeedX((int) (b.getBulletSpeedX() * Math.cos(angle)));
					b.setBulletSpeedY((int) (b.getBulletSpeedY() * Math.sin(angle)));
					b.setShoot(true);
					b.setVisible(true);
					b.addCollisionObject(brick);
					for (Enemy enemy : eList) {
						if (enemy.getParent() != null)
							b.addCollisionObject(enemy);
					}
					for (RoomSprite room : map.getRooms()) {
						for (DisplayObject roomObj : room.getChildren()) {
							b.addCollisionObject(roomObj);
						}
					}
					bList.add(b);
					lastShoot = now;
				}

			}
		});*/
		//gameParent = new DisplayObject("gameParent");

		// All objects that are not the player should be children of the map so
		// they move appropriately
		//*******************
		//map = new MapSprite("map", 2, 8);
		//map.setParent(gameParent);
		map.resetMap();
		//map.setxPosition(0);
		//map.setyPosition(0);
		//map.setRespondToKeys(true);
		//this.addChild(map);
		/*for (DisplayObject child : map.getChildren()) {
			System.out.println(child.getId());
		}*/

		//wasd = new Sprite("wasd", "wasd_keys.png");
		//wasd.setxPosition(200);
		//wasd.setyPosition(200);
		//wasd.setScaleX(0.5f);
		//wasd.setScaleY(0.5f);
		wasd.addEventListener(this, StatusEvent.VANISH);
		wasd.setVisible(true);
		wasd.setAlpha(1.0f);
		map.addChild(wasd);
		//mouse = new Sprite("mouse", "mouse_left_click.png");
		//mouse.setxPosition(1000);
		//mouse.setyPosition(400);
		//mouse.setScaleX(0.5f);
		//mouse.setScaleY(0.5f);
		mouse.addEventListener(this, StatusEvent.VANISH);
		map.addChild(mouse);
		mouse.setVisible(true);
		mouse.setAlpha(1.0f);
		//r = new Sprite("r", "key_r.png");
		//r.setxPosition(200);
		//r.setyPosition(600);
		//r.setScaleX(0.5f);
		//r.setScaleY(0.5f);
		r.addEventListener(this, StatusEvent.VANISH);
		map.addChild(r);
		r.setVisible(true);
		r.setAlpha(1.0f);

		// NOTE: if you change the id of the ball, update it in "Sprite" class's
		// collision code
		ball.resetPlayerNew("whiteBall.png", 100);
		ball.setCurrentRoomId(0);
		ball.setVisible(true);
		ball.setAlpha(1.0f);
		ball.setScaleX(1.0f);
		ball.setScaleY(1.0f);
															// before adding it
															// as collision
															// object
		//ball.setParent(gameParent);
		//ball.setMap(map);
		//ball.setxPosition(620);
		//ball.setyPosition(350);
		//ball.setHp(100);
		//ball.setPivotPoint(new Point(ball.getxPosition() + ball.getUnscaledWidth() / 2,
		//		ball.getyPosition() + ball.getUnscaledHeight() / 2));
		ball.addEventListener(this, CollideEvent.COLLIDE_START);
		ball.addEventListener(this, StatusEvent.VANISH);
		//this.addChild(ball);

		//ballCenter = new Sprite("ballCenter", null);
		//ballCenter.setxPosition(ball.getxPosition() + ball.getUnscaledWidth() / 2);
		//ballCenter.setyPosition(ball.getyPosition() + ball.getUnscaledHeight() / 2);
		//this.addChild(ballCenter);

		// Set up ColorBalls
		redBall.addEventListener(this, PickedUpEvent.RED_BALL_PICKED_UP);
		map.addChild(redBall);
		redBall.setVisible(true);
		/*note.setImage("double damage.png");
		note.setxPosition(redBall.getxPosition()+redBall.getUnscaledWidth()/2-note.getUnscaledWidth()/2);
		note.setyPosition(redBall.getyPosition()+100);*/

		orangeBall.setScaleX(0.01f);
		orangeBall.setScaleY(0.01f);
		orangeBall.setVisible(true);
		orangeBall.addEventListener(this, PickedUpEvent.ORANGE_BALL_PICKED_UP);
		orangeBall.setxPosition(-5000);

		blueBall.setScaleX(0.01f);
		blueBall.setScaleY(0.01f);
		blueBall.setVisible(true);
		blueBall.addEventListener(this, PickedUpEvent.BLUE_BALL_PICKED_UP);
		blueBall.setxPosition(-5000);

		greenBall.setScaleX(0.01f);
		greenBall.setScaleY(0.01f);
		greenBall.setVisible(true);
		greenBall.addEventListener(this, PickedUpEvent.GREEN_BALL_PICKED_UP);
		greenBall.setxPosition(-5000);

		yellowBall.setScaleX(0.01f);
		yellowBall.setScaleY(0.01f);
		yellowBall.setVisible(true);
		yellowBall.addEventListener(this, PickedUpEvent.YELLOW_BALL_PICKED_UP);
		yellowBall.setxPosition(-5000);
		
		purpleBall.setScaleX(0.01f);
		purpleBall.setScaleY(0.01f);
		purpleBall.setVisible(true);
		purpleBall.addEventListener(this, PickedUpEvent.YELLOW_BALL_PICKED_UP);
		purpleBall.setxPosition(-5000);
		

		// Set up enemies
		boss1.resetEnemy(1000, 2500, 150);// = new Enemy("boss1", "smog_cloud.png");
		boss1.setScaleX(0.1f);
		boss1.setScaleY(0.1f);
		boss1.setAlpha(1.0f);
		boss1.setHp(150);
		boss1.setMaxHP(150);
		boss1.addEventListener(this, StatusEvent.VANISH);
		// boss1.addCollisionObject(ball);
		// map.addChild(boss1);

		boss2.resetEnemy(2000, 1500, 150);// = new Enemy("boss2", "lighting_cloud.png");
		boss2.setScaleX(0.1f);
		boss2.setScaleY(0.1f);
		boss2.setAlpha(1.0f);
		boss2.setHp(150);
		boss2.setMaxHP(150);
		boss2.addEventListener(this, StatusEvent.VANISH);
		//map.addChild(boss2);
		
		boss3.resetEnemy(1000, 4500, 200);// = new Enemy("boss3", "weird_cloud.png");
		boss3.setScaleX(0.1f);
		boss3.setScaleY(0.1f);
		boss3.setAlpha(1.0f);
		boss3.setHp(200);
		boss3.setMaxHP(200);
		boss3.addEventListener(this, StatusEvent.VANISH);
		
		boss4.resetEnemy(2000, 5500, 200);// = new Enemy("boss4", "angry_cloud.png");
		boss4.setScaleX(0.1f);
		boss4.setScaleY(0.1f);
		boss4.setAlpha(1.0f);
		boss4.setHp(200);
		boss4.setMaxHP(200);
		boss4.addEventListener(this, StatusEvent.VANISH);
		
		sb1.resetEnemy(0, 0, 100);// = new Enemy("smallBoss4", "angry_cloud_2.png");
		//sb1.setxPosition(boss4.getxPosition()+150);
		//sb1.setyPosition(boss4.getyPosition());
		sb1.setScaleX(0.1f);
		sb1.setScaleY(0.1f);
		sb1.setHp(100);
		sb1.setMaxHP(100);
		sb1.addEventListener(this, StatusEvent.VANISH);
		sb1.addEventListener(this, StatusEvent.APPEAR);
		
		sb2.resetEnemy(0, 0, 100); //= new Enemy("smallBoss4", "angry_cloud_2.png");
		//sb2.setxPosition(boss4.getxPosition()-150);
		//sb2.setyPosition(boss4.getyPosition());
		sb2.setScaleX(0.1f);
		sb2.setScaleY(0.1f);
		sb2.setHp(100);
		sb2.setMaxHP(100);
		sb2.addEventListener(this, StatusEvent.VANISH);
		sb2.addEventListener(this, StatusEvent.APPEAR);
		
		FinalBoss.resetEnemy(2000, 7500, 300);// = new Enemy("finalBoss", "FinalBoss.png");
		FinalBoss.setScaleX(0.1f);
		FinalBoss.setScaleY(0.1f);
		FinalBoss.setHp(250);
		FinalBoss.setMaxHP(250);
		FinalBoss.addEventListener(this, StatusEvent.VANISH);
		
		
		// roomVisited setup
		for (int i = 0; i < 16; i++) {
			roomVisited.add(false);
		}

		//brick = new Sprite("brick", "bricks.png");
		//brick.setxPosition(1800);
		//brick.setyPosition(400);
		//ball.addCollisionObject(brick);
		//boss1.addCollisionObject(brick);
		//boss2.addCollisionObject(brick);
		//map.addChild(brick);
		map.addChild(brick1);
		//map.addChild(brick3);
		//map.addChild(brick5);

		//h = new HealthBar("ball_healthBar");
		h.setBarWidth(1440);
		h.setBarHeight(30);
		h.setDrawHealth(1436);
		h.setxPosition(-ball.getxPosition() - ball.getUnscaledWidth() / 2);
		h.setyPosition(-ball.getyPosition() - ball.getUnscaledHeight() / 2);
		//ballCenter.addChild(h);

		gg.setAlpha(0);
		gg.setVisible(true);
		gg.setxPosition(-gg.getUnscaledWidth() / 2);
		gg.setyPosition(-gg.getUnscaledHeight() / 2);
		//ballCenter.addChild(gg);

		/*for (RoomSprite room : map.getRooms()) { // add walls as enemy's
													// collidable objects
			for (DisplayObject roomObj : room.getChildren()) {
				boss1.addCollisionObject(roomObj);
				boss2.addCollisionObject(roomObj);
				boss3.addCollisionObject(roomObj);
				boss4.addCollisionObject(roomObj);
				sb1.addCollisionObject(roomObj);
				sb2.addCollisionObject(roomObj);
				FinalBoss.addCollisionObject(roomObj);
			}
		}*/

		
		//this.addChild(startScreen);	
		
		//win = new DisplayObject("winimage", "winning.png");
		//this.addChild(win);
		win.setVisible(false);
		
		startScreen.setVisible(false);
		gameState = GameState.PLAY;
	}


	private boolean gotIt(Sprite coloredBall) {
		return (Math
				.pow((ball.getxPosition() + ball.getUnscaledWidth() / 2)
						- (coloredBall.getxPosition() + map.getxPosition() + coloredBall.getUnscaledWidth() / 2), 2)
				+ Math.pow((ball.getyPosition() + ball.getUnscaledHeight() / 2)
						- (coloredBall.getyPosition() + map.getyPosition() + coloredBall.getUnscaledHeight() / 2),
						2)) < 2500;
	}

	@Override
	public void update(ArrayList<String> pressedKeys) {
		super.update(pressedKeys);
		if (gameState == null)
		{
			return;
		}
		switch(gameState)
		{
			case START:
				if(pressedKeys.size() > 0)
				{
					startScreen.setVisible(false);
					gameState = GameState.PLAY;
				}
				break;
			case PLAY:
				if (redBall != null && ball != null && boss1 != null && boss2 != null && boss3 != null && boss4 != null && FinalBoss != null && h != null) {
					
					
					// rotate the ball
					ball.setRotation(ball.getRotation() + Math.PI / 60);

					// instruction
					if (ball.getCurrentRoomId() != 0) {
						wasd.dispatchEvent(new StatusEvent(StatusEvent.VANISH, wasd)); // make wasd dissapears
						mouse.dispatchEvent(new StatusEvent(StatusEvent.VANISH, mouse)); // make mouse dissapears
						r.dispatchEvent(new StatusEvent(StatusEvent.VANISH, r)); // r key dissapears
					}
					
					
					// generate enemies
					for (int a = 1; a < 16; a++) {
						if (ball.getCurrentRoomId() == 4 && eList.isEmpty() && boss1.isAlive()) { // boss1 appears
							boss1.addEventListener(this, StatusEvent.APPEAR);
							map.addChild(boss1);
							//ball.addCollisionObject(boss1);
							eList.add(boss1);
							roomVisited.set(4, true);
						}
						else if (ball.getCurrentRoomId() == 3 && eList.isEmpty() && boss2.isAlive()) { // boss2 appears
							boss2.addEventListener(this, StatusEvent.APPEAR);
							map.addChild(boss2);
							eList.add(boss2);
							roomVisited.set(3, true);
							}
						else if (ball.getCurrentRoomId() == 8 && eList.isEmpty() && boss3.isAlive()) { // boss3 appears
							boss3.addEventListener(this, StatusEvent.APPEAR);
							map.addChild(boss3);
							//ball.addCollisionObject(boss1);
							eList.add(boss3);
							roomVisited.set(8, true);
						}
						else if (ball.getCurrentRoomId() == 11 && eList.isEmpty()) { // boss4 appears
							boss4.addEventListener(this, StatusEvent.APPEAR);
							map.addChild(boss4);
							eList.add(boss4);
							if (!boss4.isAlive() && sb1.isAlive() && sb2.isAlive()) {
								sb1.setxPosition(boss4.getxPosition()+boss4.getUnscaledWidth()+150);
								sb1.setyPosition(boss4.getyPosition());
								sb2.setxPosition(boss4.getxPosition()+boss4.getUnscaledWidth()-150);
								sb2.setyPosition(boss4.getyPosition());
								map.addChild(sb1);
								map.addChild(sb2);
								eList.add(sb1);
								eList.add(sb2);
								}
							roomVisited.set(11, true);
						}
						else if ((ball.getCurrentRoomId() == 14 || ball.getCurrentRoomId() == 15) && eList.isEmpty()) {
							FinalBoss.addEventListener(this, StatusEvent.APPEAR);
							FinalBoss.addEventListener(this, StatusEvent.CLEAR);
							map.addChild(FinalBoss);
							eList.add(FinalBoss);
							roomVisited.set(14, true);
							roomVisited.set(15, true);
						}
						else if (ball.getCurrentRoomId() == a && eList.isEmpty() && !roomVisited.get(a)) {
							roomVisited.set(a, true);
							for (int i = 0; i < 3; i++) {
								Enemy e = new Enemy("cloud", "cloud.png");
								e.setxPosition((int)(Math.random()*1160+80+(a%2)*1460));
								e.setyPosition((int)(Math.random()*840+80+(a/2)*1000));
								e.setScaleX(0.1f);
								e.setScaleY(0.1f);
								e.setHp(30);
								e.setMaxHP(30);
								e.setEnemyAttack(2);
								e.addEventListener(this, StatusEvent.VANISH);
								e.addEventListener(this, StatusEvent.APPEAR);
								//e.addCollisionObject(brick);
								e.addCollisionObject(brick1);
								//e.addCollisionObject(brick3);
								//e.addCollisionObject(brick5);
								for (RoomSprite room : map.getRooms()) {
									for (DisplayObject roomObj : room.getChildren()) {
										e.addCollisionObject(roomObj);
									}
								}
								map.addChild(e);
								eList.add(e);
								}
							}
						if (!eList.isEmpty()) {
							
							Iterator<Enemy> iter = eList.iterator();
							while (iter.hasNext()) {
								Enemy e = iter.next();
								if (!e.isAlive()) {
									if (e.getId() == "boss1") {
										blueBall.setxPosition(e.getxPosition()+e.getUnscaledWidth()/2);
										blueBall.setyPosition(e.getyPosition()+e.getUnscaledHeight()/2);
										note.setImage("freeze.png");
										note.setxPosition(blueBall.getxPosition()+blueBall.getUnscaledWidth()/2-note.getUnscaledWidth()/2);
										note.setyPosition(blueBall.getyPosition()+100);
										map.addChild(blueBall);
									}
									if (e.getId() == "boss2") {
										orangeBall.setxPosition(e.getxPosition()+e.getUnscaledWidth()/2);
										orangeBall.setyPosition(e.getyPosition()+e.getUnscaledHeight()/2);
										note.setImage("haste.png");
										note.setxPosition(orangeBall.getxPosition()+orangeBall.getUnscaledWidth()/2-note.getUnscaledWidth()/2);
										note.setyPosition(orangeBall.getyPosition()+100);
										map.addChild(orangeBall);
									}
									if (e.getId() == "boss3") {
										greenBall.setxPosition(e.getxPosition()+e.getUnscaledWidth()/2);
										greenBall.setyPosition(e.getyPosition()+e.getUnscaledHeight()/2);
										note.setImage("regeneration.png");
										note.setxPosition(greenBall.getxPosition()+greenBall.getUnscaledWidth()/2-note.getUnscaledWidth()/2);
										note.setyPosition(greenBall.getyPosition()+100);
										map.addChild(greenBall);
									}
									if (e.getId() == "boss4") {
										yellowBall.setxPosition(e.getxPosition()+e.getUnscaledWidth()/2);
										yellowBall.setyPosition(e.getyPosition()+e.getUnscaledHeight()/2);
										note.setImage("maxHP.png");
										note.setxPosition(yellowBall.getxPosition()+yellowBall.getUnscaledWidth()/2-note.getUnscaledWidth()/2);
										note.setyPosition(yellowBall.getyPosition()+100);
										map.addChild(yellowBall);
									}
									if (e.getId() == "finalBoss") {
										purpleBall.setxPosition(e.getxPosition()+e.getUnscaledWidth()/2);
										purpleBall.setyPosition(e.getyPosition()+e.getUnscaledHeight()/2);
										map.addChild(purpleBall);
									}
									
									iter.remove();
								}
							}
						}
					}
					
					// melee attack
					for (Enemy e : eList) {
						
						for (Enemy other : eList) {
							if (!e.equals(other) && other.isAlive()) {
								e.addCollisionObject(other);
							}
						}
						
						if (e.getId() == "boss1") {
							if (e.isKeepShooting()) {
								e.shoot(ball, soundManager);
							}
						}
						else {
							if (e.isKeepChasing()) {
								e.chase(ball);
								if (e.getId() == "boss3") {
									e.speedUp();
								}
								if (e.getId() == "finalBoss") {
									if (e.isKeepShooting()) {
										e.shoot(ball, soundManager);
									}
									e.teleport(ball);
								}
							}
								
						}
						// Enemy Melee Attack
						if (e.collideWith(ball)) {
							long now = System.currentTimeMillis();
							if (now - lastDeduct > hdThreshold) {
								ball.setHealth(ball.getHealth()-((Enemy)e).getEnemyAttack());
								lastDeduct = now;
							}
							
							}
					}
					
					//unlock rooms as needed
					if(this.eList.isEmpty() && ball.getNumVisitedRooms() == map.getUnlockedRooms().size() && map.getUnlockedRooms().size()%2 == 0)
					{
						map.unlockNext(ball.getNumVisitedRooms());
					}
					
					// Update speed to ball's speed
					map.setSpeed(ball.getSpeed());
		
					
					// Switch weapon
					//System.out.println(ball.getCurrentWeapon() + "\tFreezeUnlocked: " + ball.isFreezeWeaponUnlocked()
						//	+ "\tUpgradedUnlocked: " + ball.isUpgradedWeaponUnlocked() + "\tFreezeAtk: " + ball.getFreezeWeaponAttack() + "\tUpgradedAtk: " + ball.getUpgradedWeaponAttack());
		
					if (pressedKeys.contains("R") && rReleased) {
						if (ball.getCurrentWeapon().equals("basic")) {
							if (ball.isUpgradedWeaponUnlocked()) {
								ball.setCurrentWeapon("upgraded");
								ball.setImage("Fireball.png");
							} else if (ball.isFreezeWeaponUnlocked()) {
								ball.setCurrentWeapon("freeze");
								ball.setImage("IceBall.png");
							}
						} else if (ball.getCurrentWeapon().equals("upgraded")) {
							if (ball.isFreezeWeaponUnlocked()) {
								ball.setCurrentWeapon("freeze");
								ball.setImage("IceBall.png");
							} else {
								ball.setCurrentWeapon("basic");
								ball.setImage("whiteBall.png");
							}
						} else if (ball.getCurrentWeapon().equals("freeze")) {
							ball.setCurrentWeapon("basic");
							ball.setImage("whiteBall.png");
						}
						rReleased = false;
					} else if (!pressedKeys.contains("R"))
						rReleased = true;
		
					
		
					// ColorBall pick up events
					if (gotIt(redBall) && redBall.isVisible()) {
						coinCollect();
						redBall.dispatchEvent(new PickedUpEvent(PickedUpEvent.RED_BALL_PICKED_UP, redBall));
						note.dispatchEvent(new StatusEvent(StatusEvent.VANISH, note));
					}
					if (gotIt(orangeBall) && orangeBall.isVisible()) {
						coinCollect();
						orangeBall.dispatchEvent(new PickedUpEvent(PickedUpEvent.ORANGE_BALL_PICKED_UP, orangeBall));
						note.dispatchEvent(new StatusEvent(StatusEvent.VANISH, note));
					}
					if (gotIt(blueBall) && blueBall.isVisible()) {
						coinCollect();
						blueBall.dispatchEvent(new PickedUpEvent(PickedUpEvent.BLUE_BALL_PICKED_UP, blueBall));
						note.dispatchEvent(new StatusEvent(StatusEvent.VANISH, note));
					}
					if (gotIt(greenBall) && greenBall.isVisible()) {
						coinCollect();
						greenBall.dispatchEvent(new PickedUpEvent(PickedUpEvent.GREEN_BALL_PICKED_UP, greenBall));
						note.dispatchEvent(new StatusEvent(StatusEvent.VANISH, note));
					}
					if (gotIt(yellowBall) && yellowBall.isVisible()) {
						coinCollect();
						yellowBall.dispatchEvent(new PickedUpEvent(PickedUpEvent.YELLOW_BALL_PICKED_UP, yellowBall));
						note.dispatchEvent(new StatusEvent(StatusEvent.VANISH, note));
					}
					if (gotIt(purpleBall) && purpleBall.isVisible()) {
						coinCollect();
						purpleBall.dispatchEvent(new PickedUpEvent(PickedUpEvent.PURPLE_BALL_PICKED_UP, purpleBall));
					}
		
		
					// bullet setup
					Iterator<Bullet> iter = bList.iterator();
					while (iter.hasNext()) {
						Bullet b = iter.next();
						for (RoomSprite room : map.getRooms()) {
							for (DisplayObject roomObj : room.getChildren()) {
								b.addCollisionObject(roomObj);
							}
						}
						map.addChild(b);
						for (DisplayObject s : b.collidableObjects) {
							if (s.getParent() != null && b.collideWith(s) && s.getAlpha() == 1) {
								map.removeChild(b);
								
								// Freeze effect
								if (s.getClass().getName() == "edu.virginia.engine.display.Enemy") {
									
									if (ball.getCurrentWeapon() == "freeze"  ) {
										if (((Enemy)s).getEnemySpeed() > ((Enemy)s).getEnemyMaxSpeed()*0.5) {
											((Enemy)s).setEnemySpeed((int)(((Enemy)s).getEnemySpeed() * 0.8));
										}
										//((Enemy)s).setEnemySpeed(5);
									}
								}

								s.setHp(s.getHp() - b.getDamage());
								iter.remove();
								break;
							}
						}
					}
					
					if(FinalBoss.isAlive() == false && whiteLight.getAlpha() == 0)
					{
						Tween whiteLightAppear = new Tween(whiteLight);
						whiteLightAppear.animate(TweenableParams.ALPHA, whiteLight.getAlpha(), 1, 1000);
						TweenJuggler.getInstance().add(whiteLightAppear);
					}
					
					if (gotIt(whiteLight) && ball.isUltimateWeaponUnlocked() && rainbow.getAlpha() == 0) {
						whiteLight.dispatchEvent(new StatusEvent(StatusEvent.CLEAR, whiteLight));
					}
					
					if (rainbow.getAlpha() > 0.999f) {
						gameState = GameState.WIN;
					}
		
					
					// update ball's health
					h.setDrawHealth((int) (1436 * ball.getHealth() / ball.getMaxHealth()));
		
					
					// tweening
					TweenJuggler.getInstance().nextFrame();
		
				}
				
				
				//  gameOver mechanism
				if (ball != null && ball.getHealth() <= 0) { // once health is empty, the
															// ball can no longer move
					for (Enemy e : eList) {
						map.removeChild(e);
					}
					map.setRespondToKeys(false);
				}

				if (gg != null && gg.getAlpha() >= 0.9){//9) { // stop the game when
															// gameOver shows
					gameState = GameState.GAME_OVER;
				}
				break;
			case GAME_OVER: 
				//System.out.println("gameover");
				//TODO: add text saying like 'press any key to restart'
				if(pressedKeys.size() > 0)
				{
					gg.setVisible(false);
					gameState = GameState.RESTART;
				}
				break;
			case RESTART:
				System.out.println("restart");
				//initializeGame();
				restartGame(); //TODO: implement this method
				//need to reset all array lists / sprites to be drawn
				//declare before init method, then init
				//rooms getting redrawn or something
				//"Exception in thread "AWT-EventQueue-0" java.lang.OutOfMemoryError: Java heap space"
				//TODO: only reinit what needs to be reinit - make sure not adding to heap space
				//make a restartGame() method instead of using the initialize method?
				
				break;
			case WIN:
				//go here after final boss destroyed
				//will be short winning animation sequence or sth
				win.setVisible(true);
				break;
		}

	}

	@Override
	public void draw(Graphics g) {
		g.drawImage(background, 0, 0, null);
		super.draw(g);
		
	}

	public static void main(String[] args) {
		prototype p = new prototype();
		p.start();
	}
	
	public void coinCollect()
	{
		try {
			soundManager.PlaySoundEffect("gotit");
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (UnsupportedAudioFileException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
	}

	@Override
	public void handleEvent(Event event) {

		// Enemy tweening
		for (Enemy enemy : eList) {
			if (event.getSource() == enemy && event.getEventType() == "vanished") {
				enemy.setDead(new Tween(enemy));
				enemy.getDead().animate(TweenableParams.ALPHA, enemy.getAlpha(), 0, 1000);
				enemy.getDead().addEventListener(this, TweenEvent.TWEEN_COMPLETE_EVENT);
				TweenJuggler.getInstance().add(enemy.getDead());
			}

			if (event.getSource() == enemy && event.getEventType() == "appeared") {
				Tween enemyAppear = new Tween(enemy);
				enemyAppear.animate(TweenableParams.SCALE_X, enemy.getScaleX(), 1, 1000);
				enemyAppear.animate(TweenableParams.SCALE_Y, enemy.getScaleY(), 1, 1000);
				TweenJuggler.getInstance().add(enemyAppear);
			}
		}

		// coloredBall appear
		if (event.getSource() == boss1.getDead() && event.getEventType() == "TweenComplete") {
			Tween coloredBallAppear = new Tween(blueBall);
			coloredBallAppear.animate(TweenableParams.SCALE_X, blueBall.getScaleX(), 1, 1000);
			coloredBallAppear.animate(TweenableParams.SCALE_Y, blueBall.getScaleY(), 1, 1000);
			TweenJuggler.getInstance().add(coloredBallAppear);
			
			note.dispatchEvent(new StatusEvent(StatusEvent.APPEAR, note));
			
		}
		
		if (event.getSource() == boss2.getDead() && event.getEventType() == "TweenComplete") {
			Tween coloredBallAppear = new Tween(orangeBall);
			coloredBallAppear.animate(TweenableParams.SCALE_X, orangeBall.getScaleX(), 1, 1000);
			coloredBallAppear.animate(TweenableParams.SCALE_Y, orangeBall.getScaleY(), 1, 1000);
			TweenJuggler.getInstance().add(coloredBallAppear);
			
			note.dispatchEvent(new StatusEvent(StatusEvent.APPEAR, note));
		}
		
		if (event.getSource() == boss3.getDead() && event.getEventType() == "TweenComplete") {
			Tween coloredBallAppear = new Tween(greenBall);
			coloredBallAppear.animate(TweenableParams.SCALE_X, greenBall.getScaleX(), 1, 1000);
			coloredBallAppear.animate(TweenableParams.SCALE_Y, greenBall.getScaleY(), 1, 1000);
			TweenJuggler.getInstance().add(coloredBallAppear);
			
			note.dispatchEvent(new StatusEvent(StatusEvent.APPEAR, note));
		}
		
		if (event.getSource() == boss4.getDead() && event.getEventType() == "TweenComplete") {
			Tween coloredBallAppear = new Tween(yellowBall);
			coloredBallAppear.animate(TweenableParams.SCALE_X, yellowBall.getScaleX(), 1, 1000);
			coloredBallAppear.animate(TweenableParams.SCALE_Y, yellowBall.getScaleY(), 1, 1000);
			TweenJuggler.getInstance().add(coloredBallAppear);
			
			note.dispatchEvent(new StatusEvent(StatusEvent.APPEAR, note));

		}
		
		if (event.getSource() == FinalBoss.getDead() && event.getEventType() == "TweenComplete") {
			Tween coloredBallAppear = new Tween(purpleBall);
			coloredBallAppear.animate(TweenableParams.SCALE_X, purpleBall.getScaleX(), 1, 1000);
			coloredBallAppear.animate(TweenableParams.SCALE_Y, purpleBall.getScaleY(), 1, 1000);
			TweenJuggler.getInstance().add(coloredBallAppear);
			
		}

		
		// coloredBall handling
		if (event.getSource() == redBall && event.getEventType() == "redBallGotPicked") {
			if (!ball.isUpgradedWeaponUnlocked())
				ball.setUpgradedWeaponUnlocked(true);
			else
				ball.setUpgradedWeaponAttack(ball.getUpgradedWeaponAttack());

			ball.setImage("Fireball.png");
			ball.setCurrentWeapon("upgraded");
			redBall.setVisible(false);
			
			Sprite redFrame = new Sprite("redFrame", "redFrame.png");
			redFrame.setxPosition(-ball.getxPosition() - ball.getUnscaledWidth() / 2);
			redFrame.setyPosition(-ball.getyPosition() - ball.getUnscaledHeight() / 2 + 30);
			redFrame.setAlpha(0);
			ballCenter.addChild(redFrame);
			
			Tween redBallPicked = new Tween(redFrame);
			redBallPicked.animate(TweenableParams.ALPHA, redFrame.getAlpha(), 1, 1000); 
			TweenJuggler.getInstance().add(redBallPicked);
		}
		if (event.getSource() == blueBall && event.getEventType() == "blueBallGotPicked") {
			if (!ball.isFreezeWeaponUnlocked())
				ball.setFreezeWeaponUnlocked(true);
			else
				ball.setFreezeWeaponAttack(ball.getFreezeWeaponAttack());// + 5);

			ball.setImage("IceBall.png");
			ball.setCurrentWeapon("freeze");
			blueBall.setVisible(false);
			
			Sprite blueFrame = new Sprite("blueFrame", "blueFrame.png");
			blueFrame.setxPosition(-ball.getxPosition() - ball.getUnscaledWidth() / 2 + 170);
			blueFrame.setyPosition(-ball.getyPosition() - ball.getUnscaledHeight() / 2 + 30);
			blueFrame.setAlpha(0);
			ballCenter.addChild(blueFrame);
			
			Tween blueBallPicked = new Tween(blueFrame);
			blueBallPicked.animate(TweenableParams.ALPHA, blueFrame.getAlpha(), 1, 1000); 
			TweenJuggler.getInstance().add(blueBallPicked);
		}
		if (event.getSource() == orangeBall && event.getEventType() == "orangeBallGotPicked") {
			ball.setMaxSpeed(15);//ball.getMaxSpeed() + 5);
			ball.setSpeed(ball.getMaxSpeed());
			orangeBall.setVisible(false);
			
			Sprite orangeFrame = new Sprite("orangeFrame", "orangeFrame.png");
			orangeFrame.setxPosition(-ball.getxPosition() - ball.getUnscaledWidth() / 2 + 85);
			orangeFrame.setyPosition(-ball.getyPosition() - ball.getUnscaledHeight() / 2 + 30);
			orangeFrame.setAlpha(0);
			ballCenter.addChild(orangeFrame);
			
			Tween orangeBallPicked = new Tween(orangeFrame);
			orangeBallPicked.animate(TweenableParams.ALPHA, orangeFrame.getAlpha(), 1, 1000); 
			TweenJuggler.getInstance().add(orangeBallPicked);
		}
		if (event.getSource() == greenBall && event.getEventType() == "greenBallGotPicked") {
			ball.setHealth(ball.getMaxHealth());
			greenBall.setVisible(false);
			
			Sprite greenFrame = new Sprite("greenFrame", "greenFrame.png");
			greenFrame.setxPosition(-ball.getxPosition() - ball.getUnscaledWidth() / 2 + 255);
			greenFrame.setyPosition(-ball.getyPosition() - ball.getUnscaledHeight() / 2 + 30);
			greenFrame.setAlpha(0);
			ballCenter.addChild(greenFrame);
			
			Tween greenBallPicked = new Tween(greenFrame);
			greenBallPicked.animate(TweenableParams.ALPHA, greenFrame.getAlpha(), 1, 1000); 
			TweenJuggler.getInstance().add(greenBallPicked);
		}
		if (event.getSource() == yellowBall && event.getEventType() == "yellowBallGotPicked") {
			int oldHealth = ball.getMaxHealth();
			int newHealth = ball.getMaxHealth() + 100;
			ball.setMaxHealth(newHealth);
			ball.setHealth(ball.getMaxHealth());
			yellowBall.setVisible(false);
			
			Sprite yellowFrame = new Sprite("yellowFrame", "yellowFrame.png");
			yellowFrame.setxPosition(-ball.getxPosition() - ball.getUnscaledWidth() / 2 + 340);
			yellowFrame.setyPosition(-ball.getyPosition() - ball.getUnscaledHeight() / 2 + 30);
			yellowFrame.setAlpha(0);
			ballCenter.addChild(yellowFrame);
			
			Tween yellowBallPicked = new Tween(yellowFrame);
			yellowBallPicked.animate(TweenableParams.ALPHA, yellowFrame.getAlpha(), 1, 1000); 
			TweenJuggler.getInstance().add(yellowBallPicked);
		}
		if (event.getSource() == purpleBall && event.getEventType() == "purpleBallGotPicked") {
			ball.setImage("rainbow_ball.png");
			if (!ball.isUltimateWeaponUnlocked())
				ball.setUltimateWeaponUnlocked(true);
			
			purpleBall.setVisible(false);
			
			Sprite purpleFrame = new Sprite("purpleFrame", "purpleFrame.png");
			purpleFrame.setxPosition(-ball.getxPosition() - ball.getUnscaledWidth() / 2 + 425);
			purpleFrame.setyPosition(-ball.getyPosition() - ball.getUnscaledHeight() / 2 + 30);
			purpleFrame.setAlpha(0);
			ballCenter.addChild(purpleFrame);
			
			Tween purpleBallPicked = new Tween(purpleFrame);
			purpleBallPicked.animate(TweenableParams.ALPHA, purpleFrame.getAlpha(), 1, 1000); 
			TweenJuggler.getInstance().add(purpleBallPicked);
		}

		
		if (event.getSource() == ball && event.getEventType() == "vanished") { // ball
																				// vanishes
																				// and
																				// gameOver
																				// appears
			Tween gameOver = new Tween(ball);
			gameOver.animate(TweenableParams.SCALE_X, ball.getScaleX(), 0, 2000);
			gameOver.animate(TweenableParams.SCALE_Y, ball.getScaleY(), 0, 2000);
			gameOver.animate(TweenableParams.ALPHA, ball.getAlpha(), 0, 2000);
			TweenJuggler.getInstance().add(gameOver);
			Tween appear = new Tween(gg);
			appear.animate(TweenableParams.ALPHA, gg.getAlpha(), 1, 2000);
			TweenJuggler.getInstance().add(appear);
		}

		// instruction tweening
		if (event.getSource() == wasd && event.getEventType() == "vanished") {
			Tween dead = new Tween(wasd);
			dead.animate(TweenableParams.ALPHA, wasd.getAlpha(), 0, 1000);
			TweenJuggler.getInstance().add(dead);
		}
		if (event.getSource() == mouse && event.getEventType() == "vanished") {
			Tween dead = new Tween(mouse);
			dead.animate(TweenableParams.ALPHA, mouse.getAlpha(), 0, 1000);
			TweenJuggler.getInstance().add(dead);
		}
		if (event.getSource() == r && event.getEventType() == "vanished") {
			Tween dead = new Tween(r);
			dead.animate(TweenableParams.ALPHA, r.getAlpha(), 0, 1000);
			TweenJuggler.getInstance().add(dead);
		}
		
		
		// winning tweening
		if (event.getSource() == whiteLight && event.getEventType() == "clear") {
			map.setRespondToKeys(false);
			Tween rainbowAppear = new Tween(rainbow, new EaseOut());
			rainbowAppear.animate(TweenableParams.ALPHA, rainbow.getAlpha(), 1, 10000);
			TweenJuggler.getInstance().add(rainbowAppear);
		}
		
		
		// note tweening
		if (event.getSource() == note && event.getEventType() == "appeared") {
			Tween noteAppear = new Tween(note);
			noteAppear.animate(TweenableParams.ALPHA, note.getAlpha(), 1, 1000);
			TweenJuggler.getInstance().add(noteAppear);
		}
		
		if (event.getSource() == note && event.getEventType() == "vanished") {
			Tween noteVanish = new Tween(note);
			noteVanish.animate(TweenableParams.ALPHA, note.getAlpha(), 0, 1000);
			TweenJuggler.getInstance().add(noteVanish);
		}

	}

}
