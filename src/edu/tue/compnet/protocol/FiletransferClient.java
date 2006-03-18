package edu.tue.compnet.protocol;

import java.net.*;
import java.io.*;

import edu.tue.compnet.Output;
import edu.tue.compnet.protocol.state.*;

/**
 * The class that handles the download of a file.
 * @author Pal Hargitai
 * @author Siu-Hong Li
 */
public class FiletransferClient extends Filetransfer {
	// The port it will download to
	int port;
	// The address to connect to
	InetAddress address;
	// The socket that is downloaded to
	Socket socket;
	// The transfer thread
	TransferThread thread; 
	// The chunk we download to
	Chunk chunk;
	// The offset
	int offset;
	// The transport
	Transport daemon;

	/**
	 * The constructor of a file transfer client
	 * @param c The chunk we are allowed to download to.
	 * @param si The size of what was given to us.
	 * @param s The state of the protocol.
	 * @param p The port to open a connection to.
	 * @param a The address to open the connection to.
	 * @param d The transport where we can later request another chunk.
	 */
	public FiletransferClient(Chunk c, int si, State s, int p, InetAddress a,
			Transport d) {
		super(s);
		chunk = c;
		port = p;
		address = a;
		shouldrun = true;
		daemon = d;
		offset = c.getTransferFile().getLength() - si;
	}

	/**
	 * Get the length of how much there is to do.
	 * @return The amount to download to the chunk.
	 */
	public int getFileTodo() {
		return chunk.getTodo();
	}
	
	/**
	 * Start the download.
	 */
	public boolean start() {

		// Try to open the socket
		try {
			Output.out("Connecting to:");
			Output.out(address);
			socket = new Socket();
			socket.connect(new InetSocketAddress(address, port));
			input = socket.getInputStream();
		} catch (IOException ie) {
			Output.err("Error connecting to server!");
			return false;
		}
		/*
		 * Check wether the chunk matches what we asked for.
		 */
		if (chunk.getMark() != offset) {			
			TransferFile tf = chunk.getTransferFile();
			// Check if it's an old client
			if (offset == 0) {
				tf.releaseChunk(chunk);
				Chunk c = tf.getFirstChunk();
				if (c.isLocked()) {
					return false;
				} else {
					// We try to lock it
					tf.lockChunk(c);
					chunk = c;
					return true;
				}
			} else {
				// Trying to do something bogus
				return false;
			}
		} else {
			// Start the thread
			shouldrun = true;
			isrunning = true;
			thread = new TransferThread();
			thread.start();
			return true;
		}
	}
	
	/**
	 * Stop running.
	 */
	public void stop() {
		shouldrun = false;
	}
	
	/**
	 * The transfer thread, handles actual data transfer.
	 * @author Pal hargitai
	 * @author Siu-Hong Li
	 */
	private class TransferThread extends Thread {
		/**
		 * Runs the file transfer
		 */
		public void run() {
			// Transfer
			Transfer trans = chunk.getTransferFile().getTransfer();
			trans.getAddress().add(address);
			
			// Create multiple source transfer thing
			state.getFiletransferList().registerDownload(trans);
			while (shouldrun && !chunk.isFinished()) {
				try {
					sem.acquire();
				} catch (InterruptedException ie) {
					Output.err("Something very fishy..");
				}
				int bllen = Math.min(MAX_PACK, chunk.getTodo());
				byte[] block = new byte[bllen];
				try {
					// Read max 1k block
					int isread = input.read(block);
					// Write max 1k block
					chunk.write(block, isread);
					rate = trans.getRate();
					
					int wait = 0;
					if (rate > 0 && rate < 1000) {
						// wait a bit
						wait = 1000 / rate;
					}
					sleep(wait);
				} catch (IOException ie) {
					// Something went wrong, just close it all
					shouldrun = false;
					// Notify
					Output.err("Client: Error transferring file!");
					//ie.printStackTrace();
				} catch (InterruptedException ie) {
					// Ignore
				}
				sem.release();
			}
			// Close things down.
			try {
				socket.close();
			} catch (IOException ie) {
				Output.err("Error while closing socket!");
			}
			try {
				input.close();
			} catch (IOException ie) {
				Output.err("Error while closing file!");
			}
			trans.getAddress().remove(address);
			chunk.getTransferFile().releaseChunk(chunk);
			if (!chunk.getTransferFile().isFinished()) {
				// Download more
				state.getTasks().requestFile(chunk.getName(), address, chunk.
						getTransferFile().getLength());
			}
			// it's not running anymore
			isrunning = false;
			shouldrun = false;
		}
	}
}
