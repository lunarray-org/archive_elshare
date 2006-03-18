package edu.tue.compnet.protocol.packet;

import java.io.File;
import java.net.DatagramPacket;
import java.net.InetAddress;

import edu.tue.compnet.Output;
import edu.tue.compnet.protocol.State;
import edu.tue.compnet.protocol.Transport;
import edu.tue.compnet.protocol.state.HashList;

/**
 * The class that implements a packet of type 8 (extended)
 * (1)(2_)*(3_)(4*)<br>
 * 1: 1 byte that denotes the type, has the number 8<br>
 * 2: 2 bytes that denote amount of files to be sent<br>
 * The amount of 3/4 is the amount specified in 2:<br>
 * 3: the length of the name of the file<br>
 * 4: the name of the file of length specified in 3
 * @author Pal Hargitai
 * @author Siu-Hong Li
 */
public class PacketEight extends Packet {
	/** The ID of the packet. */
	public static byte TYPE = 0x08;

	/**
	 * The constructor of a packet of type eight.
	 * @param address The address to send to.
	 * @param port The port to send on.
	 * @param mtu The maximum transferrable units.
	 * @param state The state of the protocol.
	 */
	public PacketEight(InetAddress address, int port, int mtu, State state) {
		byte[] data = new byte[mtu];
		data[0] = TYPE;
		// Here it begins
		int offset = 3;
		File[] f = state.getStateSettings().getShareDirectory().
			listFiles();
		int amount = 0;
		// Start adding
		add: {
			for (int i = 0; i < Math.min(10, f.length); i++) {
				if (f[i].isFile()) {
					byte[] str = encode(f[i].getName());
					if (offset + str.length + 2 > mtu) {
						break add;
					}
					shortToByteArray(Integer.valueOf(str.length).shortValue(),
							data, offset);
					offset += 2;
					injectByteArrayIntoByteArray(str, str.length, data, offset);
					offset += str.length;
					amount++;
				}
			}
		}
		shortToByteArray(Integer.valueOf(amount).shortValue(), data, 1);
		packet = new DatagramPacket(data, data.length, address, port);
	}
	
	/**
	 * Handler of a packet of type eight.
	 * @param packet The packet to handle.
	 * @param s The state of the protocol.
	 * @param t The transport.
	 */
	public static void handlePacket(DatagramPacket packet, State s,
			Transport t) {
		/*
		 * Check for correctness and process
		 */
		byte[] data = packet.getData();
		// Something went wrong
		if (data[0] != TYPE) {
			return;
		}
		Output.out("Packet type eight received!");
		
		if (s.getUserlistRequestList().take(packet.getAddress())) {
			int amount = byteArrayToShort(data, 1);
			int offset = 3;
			String[] res = new String[amount];
			for (int i = 0; i < amount; i++) {
				// get them
				short len = byteArrayToShort(data, offset);
				offset += 2;
				byte[] str = getByteArrayFromByteArray(data, len, offset);
				offset += len;
				res[i] = decode(str);
				s.getSearchList().searchResult(res[i], 0, 0, packet.
						getAddress(), HashList.HASH_EMPTY);
			}
		}
	}
}
