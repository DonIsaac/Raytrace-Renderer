package tools;

/**
 * EventListeners can bind to an EventEmitter for notifications that an event
 * has been emitted. EventListeners can listen to one or more events from
 * one or more EventEmitters.
 * 
 * @author Don Isaac
 *
 * @param <T>
 */
public interface EventListener {
	/**
	 * Notifies the EventListener that an event was emitted.
	 * 
	 * @param event the even that was emitted
	 * @param args a list of arguments sent by the EventEmitter
	 */
	public void onEvent(String event, Object ...args);
}
