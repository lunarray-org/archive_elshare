package org.lunarray.lshare.protocol.tasks;

import org.lunarray.lshare.protocol.Controls;

public abstract class TimeoutTask extends TimedRunnableTask {

	private int togo;
	
	public TimeoutTask() {
		togo = amount();
	}
	
	@Override
	public abstract int getDelay();
	
	public abstract void cleanupTimedout(Controls c);
	
	public abstract void cleanupFinished(Controls c);
	
	public abstract void timedOut(Controls c);
	
	public abstract boolean finished(Controls c);
	
	public abstract int amount();
	
	@Override
	public void runTask(Controls c) {
		if (finished(c)) {
			cleanupFinished(c);
			cancel();
		} else if (togo > 0) {
			timedOut(c);
			togo--;
		} else {
			cleanupTimedout(c);
			cancel();
		}
	}
}
