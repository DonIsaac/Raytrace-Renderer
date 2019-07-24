package tools;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Abstract implementation of EventListener for convenience.
 * @author Don Isaac
 *
 */
public class AbstractEventEmitter implements EventEmitter{
	private Map<String, List<EventListener>> listeners;
	
	/**
	 * Creates an empty AbstractEventListener.
	 */
	public AbstractEventEmitter() {
		this.listeners = new HashMap<String, List<EventListener>>();
	}

	@Override
	public int addListener(EventListener l, String event) {
		if (!this.listeners.containsKey(event))
			this.listeners.put(event, new LinkedList<EventListener>());
		
		this.listeners.get(event).add(l);
		return this.listeners.get(event).size();
	}

	@Override
	public int removeListener(EventListener l, String event) {
		if (!this.listeners.containsKey(event))
			return 0;
		
		this.listeners.get(event).remove(l);
		
		if (this.listeners.get(event).isEmpty()) {
			this.listeners.remove(event);
			return 0;
		}
		return this.listeners.get(event).size();
	}

	@Override
	public void emit(String event, Object... args) {
		List<EventListener> listeners = this.listeners.get(event);
		
		if (listeners != null && !listeners.isEmpty())
		for (EventListener listener: listeners) {
			listener.onEvent(event, args);
		}
		
	}

}
