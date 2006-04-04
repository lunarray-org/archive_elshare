package org.lunarray.lshare.protocol;

import org.lunarray.lshare.protocol.settings.GUISettings;
import org.lunarray.lshare.protocol.settings.RawSettings;
import org.lunarray.lshare.protocol.state.sharing.ShareSettings;
import org.lunarray.lshare.protocol.state.userlist.UserSettings;

/**
 * The basic settings for the protocol.
 * @author Pal Hargitai
 */
public class Settings implements ExternalSettings {
	
	/**
	 * The default location of the settings that are to be saved. This location
	 * is {@value}.
	 */
	public final static String DEFAULT_LOC = "/lshare";
	
	/**
	 * The key used for retrieving the username. The key for the username
	 * {@value}.
	 */
	public final static String USERNAME_KEY = "username";
	
	/**
	 * The username if it's unset. This is {@value}.
	 */
	public final static String USERNAME_UNSET = "anonymous";
	
	/**
	 * The key used for retrieving the challenge. The key for the challenge is
	 * {@value}.
	 */
	public final static String CHALLENGE_KEY = "challenge";
	
	/**
	 * The challenge if it's unset. The is {@value}.
	 */
	public final static String CHALLENGE_UNSET = "";
	
	/**
	 * Raw settings to be used as a backend for settings.
	 */
	private RawSettings rsettings;
	
	/**
	 * The username currently set in the system.
	 */
	private String username;
	
	/**
	 * The challenge currently set in the system.
	 */
	private String challenge;
	
	/**
	 * The settings for user list management.
	 */
	private UserSettings usettings;
	
	/**
	 * The settings for share management.
	 */
	private ShareSettings ssettings;

	/**
	 * Constructs the settings and all settings that are dependant on it.
	 * @param c The protocol controls.
	 */
	public Settings(Controls c) {
		rsettings = new RawSettings();
		usettings = new UserSettings(rsettings);
		ssettings = new ShareSettings(rsettings);
		initSettings();
	}
	
	/**
	 * Gets the username currently set in the system.
	 * @return The username currently set.
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * Sets the username in the system.
	 * @param un The username to set to.
	 */
	public void setUsername(String un) {
		if (un.length() > 0) {
			Controls.getLogger().finer("Set username: " + un);
			
			username = un;
			rsettings.setString(DEFAULT_LOC, USERNAME_KEY, username);
		}
	}
	
	/**
	 * Gets the challenge currently set in the system.
	 * @return The challenge currently set in the system.
	 */
	public String getChallenge() {
		return challenge;
	}
	
	/**
	 * Sets the challenge in the system.
	 * @param c The new challenge to set in the system.
	 */
	public void setChallenge(String c) {
		if (c.length() > 0) {
			Controls.getLogger().finer("Set challenge: " + c);
		
			challenge = c;
			rsettings.setString(DEFAULT_LOC, CHALLENGE_KEY, challenge);
		}
	}
	
	/**
	 * Gets the settings that are relevant to userlist management.
	 * @return The usersettings.
	 */
	public UserSettings getUserSettings() {
		return usettings;
	}
	
	/**
	 * Gets the settings that are relevant to shareing.
	 * @return The sharesettings.
	 */
	public ShareSettings getShareSettings() {
		return ssettings;
	}
	
	/**
	 * Gets the settings that are usefull for a gui.
	 * @return The gui settings.
	 */
	public GUISettings getSettingsForGUI() {
		return new GUISettings(rsettings);
	}

	/**
	 * Initialises the standard settings.
	 */
	private void initSettings() {
		username = rsettings.getString(DEFAULT_LOC, USERNAME_KEY, 
				USERNAME_UNSET);
		challenge = rsettings.getString(DEFAULT_LOC, CHALLENGE_KEY,
				CHALLENGE_UNSET);
	}
}
