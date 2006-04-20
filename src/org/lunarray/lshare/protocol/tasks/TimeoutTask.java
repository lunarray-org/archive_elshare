package org.lunarray.lshare.protocol.tasks;

import org.lunarray.lshare.protocol.Controls;

/** A task for handling timeout type tasks.
 * @author Pal Hargitai
 */
public abstract class TimeoutTask extends TimedRunnableTask {
	/** The amount of timeouts till cleanup.
	 */
	private int togo;
	
	/** Constructs a timeout task.
	 */
	public TimeoutTask() {
		togo = amount();
	}
	
	@Override
	/** Gets the delay between timeouts.
	 */
	public abstract int getDelay();
	
	/** Cleanup the task incase of all possible timeouts having passed.
	 * @param c The controls to the protocol.
	 */
	public abstract void cleanupTimedout(Controls c);
	
	/** Cleanup the task incase it is not relevant.
	 * @param c The controls of the protocol.
	 */
	public abstract void cleanupFinished(Controls c);
	
	/** Handle a single passing of a timeout.
	 * @param c The controls of the protocol.
	 */
	public abstract void timedOut(Controls c);
	
	/** Checks of the task is still relevant.
	 * @param c The controls of the protocol.
	 * @return True if it is not relevant, false if it is.
	 */
	public abstract boolean finished(Controls c);
	
	/** The amount of timeouts that are allowed to pas.
	 * @return The amount of passable timeouts.
	 */
	public abstract int amount();
	
	@Override
	/** The task of handling a single timeout.
	 * @param c The controls to the protocol.
	 */
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
