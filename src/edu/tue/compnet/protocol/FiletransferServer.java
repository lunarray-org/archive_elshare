package edu.tue.compnet.protocol;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import edu.tue.compnet.Output;
import edu.tue.compnet.protocol.state.Transfer;

/**
 * The class that encompasses the file transfer.
 * @author Pal Hargitai
 * @author Siu-Hong Li
 */
public class FiletransferServer extends Filetransfer{
	// The socket
	ServerSocket socket;
	// Upload Thread
	TransferThread upload;
	// THe file
	File file;
	// The network output stream
	OutputStream output;
	// The amount to skip in reading the file
	int offset;
	// The length of the file still to be transferred 
	int lengthtodo;
	// The actual length of the file.
	int lengthtotal;
	
	/**
	 * Constructs the filetransfer.
	 * @param s The state of the protocol.
	 * @param f The file to upload.
	 * @param o The offset to start uploading at.
	 */
	public FiletransferServer(State s, File f, int o) {
		super(s);
		file = f;
		offset = o;
		try {
			socket = new ServerSocket();
		} catch (IOException ie) {
			// Something went wrong here
		}
		upload = new TransferThread();
		isrunning = false;
	}
	
	/**
	 * Bind to a certain port to allow uploading of a file.
	 * @return The port it is bound to.
	 */
	public int bind() {
		int port = BEGIN_PORT;
		while (port < END_PORT && !socket.isBound()) {
			try {
				socket.bind(new InetSocketAddress(port));
			} catch (IOException io) {
				// Didn't work, so just do a next port
				port++;
			}
		}
		// It's also important to lock the file
		try {
			input = new FileInputStream(file);
			input.skip(offset);
		} catch (FileNotFoundException fnf) {
			// The port is invalid
			try {
				socket.close();
			} catch (IOException ie) {
				// This is serious shit, just allow a return.
			}
			return -1;
		} catch (IOException i) {
			// Something went wrong here
			try {
				socket.close();
			} catch (IOException ie) {
				// This is serious shit, just allow a return.
			}
			return -1;
		}
		// Return -1 if something went wrong.
		if (!socket.isBound()) {
			return -1;
		} else {
			return port;
		}
	}
	
	/**
	 * Start the upload thread.
	 */
	public boolean start() {
		shouldrun = true;
		upload.start();
		return true;
	}
	
	/**
	 * Stop the thread.
	 */
	public synchronized void stop() {
		shouldrun = false;
		if (!isrunning) {
			try {
				socket.close();
			} catch (IOException ie) {
				Output.err("Error closing socket!");
			}
		}
	}
	
	public int getLength() {
		return lengthtotal;
	}
	
	public String getName() {
		return file.getName();
	}
	
	public int getTodo() {
		return lengthtodo;
	}
	
	/**
	 * This class ensures proper uploading.
	 * @author Pal Hargitai
	 * @author Siu-Hong Li
	 */
	private class TransferThread extends Thread{
		/**
		 * Runs the actual transfer.
		 */
		public void run() {
			// Get a connection going
			try {
				Socket client = socket.accept();
				output = client.getOutputStream();
			} catch (IOException ie) {
				// Something went wrong here, just not allow it to run
				Output.err("Client didn't connect!");
				shouldrun = false;
			}
			// Only execute the rest if possible!
			if (shouldrun) {
				// it's running
				isrunning = true;
				// See how much is to be uploaded
				lengthtotal = 0;
				try {
					lengthtodo = input.available();
				} catch (IOException ie) {
					Output.err("Error getting file length!");
				}
				lengthtotal = lengthtodo + offset;

				// Transfer
				Transfer trans = new Transfer();
				trans.setName(file.getName());
				trans.setRate(rate);
				trans.setLength(lengthtotal);
				trans.setTodo(lengthtodo);
				ArrayList<InetAddress> l = new ArrayList<InetAddress>();
				l.add(socket.getInetAddress());
				trans.setAddress(l);
				
				state.getFiletransferList().registerUpload(trans);
				
				while (shouldrun && lengthtodo > 0) {
					// Transfer the file here.
					int bllen = Math.min(MAX_PACK, lengthtodo);
					byte[] block = new byte[bllen];
					try {
						
						int isread = input.read(block);
						// Write max 1k block
						output.write(block, 0, isread);
						// update length
						lengthtodo = lengthtodo - isread;
						
						// Set/Get transfer info
						trans.setTodo(lengthtodo);
						rate = trans.getRate();
						
						// Rate
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
						Output.err("Server: Error transferring file!");
						//ie.printStackTrace();
					} catch (InterruptedException ie) {
						// Ignore
					}
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
				// it's not running anymore
				isrunning = false;
				shouldrun = false;
			}
		}
	}
}
