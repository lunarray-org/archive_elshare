package edu.tue.compnet.protocol.packet;

import java.net.DatagramPacket;
import java.net.InetAddress;

import edu.tue.compnet.Output;
import edu.tue.compnet.protocol.State;
import edu.tue.compnet.protocol.Transport;

/**
 * This implements a type 2 packet of the protocol.
 * Packet two looks as follows:<br>
 * (1)(2_)(3*)<br>
 * 1: 1 byte that denotes the type, has the number 2<br>
 * 2: 2 bytes that denote the length of the name<br>
 * 3: the name of the user of the length specified in part 2
 * @author Pal Hargitai
 * @author Siu-Hong Li
 */
public class PacketTwo extends Packet {
	/** The ID of the packet. */
	public static byte TYPE = 0x02;


	/**
	 * The packet constructor
	 * @param address The address it is to be sent to.
	 * @param name The name of the person.
	 * @param port The port to send on.
	 * @param mtu The maximum transferrable units.
	 */
	public PacketTwo(InetAddress address, String name, int port, int mtu) {
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
	public static void handlePacket(DatagramPacket packet, State state,
			Transport daemon) {
		Output.out("Packet type two received!");
		
		byte[] data = packet.getData();
		// Rebuilding data
		// First check for correct type
		if (data[0] != TYPE) {
			return;
		}
		// Get back size
		int length = byteArrayToShort(data, 1);
		// Rebuild string
		byte[] rebuilt = getByteArrayFromByteArray(data, length, 3); 
		// Now put it in a normal string
		String pack = decode(rebuilt);
		state.getUserList().addUserToList(pack, packet.getAddress());
	}
}
