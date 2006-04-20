package org.lunarray.lshare.protocol.state.sharing;

import org.lunarray.lshare.protocol.Controls;
import org.lunarray.lshare.protocol.tasks.RunnableTask;
import org.lunarray.lshare.protocol.tasks.TimedRunnableTask;

/** The task for hasing the files.
 * @author Pal Hargitai
 */
public class HashTask extends TimedRunnableTask {
	/** The time between a timed task. That is {@value} milliseconds. 
	 */
	public final static int DELAY = 6000;
	
	@Override
	/** Gets the delay between hash checks.
	 */
	public int getDelay() {
		return DELAY;
	}
	
	@Override
	/** Runs the hashing task.
	 * @param c The controls to the protocol.
	 */
	public void runTask(Controls c) {
		Controls.getLogger().finer("Started file hashing");
		c.getTasks().backgroundTask(new Task());
	}
	
	/** A hidden class that is run in the background to hash the file list.
	 * @author Pal Hargitai
	 */
	private class Task implements RunnableTask {
		/** Processes hashing.
		 * @param c The controls to the protocol.
		 */
		public void runTask(Controls c) {
			c.getState().getShareList().hash(c.getSettings().getShareSettings());
		}
	}
}
