package edu.tue.compnet.protocol.packet;

import java.io.File;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.util.List;

import edu.tue.compnet.Output;
import edu.tue.compnet.protocol.State;
import edu.tue.compnet.protocol.Transport;
import edu.tue.compnet.protocol.state.HashList;

/**
 * The class that implements a packet of type 3 (extended)
 * (1)(2_)(3*)(4___ ____ ____ ____)<br>
 * 1: 1 byte that denotes the type, has the number 3<br>
 * 2: 2 bytes that denote the length of the filename<br>
 * 3: the name of the file to search for of the length specified in part 2<br>
 * 4: The md5 hash of the file, if available. (extension)<br>
 * @author Pal Hargitai
 * @author Siu-Hong Li
 */
public class PacketThree extends Packet {
	/** The ID of the packet. */
	public static byte TYPE = 0x03;

	/**
	 * The constructor of a type 3 packet
	 * @param filename The filename to search for.
	 * @param address The address to send it to.
	 * @param port The port to send it on.
	 * @param mtu The maximum transferrable units
	 * @param md5 The hash of the file.
	 */
	public PacketThree(String filename, InetAddress address, int port, int
			mtu, byte[] md5) {
		byte[] file = encode(filename);
		// Set length
		int len = file.length;
		if ((file.length + 3 + HashList.HASH_LENGTH) > mtu) {
			len = mtu - (3 + HashList.HASH_LENGTH);
		}
		
		byte[] data = new byte[len + 3 + HashList.HASH_LENGTH];
		data[0] = TYPE;

		// Get the length of the name
		shortToByteArray((new Integer(len)).shortValue(), data, 1);
		// Fill data
		injectByteArrayIntoByteArray(file, len, data, 3);
		// Add md5
		injectByteArrayIntoByteArray(md5, HashList.HASH_LENGTH, data,
				3 + len);
		// Get the packet right
		packet = new DatagramPacket(data, data.length, address, port);
	}
	
	/**
	 * Handles the packet of type 3.
	 * @param packet The packet to be handled.
	 * @param state The state of the protocol.
	 * @param daemon The daemon where the protocol runs.
	 */
	public static void handlePacket(DatagramPacket packet,
			State state, Transport daemon) {
		Output.out("Packet type three received!");
		
		// Check if the file exists, then send a reply of 4
		// check if packet is proper
		byte[] data = packet.getData();
		if (data[0] != TYPE) {
			return;
		}
		
		// get back the size
		int length = byteArrayToShort(data, 1);
		// Rebuild string
		byte[] rebuilt = getByteArrayFromByteArray(data, length, 3);
		// rebuild md5
		byte[] md5 = getByteArrayFromByteArray(data, HashList.HASH_LENGTH,
				length + 3);
		// Now put it in a normal string
		String search = decode(rebuilt);
		Output.out("Searched for: " + search);
		
		// Search for the proper file asked for.
		List<File> f = fileMatch(state.getStateSettings().getShareDirectory(),
				search);
		// Send on with matching md5
		if (!HashList.equals(md5, HashList.HASH_EMPTY)) {
			for (File mf: state.getHashList().getFile(md5)) {
				PacketFour pack = new PacketFour(mf, packet.getAddress(),
						Transport.PORT, Transport.MTU, md5);
				daemon.send(pack.getPacket());
				Output.out("Packet type four enqueued!");
			}
		}
		// Sent other files
		for (File fl: f) {
			// Name is found, send a packet 4
			PacketFour pack = new PacketFour(fl, packet.getAddress(),
					Transport.PORT, Transport.MTU, state.getHashList().
					getHash(fl));
			daemon.send(pack.getPacket());
			Output.out("Packet type four enqueued!");
		}
	}
}
