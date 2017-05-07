package edu.virginia.engine.display;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AnimatedSprite extends Sprite{
	private List<BufferedImage> frames;
	private long cycleTimeInNanoSec;
	private long startTime;
	private int currentFrame;
	private int startIndex;
	private int endIndex;
	private int animationSpeed;
	private boolean isPlaying;
	private String animationName;
	private Map<String, int[]> animations;
	
	public AnimatedSprite(String id, List<String> fileNames) {
		super(id);
		frames = new ArrayList<>();
		cycleTimeInNanoSec = 1000000000;
		currentFrame = 0;
		animations = new HashMap<>();
		for (String fileName : fileNames) {
			BufferedImage image = this.readImage(fileName);
			if (image != null) {
				frames.add(image);
			}
		}
		isPlaying = true;
		//startTime = System.nanoTime();
	}
	
	public boolean isPlaying() {
		return isPlaying;
	}

	public void setPlaying(boolean isPlaying) {
		this.isPlaying = isPlaying;
	}

	public void setAnimationSpeed(int animationSpeed) {
		this.animationSpeed = animationSpeed;
	}
	
	public void setAnimation(String name, int[] index) {
		this.animations.put(name, index);
	}
	
	public void animate(String name) {
		if (animations.containsKey(name)) {
			this.animationName = name;
		}
		
	}

	
	@Override
	public void update(ArrayList<String> pressedKeys) {
		super.update(pressedKeys);
		if (isPlaying && animationName != null) {
			
			long time = System.nanoTime() - startTime;
			time %= cycleTimeInNanoSec;
			int[] list = animations.get(animationName);
			long frameTime = cycleTimeInNanoSec / list.length;
			currentFrame = (int) (time / frameTime);
			if (currentFrame == list.length)
				currentFrame--;
			this.setImage(frames.get(list[currentFrame]));
		}
		else {
			this.setImage(frames.get(0));
		}
			/*int[] list = animations.get(animationName);
			//System.out.println(list.length);
			if (currentFrame < list.length) {
				currentFrame++;
				if (currentFrame == list.length) {
					currentFrame = 0;
					}
				this.setImage(frames.get(list[currentFrame]));
				}
			}
		else {
			this.setImage(frames.get(0));
		}*/
	}

	public long getCycleTimeInNanoSec() {
		return cycleTimeInNanoSec;
	}

	public void setCycleTimeInNanoSec(long cycleTimeInNanoSec) {
		this.cycleTimeInNanoSec = cycleTimeInNanoSec;
	}
	
}
