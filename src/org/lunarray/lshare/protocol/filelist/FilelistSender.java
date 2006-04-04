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
					for (ShareEntry e: entries) {
						send(e);
					}
				} catch (IOException ie) {
					Controls.getLogger().fine("Cannot write!");
					break run;
				}
			}
		}
		try {
			socket.close();
		} catch (IOException ie) {
			Controls.getLogger().finer("Closed socket");
		}
	}
	
	private void send(ShareEntry d) throws IOException {
		byte[] name = PacketUtil.encode(d.getName());
		byte[] nlen = {Integer.valueOf(Math.min(name.length, 255)).byteValue()};
		byte[] data = new byte[8 + 8 + ShareSettings.HASH_UNSET.length + 1 + nlen[0]];
		if (d.isDirectory()) {
			PacketUtil.longToByteArray(0, data, 0);
			PacketUtil.longToByteArray(-1, data, 8);
			PacketUtil.injectByteArrayIntoByteArray(ShareSettings.HASH_UNSET, ShareSettings.HASH_UNSET.length, data, 16);
		} else {
			PacketUtil.longToByteArray(d.getLastModified(),data, 0);
			PacketUtil.longToByteArray(d.getSize(), data, 8);
			PacketUtil.injectByteArrayIntoByteArray(d.getHash(), d.getHash().length, data, 16);
		}
		PacketUtil.injectByteArrayIntoByteArray(nlen, 1, data, 16 + ShareSettings.HASH_UNSET.length);
		PacketUtil.injectByteArrayIntoByteArray(name, nlen[0], data, 16 + 1 + ShareSettings.HASH_UNSET.length);
		ostream.write(data);
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
