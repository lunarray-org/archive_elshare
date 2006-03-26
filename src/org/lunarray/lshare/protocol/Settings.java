package org.lunarray.lshare.protocol;

import org.lunarray.lshare.protocol.settings.GUISettings;
import org.lunarray.lshare.protocol.settings.RawSettings;

public class Settings implements ExternalSettings {
	
	public static String DEFAULT_LOC = "/lshare";
	public static String BUDDY_LOC = "/buddies";
	
	public static String USERNAME_KEY = "username";
	public static String USERNAME_UNSET = "anonymous";
	
	public static String CHALLENGE_KEY = "challenge";
	public static String CHALLENGE_UNSET = "";
	
	private RawSettings rsettings;
	private String username;
	private String challenge;

	public Settings(Controls c) {
		rsettings = new RawSettings();
		initSettings();
	}
	
	private void initSettings() {
		username = rsettings.getString(DEFAULT_LOC, USERNAME_KEY, 
				USERNAME_UNSET);
		challenge = rsettings.getString(DEFAULT_LOC, CHALLENGE_KEY,
				CHALLENGE_UNSET);
	}
	
	public String getUsername() {
		return username;
	}
	
	public void setUsername(String un) {
		if (un.length() > 0) {
			Controls.getLogger().finer("Settings: Set username: " + un);
			
			username = un;
			rsettings.setString(DEFAULT_LOC, USERNAME_KEY, username);
		}
	}
	
	public String getChallenge() {
		return challenge;
	}
	
	public void setChallenge(String c) {
		if (c.length() > 0) {
			Controls.getLogger().finer("Settings: Set challenge: " + c);
		
			challenge = c;
			rsettings.setString(DEFAULT_LOC, CHALLENGE_KEY, challenge);
		}
	}
	
	public void saveBuddy(String un, String ch) {
		Controls.getLogger().finer("Settings: Buddy added: " + un);
		rsettings.setString(DEFAULT_LOC + BUDDY_LOC, ch, un);
	}
	
	public void removeBuddy(String ch) {
		Controls.getLogger().finer("Settings: Buddy removed: " + rsettings.
				getString(DEFAULT_LOC + BUDDY_LOC, ch, USERNAME_UNSET));
		rsettings.remove(DEFAULT_LOC + BUDDY_LOC, ch);
	}

	public String getSavedName(String su) {
		return rsettings.getString(DEFAULT_LOC, su, CHALLENGE_UNSET);
	}	
	
	public String[] getChallenges() {
		return rsettings.getKeys(DEFAULT_LOC + BUDDY_LOC);
	}
	
	public GUISettings getSettingsForGUI() {
		return new GUISettings(rsettings);
	}
}
