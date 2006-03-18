package edu.tue.compnet.protocol.packet;

import java.net.*;
import java.io.*;

import edu.tue.compnet.Output;
import edu.tue.compnet.protocol.*;
import edu.tue.compnet.protocol.state.HashList;
import edu.tue.compnet.protocol.state.Request;

/**
 * The class that implements a packet of type 6 (extended)
 * (1)(2_)(3*)(4___)(5_)(6___ ____ ____ ____)<br>
 * 1: 1 byte that denotes the type, has the number 2<br>
 * 2: 2 bytes that denote the length of the filename<br>
 * 3: the name of the file requested of the length specified in part 2<br>
 * 4: 4 bytes denoting the length of the file specified in part 2 minus the
 * offset at which to start downloading. This for download resuming.<br>
 * 5: 2 bytes denoting the port that the client may connect to<br> 
 * 6: The md5 hash of the file sent.
 * @author Pal Hargitai
 * @author Siu-Hong Li
 */
public class PacketSix extends Packet {
	/** The ID of the packet. */
	public static byte TYPE = 0x06;


	/**
	 * Constructs a packet of type six
	 * @param f The file to request.
	 * @param a The address to send it to.
	 * @param port The port to send it on.
	 * @param mtu The maximal transferrable units.
	 * @param sport The that will allow a connection
	 * @param offset The offset at which the download should proceed.
	 * @param md5 The md5 hash of the file.
	 */
	public PacketSix(File f, InetAddress a, int port, int mtu, short sport,
			int offset, byte[] md5) {
		// Build packet
		byte[] file = encode(f.getName());
		// Set length
		short len = Integer.valueOf(file.length).shortValue();
		if ((file.length + 9 + HashList.HASH_LENGTH) > mtu) {
			len = Integer.valueOf(mtu - (9 + HashList.HASH_LENGTH)).shortValue();
		}
		// Data
		byte[] data = new byte[len + 9 + HashList.HASH_LENGTH];
		data[0] = TYPE;
		
		// Inject file name length
		shortToByteArray(len, data, 1);
		// Inject file name
		injectByteArrayIntoByteArray(file, len, data, 3);
		// Inject file size
		intToByteArray(Long.valueOf(f.length()).intValue() - offset, data,
				len + 3);
		Output.out(Long.valueOf(f.length()).intValue() - offset);
		// Inject port
		shortToByteArray(sport, data, len + 7);
		// Inject hash
		injectByteArrayIntoByteArray(md5, HashList.HASH_LENGTH, data, len + 9);
		// Put it in the right place
		packet = new DatagramPacket(data, data.length, a, port);
	}

	
	/**
	 * Handles the packet of type 6.
	 * @param packet The packet to be handled.
	 * @param state The state of the protocol.
	 * @param daemon The daemon the protocol runs in.
	 */
	public static void handlePacket(DatagramPacket packet,
			State state, Transport daemon) {
		Output.out("Packet type six received!");
		
		// check if packet is proper
		byte[] data = packet.getData();
		if (data[0] != TYPE) {
			return;
		}
		// Get filename length
		short fsize = byteArrayToShort(data, 1);
		// Get filename
		byte[] rebuilt = getByteArrayFromByteArray(data, fsize, 3);
		String filename = decode(rebuilt);
		// Get filesize
		int filesize = byteArrayToInt(data, fsize + 3);
		// Get port
		short port = byteArrayToShort(data, fsize + 7);
		// Get hash
		byte[] h = getByteArrayFromByteArray(data, HashList.HASH_LENGTH,
				fsize + 11);
		
		Request req = null;
		synchronized (state.getRequestList()) {
			if (state.getRequestList().requestExists(filename, 
					packet.getAddress())) {
				req = state.getRequestList().getRequest(filename, packet.
						getAddress());
				state.getRequestList().removeRequest(filename, packet.
						getAddress());
			}
		}
		if (req != null) {
			boolean validhash = false;
			if (!HashList.equals(HashList.HASH_EMPTY, req.getChunk().
					getTransferFile().getHash())) {
				if (HashList.equals(h, req.getChunk().getTransferFile().
						getHash())) {
					validhash = true;
				} else {
					if (HashList.equals(h, HashList.HASH_EMPTY)) {
						if 	(state.getStateSettings().getCheckHashes()) {
							validhash = false;
						} else {
							validhash = true;
						}
					} else {
						validhash = false;
					}
				}
			} else {
				validhash = true;
			}
			if (validhash) {
				// It's a valid request
				Output.out("Connecting to port: ");
				Output.out(port);
				Output.out(packet.getAddress().getHostName());
				FiletransferClient fc = new FiletransferClient(req.
						getChunk(), filesize, state, port, packet.
						getAddress(), daemon);
				if (fc.start()) {
					state.getFiletransferList().addClientFiletransfer(fc);
				} else {
					fc.stop();
				}
			}
		}
	}
}