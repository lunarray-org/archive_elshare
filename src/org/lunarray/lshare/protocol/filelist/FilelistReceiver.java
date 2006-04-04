package org.lunarray.lshare.protocol.filelist;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import org.lunarray.lshare.protocol.Controls;
import org.lunarray.lshare.protocol.packets.PacketUtil;
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
	
	public List<FilelistEntry> getEntries(String path, boolean isroot) {
		//Exception e = new Exception();
		//e.printStackTrace();
		
		System.out.println(path);
		
		if (isroot) {
			path = ".";
		}
		
		ArrayList<FilelistEntry> ret = new ArrayList<FilelistEntry>();
		run: {
			try {
				write: {
					Controls.getLogger().finer("Requested path: " + path);
					ostream = socket.getOutputStream();
					byte[] pdat = PacketUtil.encode(path);
					byte[] data = new byte[pdat.length + 2];
					PacketUtil.shortUToByteArray(pdat.length, data, 0);
					PacketUtil.injectByteArrayIntoByteArray(pdat, pdat.length, data, 2);
					ostream.write(data);
				}
				read: {
					istream = socket.getInputStream();
					byte[] a = get(8);
					long amount = PacketUtil.byteArrayToLong(a, 0);
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
		long ad = PacketUtil.byteArrayToLong(predata, 0);
		long size = PacketUtil.byteArrayToLong(predata, 8);
		byte[] hash = PacketUtil.getByteArrayFromByteArray(predata, ShareSettings.HASH_UNSET.length, 16);
		int nlen = predata[16 + ShareSettings.HASH_UNSET.length] & 0xFF;
		String name = PacketUtil.decode(get(nlen)).trim();
		Controls.getLogger().finer("Data for: " + name);
		return new FilelistEntry(this, p, name, hash, ad, size, false);
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
		return new FilelistEntry(this, ".", user.getName(), ShareSettings.HASH_UNSET, 0, -1, true);
	}
}
