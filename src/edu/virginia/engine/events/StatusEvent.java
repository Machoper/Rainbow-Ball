package edu.virginia.engine.events;

public class StatusEvent extends Event{
	public static final String APPEAR = "appeared";
	public static final String VANISH = "vanished";
	public static final String CLEAR = "clear";

	public StatusEvent(String eventType, IEventDispatcher source) {
		super(eventType, source);
		// TODO Auto-generated constructor stub
	}

}
