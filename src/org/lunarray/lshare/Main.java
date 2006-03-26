package org.lunarray.lshare;

import org.lunarray.lshare.gui.MainGUI;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		LShare ls = new LShare();
		MainGUI mg = new MainGUI(ls);
		mg.start();
		ls.start();
	}

}
