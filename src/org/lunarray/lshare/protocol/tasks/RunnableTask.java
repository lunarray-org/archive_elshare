package org.lunarray.lshare.protocol.tasks;

import org.lunarray.lshare.protocol.Controls;

/**
 * A simple runnable task.
 * @author Pal Hargitai
 */
public interface RunnableTask {

	/**
	 * Run the task.
	 * @param c The controls to the protocol.
	 */
	public void runTask(Controls c);
}
