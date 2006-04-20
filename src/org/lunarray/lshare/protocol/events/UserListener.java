package org.lunarray.lshare.protocol.events;

/** The user listener implemented by classes that should be interested in
 * events for user logon, logoff and update.
 * @author Pal Hargitai
 */
public interface UserListener {
	/** Triggered when a user is signed on.
	 * @param e The event that triggered this call.
	 */
	void signon(UserEvent e);
	
	/** Triggered when a user is signed off.
	 * @param e The event that triggered this call.
	 */
	void signoff(UserEvent e);
	
	/** Triggered when a user is updated.
	 * @param e The event that triggered this call.
	 */
	void update(UserEvent e);
}
