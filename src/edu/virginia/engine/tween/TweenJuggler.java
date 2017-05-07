package edu.virginia.engine.tween;

import java.util.ArrayList;
import java.util.List;

public class TweenJuggler {
	
	private static TweenJuggler instance;
	static List<Tween> tweens;
	
	private TweenJuggler() {
		if(instance != null) System.out.println("ERROR: Cannot re-initialize singleton class!");
		instance = this;
		tweens = new ArrayList<Tween>();
	}
	
	public static TweenJuggler getInstance() {
		if(instance == null) {
			instance = new TweenJuggler();
		}
		return instance;
	}
	
	public void add(Tween tween) {
		tweens.add(tween);
	}
	           
	public void nextFrame() {
		//invoked every frame by Game, calls update() on every Tween and cleans up old / complete Tweens
		List<Tween> tweensCopy = new ArrayList<Tween>();
		List<Tween> removeTween = new ArrayList<Tween>();
		tweensCopy.addAll(tweens);
		for(Tween tween : tweensCopy) {
			if(tween.isComplete()) {
				tween.dispatchEvent(new TweenEvent(TweenEvent.TWEEN_COMPLETE_EVENT, tween));
				removeTween.add(tween);
			} else {
				tween.update();
			}
		}
		tweens.removeAll(removeTween);
	
		
	}

}
