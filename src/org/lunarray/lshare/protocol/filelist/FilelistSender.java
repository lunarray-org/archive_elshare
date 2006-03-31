package org.lunarray.lshare.protocol.filelist;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import org.lunarray.lshare.protocol.Controls;
import org.lunarray.lshare.protocol.packets.Util;
import org.lunarray.lshare.protocol.state.sharing.ShareSettings;
import org.lunarray.lshare.protocol.state.sharing.SharedDirectory;
import org.lunarray.lshare.protocol.state.sharing.SharedFile;

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
				String path = "";
				read: {
					// read and write data
					try {
						byte[] dlenb = get(2);
						int dlen = Util.byteArrayToShortU(dlenb, 0);
						byte[] dstrb = get(dlen);
						path = Util.decode(dstrb);
					} catch (IOException ie) {
						Controls.getLogger().fine("Could not read!");
						break run;
					}
				}
				Controls.getLogger().finer("Requested " + path);
				if (path.equals(".")) {
					// get .
					try {
						writeRoots();
					} catch (IOException ie) {
						Controls.getLogger().fine("Cannot write!");
						break run;
					}
				} else {
					// Get entries
					SharedDirectory dir = controls.getState().getShareList().getShareByName(path);
					long len = 0;
					if (dir != null) {
						len = (long)dir.getDirectories().size() + (long)dir.getFiles().size();
					}
					writelen: {
						byte[] tlen = new byte[8];
						Util.longToByteArray(len, tlen, 0);
						try {
							ostream.write(tlen);
						} catch (IOException ie) {
							Controls.getLogger().fine("Cannot write!");
							break run;
						}
					}
					if (len > 0) {
						try {
							for (SharedDirectory d: dir.getDirectories()) {
								send(d);
							}
							for (SharedFile f: dir.getFiles()) {
								send(f);
							}
						} catch (IOException ie) {
							Controls.getLogger().fine("Cannot write!");
							break run;
						}
					}
				}
			}
		}
		try {
			socket.close();
		} catch (IOException ie) {
			Controls.getLogger().finer("Closed socket");
		}
	}
	
	private void writeRoots() throws IOException {
		byte[] tlen = new byte[8];
		Util.longToByteArray(controls.getState().getShareList().getShareNames().size(), tlen, 0);
		ostream.write(tlen);		
		for (String s: controls.getState().getShareList().getShareNames()) {
			byte[] name = Util.encode(s);
			byte[] nlen = {Integer.valueOf(Math.min(name.length, 255)).byteValue()};
			byte[] data = new byte[8 + 8 + ShareSettings.HASH_UNSET.length + 1 + nlen[0]];
			Util.longToByteArray(0, data, 0);
			Util.longToByteArray(-1, data, 8);
			Util.injectByteArrayIntoByteArray(ShareSettings.HASH_UNSET, ShareSettings.HASH_UNSET.length, data, 16);
			Util.injectByteArrayIntoByteArray(nlen, 1, data, 16 + ShareSettings.HASH_UNSET.length);
			Util.injectByteArrayIntoByteArray(name, nlen[0] & 0xFF, data, 16 + 1 + ShareSettings.HASH_UNSET.length);
			ostream.write(data);
		}
	}

	
	private void send(SharedFile d) throws IOException {
		byte[] name = Util.encode(d.getName());
		byte[] nlen = {Integer.valueOf(Math.max(name.length, 255)).byteValue()};
		byte[] data = new byte[8 + 8 + ShareSettings.HASH_UNSET.length + 1 + nlen[0]];
		Util.longToByteArray(d.getLastModified(),data, 0);
		Util.longToByteArray(d.getSize(), data, 8);
		Util.injectByteArrayIntoByteArray(d.getHash(), d.getHash().length, data, 16);
		Util.injectByteArrayIntoByteArray(nlen, 1, data, 16 + ShareSettings.HASH_UNSET.length);
		Util.injectByteArrayIntoByteArray(name, nlen[0], data, 16 + 1 + ShareSettings.HASH_UNSET.length);
	}
	
	private void send(SharedDirectory d) throws IOException {
		byte[] name = Util.encode(d.getName());
		byte[] nlen = {Integer.valueOf(Math.max(name.length, 255)).byteValue()};
		byte[] data = new byte[8 + 8 + ShareSettings.HASH_UNSET.length + 1 + nlen[0]];
		Util.longToByteArray(0,data, 0);
		Util.longToByteArray(0, data, 8);
		Util.injectByteArrayIntoByteArray(ShareSettings.HASH_UNSET, ShareSettings.HASH_UNSET.length, data, 16);
		Util.injectByteArrayIntoByteArray(nlen, 1, data, 16 + ShareSettings.HASH_UNSET.length);
		Util.injectByteArrayIntoByteArray(name, nlen[0], data, 16 + 1 + ShareSettings.HASH_UNSET.length);
	}
	
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
	
	public void close() {
		try {
			socket.close();
		} catch (IOException ie) {
			Controls.getLogger().severe("Could not close client socket!");
		}
	}
}
