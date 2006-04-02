package org.lunarray.lshare.protocol.state.sharing;

import org.lunarray.lshare.protocol.Controls;
import org.lunarray.lshare.protocol.tasks.RunnableTask;
import org.lunarray.lshare.protocol.tasks.TimedRunnableTask;

public class HashTask extends TimedRunnableTask {

	public static int DELAY = 6000;
	
	@Override
	public int getDelay() {
		return DELAY;
	}
	
	@Override
	public void runTask(Controls c) {
		Controls.getLogger().finer("Started file hashing");
		c.getTasks().backgroundTask(new Task());
	}
	
	private class Task implements RunnableTask {
		public void runTask(Controls c) {
			c.getState().getShareList().hash(c.getSettings().getShareSettings());
		}
	}
}
