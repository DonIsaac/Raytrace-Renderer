package test;
import org.junit.*;
import static org.junit.Assert.*;

import java.util.concurrent.atomic.AtomicInteger;

import tools.ThreadPool;
import tools.ThreadPool.PoolState;

public class ThreadPoolTests {

	@Test
	public void willExecuteTasks() throws InterruptedException {
		ThreadPool pool = new ThreadPool();
		int taskCount = 5;
		AtomicInteger result = new AtomicInteger(0);
		
		Runnable increment = () -> {
				result.incrementAndGet();
		};
		
		for (int i = 0; i < taskCount; i++) {
			pool.register(increment);
		}
		pool.start();
		
		pool.stop();
		
		assertEquals(taskCount, result.get());
		
	}
	
	@Test
	public void testShutdown() throws InterruptedException {
		ThreadPool pool = new ThreadPool();
		int taskCount = 20;
		AtomicInteger result = new AtomicInteger(0);
		
		Runnable increment = () -> {
				result.incrementAndGet();
		};
		
		for (int i = 0; i < taskCount; i++) {
			pool.register(increment);
		}
		pool.start();
		
		pool.shutdown();
		
		assertEquals(taskCount, result.get());
		assertTrue(pool.getState() == PoolState.COMPLETE);
	}
}
