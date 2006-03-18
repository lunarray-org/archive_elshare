package edu.tue.compnet.protocol.state;

import java.util.*;

import edu.tue.compnet.Output;
import edu.tue.compnet.protocol.packet.PacketTimeout;

/**
 * This class handles most packet timeouts, aswell as other timeouts.
 * @author Pal Hargitai
 * @author Siu-Hong Li
 */
public class PacketTimeoutList {
	// The packet timeouts
	ArrayList<PacketTimeout> packettimeouts;
	
	/**
	 * The constructor for the timeout list.
	 */
	public PacketTimeoutList() {
		packettimeouts = new ArrayList<PacketTimeout>();
	}

	/**
	 * Adds a packet to the timeout handler.
	 * @param t The packet timeout.
	 */
	public synchronized void addPacketTimeout(PacketTimeout t) {
		packettimeouts.add(t);
	}
	
	
	/**
	 * Removes a packet from the timeout handler, generally not advised
	 * unless it's done!
	 * @param t The timeout to remove.
	 */
	public synchronized void removePacketTimeout(PacketTimeout t) {
		packettimeouts.remove(t);
	}

	/**
	 * Check the timeouts of the users.
	 * @param diff The difference since the last check.
	 */
	public synchronized void checkTimeouts(long diff) {
		// Check the packet timeouts
		try {
			ArrayList<PacketTimeout> toremove = new ArrayList<PacketTimeout>();
			for (PacketTimeout t: packettimeouts) {
				synchronized (t) {
					/*
					 * Check if a timeout has passed, if so, lock the
					 * resources. Check wether the timeout even matters.
					 * If it does matter, handle the timeout and clean up
					 * if it's dead, ie. timeout amount passed. If the
					 * timeout doesn't matter, just remove it.
					 * Unlock
					 */
					if (t.checkTimeout(Long.valueOf(diff).intValue())) {
						t.lock();
						if (t.isValid()) {
							t.handleTimeout();
							if (t.isDead()) {
								t.cleanUp();
								toremove.add(t);
							}
						} else {
							toremove.add(t);
						}
						t.unlock();
					}
				}
			}
			for (PacketTimeout t: toremove) {
				packettimeouts.remove(t);
			}
		} catch (ConcurrentModificationException cme) {
			Output.err("Concurrent modification! (1)");
		}
	}
}
