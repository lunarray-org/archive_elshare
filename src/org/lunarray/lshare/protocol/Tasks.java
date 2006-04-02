package org.lunarray.lshare.protocol;

import java.util.Timer;

import org.lunarray.lshare.protocol.state.sharing.HashTask;
import org.lunarray.lshare.protocol.state.userlist.UserListTask;
import org.lunarray.lshare.protocol.tasks.LazyThread;
import org.lunarray.lshare.protocol.tasks.RunnableTask;
import org.lunarray.lshare.protocol.tasks.SignOnTimer;
import org.lunarray.lshare.protocol.tasks.TaskThread;
import org.lunarray.lshare.protocol.tasks.TimedRunnableTask;

public class Tasks {
	// 2 threads, timer tasks, single tasks
	// Standard controls for accessing functionality.
	private Controls controls;
	// A standard timer for multi tasks
	private Timer timer;
	// The group of threads
	private ThreadGroup tgroup;
	// The lazy thread to handle noncritical tasks
	private LazyThread lazy;
	
	public Tasks(Controls c) {
		controls = c;
		tgroup = new ThreadGroup(c.getThreadGroup(), "tasks");
		timer = new Timer();
		lazy = new LazyThread(controls);
	}
	
	public void start() {
		setupDefaultTasks();
		lazy.start();
	}
	
	public void stop() {
		timer.cancel();
		lazy.close();
	}
	
	public void setupDefaultTasks() {
		// Signon broadcasts
		enqueueMultiTask(new SignOnTimer());
		// Userlist timeouts
		enqueueMultiTask(new UserListTask());
		// File hashing
		enqueueMultiTask(new HashTask());
	}
	
	public void enqueueSingleTask(RunnableTask r) {
		r.runTask(controls);
	}
	
	public void enqueueLazyTask(RunnableTask r) {
		lazy.enqueue(r);
	}
	
	public void enqueueMultiTask(TimedRunnableTask r) {
		r.setControls(controls);
		timer.schedule(r, 0, r.getDelay());
	}
	
	public void backgroundTask(RunnableTask r) {
		TaskThread t = new TaskThread(tgroup, r, controls);
		t.start();
	}
}
