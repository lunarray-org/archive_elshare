package org.lunarray.lshare.protocol.state.upload;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

import org.lunarray.lshare.protocol.Controls;
import org.lunarray.lshare.protocol.tasks.RunnableTask;

// TODO bandwidth throttle
public class UploadTransfer implements RunnableTask {

	private File file;
	private long offset;
	private FileInputStream istream;
	private Socket socket;
	private ServerSocket server;
	private UploadManager manager;
	private boolean done;
	
	public UploadTransfer(File f, long o, UploadManager m) {
		file = f;
		manager = m;
		offset = o;
		done = false;
	}
	
	public int init() throws IOException {
		// Setup file
		istream = new FileInputStream(file);
		istream.skip(offset);
		// Setup socket
		bind: {
			for (int i = UploadManager.BEGIN_PORT; i < UploadManager.END_PORT;
					i++) {
				server = new ServerSocket();
				try {
					server.bind(new InetSocketAddress(i));
					break bind;
				} catch (IOException ie) {
					// try again
				}
			}
		}
		
		Controls.getLogger().fine("Started transfer.");
		
		if (server.getLocalPort() >= UploadManager.BEGIN_PORT && server.
				getLocalPort() <= UploadManager.END_PORT) {
			return server.getLocalPort();
		} else {
			throw new IOException();
		}
	}
	
	public boolean isDone() {
		return done;
	}
	
	public boolean isRunning() {
		if (socket == null) {
			return false;
		} else {
			return socket.isConnected();
		}
	}
	
	public void close() {
		try {
			istream.close();
			server.close();
			if (socket != null) {
				socket.close();
			}
		} catch (IOException ie) {
			// Ignore now
		}
		
		Controls.getLogger().fine("Closed transfer.");
	}
	
	public void runTask(Controls c) {
		run: {
			try {
				socket = server.accept();
				manager.addTransfer(this);
				OutputStream ostream = socket.getOutputStream();
				
				while (istream.available() > 0) {
					int totrans = Math.min(1024, istream.available());
					byte[] dat = new byte[totrans];
					istream.read(dat);
					ostream.write(dat);
				}
			} catch (IOException ie) {
				// Failed..
				Controls.getLogger().warning("Error uploading file.");
				break run;
			}
		}
		done = true;
		manager.removeTransfer(this);
	}
}
