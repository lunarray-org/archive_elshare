package org.lunarray.lshare.protocol.state.download;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

import org.lunarray.lshare.protocol.Controls;
import org.lunarray.lshare.protocol.state.download.file.Chunk;
import org.lunarray.lshare.protocol.state.userlist.User;
import org.lunarray.lshare.protocol.tasks.RunnableTask;

public class DownloadTransfer implements RunnableTask{
	
	private Chunk chunk;
	private User user;
	private DownloadHandler handler;
	private int port;
	private Socket socket;
	private InputStream istream;
	
	public DownloadTransfer(Chunk c, User u, DownloadHandler h, int p) {
		chunk = c;
		user = u;
		handler = h;
		port = p;
	}
	
	public void runTask(Controls c) {
		// Actually download
		run: {
			while (!chunk.isDone()) {
				// download some
				int toread = Long.valueOf(Math.min(chunk.getTodo(), 1024)).
						intValue();
				
				byte[] data = new byte[toread];
				
				try {
					istream.read(data);
					chunk.write(data, toread);
				} catch (IOException ie) {
					Controls.getLogger().warning("Error transferring file.");
					break run;
				}
			}
		}
		handler.done();
	}
	
	public void init() throws IOException {
		socket = new Socket(user.getAddress(), port);
		istream = socket.getInputStream();
	}
	
	public void close() {
		try {
			if (socket != null) {
				socket.close();
			}
			chunk.unlock();
		} catch (IOException ie) {
			// Ignore
		}
	}
	
	public User getUser() {
		return user;
	}
}
