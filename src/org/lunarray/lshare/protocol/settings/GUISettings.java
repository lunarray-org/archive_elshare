package org.lunarray.lshare.protocol.settings;

import org.lunarray.lshare.protocol.Settings;

public class GUISettings {
	
	public static String DEFAULT_LOC = Settings.DEFAULT_LOC + "/gui";
	
	private RawSettings rs;
	
	public GUISettings(RawSettings r) {
		rs = r;
	}

	public void remove(String loc, String key) {
		rs.remove(DEFAULT_LOC + loc, key);
	}
	
	public void removeNode(String loc) {
		rs.removeNode(DEFAULT_LOC + loc);
	}

	public String[] getKeys(String loc) {
		return rs.getKeys(DEFAULT_LOC + loc); 
	}
	
	public String getString(String loc, String key, String def) {
		return rs.getString(DEFAULT_LOC + loc, key, def);
	}

	public void setString(String loc, String key, String val) {
		rs.setString(DEFAULT_LOC + loc, key, val);
	}
	
	public int getInt(String loc, String key, int def) {
		return rs.getInt(DEFAULT_LOC, key, def);
	}
	
	public void setInt(String loc, String key, int val) {
		rs.setInt(DEFAULT_LOC + loc, key, val);
	}
	
	public boolean getBool(String loc, String key, boolean def) {
		return rs.getBool(DEFAULT_LOC + loc, key, def);
	}
	
	public void setBool(String loc, String key, boolean val) {
		rs.setBool(DEFAULT_LOC + loc, key, val);
	}
	
	public byte[] getByteArray(String loc, String key, byte[] def) {
		return rs.getByteArray(DEFAULT_LOC + loc, key, def);
	}
	
	public void setByteArray(String loc, String key, byte[] val) {
		rs.setByteArray(DEFAULT_LOC + loc, key, val);
	}

	public long getLong(String loc, String key, long def) {
		return rs.getLong(DEFAULT_LOC + loc, key, def);	
	}

	public void setLong(String loc, String key, long val) {
		rs.setLong(DEFAULT_LOC, key, val);
	}
}
