package org.lunarray.lshare.protocol.state.sharing;

import org.lunarray.lshare.protocol.Controls;
import org.lunarray.lshare.protocol.Settings;
import org.lunarray.lshare.protocol.settings.RawSettings;

public class ShareSettings {
	
	public static String SHARE_LOC = "/shares";
	public static String HASH_LOC = "/hashes";
	public static String ACCESSDATE_LOC = "/accessdate";
	public static String SHARE_UNSET = ".";
	public static byte[] HASH_UNSET = {
		0x00, 0x00, 0x00, 0x00,
		0x00, 0x00, 0x00, 0x00,
		0x00, 0x00, 0x00, 0x00,
		0x00, 0x00, 0x00, 0x00
	};
	public static long ACCESSDATE_UNSET = 0;
	public static String HASH_ALGO = "md5";
	
	private RawSettings rsettings;
	
	public ShareSettings(RawSettings rs) {
		rsettings = rs;
	}

	public String[] getShareNames() {
		return rsettings.getKeys(Settings.DEFAULT_LOC + SHARE_LOC);
	}
	
	public String getSharePath(String sharename) {
		return rsettings.getString(Settings.DEFAULT_LOC + SHARE_LOC, sharename, SHARE_UNSET);
	}
	
	public void removeSharePath(String sharename) {
		rsettings.remove(Settings.DEFAULT_LOC + SHARE_LOC, sharename);
		Controls.getLogger().finer("ShareSettings: removed share: \"" + sharename + "\"");
	}
	
	public void setSharePath(String sharename, String sharepath) {
		rsettings.setString(Settings.DEFAULT_LOC + SHARE_LOC, sharename, sharepath);
		Controls.getLogger().finer("ShareSettings: added share: \"" + sharename + "\" at " + sharepath);
	}

	public String[] getFilesInPath() {
		return rsettings.getKeys(Settings.DEFAULT_LOC + HASH_LOC);
	}
	
	public byte[] getHash(String loc) {
		return rsettings.getByteArray(Settings.DEFAULT_LOC + HASH_LOC, loc, HASH_UNSET);
	}
	
	public long getAccessDate(String loc) {
		return rsettings.getLong(Settings.DEFAULT_LOC + ACCESSDATE_LOC, loc, ACCESSDATE_UNSET);
	}
	
	public void removePath(String loc) {
		rsettings.remove(Settings.DEFAULT_LOC + HASH_LOC, loc);
		rsettings.remove(Settings.DEFAULT_LOC + ACCESSDATE_LOC, loc);		
		Controls.getLogger().finer("ShareSettings: removed file \"" + loc + "\"");
	}
	
	public void setHash(String loc, byte[] h) {
		rsettings.setByteArray(Settings.DEFAULT_LOC + HASH_LOC, loc, h);
		Controls.getLogger().finer("ShareSettings: set hash \"" + loc + "\"");
	}
	
	public void setAccessDate(String loc, long i) {
		rsettings.setLong(Settings.DEFAULT_LOC + ACCESSDATE_LOC, loc, i);
		Controls.getLogger().finer("ShareSettings: set accessdate \"" + loc + "\"");
	}
}
