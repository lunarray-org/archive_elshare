package org.lunarray.lshare.protocol.filelist;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import org.lunarray.lshare.protocol.Controls;
import org.lunarray.lshare.protocol.packets.Util;
import org.lunarray.lshare.protocol.state.sharing.ShareSettings;
import org.lunarray.lshare.protocol.state.userlist.User;

public class FilelistReceiver {

	private User user;
	private Socket socket;
	private OutputStream ostream;
	private InputStream istream;
	
	public FilelistReceiver(User u) {
		user = u;
		// Setup stuff
		socket = new Socket();
		try {
			socket.connect(new InetSocketAddress(user.getAddress(), Controls.TCP_PORT));
		} catch (IOException ie) {
			Controls.getLogger().warning("Could not connect to user!");
		}
	}
	
	public List<FilelistEntry> getEntries(String path) {
		ArrayList<FilelistEntry> ret = new ArrayList<FilelistEntry>();
		run: {
			try {
				write: {
					ostream = socket.getOutputStream();
					byte[] pdat = Util.encode(path);
					byte[] data = new byte[pdat.length + 2];
					Util.shortUToByteArray(pdat.length, data, 0);
					Util.injectByteArrayIntoByteArray(pdat, pdat.length, data, 2);
					ostream.write(data);
				}
				read: {
					istream = socket.getInputStream();
					byte[] a = get(8);
					long amount = Util.byteArrayToLong(a, 0);
					for (long i = 0; i < amount; i++) {
						ret.add(getOne(path));
					}
				}
			} catch (IOException ie) {
				Controls.getLogger().warning("Could not request path!");
				try {
					socket.close();
				} catch (IOException i) {
					// error
				}
				break run;
			}
		}
		return ret;
	}
	
	private FilelistEntry getOne(String p) throws IOException {
		byte[] predata = get(8 + 8 + ShareSettings.HASH_UNSET.length + 1);
		long ad = Util.byteArrayToLong(predata, 0);
		long size = Util.byteArrayToLong(predata, 8);
		byte[] hash = Util.getByteArrayFromByteArray(predata, ShareSettings.HASH_UNSET.length, 16);
		int nlen = predata[16 + ShareSettings.HASH_UNSET.length] & 0xFF;
		String name = Util.decode(get(nlen));
		Controls.getLogger().finer("Data for: " + name);
		return new FilelistEntry(this, p, name, hash, ad, size);
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
			Controls.getLogger().fine("Could not close socket!");
		}
		Controls.getLogger().finer("Closed socket!");
	}

	public FilelistEntry getRoot() {
		return new FilelistEntry(this, ".", user.getName(), ShareSettings.HASH_UNSET, 0, -1);
	}
}
