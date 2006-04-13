package edu.tue.compnet.protocol.state;

import java.util.Map;
import java.util.TreeMap;

import edu.tue.compnet.protocol.Settings;

/**
 * This class mostly handles the buddylist.
 * @author Pal Hargitai
 * @author Siu-Hong Li
 */
public class BuddyList {
	// The trusted addresses
	TreeMap<String, String> buddies;
	// The settings
	Settings settings;

	/**
	 * Constructs a buddylist and tries to import the buddies from the
	 * preferences.
	 * @param p The settings that are associated with this.
	 */
	public BuddyList(Settings p) {
		buddies = new TreeMap<String, String>();
		settings = p;
		// Fill the buddy list.
		String[] names = settings.getKeys("/buddies");
		for (String n: names) {
			buddies.put(n, settings.getValue("/buddies", n, "anonymous"));
		}

	}
	
	/**
	 * Adds a buddy, by different parameters.
	 * @param n The name of the buddy.
	 * @param a The address of the buddy.
	 */
	public synchronized void addBuddy(String n, String a) {
		buddies.put(a, n);
		settings.setValue("/buddies", a, n);
	}
	
	/**
	 * Get a map of address -> username of all buddies.
	 * @return The buddy map.
	 */
	public synchronized Map<String, String> getBuddies() {
		return buddies;
	}
	
	/**
	 * Remove the buddy.
	 * @param a The address of the buddy 
	 * to remove.
	 */
	public synchronized void removeBuddy(String a) {
		settings.remove("/buddies", a);
		buddies.remove(a);
	}
	
	/**
	 * Checks wether a given address belongs to a buddy.
	 * @param ad The address to check with.
	 * @return True if the address belongs to a buddy false if not.
	 */
	public synchronized boolean isBuddy(String ad) {
		return buddies.containsKey(ad);
	}
}
