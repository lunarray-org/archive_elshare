package edu.tue.compnet;

/**
 * This is the main class, it starts up a network backend and a GUI frontend.
 * With networking it's important to allow for asynchonisity to maximise
 * performance and reduce bugs.
 * @author Pal Hargitai
 * @author Siu-Hong Li
 */
public class Main {

	/**
	 * The application runs here
	 * @param args The arguments to be given with the application. Has no real
	 * effect.
	 */
	public static void main(String[] args) {
		// init		
		Backend b = new Backend();
		Frontend f = new Frontend(b);
		// startup
		b.startDaemons();
		f.startGUI();
	}
}
