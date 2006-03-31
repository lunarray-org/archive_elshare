package org.lunarray.lshare.protocol;

import org.lunarray.lshare.protocol.settings.GUISettings;
import org.lunarray.lshare.protocol.settings.RawSettings;
import org.lunarray.lshare.protocol.state.sharing.ShareSettings;
import org.lunarray.lshare.protocol.state.userlist.UserSettings;

public class Settings implements ExternalSettings {
	
	public static String DEFAULT_LOC = "/lshare";
	
	public static String USERNAME_KEY = "username";
	public static String USERNAME_UNSET = "anonymous";
	
	public static String CHALLENGE_KEY = "challenge";
	public static String CHALLENGE_UNSET = "";
	
	
	private RawSettings rsettings;
	private String username;
	private String challenge;
	private UserSettings usettings;
	private ShareSettings ssettings;

	public Settings(Controls c) {
		rsettings = new RawSettings();
		usettings = new UserSettings(rsettings);
		ssettings = new ShareSettings(rsettings);
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
			Controls.getLogger().finer("Set username: " + un);
			
			username = un;
			rsettings.setString(DEFAULT_LOC, USERNAME_KEY, username);
		}
	}
	
	public String getChallenge() {
		return challenge;
	}
	
	public void setChallenge(String c) {
		if (c.length() > 0) {
			Controls.getLogger().finer("Set challenge: " + c);
		
			challenge = c;
			rsettings.setString(DEFAULT_LOC, CHALLENGE_KEY, challenge);
		}
	}
	
	public UserSettings getUserSettings() {
		return usettings;
	}
	
	public ShareSettings getShareSettings() {
		return ssettings;
	}
	
	public GUISettings getSettingsForGUI() {
		return new GUISettings(rsettings);
	}

}
