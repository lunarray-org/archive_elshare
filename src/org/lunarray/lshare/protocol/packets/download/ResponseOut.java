package org.lunarray.lshare.protocol.packets.download;

import java.net.InetAddress;

import org.lunarray.lshare.protocol.Hash;
import org.lunarray.lshare.protocol.RemoteFile;
import org.lunarray.lshare.protocol.packets.PacketOut;
import org.lunarray.lshare.protocol.packets.PacketUtil;
import org.lunarray.lshare.protocol.state.userlist.User;
import org.lunarray.lshare.protocol.state.userlist.UserNotFound;

public class ResponseOut extends PacketOut {

	private User user;
	
	public ResponseOut(RemoteFile r, int port, User u, long offset) throws UserNotFound {
		// Construct data
		if (u.getAddress() == null) {
			throw new UserNotFound();
		}
		user = u;
		
		byte[] path = PacketUtil.encode(r.getPath());
		byte[] name = PacketUtil.encode(r.getName());
		byte nlen = (byte)Math.min(name.length, 255);
		
		data = new byte[1 + 8 + 8 + 2 + Hash.length() + 2 + path.length + 1 + nlen];
		
		data[0] = ResponseIn.getType();
		
		PacketUtil.longToByteArray(offset, data, 1);
		PacketUtil.longToByteArray(r.getSize(), data, 9);
		
		PacketUtil.shortUToByteArray(port, data, 17);
		
		PacketUtil.injectByteArrayIntoByteArray(r.getHash().getBytes(), Hash.
				length(), data, 19);
		PacketUtil.shortUToByteArray(path.length, data, 19 + Hash.length());
		PacketUtil.injectByteArrayIntoByteArray(path, path.length, data, 21 + 
				Hash.length());
		data[21 + Hash.length() + path.length] = nlen;
		PacketUtil.injectByteArrayIntoByteArray(name, nlen, data, 22 + Hash.
				length() + path.length);
	}
	
	@Override
	public InetAddress getTarget() {
		return user.getAddress();
	}
}
