package edu.tue.compnet;

/**
 * Handles outputting data.
 * @author Pal Hargitai
 * @author Siu-Hong Li
 */
public class Output {
	// Wether we're debugging
	public static boolean DEBUG = true;
	
	/**
	 * Incase the system is being debugged, actually output.
	 * @param o The object to output.
	 */
	public static void out(Object o) {
		if (DEBUG) {
			System.out.println(o);
		}
	}
	
	/**
	 * Output an error.
	 * @param o The error to output.
	 */
	public static void err(Object o) {
		System.err.println(o);
	}
}
