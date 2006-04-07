package org.lunarray.lshare.protocol.state.userlist;

import org.lunarray.lshare.protocol.Controls;
import org.lunarray.lshare.protocol.Settings;
import org.lunarray.lshare.protocol.settings.RawSettings;

/**
 * The settings that a userlist object can use.
 * @author Pal Hargitai
 */
public class UserSettings {
	
	/**
	 * The location of buddy information. This is: {@value}.
	 */
	public final static String BUDDY_LOC = "/buddies";

	/**
	 * The raw settings to which these settings have access.
	 */
	private RawSettings rsettings;
	
	/**
	 * Constructs the user settings.
	 * @param rs The raw settings to which these settings have access.
	 */
	public UserSettings(RawSettings rs) {
		rsettings = rs;
	}
	
	/**
	 * Registers a username and challenge as a buddy.
	 * @param un The username to register.
	 * @param ch The challenge to register.
	 */
	public void saveBuddy(String un, String ch) {
		Controls.getLogger().finer("Buddy added: " + un);
		rsettings.setString(Settings.DEFAULT_LOC + BUDDY_LOC, ch, un);
	}
	
	/**
	 * Remove a specified challenge as a buddy.
	 * @param ch The challenge to unregister.
	 */
	public void removeBuddy(String ch) {
		Controls.getLogger().finer("Buddy removed: " + rsettings.
				getString(Settings.DEFAULT_LOC + BUDDY_LOC, ch, Settings.
				USERNAME_UNSET));
		rsettings.remove(Settings.DEFAULT_LOC + BUDDY_LOC, ch);
	}

	/**
	 * Gets the name associated with the 
	 * @param su The challenge to get the username of.
	 * @return The username associated with the challenge.
	 */
	public String getSavedName(String su) {
		return rsettings.getString(Settings.DEFAULT_LOC + BUDDY_LOC, su, 
				Settings.CHALLENGE_UNSET);
	}	
	
	/**
	 * Get all saved challenges.
	 */
	public String[] getChallenges() {
		return rsettings.getKeys(Settings.DEFAULT_LOC + BUDDY_LOC);
	}
}
