package org.lunarray.lshare.protocol;

import org.lunarray.lshare.protocol.settings.GUISettings;

/** An interface describing some standard settings available for the user
 * interface.
 * @author Pal Hargitai
 */
public interface ExternalSettings {
	
	/** Get the username set in the protocol to identify this user to other
	 * instances of this protocol.
	 * @return The string that is used to identify this user.
	 */
	public String getUsername();
	
	/** Set the username in this protocol to identify this user to other
	 * instances of this protocol.
	 * @param un The new username.
	 */
	public void setUsername(String un);
	
	/** Gets the challenge to uniquely identify this user and identify it as
	 * a buddy to other users.
	 * @return The challenge set in this protocol.
	 */
	public String getChallenge();
	
	/** Sets the challenge to uniquely identify this user.
	 * @param c The new challenge.
	 */
	public void setChallenge(String c);

	/** Gets settings available for the user interface.
	 * @return The settings available for the user interface.
	 */
	public GUISettings getSettingsForGUI();
}
