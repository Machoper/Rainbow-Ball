package edu.virginia.engine.display;

import java.awt.AlphaComposite;


import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import edu.virginia.engine.events.CollideEvent;
import edu.virginia.engine.events.EventDispatcher;


/**
 * A very basic display object for a java based gaming engine
 * 
 * */
public class DisplayObject extends EventDispatcher{

	/* All DisplayObject have a unique id */
	private String id;

	/* The image that is displayed by this object */
	private BufferedImage displayImage;
	
	private DisplayObject parent;
	private boolean respondToKeys;
	private int speed;
	
	/* Should be true iff this display object is meant to be drawn */
	private boolean visible;
	
	/* Describe the x,y position where this object will be drawn */
	private int xPosition;
	private int yPosition;
	
	
	/* The point, relative to the upper left corner of the image that is the origin of this object */
	private Point pivotPoint;
	
	/* Scales the image up or down. 1.0 would be actual size. */
	private float scaleX;
	private float scaleY;
	
	/* Defines the amount (in degrees or radians, your choice) to rotate this object */
	private double rotation;
	
	/* Defines how transparent to draw this object. */
	private float alpha;
	
	private int hp;

	public ArrayList<DisplayObject> collidableObjects = new ArrayList<DisplayObject>();
	
	public int getHp() {
		return hp;
	}

	public void setHp(int hp) {
		if (this.hp >= 0) {
			this.hp = hp;
		}
		else this.hp = 0;
	}

	public boolean isRespondToKeys() {
		return respondToKeys;
	}

	public void setRespondToKeys(boolean respondToKeys) {
		this.respondToKeys = respondToKeys;
	}

	/* Setters and getters for fields */
	public boolean isVisible() {
		return visible;
	}
	
	public void setVisible(boolean visible) {
		this.visible = visible;
	}
	
	public int getxPosition() {
		return xPosition;
	}
	
	public void setxPosition(int xPosition) {
		this.xPosition = xPosition;
	}
	
	public int getyPosition() {
		return yPosition;
	}
	
	public void setyPosition(int yPosition) {
		this.yPosition = yPosition;
	}
	
	public Point getPivotPoint() {
		return pivotPoint;
	}
	
	public void setPivotPoint(Point pivotPoint) {
		this.pivotPoint = pivotPoint;
	}
	
	
	public float getScaleX() {
		return scaleX;
	}

	public void setScaleX(float scaleX) {
		this.scaleX = scaleX;
	}

	public float getScaleY() {
		return scaleY;
	}

	public void setScaleY(float scaleY) {
		this.scaleY = scaleY;
	}

	public double getRotation() {
		return rotation;
	}

	public void setRotation(double rotation) {
		this.rotation = rotation;
	}

	public float getAlpha() {
		return alpha;
	}

	public void setAlpha(float alpha) {
		this.alpha = alpha;
	}
	
	public DisplayObject getParent() {
		return parent;
	}
	
	public void setParent(DisplayObject parent) {
		this.parent = parent;
	}

	/**
	 * Constructors: can pass in the id OR the id and image's file path and
	 * position OR the id and a buffered image and position
	 */
	public DisplayObject(String id) {
		this.setId(id);
		
		respondToKeys = false;
		visible = true;
		/* Describe the x,y position where this object will be drawn */
		xPosition = 0;
		yPosition = 0;
		/* The point, relative to the upper left corner of the image that is the origin of this object */
		pivotPoint = new Point(0,0);
		/* Scales the image up or down. 1.0 would be actual size. */
		scaleX = 1.0f;
		scaleY = 1.0f;
		/* Defines how transparent to draw this object. */
		alpha = 1.0f;
	}

	public DisplayObject(String id, String fileName) {
		this.setId(id);
		this.setImage(fileName);
		
		respondToKeys = false;
		visible = true;
		/* Describe the x,y position where this object will be drawn */
		xPosition = 0;
		yPosition = 0;
		/* The point, relative to the upper left corner of the image that is the origin of this object */
		pivotPoint = new Point(0,0);
		/* Scales the image up or down. 1.0 would be actual size. */
		scaleX = 1.0f;
		scaleY = 1.0f;
		/* Defines how transparent to draw this object. */
		alpha = 1.0f;
	}
	
