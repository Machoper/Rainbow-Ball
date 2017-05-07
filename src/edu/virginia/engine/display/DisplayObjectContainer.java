package edu.virginia.engine.display;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

public class DisplayObjectContainer extends DisplayObject{
	
	private List<DisplayObject> children;

	public DisplayObjectContainer(String id) {
		super(id);
		children  = new ArrayList<DisplayObject>();
		// TODO Auto-generated constructor stub
	}
	
	public DisplayObjectContainer(String id, String fileName) {
		super(id, fileName);
		children = new ArrayList<DisplayObject>();
		// TODO Auto-generated constructor stub
	}
	
	public void addChild(DisplayObject child) {
		//if ( child.getParent() == this )
			if ( !this.contains(child) )
				this.children.add(child);
				child.setParent(this);
	}
	
	public void addChildAtIndex(int i, DisplayObject child){
		children.add(i, child);
	}
	
	public void removeChild(DisplayObject child) {
		children.remove(child);
		child.setParent(child.getRoot(child));
	}
	
	public void removeByIndex(int i){
		children.remove(i);
	}
	
	public void removeAll() {
		children.clear();
	}
	
	public boolean contains(DisplayObject child) {
		return this.children.contains(child);
	}
	
	public DisplayObject getChildByIndex(int i){
		return children.get(i);
	}
	
	public DisplayObject getChildByID(String id){
		for (DisplayObject child : children){
			if (child.getId() == id) {
				return child;
			}	
		}
		return null;
	}
	
	public List<DisplayObject> getChildren() {
		return children;
	}

	@Override
	protected void update(ArrayList<String> pressedKeys) {
		// TODO Auto-generated method stub
		super.update(pressedKeys);
		for (DisplayObject child : children) {
			child.update(pressedKeys);
		}
	}

	@Override
	public void draw(Graphics g) {
		// TODO Auto-generated method stub
		if (isVisible()) {
			Graphics2D g2d = (Graphics2D) g;
			super.draw(g);
			applyTransformations(g2d);
			
			for (DisplayObject child : children) {
				child.draw(g);
			}
			
			reverseTransformations(g2d);
		}
	}
	
}
