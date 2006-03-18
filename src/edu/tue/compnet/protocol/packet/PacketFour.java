package edu.tue.compnet.protocol.packet;

import java.io.File;
import java.net.*;

import edu.tue.compnet.Output;
import edu.tue.compnet.protocol.*;
import edu.tue.compnet.protocol.state.HashList;

/**
 * The class that implements a packet of type 4 (extended)
 * New packet:
 * (1)(2_)(3*)(4___)(5_)(6___ ____ ____ ____)<br>
 * 1: 1 byte that denotes the type, has the number 4<br>
 * 2: 2 bytes that denote the length of the filename<br>
 * 3: the name of the file to search for of the length specified in part 2<br>
 * 4: 4 bytes denoting the size of the file.<br>
 * 5: 4 bytes denoting the date of creation/modification of the file:
 * epoch / 1000<br>
 * 6: 16 bytes denoting the files md5 hash of the file
 * Old packet:
 * (1)(2_)(3*)(4___)(5_)<br>
 * 1: 1 byte that denotes the type, has the number 4<br>
 * 2: 2 bytes that denote the length of the filename<br>
 * 3: the name of the file to search for of the length specified in part 2<br>
 * 4: 4 bytes denoting the size of the file.<br>
 * 5: 2 bytes denoting the date of creation/modification of the file
 * @author Pal Hargitai
 * @author Siu-Hong Li
 */
public class PacketFour extends Packet {
	/** The ID of the packet. */
	public static byte TYPE = 0x04;

	/**
	 * Constructs a packet of type four
	 * @param f The file to send.
	 * @param a The address to send it to.
	 * @param port The port to send on.
	 * @param mtu The maximal transferrable units.
	 * @param md5 The md5 hash of the file.
	 */
	public PacketFour(File f, InetAddress a, int port, int mtu, byte[] md5) {
		// Build packet
		byte[] file = encode(f.getName());
		// Set length
		int len = file.length;
		if ((file.length + 11 + HashList.HASH_LENGTH) > mtu) {
			len = mtu - (11 + HashList.HASH_LENGTH);
		}
		
		// Data
		byte[] data = new byte[len + 11 + HashList.HASH_LENGTH];
		data[0] = TYPE;
		// Insert file data
		// Filename
		// Get the length of the name
		shortToByteArray((new Integer(len)).shortValue(), data, 1);
		// Fill array
		injectByteArrayIntoByteArray(file, len, data, 3);
		// Put byte in there
		intToByteArray((new Long(f.length())).intValue(), data, len + 3);
		// Get date
		intToByteArray(Long.valueOf(f.lastModified() / 1000).intValue(), data,
				len + 7);
		// md5
		injectByteArrayIntoByteArray(md5, HashList.HASH_LENGTH, data,
				len + 11);
		// Put it in place
		packet = new DatagramPacket(data, data.length, a, port);
	}

	/**
	 * Handles the packet of type 4.
	 * @param packet The packet to be handled.
	 * @param state The state of the protocol.
	 * @param daemon The daemon the protocol runs in.
	 */
	public static void handlePacket(DatagramPacket packet, State state,
			Transport daemon) {
		Output.out("Packet type four received!");
		
		// check if packet is proper
		byte[] data = packet.getData();
		if (data[0] != TYPE) {
			return;
		}
		// Get back size
		int length = byteArrayToShort(data, 1);
		// Rebuild string
		byte[] rebuilt = getByteArrayFromByteArray(data, length, 3); 
		// Now put it in a normal string
		String pack = decode(rebuilt);
		// get size and date
		int size = byteArrayToInt(data, length + 3);
		int date = byteArrayToInt(data, length + 7);
		// the hash
		byte[] h = getByteArrayFromByteArray(data, HashList.HASH_LENGTH,
				length + 11);
		// Trigger result.
		state.getSearchList().searchResult(pack, size, date, packet.
				getAddress(), h);
	}
}
