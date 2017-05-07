package edu.virginia.engine.tween;

public class TweenParam {
	
	private TweenableParams paramToTween;
	private double startVal;
	private double endVal;
	private double tweenTime;
	
	public TweenParam(TweenableParams paramToTween, double startVal, double endVal, double tweenTime) {
		this.paramToTween = paramToTween;
		this.startVal = startVal;
		this.endVal = endVal;
		this.tweenTime = tweenTime;
	}
	
	public TweenableParams getParamToTween() {
		return paramToTween;
	}
	
	public double getStartVal() {
		return startVal;
	}
	
	public double getEndVal() {
		return endVal;
	}
	
	public double getTweenTime() {
		return tweenTime;
	}
}
