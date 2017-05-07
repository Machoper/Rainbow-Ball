package edu.virginia.engine.display;

public class ColorBall extends Sprite {

	private ColorBalls color;
	private String powerUp;
	
	public ColorBall(String id, String imageFileName, String color) {
		super(id, imageFileName);
		
		switch(color) {
		case "white":
			this.color = ColorBalls.White;
			this.powerUp = "Basic Weapon";
			break;
		case "red":
			this.color = ColorBalls.Red;
			this.powerUp = "Upgraded Weapon";
			break;
		case "blue":
			this.color = ColorBalls.Blue;
			this.powerUp = "Freeze Weapon";
			break;
		case "purple":
			this.color = ColorBalls.Purple;
			this.powerUp = "Ultimate Weapon";
			break;
		case "yellow":
			this.color = ColorBalls.Yellow;
			this.powerUp = "Max Health Boost";
			break;
		case "orange":
			this.color = ColorBalls.Orange;
			this.powerUp = "Max Speed Boost";
			break;
		case "green":
			this.color = ColorBalls.Green;
			this.powerUp = "Heal";
			break;
		default:
			System.out.println("ERROR: Unknown color ball. (ColorBall.java)");
			System.exit(0);
		}
		
		
	}

	public ColorBalls getColor() {
		return color;
	}

	public void setColor(ColorBalls color) {
		this.color = color;
	}

	public String getPowerUp() {
		return powerUp;
	}

	public void setPowerUp(String powerUp) {
		this.powerUp = powerUp;
	}

	
}
