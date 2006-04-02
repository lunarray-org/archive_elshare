package org.lunarray.lshare.protocol;

import java.io.IOException;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import org.lunarray.lshare.protocol.packets.user.SignOffOut;

public class Controls {
	public static int UDP_PORT = 7400;
	public static int TCP_PORT = 7400;
	public static int UDP_MTU = 1400;
	
	private UDPTransport utrans;
	private TCPSharesTransport tstrans;
	private State state;
	private Settings settings;
	private Tasks tasks;
	private ThreadGroup lsgroup;
	
	public Controls() {
		// Init logger
		Handler ha;
		try {
			// Try to use a file handler.
			ha = new FileHandler("lshare.log");
		} catch (IOException ie) {
			// We assume that something went wrong, use a console handler.
			ha = new ConsoleHandler();
		}
		ha.setFormatter(new SimpleFormatter());
		ha.setLevel(Level.ALL);
		Logger ls = Logger.getLogger("lshare");
		// >TEMP
		Handler hb = new ConsoleHandler();
		hb.setLevel(Level.ALL);
		hb.setFormatter(new SimpleFormatter());
		ls.addHandler(hb);
		// <TEMP
		ls.setLevel(Level.ALL);
		ls.addHandler(ha);

		lsgroup = new ThreadGroup("lshare");
		settings = new Settings(this);
		state = new State(this);
		tasks = new Tasks(this);
		utrans = new UDPTransport(this);
		tstrans = new TCPSharesTransport(this);
	}
	
	protected ThreadGroup getThreadGroup() {
		return lsgroup;
	}	
	
	public void start() {
		tstrans.init();
		getUDPTransport().init();
		getTasks().start();
	}
	
	public void stop() {
		tstrans.close();
		getTasks().stop();
		// Send logout
		SignOffOut soo = new SignOffOut();
		getUDPTransport().send(soo);
		
		
		getUDPTransport().close();
		getState().commit();
		//controls.getSettings().commit();
	}
	public Settings getSettings() {
		return settings;
	}
	
	public State getState() {
		return state;
	}
	
	public UDPTransport getUDPTransport() {
		return utrans;
	}
	
	public Tasks getTasks() {
		return tasks;
	}
	
	public static Logger getLogger() {
		return LogManager.getLogManager().getLogger("lshare");
	}
}