	public void resetDisplayObject()
	{
		visible = true;
		xPosition = 0;
		yPosition = 0;
		alpha = 1.0f;
		scaleX = 1.0f;
		scaleY = 1.0f;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}


	/**
	 * Returns the unscaled width and height of this display object
	 * */
	public int getUnscaledWidth() {
		if(displayImage == null) return 0;
		return displayImage.getWidth();
	}

	public int getUnscaledHeight() {
		if(displayImage == null) return 0;
		return displayImage.getHeight();
	}
	
	public int getScaledWidth() {
		if(displayImage == null) return 0;
		return (int)(displayImage.getWidth()*this.getScaleX());
	}
	
	public int getScaledHeight() {
		if(displayImage == null) return 0;
		return (int)(displayImage.getHeight()*this.getScaleY());
	}

	public BufferedImage getDisplayImage() {
		return this.displayImage;
	}

	public void setImage(String imageName) {
		if (imageName == null) {
			return;
		}
		displayImage = readImage(imageName);
		if (displayImage == null) {
			System.err.println("[DisplayObject.setImage] ERROR: " + imageName + " does not exist!");
		}
	}


	/**
	 * Helper function that simply reads an image from the given image name
	 * (looks in resources\\) and returns the bufferedimage for that filename
	 * */
	public BufferedImage readImage(String imageName) {
		BufferedImage image = null;
		try {
			String file = ("resources" + File.separator + imageName);
			image = ImageIO.read(new File(file));
		} catch (IOException e) {
			System.out.println("[Error in DisplayObject.java:readImage] Could not read image " + imageName);
			e.printStackTrace();
		}
		return image;
	}

	public void setImage(BufferedImage image) {
		if(image == null) return;
		displayImage = image;
	}


	/**
	 * Invoked on every frame before drawing. Used to update this display
	 * objects state before the draw occurs. Should be overridden if necessary
	 * to update objects appropriately.
	 * 
	 * With player scrolling, directions are reversed since we are keeping
	 * the player stationary and moving the background.
	 * */
	protected void update(ArrayList<String> pressedKeys) {
		if (this != null && respondToKeys == true) {
			if (pressedKeys.contains("A")) {
				xPosition += speed;
			}
			if (pressedKeys.contains("D")) {
				xPosition -= speed;
			}
			if(pressedKeys.contains("W")) {
				yPosition += speed;
			}
			if (pressedKeys.contains("S")) {
				yPosition -= speed;
			}
			for (DisplayObject obj : collidableObjects) {
				if (this.collideWith(obj)) {
					this.dispatchEvent(new CollideEvent(CollideEvent.COLLIDE_START, this));
				}
			}
		}
		
	}

	/**
	 * Draws this image. This should be overloaded if a display object should
	 * draw to the screen differently. This method is automatically invoked on
	 * every frame.
	 * */
	public void draw(Graphics g) {
		
		if (displayImage != null && visible == true) {
			
			/*
			 * Get the graphics and apply this objects transformations
			 * (rotation, etc.)
			 */
			Graphics2D g2d = (Graphics2D) g;
			applyTransformations(g2d);

			/* Actually draw the image, perform the pivot point translation here */
			g2d.drawImage(displayImage, 0/*-displayImage.getWidth()/2*/, 0/*-displayImage.getHeight()*/,
					(int) (getUnscaledWidth()),
					(int) (getUnscaledHeight()), null);
			
			/*
			 * undo the transformations so this doesn't affect other display
			 * objects
			 */
			reverseTransformations(g2d);
		}
	}

	/**
	 * Applies transformations for this display object to the given graphics
	 * object
	 * */
	protected void applyTransformations(Graphics2D g2d) {
		g2d.rotate(rotation, pivotPoint.getX(),pivotPoint.getY());
		g2d.translate(xPosition, yPosition);
		g2d.scale(scaleX, scaleY);
		g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
		
	}

	/**
	 * Reverses transformations for this display object to the given graphics
	 * object
	 * */
	protected void reverseTransformations(Graphics2D g2d) {
		
		g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
		g2d.scale(1/scaleX, 1/scaleX);
		g2d.translate(-xPosition, -yPosition);
		g2d.rotate(-rotation, pivotPoint.getX(),pivotPoint.getY());
	}
	
