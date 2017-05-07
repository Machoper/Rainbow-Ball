package edu.virginia.engine.events;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class EventDispatcher implements IEventDispatcher {
	
	private HashMap<String, List<IEventListener>> eventListener = new HashMap<String, List<IEventListener>>();

	@Override
	public void addEventListener(IEventListener listener, String eventType) {
		if (!eventListener.containsKey(eventType)) {
			List<IEventListener> listeners = new ArrayList<IEventListener>();
			listeners.add(listener);
			eventListener.put(eventType, listeners);
			
		}
		else {
			eventListener.get(eventType).add(listener);
		}
		
	}

	@Override
	public void removeEventListener(IEventListener listener, String eventType) {
		if (eventListener.containsKey(eventType)) {
			eventListener.get(eventType).remove(listener);
		}
		
	}
	
	public void clearEventListener()
	{
		eventListener.clear();
	}

	@Override
	public void dispatchEvent(Event event) {
		if (eventListener.containsKey(event.getEventType())) {
			for (IEventListener ies : eventListener.get(event.getEventType())) {
				ies.handleEvent(event);
			}
		}
		
	}

	@Override
	public boolean hasEventListener(IEventListener listener, String eventType) {
		if (eventListener.containsKey(eventType)) {
			return eventListener.get(eventType).contains(listener);
		}
		return false;
	}

	
}
