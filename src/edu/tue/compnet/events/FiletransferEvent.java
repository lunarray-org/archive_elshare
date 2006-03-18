package edu.tue.compnet.events;

import edu.tue.compnet.protocol.*;
import edu.tue.compnet.protocol.state.Transfer;

/**
 * The class that holds all information for a filetransfer event.
 * @author Pal Hargitai
 * @author Siu-Hong Li
 */
public class FiletransferEvent extends Event {
	// The address it came from
	Transfer transfer;

	/**
	 * Constructor of a filetransferring event. Holds all usefull information
	 * related to a filetransfer.
	 * @param s The state of the protocol.
	 * @param t The transfer associated.
	 */
	public FiletransferEvent(State s, Transfer t) {
		super(s, "File transfer event.");
		transfer = t;
	}
	
	/**
	 * Gets the transfer that's associated with this. Has info on length etc.
	 * @return The transfer.
	 */
	public Transfer getTransfer() {
		return transfer;
	}
}
