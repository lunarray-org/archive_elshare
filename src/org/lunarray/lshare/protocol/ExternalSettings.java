package org.lunarray.lshare.protocol;

import org.lunarray.lshare.protocol.settings.GUISettings;
import org.lunarray.lshare.protocol.state.sharing.ShareSettings;

public interface ExternalSettings {
	
	public String getUsername();
	
	public void setUsername(String un);
	
	public String getChallenge();
	
	public void setChallenge(String c);

	public GUISettings getSettingsForGUI();
	
	public ShareSettings getShareSettings();
}
