package edu.tue.compnet.gui;

/**
 * This interface will describe some basic functions that can be triggered
 * through the main menubar.
 * @author Pal Hargitai
 * @author Siu-Hong Li
 */
public interface MenuListener {
	/**
	 * Quit the application.
	 */
	void quit();
	
	/**
	 * Change the users nickname.
	 */
	void changeNickname();
	
	/**
	 * Change the incoming folder.
	 */
	void changeIncomingFolder();
	
	/**
	 * Change share folder.
	 */
	void changeShareFolder();
	
	/**
	 * Search for a file
	 * @param filter If set to true, filter on buddy list.
	 */
	void search(boolean filter);
	
	/**
	 * Show uploads
	 * @param yn set to True to show uploads, false if not.
	 */
	void showUploads(boolean yn);
	
	/**
	 * Check hashes
	 * @param yn set to True to check hashes, false if not.
	 */
	void checkHashes(boolean yn);
}
