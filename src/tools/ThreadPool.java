package tools;

import java.util.Collection;
import java.util.EmptyStackException;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;

/**
 * Manages a pool of threads, because ExecutorServices apparently don't want to
 * work.
 * 
 * Threads can be registered to be executed immediately, or they can be executed
 * immediately. Threads registered for immediate execution will be added to the
 * execution queue if all thread resources are in use.
 * 
 * @author Don Isaac
 *
 */
public class ThreadPool extends AbstractEventEmitter implements EventListener {

	/**
	 * Initial size for the thread queue.
	 */
	private static int QUEUE_SIZE = 32;
	/**
	 * Percentage of the computer's processing resources to dedicate to thread
	 * execution.
	 */
	private static float DEFAULT_DESIRED_LOAD = 0.7f;
	private static int DEFAULT_POOL_SIZE = (int) (Runtime.getRuntime().availableProcessors() * DEFAULT_DESIRED_LOAD);
	/**
	 * The number of threads that can run at once
	 */
	private int poolSize;
	private Queue<Task> taskQueue;
	/**
	 * Currently running threads
	 */
	private Collection<Thread> running;

	/**
	 * ThreadPool's execution state. The ThreadPool can have the following states:
	 * 
	 * <ul>
	 * <li><b>INCOMPLETE:</b> Pool is not executing anything, but there are tasks
	 * still queued;</li>
	 * <li><b>EXECUTING:</b> Pool is executing tasks;</li>
	 * <li><b>PENDING:</b> The task queue is empty and the pool is finishing the
	 * execution of currently running threads.</li>
	 * <li><b>COMPLETE:</b> Pool is not executing and there are no more tasks left
	 * to execute.</li>
	 * </ul>
	 *
	 * @author Don Isaac
	 *
	 */
	public enum PoolState {
		INCOMPLETE, EXECUTING, PENDING, COMPLETE
	}

	/**
	 * ThreadPools' current execution state
	 * 
	 * @see PoolState
	 */
	private PoolState state;

	/**
	 * Creates a new ThreadPool with the default pool size. The default pool size
	 * depends on the number of cores the computer has.
	 */
	public ThreadPool() {
		this(DEFAULT_POOL_SIZE);
	}

	/**
	 * Creates a new ThreadPool.
	 * 
	 * The size of the thread pool will determine how many threads the pool can run
	 * at once.
	 * 
	 * @param poolSize the size of the thread pool.
	 */
	public ThreadPool(int poolSize) {
		this.poolSize = poolSize;
		this.taskQueue = new PriorityBlockingQueue<Task>(QUEUE_SIZE);
		this.running = new LinkedBlockingQueue<>();
		this.state = PoolState.COMPLETE;
	}

	/**
	 * Registers a task in the task pool, but does not start execution. The task
	 * will have an execution priority of 0.
	 * 
	 * @param task the task to register
	 */
	public void register(Runnable task) {
		register(task, 0);
	}

	/**
	 * Registers a task in the task pool, but does not start execution.
	 * 
	 * @param task     the task to register
	 * @param priority the execution priority of the task
	 */
	public void register(Runnable target, int priority) {
		Task task = new Task(target, target.hashCode());
		task.addListener(this, "run");
		task.priority = priority;
		this.taskQueue.add(task);

		if (this.state == PoolState.COMPLETE)
			this.state = PoolState.INCOMPLETE;
	}

	/**
	 * Registers a task for execution with an execution priority of 1 and starts the
	 * execution process.
	 * 
	 * @param task
	 */
	public void submit(Runnable task) {
		this.submit(task, 1);
	}

	/**
	 * Registers a task for execution and starts the execution process.
	 *
	 * @param task     the task to register
	 * @param priority execution priority of the task
	 */
	public void submit(Runnable task, int priority) {
		this.register(task, priority);
		if (!this.isRunning())
			start();
	}

	/**
	 * 
	 * @return the number of tasks that will be run when start is called.
	 * 
	 * @throws EmptyStackException if the task queue is empty
	 */
	public void start() throws EmptyStackException {
		if (this.taskQueue.isEmpty())
			throw new EmptyStackException();

		// Already started, do nothing
		if (this.isRunning())
			return;

		this.state = PoolState.EXECUTING;

		this.populateRunningList();

//		if (this.taskQueue.isEmpty())
//			return;
	}

