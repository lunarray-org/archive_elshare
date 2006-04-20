package org.lunarray.lshare;

import org.lunarray.lshare.gui.MainGUI;

/** 
 * A main class that does little other than start the eLShare application.
 * @author Pal Hargitai
 */
public class Main {

	/** 
     * Runs the eLShare application.
	 * @param args Arguments for run.
	 */
	public static void main(String[] args) {
		LShare ls = new LShare();
		MainGUI mg = new MainGUI(ls);
		mg.start();
		ls.start();
	}
}
