package edu.tue.compnet.protocol.state;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;
import java.util.TreeMap;

import edu.tue.compnet.Output;
import edu.tue.compnet.protocol.Settings;
import edu.tue.compnet.protocol.State;

/**
 * A class that does file hash checking and listing.
 * @author Pal Hargitai
 * @author Siu-Hong Li
 */
public class HashList {
	/** The hash algorithm to use. */
	public static String HASH_ALGO = "md5";
	/** The length of the hash. */
	public static int HASH_LENGTH = 16;
	/** The empty hash. */
	public static byte[] HASH_EMPTY = {
		0x00, 0x00, 0x00, 0x00,
		0x00, 0x00, 0x00, 0x00,
		0x00, 0x00, 0x00, 0x00,
		0x00, 0x00, 0x00, 0x00
	};
	/** The time between an update of the hashes. */
	public static int TASK_TIME = 5000;

	// The list of known hashes
	private TreeMap<File, byte[]> HashList;
	// The list of known hashes
	private TreeMap<File, Long> AccessList;
	// The settings
	Settings settings;
	// The state
	State state;
	// The task
	HashTask task;
	
	/**
	 * Constructs the hash list.
	 * @param s The settings.
	 * @param st The state.
	 */
	public HashList(Settings s, State st) {
		settings = s;
		state = st;
		HashList = new TreeMap<File, byte[]>();
		AccessList = new TreeMap<File, Long>();
		task = new HashTask();
		initFromSettings();
		st.addProcotolTask(task, TASK_TIME);
	}
	
	/**
	 * Gets the task for the tasks to execute.
	 * @return The task.
	 */
	public TimerTask getTask() {
		return task;
	}
	
	/**
	 * Check all files that can be checked
	 */
	public synchronized void checkFiles() {
		// Get the file list
		File[] flist = state.getStateSettings().getShareDirectory().
			listFiles();
		for (File f: flist) {
			if (f.isFile()) {
				if (!HashList.containsKey(f) || AccessList.get(f).
						compareTo(f.lastModified()) < 0) {
					updateFile(f);
				}
			}
		}
		updateSettings();
	}
	
	/**
	 * Initialise the hash and access lists from the settings.
	 */
	private void initFromSettings() {
		for (String s: settings.getKeys("/access")) {
			File f = new File(state.getStateSettings().getShareDirectory().
					getAbsolutePath() + File.separator + s);
			if (f.exists()) {
				AccessList.put(f, settings.getLong("/access", f.getName(), 0));
			} else {
				settings.remove("/access", f.getName());
			}
		}
		for (String s: settings.getKeys("/hashes")) {
			File f = new File(state.getStateSettings().getShareDirectory().
					getAbsolutePath() + File.separator + s);
			if (f.exists()) {
				HashList.put(f, settings.getByteArray("/hashes", f.getName(), HASH_EMPTY));
			} else {
				settings.remove("/hashes", f.getName());
			}
		}
	}
	
	/**
	 * Updates settings so that they may be loaded on next run.
	 */
	private synchronized void updateSettings() {
		for (File f: HashList.keySet()) {
			settings.setByteArray("/hashes", f.getName(), HashList.get(f));
		}
		for (File f: AccessList.keySet()) {
			settings.setLong("/access", f.getName(), AccessList.get(f));
		}
		for (File f: HashList.keySet()) {
			if (!f.exists()) {
				Output.out("File removed: " + f.getName());
				settings.remove("/access", f.getName());
				AccessList.remove(f);
				settings.remove("/hashes", f.getName());
				HashList.remove(f);
			}
		}
	}
	
	/**
	 * Update the file information.
	 * @param f The file to update.
	 */
	private synchronized void updateFile(File f) {
		byte[] md5 = new byte[16];
		md5 = hash(f);
		if (!equals(md5, HASH_EMPTY)) {
			HashList.put(f, md5);
			AccessList.put(f, Long.valueOf(f.lastModified()));
			Output.out("Hashed: " + f.getName());
		}
	}
	
	/**
	 * Get the hash of a file.
	 * @param f The file we want a hash of.
	 * @return The hash of the file.
	 */
	public byte[] getHash(File f) {
		if (HashList.containsKey(f)) {
			return HashList.get(f);
		} else {
			return HASH_EMPTY;
		}
	}
	
	/**
	 * Checks if a hash is the empty hash.
	 * @param h The hash to check.
	 * @param j The hash to compare to.
	 * @return True if it's an empty hash, false if not.
	 */
	public static boolean equals(byte[] h, byte[] j) {
		boolean eq = true;
		search: {
			for (int i = 0; i < HASH_LENGTH; i++) {
				if (h[i] != j[i]) {
					eq = false;
					break search;
				}
			}	
		}
		return eq;
	}
	
	/**
	 * Give a hash of the given file.
	 * @param f The file to hash.
	 * @return The hash of file f.
	 */
	public static byte[] hash(File f) {
		byte[] md5 = HASH_EMPTY;
		try {
			FileInputStream fi = new FileInputStream(f);
			MessageDigest md = MessageDigest.getInstance(HASH_ALGO);
			byte[] data = new byte[1000];
			int done = 0;
			while (fi.available() > 0) {
				done = fi.read(data);
				md.update(data, 0, done);
			}
			md5 = md.digest();
		} catch (FileNotFoundException e) {
			Output.err("File not found!");
		} catch (NoSuchAlgorithmException nse) {
			Output.err("Hashing not supported!");
		} catch (IOException ie) {
			Output.err("File error!");
		}
		return md5;
	}
	
	/**
	 * Get file from hash
	 * @param h The hash to get
	 * @return The file that matches the hash, or null
	 */
	public List<File> getFile(byte[] h) {
		ArrayList<File> ret = new ArrayList<File>();
		for (File f: HashList.keySet()) {
			// compare
			if (equals(h, HashList.get(f))) {
				ret.add(f);
				Output.out("Matched hash!");
			}
		}
		return ret;
	}
	
	public static String toString(byte[] h) {
		if (equals(h, HASH_EMPTY)) {
			return "";
		} else {
			String ret = "";
			for (byte b: h) {
				int p1 = b & 0x0f;
				int p2 = (b >> 4) & 0x0f;
				ret += getHex(p1);
				ret += getHex(p2);
			}
			return ret;
		}
	}
	
	private static String getHex(int b) {
		String ret = "";
		switch (b) {
		case 0:
			ret += "0";
			break;
		case 1:
			ret += "1";
			break;
		case 2:
			ret += "2";
			break;
		case 3:
			ret += "3";
			break;
		case 4:
			ret += "4";
			break;
		case 5:
			ret += "5";
			break;
		case 6:
			ret += "6";
			break;
		case 7:
			ret += "7";
			break;
		case 8:
			ret += "8";
			break;
		case 9:
			ret += "9";
			break;
		case 10:
			ret += "A";
			break;
		case 11:
			ret += "B";
			break;
		case 12:
			ret += "C";
			break;
		case 13:
			ret += "D";
			break;
		case 14:
			ret += "E";
			break;
		case 15:
			ret += "F";
			break;
		}
		return ret;
	}
	
	/**
	 * The task that does the checking.
	 * @author Pal Hargitai
	 * @author Siu-Hong Li
	 */
	private class HashTask extends TimerTask {
		/**
		 * Just runs the check
		 */
		public void run() {
			checkFiles();
		}
	}
}
