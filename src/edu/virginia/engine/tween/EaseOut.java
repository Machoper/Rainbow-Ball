package edu.virginia.engine.tween;

public class EaseOut extends TweenTransitions{
	@Override
	public double applyTransition(double percentDone) {
		return - percentDone * percentDone + 2 * percentDone;
	}

}
