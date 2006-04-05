package org.lunarray.lshare.protocol.filelist;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Collections;
import java.util.List;

import org.lunarray.lshare.protocol.Controls;
import org.lunarray.lshare.protocol.packets.PacketUtil;
import org.lunarray.lshare.protocol.state.sharing.ShareEntry;
import org.lunarray.lshare.protocol.state.sharing.ShareSettings;

/**
 * A class for sending file entries.<br>
 * Stream 1:<br>
 * Purpose:<br>
 * File browsing.<br>
 * TCP socket.<br>
 * Input:<br>
 * Directory request as follows:<br>
 * 2 fields:<br>
 * 0: 2 bytes, representing an int n<br>
 * 1: n bytes, path name<br>
 * Output:<br>
 * Directory contents as follows:<br>
 * 8 bytes, representing the int n<br>
 * n times:<br>
 * 5 fields:<br>
 * 0: 8 bytes, long, epoch of file modification<br>
 * 1: 8 bytes, long, file size<br>
 * 2: 16 bytes, the hash of the file<br>
 * 3: 1 byte, representing an int n<br>
 * 4: n bytes, file name encoded in UTF-8<br>
 * Constraints:<br>
 * A new directory name may not be sent while a directories content isbeing
 * requested. Multiple clients may connect simultaneously to get the file
 * contents.<br>
 * Note:<br>
 * Root path is given by requesting the '.' path<br>
 * Path elements are seperated with a '/'.<br>
 * Directories will be denoted by having a file size of '-1'.<br>
 * While a connection stands, the contents of any number of directories may be
 * requested.<br>
 * The file hash of a directory will always be an empty hash, ie. just zeroes.
 * @author Pal Hargitai
 */
public class FilelistSender extends Thread {

	/**
	 * The client socket for sending file entries.
	 */
	private Socket socket;
	
	/**
	 * Access to the protocol.
	 */
	private Controls controls;
	
	/**
	 * The input stream from which data is read.
	 */
	private InputStream istream;
	
	/**
	 * The output stream to which data is written.
	 */
	private OutputStream ostream;
	
	/**
	 * The constructor for the file sender.
	 * @param g The thread group in which this thread will run.
	 * @param c The controls for the protocol.
	 * @param s The socket that this is to communicate to.
	 */
	public FilelistSender(ThreadGroup g, Controls c, Socket s) {
		super(g, "filelistsender");
		controls = c;
		socket = s;
	}

	/**
	 * The main code for sending file entries.
	 */
	public void run() {
		run: {
			// Set streams
			try {
				istream = socket.getInputStream();
				ostream = socket.getOutputStream();
			} catch (IOException ie) {
				Controls.getLogger().severe("Could not get streams!");
				break run;
			}
			while (true) {
				String path = "";
				read: {
					// read and write data
					try {
						byte[] dlenb = get(2);
						int dlen = PacketUtil.byteArrayToShortU(dlenb, 0);
						byte[] dstrb = get(dlen);
						path = PacketUtil.decode(dstrb).trim();
					} catch (IOException ie) {
						Controls.getLogger().fine("Could not read!");
						break run;
					}
				}
				Controls.getLogger().finer("Requested " + path);
				
				// Get entries
				List<ShareEntry> entries = Collections.emptyList();
				try {
					entries = controls.getState().getShareList().getChildrenIn(path);
				} catch (FileNotFoundException ne) {
					// We can safely ignore this.
				}
				
				writelen: {
					// Write length
					byte[] tlen = new byte[8];
					PacketUtil.longToByteArray(entries.size(), tlen, 0);
					try {
						ostream.write(tlen);
					} catch (IOException ie) {
						Controls.getLogger().fine("Cannot write!");
						break run;
					}
				}
				try {
					// Write entries
					for (ShareEntry e: entries) {
						send(e);
					}
				} catch (IOException ie) {
					Controls.getLogger().fine("Cannot write!");
					break run;
				}
			}
		}
		// Close socket
		try {
			socket.close();
		} catch (IOException ie) {
			Controls.getLogger().finer("Closed socket");
		}
	}
	
	/**
	 * Closes the socket.
	 */
	public void close() {
		try {
			socket.close();
		} catch (IOException ie) {
			Controls.getLogger().severe("Could not close client socket!");
		}
	}

	/**
	 * Send a file entry over the socket.
	 * @param d The entry to send.
	 * @throws IOException Incase a transferring an entry fails.
	 */
	private void send(ShareEntry d) throws IOException {
		byte[] name = PacketUtil.encode(d.getName());
		byte[] nlen = {Integer.valueOf(Math.min(name.length, 255)).
				byteValue()};
		byte[] data = new byte[8 + 8 + ShareSettings.HASH_UNSET.length + 1 + 
		        nlen[0]];
		if (d.isDirectory()) {
			PacketUtil.longToByteArray(0, data, 0);
			PacketUtil.longToByteArray(-1, data, 8);
			PacketUtil.injectByteArrayIntoByteArray(ShareSettings.HASH_UNSET,
					ShareSettings.HASH_UNSET.length, data, 16);
		} else {
			PacketUtil.longToByteArray(d.getLastModified(),data, 0);
			PacketUtil.longToByteArray(d.getSize(), data, 8);
			PacketUtil.injectByteArrayIntoByteArray(d.getHash(), d.getHash().
					length, data, 16);
		}
		PacketUtil.injectByteArrayIntoByteArray(nlen, 1, data, 16 + 
				ShareSettings.HASH_UNSET.length);
		PacketUtil.injectByteArrayIntoByteArray(name, nlen[0], data, 16 + 1 +
				ShareSettings.HASH_UNSET.length);
		ostream.write(data);
	}
	
	/**
	 * Gets a certain amount of bytes.
	 * @param a The amount of bytes to get.
	 * @return An a amount of bytes in an array.
	 * @throws IOException In case recieving an file fails.
	 */
	private byte[] get(int a) throws IOException {
		byte[] dat = new byte[a];
		int todo = a;
		int done = 0;
		while (todo > 0) {
			int read = 0;
			read = istream.read(dat, done, todo);
			done += read;
			todo -= read;
		}
		return dat;
	}
}
