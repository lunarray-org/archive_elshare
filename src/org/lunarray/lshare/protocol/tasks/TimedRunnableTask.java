package org.lunarray.lshare.protocol.tasks;

import java.util.TimerTask;

import org.lunarray.lshare.protocol.Controls;

public abstract class TimedRunnableTask extends TimerTask implements RunnableTask {

	private Controls controls;
	
	public abstract int getDelay();
	
	public void setControls(Controls c) {
		controls = c;
	}
	
	@Override
	public void run() {
		runTask(controls);
	}

	public abstract void runTask(Controls c);
}
