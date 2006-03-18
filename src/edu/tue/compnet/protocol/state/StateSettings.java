package edu.tue.compnet.protocol.state;

import java.io.File;

import edu.tue.compnet.protocol.Settings;

/**
 * Handles setting settings for the state.
 * @author Pal Hargitai
 * @author Siu-Hong Li
 */
public class StateSettings {
	/** The key of the Check Hashes. */
	public static String CHECK_HASHES = "checkhashes";
	/** The key of the Username. */
	public static String USERNAME = "username";
	/** The key of the Incoming directory. */
	public static String INCOMING = "incoming";
	/** The key of the Share directory. */
	public static String SHARE = "share";

	// The incoming file folder
	File incoming;
	// The share file folder
	File share;
	// The nickname
	String nickname;
	// The settings
	Settings settings;
	// Check hashes
	boolean checkhashes;
	
	/**
	 * Constructor of the state settings.
	 * @param s The preferences.
	 */
	public StateSettings(Settings s) {
		settings = s;
		nickname = settings.getValue("/", USERNAME, "anonymous");
		incoming = new File(settings.getValue("/", INCOMING, System.
				getenv("HOME")));
		share = new File(settings.getValue("/", SHARE,	System.
				getenv("HOME")));
		checkhashes = settings.getBool("/", CHECK_HASHES, false);
	}
	
	/**
	 * Set to check for hashes.
	 * @param h true to check for hashes false to not
	 */
	public synchronized void setCheckHashes(boolean h) {
		checkhashes = h;
		settings.setBool("/", CHECK_HASHES, h);
	}
	
	/**
	 * Wether to check for hashes.
	 * @return True to check.
	 */
	public synchronized boolean getCheckHashes() {
		return checkhashes;
	}
	
	/**
	 * Get the nickname of the user.
	 * @return The nickname.
	 */
	public synchronized String getNickname() {
		return new String(nickname);
	}
	
	/**
	 * Set the nickname of the user.
	 * @param nick The nickname to be set.
	 */
	public synchronized void setNickname(String nick) {
		settings.setValue("/", USERNAME, nick);
		nickname = nick;
	}
	
	/**
	 * Get the share directory.
	 * @return The share directory.
	 */
	public synchronized File getShareDirectory() {
		if (share == null) {
			return null;
		} else {
			return share;
		}
	}
	
	/**
	 * Set the share directory.
	 * @param s The share directory to be set.
	 */
	public synchronized void setShareDirectory(File s) {
		if (s.isDirectory() && s.canRead()) {
			if (share == null) {
				share = s;
			} else {
				share = s;
			}
			settings.setValue("/", SHARE, s.getPath());
		}
	}
	
	/**
	 * Get the incoming directory.
	 * @return The incoming directory.
	 */
	public synchronized File getIncomingDirectory() {
		if (incoming == null) {
			return null;
		} else {
			return incoming;
		}
	}
	
	/**
	 * Set the incoming directory.
	 * @param i The incoming directory to be set.
	 */
	public synchronized void setIncomingDirectory(File i) {
		if (i.isDirectory() && i.canWrite()) {
			if (incoming == null) {
				incoming = i;
			} else {
				incoming = i;
			}
			settings.setValue("/", INCOMING, i.getPath());
		}
	}
}
