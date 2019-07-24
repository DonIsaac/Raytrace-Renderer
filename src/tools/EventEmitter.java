package tools;

/**
 * EventEmitters are able to publish events to EventListeners.
 * 
 * @author Don Isaac
 *
 */
public interface EventEmitter {

	/**
	 * Registers an EventListener to get notifications about a specified event.
	 * 
	 * @param l the listener to add
	 * @param event the event to subscribe to
	 * @return the number of EventListeners subscribed to the specified event.
	 */
	public int addListener(EventListener l, String event);
	
	/**
	 * Unsubscribes an EventListener from event notifications.
	 * 
	 * @param l the listener to remove
	 * @param event the event to unsubscribe from
	 * @return the number of listeners listening to the specified event after removal.
	 */
	public int removeListener(EventListener l, String event);
	
	/**
	 * Publishes an event notification to all of the event's listeners.
	 * 
	 * @param event the event being published
	 * @param args any arguments to send to the listeners
	 */
	public void emit(String event, Object ...args);
	
	
}
