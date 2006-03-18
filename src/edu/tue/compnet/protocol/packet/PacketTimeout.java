package edu.tue.compnet.protocol.packet;

/**
 * Basic timeout information that is relevant to a packet that has to deal
 * with timeouts.
 * @author Pal Hargitai
 * @author Siu-Hong Li
 */
public abstract class PacketTimeout {
	// The amount of timeouts that may occur.
	int amount;
	// The time of a timeout.
	int timeout;
	// The current
	int current;
	
	/**
	 * The constructor of a timeout.
	 * @param a The amount of timeouts available.
	 * @param t The time between timeouts.
	 */
	public PacketTimeout(int a, int t) {
		amount = a;
		timeout = t;
		current = t;
	}
	
	/**
	 * Modify a packets timeout, if the timeout has passed
	 * @param mod The modifier of the timeout 
	 * @return Wether the timeout has passed
	 */
	public boolean checkTimeout(int mod) {
		// If it's run out, we don't care anymore, just ignore the mod.
		if (amount < 0) {
			return false;
		} else {
			// We've not run out of timeouts, so modify current one.
			current = current - mod;
			if (current < 0) {
				// This one ran out, update info and return true.
				current = timeout;
				amount--;
				return true;
			} else {
				return false;
			}
		}
	}
	
	/**
	 * Return wether this object should still  be allowed to live.
	 * @return True if this should be cleaned up.
	 */
	public boolean isDead() {
		return (amount <= 0);
	}
	
	/**
	 * Lock the state of a timeout so that things won't mess around with it.
	 */
	public abstract void lock();
	
	/**
	 * Unlock the state of a timeout.
	 */
	public abstract void unlock();
	
	/**
	 * Asks wether asking for a timeout is still valid, ie. the timeout has
	 * already been resolved.
	 * @return Wether it's still usefull to ask for a timeout.
	 */
	public abstract boolean isValid();
	
	/**
	 * One timeout has passed, handle it.
	 */
	public abstract void handleTimeout();
	
	/**
	 * Clean up, this thing's dead.
	 */
	public abstract void cleanUp();
}
