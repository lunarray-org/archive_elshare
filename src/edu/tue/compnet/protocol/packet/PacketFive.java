package edu.tue.compnet.protocol.packet;

import java.io.*;
import java.net.*;

import edu.tue.compnet.Output;
import edu.tue.compnet.protocol.*;
import edu.tue.compnet.protocol.state.*;

/**
 * The class that implements a packet of type 5 (extended)
 * (1)(2_)(3*)(4___)<br>
 * 1: 1 byte that denotes the type, has the number 5<br>
 * 2: 2 bytes that denote the length of the filename<br>
 * 3: the name of the file requested of the length specified in part 2<br>
 * 4: 4 bytes that denote the offset at which the downloading should proceed.
 * @author Pal Hargitai
 * @author Siu-Hong Li
 */
public class PacketFive extends Packet {
	/** The ID of the packet. */
	public static byte TYPE = 0x05;
	/** The amount of timeouts that may occur. */
	public static int TIMEOUTS = 2;
	/** The amount of time between each timeout. */
	public static int TIMEOUT = 1000;

	// Wether the file can be written to.
	boolean isvalid;

	
	/**
	 * Constructs a packet of type five
	 * @param f The file to request.
	 * @param a The address to send it to.
	 * @param port The port to send it on.
	 * @param mtu The maximal transferrable units.
	 * @param state The state of the protocol.
	 * @param daemon The transport daemon.
	 * @param size The size of the file, ignored if the file is incomplete.
	 */
	public PacketFive(String f, InetAddress a, int port, int mtu, State state,
			Transport daemon, int size) {
		int offset = 0;
		if (state.getStateSettings().getIncomingDirectory() != null) {
			File tofile = new File(state.getStateSettings().
					getIncomingDirectory().getPath() + File.separator + f);
			// Register request
			/*
			 * Get the file, see if it's ok, set it up then write.
			 * Get chunks and then register all needed info.
			 */
			TransferFile tf = state.getTransferFileList().getFile(tofile);
			if (tf.isNew() || tf.isIncomplete()) {
				if (tf.isNew()) {
					tf.setLength(size);
				}
				tf.initWrite();
				if (tf.isValid()) {
					if (tf.chunksAvailable()) {
						Chunk c = tf.getLastReadyChunk();
						tf.lockChunk(c);
						state.getRequestList().registerRequest(f, a, c.
								getBegin(), tf.getLength(), c);
						state.getPacketTimeoutList().addPacketTimeout(new
								PacketFiveTimeout(TIMEOUTS, TIMEOUT, f, a,
								this, state, daemon, tf));
						offset = c.getMark();
						isvalid = true;
					}
				}
			}
		}
		
		// Build packet
		byte[] file = encode(f);
		// Set length
		int len = file.length;
		if ((file.length + 7) > mtu) {
			len = mtu - 7;
		}
		// Data
		byte[] data = new byte[len + 7];
		data[0] = TYPE;
		shortToByteArray(Integer.valueOf(len).shortValue(), data, 1);
		injectByteArrayIntoByteArray(file, len, data, 3);
		intToByteArray(offset, data, len + 3);
		packet = new DatagramPacket(data, data.length, a, port);
		isvalid = false;
	}
	
	/**
	 * Checks wether the file we want to later write to is valid.
	 * @return Wether the file is writable and stuff.
	 */
	public boolean isValid() {
		return isvalid;
	}

	/**
	 * Handles the packet of type 5.
	 * @param packet The packet to be handled.
	 * @param state The state of the protocol.
	 * @param daemon The daemon the protocol runs in.
	 */
	public static void handlePacket(DatagramPacket packet,
			State state, Transport daemon) {
		Output.out("Packet type five received!");

		// check if packet is proper
		byte[] data = packet.getData();
		if (data[0] != TYPE) {
			return;
		}
		/*
		 * First check wether the file requested actually exists, then prompt
		 * the user to continue, or not. Try to open a stream to the file that
		 * is to be sent. If that works, set up a TCP socket and update the
		 * state if such a socket has been made. This will happen in a file
		 * transfer class that will handle this in detail. If this all works
		 * out successfully, send a packet of type 6. 
		 */
		// get back the size
		int length = byteArrayToShort(data, 1);
		// Rebuild string
		byte[] rebuilt = getByteArrayFromByteArray(data, length, 3); 
		// The offset
		int offset = byteArrayToInt(data, length + 3);
		
		// Now put it in a normal string
		String request = decode(rebuilt);
		Output.out("Request for: " + request);
		File f = fileExists(state.getStateSettings().getShareDirectory(),
				request);
		if (f != null) {
			if (f.canRead()) {
				// File exists and can be read.
				/*
				 * Ask wether the address is of a buddies, so to automatically
				 * allow the download.
				 */ 
				boolean allowed = state.getBuddyList().isBuddy(packet.
						getAddress().getHostName()) || 
						state.getAllowList().isAllowed(packet.getAddress(),
						f);
				if (!allowed) {
					// Find username associated with this:
					String username = state.getUserList().
						getUserName(packet.getAddress());
					String message = "The '" + request + "' is requested " +
							"by user: "	+ username + " (" + packet.
							getAddress().getHostName() + ")";
					// Ask wether the user allowes it
					allowed = state.getQuery().askListeners(message, "Allow" +
							"download?");
				}
				if (allowed) {
					state.getAllowList().allow(packet.getAddress(), f);
					FiletransferServer fs = new FiletransferServer(state, f, offset);
					int p = fs.bind();
					fs.start();
					Output.out("Server opened at port: ");
					Output.out(p);
					
					// Server is now started, send packet of type 6
					// (File name)(int port)
					PacketSix ps = new PacketSix(f, packet.getAddress(),
							Transport.PORT, Transport.MTU, Integer.valueOf(
							p).shortValue(), offset, state.getHashList().
							getHash(f));
					daemon.send(ps.getPacket());
					state.getPacketTimeoutList().addPacketTimeout(new
						PacketFiveHandleTimeout(TIMEOUTS, TIMEOUT, ps, daemon,
						fs, state));
				}
			}
		}
	}
}