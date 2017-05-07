package edu.virginia.engine.display;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

public class HealthBar extends Sprite {
	
	private int drawHealth;
	private int barWidth;
	private int barHeight;

	public HealthBar(String id) {
		super(id);
		// TODO Auto-generated constructor stub
	}
	
	public HealthBar(String id, String imageFileName) {
		super(id, imageFileName);
	}
	
	public int getDrawHealth() {
		return drawHealth;
	}
	
	public void setDrawHealth(int drawHealth) {
		this.drawHealth = drawHealth;
	}

	public int getBarWidth() {
		return barWidth;
	}

	public void setBarWidth(int barLength) {
		this.barWidth = barLength;
	}

	public int getBarHeight() {
		return barHeight;
	}

	public void setBarHeight(int barWidth) {
		this.barHeight = barWidth;
	}

	@Override
	public void draw(Graphics g) {
		// TODO Auto-generated method stub
		Graphics2D g2d = (Graphics2D) g;
		super.applyTransformations(g2d);
		g2d.setColor(Color.black);
		g2d.fillRect(0, 0, barWidth, barHeight); //draws healthbar outline
		g2d.setColor(Color.green);
		g2d.fillRect(2, 2, drawHealth, barHeight-4);
		super.reverseTransformations(g2d);
	}
	
	

}
