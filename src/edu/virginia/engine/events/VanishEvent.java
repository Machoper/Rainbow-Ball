package edu.virginia.engine.events;

public class VanishEvent extends Event {
	
	public static final String VANISH = "vanished";
	public static final String APPEAR = "appeared"; // test

	public VanishEvent(String eventType, IEventDispatcher source) {
		super(eventType, source);
		// TODO Auto-generated constructor stub
	}

}