	public Rectangle getHitbox() {
		return getHitboxForParent(parent);
	}

	public Rectangle getHitboxForParent(DisplayObject p) {
		if (p == parent) {
			return new Rectangle(xPosition, yPosition, getUnscaledWidth(), getUnscaledHeight());
		}
		else {
			Rectangle rec = parent.getHitboxForParent(p);
			rec.x += xPosition;
			rec.y += yPosition;
			rec.width = getUnscaledWidth();
			rec.height = getUnscaledHeight();
			return rec;
		}
	}
	
	public Rectangle getTopHitboxForParent(DisplayObject p) {
		if (p == parent) {
			return new Rectangle(xPosition+10, yPosition, getUnscaledWidth()-20, 5);
		}
		else {
			Rectangle rec = parent.getHitboxForParent(p);
			rec.x += xPosition;
			rec.y += yPosition;
			rec.width = getUnscaledWidth();
			rec.height = getUnscaledHeight();
			return rec;
		}
	}
	
	public Rectangle getBottomHitboxForParent(DisplayObject p) {
		if (p == parent) {
			return new Rectangle(xPosition+10, yPosition+getUnscaledHeight()-5, getUnscaledWidth()-20, 5);
		}
		else {
			Rectangle rec = parent.getHitboxForParent(p);
			rec.x += xPosition;
			rec.y += yPosition;
			rec.width = getUnscaledWidth();
			rec.height = getUnscaledHeight();
			return rec;
		}
	}
	
	public Rectangle getLeftHitboxForParent(DisplayObject p) {
		if (p == parent) {
			return new Rectangle(xPosition, yPosition+10, 5, getUnscaledHeight()-20);
		}
		else {
			Rectangle rec = parent.getHitboxForParent(p);
			rec.x += xPosition;
			rec.y += yPosition;
			rec.width = getUnscaledWidth();
			rec.height = getUnscaledHeight();
			return rec;
		}
	}
	
	public Rectangle getRightHitboxForParent(DisplayObject p) {
		if (p == parent) {
			return new Rectangle(xPosition+getUnscaledWidth()-5, yPosition+10, 5, getUnscaledHeight()-20);
		}
		else {
			Rectangle rec = parent.getHitboxForParent(p);
			rec.x += xPosition;
			rec.y += yPosition;
			rec.width = getUnscaledWidth();
			rec.height = getUnscaledHeight();
			return rec;
		}
	}
	
	public DisplayObject getRoot(DisplayObject obj)
	{
		if (obj.getParent() == null)
			return obj;
		else
			return obj.getRoot(obj.getParent());
	}
	
	public boolean collideWith(DisplayObject other) {
		Rectangle rec1 = this.getHitboxForParent(this.getRoot(this));
		Rectangle rec2 = other.getHitboxForParent(other.getRoot(other));
		return rec1.intersects(rec2);
	}
	
	public boolean collideWithTop(DisplayObject other) {
		Rectangle rec1 = this.getTopHitboxForParent(this.getRoot(this));
		Rectangle rec2 = other.getHitboxForParent(other.getRoot(other));
		return rec1.intersects(rec2);
	}
	
	public boolean collideWithBottom(DisplayObject other) {
		Rectangle rec1 = this.getBottomHitboxForParent(this.getRoot(this));
		Rectangle rec2 = other.getHitboxForParent(other.getRoot(other));
		return rec1.intersects(rec2);
	}
	
	public boolean collideWithLeft(DisplayObject other) {
		Rectangle rec1 = this.getLeftHitboxForParent(this.getRoot(this));
		Rectangle rec2 = other.getRightHitboxForParent(other.getRoot(other));
		return rec1.intersects(rec2);
	}
	
	public boolean collideWithRight(DisplayObject other) {
		Rectangle rec1 = this.getRightHitboxForParent(this.getRoot(this));
		Rectangle rec2 = other.getLeftHitboxForParent(other.getRoot(other));
		return rec1.intersects(rec2);
	}
	
	public void addCollisionObject(DisplayObject object) {
		collidableObjects.add(object);
	}
	
	public ArrayList<DisplayObject> getCollisionObjects() {
		return collidableObjects;
	}
	
	public void removeCollisionObject(int idx) {
		collidableObjects.remove(idx);
	}
	
	public int getSpeed() {
		return speed;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}


}