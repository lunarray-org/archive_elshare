package org.lunarray.lshare.protocol.tasks;

import org.lunarray.lshare.protocol.Controls;

public class TaskThread extends Thread {

	private RunnableTask task;
	private Controls controls;
	
	public TaskThread(ThreadGroup g, RunnableTask r, Controls c) {
		super(g, "BackgroundTask");
		task = r;
		controls = c;
	}
	
	public void run() {
		task.runTask(controls);
	}
	
}
