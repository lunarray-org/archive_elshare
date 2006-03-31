package org.lunarray.lshare.protocol.state.sharing;

import java.util.Set;
import java.util.TreeMap;

import org.lunarray.lshare.protocol.Controls;
import org.lunarray.lshare.protocol.Settings;
import org.lunarray.lshare.protocol.settings.RawSettings;

public class ShareSettings {
	
	public static String SHARE_LOC = "/shares";
	public static String HASH_LOC = "/hashes";
	public static String ACCESSDATE_LOC = "/accessdate";
	public static String FILE_LOC = "/files";
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
	private TreeMap<String, byte[]> hashmap;
	
	public ShareSettings(RawSettings rs) {
		rsettings = rs;
		hashmap = new TreeMap<String, byte[]>();
		initTree();
	}
	
	private void initTree() {
		for (String s: rsettings.getKeys(Settings.DEFAULT_LOC + FILE_LOC)) {
			String name = rsettings.getString(Settings.DEFAULT_LOC + FILE_LOC, s, "");
			if (name.length() > 0) {
				hashmap.put(name, rsettings.getByteArray(Settings.DEFAULT_LOC + HASH_LOC, s, HASH_UNSET));
			} else {
				rsettings.remove(Settings.DEFAULT_LOC + FILE_LOC, s);
				rsettings.remove(Settings.DEFAULT_LOC + ACCESSDATE_LOC, s);
				rsettings.remove(Settings.DEFAULT_LOC + HASH_LOC, s);
			}
		}
	}

	public String[] getShareNames() {
		return rsettings.getKeys(Settings.DEFAULT_LOC + SHARE_LOC);
	}
	
	public String getSharePath(String sharename) {
		return rsettings.getString(Settings.DEFAULT_LOC + SHARE_LOC, sharename, SHARE_UNSET);
	}
	
	public void removeSharePath(String sharename) {
		rsettings.remove(Settings.DEFAULT_LOC + SHARE_LOC, sharename);
		Controls.getLogger().finer("Removed share: \"" + sharename + "\"");
	}
	
	public void setSharePath(String sharename, String sharepath) {
		rsettings.setString(Settings.DEFAULT_LOC + SHARE_LOC, sharename, sharepath);
		Controls.getLogger().finer("Added share: \"" + sharename + "\" at " + sharepath);
	}
	
	public Set<String> getFilesInPath() {
		return hashmap.keySet();
	}
	
	public byte[] getHash(String loc) {
		if (hashmap.containsKey(loc)) {
			return hashmap.get(loc);
		} else {
			return HASH_UNSET;
		}
	}
	
	public long getAccessDate(String loc) {
		if (hashmap.containsKey(loc)) {
			return rsettings.getLong(Settings.DEFAULT_LOC + ACCESSDATE_LOC, hashToString(hashmap.get(loc)), ACCESSDATE_UNSET);
		} else {
			return ACCESSDATE_UNSET;
		}
	}
	
	public void removePath(String loc) {
		if (hashmap.containsKey(loc)) {
			String k = hashToString(hashmap.get(loc));
			rsettings.remove(Settings.DEFAULT_LOC + FILE_LOC, k);
			rsettings.remove(Settings.DEFAULT_LOC + HASH_LOC, k);
			rsettings.remove(Settings.DEFAULT_LOC + ACCESSDATE_LOC, k);		
			Controls.getLogger().finer("Removed file \"" + loc + "\"");
		}
	}
	
	public void setData(String loc, byte[] h, long i) {
		String k = hashToString(h);
		rsettings.setString(Settings.DEFAULT_LOC + FILE_LOC, k, loc);
		rsettings.setByteArray(Settings.DEFAULT_LOC + HASH_LOC, k, h);
		rsettings.setLong(Settings.DEFAULT_LOC + ACCESSDATE_LOC, k, i);
		Controls.getLogger().finer("Set hash \"" + loc + "\"");
	}
	
	private String hashToString(byte[] dat) {
		String ret = "";
		for (byte b: dat) {
			ret += quadBitToString(b) + quadBitToString(b >> 4);
		}
		return ret;
	}
	
	private String quadBitToString(int b) {
		switch (b) {
		case 0x0:
			return "0";
		case 0x1:
			return "1";
		case 0x2:
			return "2";
		case 0x3:
			return "3";
		case 0x4:
			return "4";
		case 0x5:
			return "5";
		case 0x6:
			return "6";
		case 0x7:
			return "7";
		case 0x8:
			return "8";
		case 0x9:
			return "9";
		case 0xA:
			return "A";
		case 0xB:
			return "B";
		case 0xC:
			return "C";
		case 0xD:
			return "D";
		case 0xE:
			return "E";
		case 0xF:
			return "F";
		default:
			return "";
		}
	}
}
