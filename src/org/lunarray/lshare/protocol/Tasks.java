package org.lunarray.lshare.protocol;

import java.util.Timer;

import org.lunarray.lshare.protocol.state.UserListTask;
import org.lunarray.lshare.tasks.RunnableTask;
import org.lunarray.lshare.tasks.SignOnTimer;
import org.lunarray.lshare.tasks.TimedRunnableTask;

public class Tasks {
	// 2 threads, timer tasks, single tasks
	// Standard controls for accessing functionality.
	private Controls controls;
	// A standard timer for multi tasks
	private Timer timer;
	
	public Tasks(Controls c) {
		controls = c;
		timer = new Timer();
	}
	
	public void start() {
		setupDefaultTasks();
	}
	
	public void stop() {
		timer.cancel();
	}
	
	public void setupDefaultTasks() {
		// Signon broadcasts
		enqueueMultiTask(new SignOnTimer());
		// Userlist timeouts
		enqueueMultiTask(new UserListTask());
	}
	
	public void enqueueSingleTask(RunnableTask r) {
		r.runTask(controls);
	}
	
	public void enqueueMultiTask(TimedRunnableTask r) {
		r.setControls(controls);
		timer.schedule(r, 0, r.getDelay());
	}
}
