package org.lunarray.lshare.protocol.tasks;

import org.lunarray.lshare.protocol.Controls;

/**
 * A seperate thread for performing a task.
 * @author Pal Hargitai
 */
public class TaskThread extends Thread {

	/**
	 * The task that is to be run.
	 */
	private RunnableTask task;
	
	/**
	 * The controls to the protocol.
	 */
	private Controls controls;
	
	/**
	 * Constructs a task thread.
	 * @param g The group that the thread should reside in.
	 * @param r The task that should be run.
	 * @param c The controls to the protocol.
	 */
	public TaskThread(ThreadGroup g, RunnableTask r, Controls c) {
		super(g, "taskthread");
		task = r;
		controls = c;
	}
	
	/**
	 * Runs the task.
	 */
	public void run() {
		task.runTask(controls);
	}
}
