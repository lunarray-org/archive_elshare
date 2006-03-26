package org.lunarray.lshare.protocol;

import org.lunarray.lshare.protocol.settings.GUISettings;

public interface ExternalSettings {
	
	public String getUsername();
	
	public void setUsername(String un);
	
	public String getChallenge();
	
	public void setChallenge(String c);

	public GUISettings getSettingsForGUI();
}
