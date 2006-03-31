package org.lunarray.lshare.protocol;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import org.lunarray.lshare.protocol.filelist.FilelistSender;

public class TCPSharesTransport extends Thread {
	
	private ServerSocket server;
	private boolean run;
	private ThreadGroup tgroup;
	private ArrayList<FilelistSender> senders;
	private Controls controls;

	public TCPSharesTransport(Controls c) {
		super(c.getThreadGroup(), "tcptransport");
		run = true;
		controls = c;
		senders = new ArrayList<FilelistSender>();
		tgroup = new ThreadGroup(c.getThreadGroup(), "filelisters");
	}
	
	public void init() {
		try {
			server = new ServerSocket(Controls.TCP_PORT);
		} catch (IOException ie) {
			Controls.getLogger().severe("Cannot instanciate socket!");
		}
		// Start running
		run = true;
		start();
	}
	
	public void run() {
		if (server.isBound()) {
			while (run) {
				try {
					Socket s = server.accept();
					FilelistSender l = new FilelistSender(tgroup, controls, s);
					senders.add(l);
					l.start();
				} catch (IOException ie) {
					if (run) {
						Controls.getLogger().severe("Cannot listen for connections!");
					}
				}
			}
		} else {
			Controls.getLogger().severe("Socket unbound!");
		}
	}
	
	public void close() {
		for (FilelistSender l: senders) {
			l.close();
		}		
		run = false;
		try {
			server.close();
		} catch (IOException ie) {
			Controls.getLogger().severe("Cannot close socket!");
		}
		Controls.getLogger().fine("Closed TCP socket and connections");
	}
}
