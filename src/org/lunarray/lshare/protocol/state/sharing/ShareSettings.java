package org.lunarray.lshare.protocol.state.sharing;

import java.util.Set;
import java.util.TreeMap;

import org.lunarray.lshare.protocol.Controls;
import org.lunarray.lshare.protocol.Hash;
import org.lunarray.lshare.protocol.Settings;
import org.lunarray.lshare.protocol.settings.RawSettings;

/**
 * The settings that may be access by the share list.
 * @author Pal Hargitai
 */
public class ShareSettings {
	
	/**
	 * The path where share names and paths are contained. This is: {@value}.
	 */
	public final static String SHARE_LOC = "/shares";
	
	/**
	 * The path where the hashes are contained. This is: {@value}.
	 */
	public final static String HASH_LOC = "/hashes";
	
	/**
	 * The path where the access dates are contained. This is: {@value}.
	 */
	public final static String ACCESSDATE_LOC = "/accessdate";
	
	/**
	 * The path where the file list is contained. This is: {@value}.
	 */
	public final static String FILE_LOC = "/files";
	
	/**
	 * The path if the share if it is unset. This is: {@value}.
	 */
	public final static String SHARE_UNSET = ".";
	
	/**
	 * The access date if it's unset.  This is: {@value}.
	 */
	public final static long ACCESSDATE_UNSET = 0;
	
	/**
	 * The hashing algorithm that is to be used. This is: {@value}.
	 */
	public final static String HASH_ALGO = "md5";
	
	/**
	 * The raw settings that are to be used.
	 */
	private RawSettings rsettings;
	
	/**
	 * The hashes that are contained, this is: a map of path to id.
	 */
	private TreeMap<String, String> hashmap;
	
	/**
	 * Constructs the share settings.
	 * @param rs The raw settings to be used.
	 */
	public ShareSettings(RawSettings rs) {
		rsettings = rs;
		hashmap = new TreeMap<String, String>();
		initTree();
	}

	/**
	 * The known share names.
	 * @return The names of the shares.
	 */
	public String[] getShareNames() {
		return rsettings.getKeys(Settings.DEFAULT_LOC + SHARE_LOC);
	}
	
	/**
	 * Gets the file path of the given sharename.
	 * @param sharename The name of the share to get the path of.
	 * @return The path of the given name.
	 */
	public String getSharePath(String sharename) {
		return rsettings.getString(Settings.DEFAULT_LOC + SHARE_LOC, sharename,
				SHARE_UNSET);
	}
	
	/**
	 * Removes a specific share.
	 * @param sharename The name of the share to remove.
	 */
	public void removeSharePath(String sharename) {
		rsettings.remove(Settings.DEFAULT_LOC + SHARE_LOC, sharename);
		Controls.getLogger().finer("Removed share: \"" + sharename + "\"");
	}
	
	/**
	 * Adds a share to the shared list.
	 * @param sharename The name of the path to share.
	 * @param sharepath The path to share.
	 */
	public void setSharePath(String sharename, String sharepath) {
		rsettings.setString(Settings.DEFAULT_LOC + SHARE_LOC, sharename, 
				sharepath);
		Controls.getLogger().finer("Added share: \"" + sharename + "\" at " + 
				sharepath);
	}
	
	/**
	 * Gets all files paths that are hashed.
	 * @return The paths that are hashed.
	 */
	public Set<String> getFilesInPath() {
		return hashmap.keySet();
	}
	
	/**
	 * Gets the hash of a given file path.
	 * @param loc The file path to get the hash of.
	 * @return The hash of the file.
	 */
	public Hash getHash(String loc) {
		if (hashmap.containsKey(loc)) {
			return new Hash(rsettings.getByteArray(Settings.DEFAULT_LOC + HASH_LOC, 
					hashmap.get(loc), Hash.UNSET));
		} else {
			return Hash.getUnset();
		}
	}
	
	/**
	 * Gets the last recorded access date of the time of hash of a specified
	 * file.
	 * @param loc The path name of the file to get the access date of.
	 * @return The access date of the time of hash of a specified file.
	 */
	public long getAccessDate(String loc) {
		if (hashmap.containsKey(loc)) {
			return rsettings.getLong(Settings.DEFAULT_LOC + ACCESSDATE_LOC, 
					hashmap.get(loc), ACCESSDATE_UNSET);
		} else {
			return ACCESSDATE_UNSET;
		}
	}
	
	/**
	 * Removes all references for a given hashed file.
	 * @param loc The file to remove.
	 */
	public void removePath(String loc) {
		if (hashmap.containsKey(loc)) {
			String k = hashmap.get(loc);
			rsettings.remove(Settings.DEFAULT_LOC + FILE_LOC, k);
			rsettings.remove(Settings.DEFAULT_LOC + HASH_LOC, k);
			rsettings.remove(Settings.DEFAULT_LOC + ACCESSDATE_LOC, k);
			hashmap.remove(loc);
			Controls.getLogger().finer("Removed file \"" + loc + "\"");
		}
	}
	
	/**
	 * Set data for a specified hashed file.
	 * @param loc The location of the file.
	 * @param h The hash of the file.
	 * @param nh The hash of the name of the file.
	 * @param i The last modified date of the file.
	 */
	public void setData(String loc, Hash h, Hash nh, long i) {
		rsettings.setString(Settings.DEFAULT_LOC + FILE_LOC, nh.toString(), loc);
		rsettings.setByteArray(Settings.DEFAULT_LOC + HASH_LOC, nh.toString(), 
				h.getBytes());
		rsettings.setLong(Settings.DEFAULT_LOC + ACCESSDATE_LOC, nh.toString(), i);
		hashmap.put(loc, nh.toString());
		Controls.getLogger().finer("Set hash \"" + loc + "\"");
	}
	
	/**
	 * Initialises the hash tree.
	 *
	 */
	private void initTree() {
		for (String s: rsettings.getKeys(Settings.DEFAULT_LOC + FILE_LOC)) {
			String name = rsettings.getString(Settings.DEFAULT_LOC + FILE_LOC,
					s, "");
			if (name.length() > 0) {
				hashmap.put(name, s);
			} else {
				rsettings.remove(Settings.DEFAULT_LOC + FILE_LOC, s);
				rsettings.remove(Settings.DEFAULT_LOC + ACCESSDATE_LOC, s);
				rsettings.remove(Settings.DEFAULT_LOC + HASH_LOC, s);
			}
		}
	}
}
