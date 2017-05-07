package edu.virginia.engine.events;

public class PickedUpEvent extends Event{
	
	public static final String COIN_PICKED_UP = "picked";
	public static final String RED_BALL_PICKED_UP = "redBallGotPicked";
	public static final String ORANGE_BALL_PICKED_UP = "orangeBallGotPicked";
	public static final String BLUE_BALL_PICKED_UP = "blueBallGotPicked";
	public static final String GREEN_BALL_PICKED_UP = "greenBallGotPicked";
	public static final String YELLOW_BALL_PICKED_UP = "yellowBallGotPicked";
	public static final String PURPLE_BALL_PICKED_UP = "purpleBallGotPicked";

	public PickedUpEvent(String eventType, IEventDispatcher source) {
		super(eventType, source);
		// TODO Auto-generated constructor stub
	}

}
