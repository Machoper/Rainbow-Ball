package edu.virginia.engine.tween;

import java.util.ArrayList;
import java.util.List;

import edu.virginia.engine.display.DisplayObject;
import edu.virginia.engine.display.HealthBar;
import edu.virginia.engine.events.EventDispatcher;

public class Tween extends EventDispatcher{
	
	private DisplayObject object;
	private HealthBar bar;
	private long startTime;
	private TweenTransitions transition = new TweenTransitions();
	private List<TweenParam> tweenParams;
	
	public Tween(DisplayObject object) {
		this.object = object;
		this.startTime = System.currentTimeMillis();
		tweenParams = new ArrayList<TweenParam>();
		this.transition = new TweenTransitions();
	}
	
	public Tween(HealthBar bar) {
		this.bar = bar;
		this.startTime = System.currentTimeMillis();
		tweenParams = new ArrayList<TweenParam>();
		this.transition = new TweenTransitions();
	}
	
	public Tween(DisplayObject object, TweenTransitions transition) {
		this.object = object;
		this.startTime = System.currentTimeMillis();
		tweenParams = new ArrayList<TweenParam>();
		this.transition = transition;
	}
	
	public void animate(TweenableParams fieldToAnimate, double startVal, double endVal, double time) {
		tweenParams.add(new TweenParam(fieldToAnimate, startVal, endVal, time));
	}
	
	public void update() {
		//invoked once per frame by the TweenJuggler. Updates this tween / DisplayObject
		long currentTime = System.currentTimeMillis();
		for (TweenParam tweenParam : tweenParams) {
			if (currentTime - startTime >= tweenParam.getTweenTime()) {
				continue;
			}
			double percent = transition.applyTransition((double) (currentTime - startTime) / tweenParam.getTweenTime());
			switch (tweenParam.getParamToTween()) {
				case X:
					object.setxPosition((int) (tweenParam.getStartVal()
							+ (percent * (tweenParam.getEndVal() - tweenParam.getStartVal()))));
					break;
				case Y:
					object.setyPosition((int) (tweenParam.getStartVal()
							+ (percent * (tweenParam.getEndVal() - tweenParam.getStartVal()))));
					break;
				case SCALE_X:
					object.setScaleX((float) tweenParam.getStartVal()
							+ (float) (percent * (tweenParam.getEndVal() - tweenParam.getStartVal())));
					break;
				case SCALE_Y:
					object.setScaleY((float) tweenParam.getStartVal()
							+ (float) (percent * (tweenParam.getEndVal() - tweenParam.getStartVal())));
					break;
				case ALPHA:
					object.setAlpha((float) tweenParam.getStartVal()
							+ (float) (percent * (tweenParam.getEndVal() - tweenParam.getStartVal())));
					break;
					//tween healthbar
				case DRAWHEALTH:
					bar.setDrawHealth((int) (tweenParam.getStartVal()
							+ (percent * (tweenParam.getEndVal() - tweenParam.getStartVal()))));
					break;
				}
		}
	}
	
	public boolean isComplete() {
		long currentTime = System.currentTimeMillis();
		for (TweenParam tweenParam : tweenParams) {
			if (currentTime - startTime < tweenParam.getTweenTime()) {
				return false;
			}
		}
		return true;
	}
	
	public void setValue(TweenableParams param, double value) {
	}


}