	/**
	 * Stops future thread execution and waits for currently executing tasks to
	 * stop, blocking the current thread until all tasks are COMPLETE. Once all
	 * tasks are COMPLETE, all waiting threads will be notified.
	 * 
	 * 
	 * @return <b>true</b> if all queued tasks have completed execution,
	 *         <b>false</b> otherwise.
	 * 
	 * @throws InterruptedException if any running threads throw an
	 *                              InterruptedException
	 */
	public boolean stop() throws InterruptedException {
		for (Thread t : this.running) {
			t.join();
		}

		if (this.taskQueue.isEmpty())
			this.state = PoolState.COMPLETE;
		else
			this.state = PoolState.INCOMPLETE;

		// notifyAll();
		this.emit("stopped", this.taskQueue.size());
		return this.state == PoolState.COMPLETE;
		
	}
	
	/**
	 * Blocks the current thread and waits for all currently running and 
	 * queue threads to finish execution.
	 * 
	 * @throws InterruptedException if any of the underlying threads throw an
	 * InterruptedException
	 */
	public synchronized void shutdown() throws InterruptedException {
		while (this.isRunning()) {
			wait();
		}
		System.out.println(this.taskQueue.size() + " " + this.taskQueue.isEmpty() + " " + this.running.size());
//		if (this.isRunning()) {
//			if (this.state == PoolState.EXECUTING)
//				this.populateRunningList();
//			
//			shutdown();
//		}
		this.emit("completed");
	}

	/**
	 * Populates the running thread list from the task queue, starting the task in
	 * the process. If the pool is not running (i.e. it has not been started) no
	 * action will occur.
	 */
	private void populateRunningList() {
		if (!this.isRunning())
			return;

		while (this.running.size() < this.poolSize) {

			if (this.taskQueue.isEmpty()) {
				this.state = this.running.isEmpty() ? PoolState.COMPLETE : PoolState.PENDING;
				return;
			}
			
			Task task = this.taskQueue.poll();
			task.start();
			this.running.add(task);
		}
	}

	/**
	 * Checks if the pool is executing tasks.
	 * 
	 * @return <b>true</b> if the pool is currently executing tasks, <b>false</b>
	 *         otherwise.
	 */
	public boolean isRunning() {
		return this.state == PoolState.EXECUTING || this.state == PoolState.PENDING;
	}

	/**
	 * 
	 * @return the pool's current state.
	 */
	public PoolState getState() {
		return this.state;
	}

	@Override
	public synchronized void onEvent(String event, Object... args) {
		if (args.length == 0 || !(args[0] instanceof Task))
			throw new IllegalStateException("The terminated task must be passed on run event.");
		
		Task ended = (Task)args[0];
		this.running.remove(ended);
		// System.out.println("Thread " + ended + " ended. " + (this.running.size() + this.taskQueue.size()) + " tasks left.");
		if (this.running.isEmpty() && this.taskQueue.isEmpty()) {
			this.state = PoolState.COMPLETE;
			notify();
		}
			
		
		if (this.isRunning())
			this.populateRunningList();

	}

	/**
	 * Decorator class for Threads registered in the ThreadPool. Tasks have a
	 * priority assigned with them. The priority determines the order in which the
	 * Task will be executed by the ThreadPool.
	 * 
	 * @author Don Isaac
	 *
	 */
	private class Task extends Thread implements EventEmitter, Comparable<Task> {

		private Runnable task;
		private LinkedList<EventListener> listeners;

		private int priority;

		public Task(Runnable task, int priority) {
			super();
			this.task = task;
			this.listeners = new LinkedList<>();
			this.priority = 0;
		}

		public void run() {
			task.run();
			this.emit("run", this);
		}

		@Override
		public int addListener(EventListener l, String event) {
			if (!event.equals("run"))
				throw new UnsupportedOperationException("Task does not publish the event " + event);

			this.listeners.add(l);
			return this.listeners.size();
		}

		@Override
		public int removeListener(EventListener l, String event) {
			if (!event.equals("run"))
				throw new UnsupportedOperationException("Task does not publish the event " + event);

			this.listeners.remove(l);
			return this.listeners.size();
		}

		@Override
		public void emit(String event, Object... args) {
			for (EventListener listener : listeners) {
				listener.onEvent(event, args);
			}

		}

		@Override
		public int compareTo(Task task) {
			return this.priority = task.priority;
		}
	}



}
