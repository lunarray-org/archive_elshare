package edu.tue.compnet;

import edu.tue.compnet.protocol.*;

/**
 * The backend that runs all the network related stuff. It should provide
 * little actual functionality, but it should first and foremost just
 * allow as an abstraction to the frontend.
 * @author Pal Hargitai
 * @author Siu-Hong Li
 */
public class Backend {
	// The protocol daemon
	Transport daemon;
	// The state of the system
	State state;

	/**
	 * The constructor, should initialise the backend, but -not- start
	 * the backend.
	 */
	public Backend() {
		/*
		 * Create a Protocol daemon and register this class as a listener
		 * to it. The protocol shouldn't start listenting just yet.
		 */
		state = new State();
		daemon = new Transport(state);
		state.getTasks().setTransport(daemon);
		state.getTasks().init();
	}
	
	/**
	 * This should start the different networking listeners and make sure
	 * that the functionality basically works.
	 */
	public void startDaemons() {
		daemon.startDaemon();
	}

	/**
	 * Cleans up the network.
	 */
	public void quit() {
		daemon.quit();
	}
	
	/**
	 * Gets the protocol state.
	 * @return Returns the state of the protocol.
	 */
	public State getState() {
		return state;
	}
	
	/**
	 * Get the protocol daemon.
	 * @return The protocol daemon.
	 */
	public Transport getDaemon() {
		return daemon;
	}

}
