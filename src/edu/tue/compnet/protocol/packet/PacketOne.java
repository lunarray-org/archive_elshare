package edu.tue.compnet.protocol.packet;

import java.net.*;

import edu.tue.compnet.Output;
import edu.tue.compnet.protocol.*;

/**
 * Describes a packet of type 1
 * Packet one looks as follows:<br>
 * New packet 1<br>
 * (1)(2_)(3*)<br>
 * 1: 1 byte that denotes the type, has the number 1<br>
 * 2: 2 bytes denoting the length of the username<br>
 * 3: the username of the length denoted in 2.<br>
 * Old packet 1<br>
 * (1)<br>
 * 1: 1 byte that denotes the type, has the number 1<br>
 * @author Pal Hargitai
 * @author Siu-Hong Li
 */
public class PacketOne extends Packet {
	/** The ID of the packet. */
	public static byte TYPE = 0x01;

	/**
	 * The constructor of the packet
	 * @param address the address it is to be sent to
	 * @param port the port it is to be sent to
	 * @param name The name to send.
	 * @param mtu The maximum transferrable units.
	 */
	public PacketOne(InetAddress address, int port, String name, int mtu) {
		byte[] bname = encode(name);
		int len = bname.length;
		if ((bname.length + 3) > mtu) {
			len = mtu - 3;
		}
		// Now come the byte stuff
		byte[] data = new byte[len + 3];
		data[0] = TYPE;
		// Convert the short to a byte array.
		shortToByteArray((new Integer(len)).shortValue(), data, 1);
		// Fill data
		injectByteArrayIntoByteArray(bname, len, data, 3);
		// Get the packet right
		packet = new DatagramPacket(data, data.length, address, port);
	}

	/**
	 * Handles when a packet of that kind has arrived.
	 * @param packet The packet that has arrived.
	 * @param state The state of the protocol.
	 * @param daemon The daemon, to queue packets to be sent back.
	 */
	public static void handlePacket(DatagramPacket packet,
			State state, Transport daemon) {
		Output.out("Packet type one received!");
		
		/*
		 * First check wether it has really been a packet of type 1. Then 
		 * send a packet of type two with the nickname of the user.
		 */
		byte[] data = packet.getData();
		// Something went wrong
		if (data[0] != TYPE) {
			return;
		}
		
		// Get back size
		int length = byteArrayToShort(data, 1);
		// Rebuild string
		byte[] rebuilt = getByteArrayFromByteArray(data, length, 3); 
		// Now put it in a normal string
		String pack = decode(rebuilt);
		// Not really nessecary, but clients before revision will work now.
		if (pack.length() > 0) {
			state.getUserList().addUserToList(pack, packet.getAddress());
		}
		
		// Send the packet of type two
		PacketTwo ptwo = new PacketTwo(packet.getAddress(),
				state.getStateSettings().getNickname(), Transport.PORT, Transport.MTU);
		daemon.send(ptwo.getPacket());
		Output.out("Packet type two enqueued!");
	}
}
