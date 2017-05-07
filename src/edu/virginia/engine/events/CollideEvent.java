package edu.virginia.engine.events;

public class CollideEvent extends Event{
	
	public static final String COLLIDE_START = "collided";
	public static final String COLLIDE_TOP = "collideTop";
	public static final String COLLIDE_BOTTOM = "collideBottom";
	public static final String COLLIDE_LEFT = "collideLeft";
	public static final String COLLIDE_RIGHT = "collideRight";

	public CollideEvent(String eventType, IEventDispatcher source) {
		super(eventType, source);
		// TODO Auto-generated constructor stub
	}

}
