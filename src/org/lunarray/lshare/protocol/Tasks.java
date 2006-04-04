package org.lunarray.lshare.protocol;

import java.util.Timer;

import org.lunarray.lshare.protocol.state.sharing.HashTask;
import org.lunarray.lshare.protocol.state.userlist.UserListTask;
import org.lunarray.lshare.protocol.tasks.LazyThread;
import org.lunarray.lshare.protocol.tasks.RunnableTask;
import org.lunarray.lshare.protocol.tasks.SignOnTimer;
import org.lunarray.lshare.protocol.tasks.TaskThread;
import org.lunarray.lshare.protocol.tasks.TimedRunnableTask;

/**
 * Handles tasks of the protocol. Puts these tasks in threads if need be.
 * @author Pal Hargitai
 */
public class Tasks {
	
	/**
	 * The controls to communicate with the rest of the protocol.
	 */
	private Controls controls;
	
	/**
	 * A timer for timed tasks.
	 */
	private Timer timer;
	
	/**
	 * The thread grou in which tasks will reside.
	 */
	private ThreadGroup tgroup;
	
	/**
	 * A lazy thread to handle non-critical events.
	 */
	private LazyThread lazy;
	
	/**
	 * Instanciates the tasks.
	 * @param c The controls to communicate with the rest of the protocol.
	 */
	public Tasks(Controls c) {
		controls = c;
		tgroup = new ThreadGroup(c.getThreadGroup(), "tasks");
		timer = new Timer();
		lazy = new LazyThread(controls);
	}
	
	/**
	 * Starts the tasks. Before this point, no thread will be active.
	 */
	public void start() {
		setupDefaultTasks();
		lazy.start();
	}

	/**
	 * Stops the threads and neatly finished off all tasks.
	 */
	public void stop() {
		timer.cancel();
		lazy.close();
	}
	
	/**
	 * Sets up default tasks.
	 */
	public void setupDefaultTasks() {
		// Signon broadcasts
		enqueueMultiTask(new SignOnTimer());
		// Userlist timeouts
		enqueueMultiTask(new UserListTask());
		// File hashing
		enqueueMultiTask(new HashTask());
	}
	
	/**
	 * Enqueues a single (quick) task. Long tasks should enqueue in either a
	 * background task or a lazy task.
	 * @param r The task to run.
	 */
	public void enqueueSingleTask(RunnableTask r) {
		r.runTask(controls);
	}
	
	/**
	 * Enqueues a lazy task. This should be a task that does not require any
	 * immediate attention.
	 * @param r The task to run.
	 */
	public void enqueueLazyTask(RunnableTask r) {
		lazy.enqueue(r);
	}
	
	/**
	 * Enqueues a task that is to be run multiple times. A timed task.
	 * @param r The timed task to enqueue.
	 */
	public void enqueueMultiTask(TimedRunnableTask r) {
		r.setControls(controls);
		timer.schedule(r, 0, r.getDelay());
	}
	
	/**
	 * A background task, this starts a thread in the background. This should
	 * be used with caution and specifically, rarely.
	 * @param r The task to run.
	 */
	public void backgroundTask(RunnableTask r) {
		TaskThread t = new TaskThread(tgroup, r, controls);
		t.start();
	}
}
