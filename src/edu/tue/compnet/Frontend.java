package edu.tue.compnet;

import edu.tue.compnet.gui.MainFrame;

/**
 * This is the frontend, should do most GUI stuff and should make sure that 
 * the functionality implemented in the backend is usable.
 * @author Pal Hargitai
 * @author Siu-Hong Li
 */
public class Frontend {
	// The main frame of the frontend
	MainFrame mainf;
	// The backend
	Backend backend;

	/**
	 * Created the backend, sets things up, adds listeners and stuff, does
	 * -not- start the gui itself.
	 * @param b The backend that it should communicate with.
	 */
	public Frontend(Backend b) {
		backend = b;
		mainf = new MainFrame(backend);
	}
	
	/**
	 * Gets the GUI going, just make sure it all runs.
	 */
	public void startGUI() {
		mainf.startGUI();
	}
}
