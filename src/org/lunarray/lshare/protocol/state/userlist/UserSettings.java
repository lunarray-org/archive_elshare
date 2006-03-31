package org.lunarray.lshare.protocol.state.userlist;

import org.lunarray.lshare.protocol.Controls;
import org.lunarray.lshare.protocol.Settings;
import org.lunarray.lshare.protocol.settings.RawSettings;

public class UserSettings {
	
	public static String BUDDY_LOC = "/buddies";

	private RawSettings rsettings;
	
	public UserSettings(RawSettings rs) {
		rsettings = rs;
	}
	
	public void saveBuddy(String un, String ch) {
		Controls.getLogger().finer("Settings: Buddy added: " + un);
		rsettings.setString(Settings.DEFAULT_LOC + BUDDY_LOC, ch, un);
	}
	
	public void removeBuddy(String ch) {
		Controls.getLogger().finer("Settings: Buddy removed: " + rsettings.
				getString(Settings.DEFAULT_LOC + BUDDY_LOC, ch, Settings.USERNAME_UNSET));
		rsettings.remove(Settings.DEFAULT_LOC + BUDDY_LOC, ch);
	}

	public String getSavedName(String su) {
		return rsettings.getString(Settings.DEFAULT_LOC + BUDDY_LOC, su, Settings.CHALLENGE_UNSET);
	}	
	
	public String[] getChallenges() {
		return rsettings.getKeys(Settings.DEFAULT_LOC + BUDDY_LOC);
	}

}
