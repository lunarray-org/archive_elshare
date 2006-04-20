package org.lunarray.lshare.protocol.packets.download;

import java.net.InetAddress;

import org.lunarray.lshare.protocol.Hash;
import org.lunarray.lshare.protocol.RemoteFile;
import org.lunarray.lshare.protocol.packets.PacketOut;
import org.lunarray.lshare.protocol.packets.PacketUtil;
import org.lunarray.lshare.protocol.state.userlist.User;
import org.lunarray.lshare.protocol.state.userlist.UserNotFound;

/** An outgoing request for a file transfer.<br>
 * Packet 6:<br>
 * Purpose:<br>
 * Request for file transfer.<br>
 * UDP unicast.<br>
 * 8 fields:<br>
 * 0: 1 byte: 0x20<br>
 * 1: 8 bytes, the file offset<br>
 * 2: 8 bytes, the total size of the file<br>
 * 3: 16 bytes, the hash of the requested file<br>
 * 4: 2 bytes, representing an int n<br>
 * 5: n bytes, file path encoded in UTF-8<br>
 * 6: 1 byte, representing an int m<br>
 * 7: m bytes, file name encoded in UTF-8<br>
 * Timeout:<br>
 * Reponse is a packet type 7,8 or 9. Wait a second to retransmit, attempt at 
 * most 3 times.
 * @author Pal Hargitai
 */
public class RequestOut extends PacketOut {
	/** The user this request is meant for.
	 */
	private User user;

	/** Constructs an outgoing request for a filetransfer.
	 * @param u The user it is meant for.
	 * @param f The remote entry requested.
	 * @param offset The offset of the entry where downloading should commence.
	 * @throws UserNotFound Thrown if a user isn't logged in.
	 */
	public RequestOut(User u, RemoteFile f, long offset) throws UserNotFound {
		if (u.getAddress() == null) {
			throw new UserNotFound();
		}
		user = u;
		
		byte[] path = PacketUtil.encode(f.getPath());
		byte[] name = PacketUtil.encode(f.getName());
		byte nlen = (byte)Math.min(name.length, 255);
		
		data = new byte[1 + 8 + 8 + Hash.length() + 2 + path.length + 1 + 
		                nlen];
		
		data[0] = RequestIn.getType();
		
		PacketUtil.longToByteArray(offset, data, 1);
		PacketUtil.longToByteArray(f.getSize(), data, 9);
		PacketUtil.injectByteArrayIntoByteArray(f.getHash().getBytes(), Hash.
				length(), data, 17);
		PacketUtil.shortUToByteArray(path.length, data, 17 + Hash.length());
		PacketUtil.injectByteArrayIntoByteArray(path, path.length, data, 19 +
				Hash.length());
		data[19 + Hash.length() + path.length] = nlen;
		PacketUtil.injectByteArrayIntoByteArray(name, nlen, data, 20 + Hash.
				length() + path.length);
	}
	
	@Override
	/** Gets the target of the packet.
	 * @return The target of the packet.
	 */
	public InetAddress getTarget() {
		return user.getAddress();
	}
}