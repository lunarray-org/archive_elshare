package org.lunarray.lshare.protocol.filelist;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import org.lunarray.lshare.protocol.Controls;

public class FilelistSender extends Thread {

	private Socket socket;
	private Controls controls;
	private InputStream istream;
	private OutputStream ostream;
	
	public FilelistSender(ThreadGroup g, Controls c, Socket s) {
		super(g, "filelistsender");
		controls = c;
		socket = s;
	}

	public void run() {
		run: {
			try {
				istream = socket.getInputStream();
				ostream = socket.getOutputStream();
			} catch (IOException ie) {
				Controls.getLogger().severe("Could not get streams!");
				break run;
			}
			while (true) {
				// read and write data
				
			}
		}
	}
	
	public void close() {
		try {
			socket.close();
		} catch (IOException ie) {
			Controls.getLogger().severe("Could not close client socket!");
		}
	}
}
