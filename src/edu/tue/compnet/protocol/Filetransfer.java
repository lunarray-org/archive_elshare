package edu.tue.compnet.protocol;

import java.io.InputStream;
import java.util.concurrent.Semaphore;

import edu.tue.compnet.Output;


/**
 * A class that encompasses most general functions applicable to a download
 * client and server.
 * @author Pal Hargitai
 * @author Siu-Hong Li
 */
public abstract class Filetransfer {
	/** The port to start binding to. */
	public static int BEGIN_PORT = 4000;
	/** The last port to bind to. */
	public static int END_PORT = 5000;
	/** The maximum packet size. */
	public static int MAX_PACK = 1000;
	/** The default rate. */
	public static int DEF_RATE = 0;
	// The state
	State state;
	// Should run
	boolean shouldrun;
	// If it's running
	boolean isrunning;
	// Upload Thread
	Thread thread;
	// The file input stream
	InputStream input;
	// The semaphore to sync with something else
	Semaphore sem;
	// The current rate
	int rate;
	
	
	/**
	 * Constructs the filetransfer.
	 * @param s The state of the protocol.
	 */
	public Filetransfer(State s) {
		state = s;
		shouldrun = false;
		isrunning = false;
		sem = new Semaphore(1);
		rate = DEF_RATE;
	}
	
	/**
	 * Set the transfer rate.
	 * @param r The rate to set it to.
	 */
	public void setRate(int r) {
		rate = r;
	}
	
	/**
	 * Get the transfer rate.
	 * @return The rate it is set to.
	 */
	public int getRate() {
		return rate;
	}
	
	/**
	 * Lock the filetransfer so we can mess with the state.
	 */
	public void lock() {
		try {
			sem.acquire();
		} catch (InterruptedException ie) {
			Output.err("Something very fishy..");
		}
	}
	
	/**
	 * Unlock the filetransfer so that we can go on again.
	 *
	 */
	public void unlock() {
		sem.release();
	}
	
	/**
	 * Start the upload thread.
	 */
	public abstract boolean start();
	
	/**
	 * Stop the thread.
	 */
	public abstract void stop();
	
	/**
	 * Asks wether the server should run in the first place.
	 * @return Wether the server should run.
	 */
	public boolean shouldRun() {
		return shouldrun;
	}
	
	/**
	 * Asks wether the filetransfer is going, ie. byes are being transferred.
	 * @return Wether a filetransfer is going on.
	 */
	public boolean isRunning() {
		return isrunning;
	}

}
