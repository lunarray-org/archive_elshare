package edu.tue.compnet.protocol.state;

import java.util.*;

import edu.tue.compnet.Output;
import edu.tue.compnet.events.*;
import edu.tue.compnet.protocol.*;

/**
 * The class that holds and handles most issues regarding filetransfers.
 * @author Pal Hargitai
 * @author Siu-Hong Li
 */
public class FiletransferList {
	// The active filetransfers
	ArrayList<FiletransferServer> serverfiletransfers;
	// The active filetransfers
	ArrayList<FiletransferClient> clientfiletransfers;
	// Listeners
	ArrayList<TransferListener> transferlisteners;
	// The state
	State state;

	/**
	 * The constructor for the filetransferlist.
	 * @param s The state of the protocol.
	 */
	public FiletransferList(State s) {
		// Listeners
		transferlisteners = new ArrayList<TransferListener>();
		// File transfers
		serverfiletransfers = new ArrayList<FiletransferServer>();
		clientfiletransfers = new ArrayList<FiletransferClient>();
		state = s;
	}

	/**
	 * Add a filetransfer.
	 * @param f the file transfer to add.
	 */
	public synchronized void addClientFiletransfer(FiletransferClient f) {
		clientfiletransfers.add(f);
	}
	
	/**
	 * Remove a file transfer.
	 * @param f The filetransfer to remove.
	 */
	public synchronized void removeClientFiletransfer(FiletransferClient f) {
		clientfiletransfers.remove(f);
	}
	
	/**
	 * Add a filetransfer.
	 * @param f the file transfer to add.
	 */
	public synchronized void addServerFiletransfer(FiletransferServer f) {
		serverfiletransfers.add(f);
	}
	
	/**
	 * Remove a file transfer.
	 * @param f The filetransfer to remove.
	 */
	public synchronized void removeServerFiletransfer(FiletransferServer f) {
		serverfiletransfers.remove(f);
	}
	
	/**
	 * Get all the file transfers.
	 * @return The filetransfers.
	 */
	public synchronized FiletransferServer[] getServerFiletransfers() {
		FiletransferServer[] transfers= new FiletransferServer
			[serverfiletransfers.size()];
		serverfiletransfers.toArray(transfers);
		return transfers;
	}
	
	/**
	 * Get all the file transfers.
	 * @return The filetransfers.
	 */
	public synchronized FiletransferClient[] getClientFiletransfers() {
		FiletransferClient[] transfers= new FiletransferClient
			[clientfiletransfers.size()];
		clientfiletransfers.toArray(transfers);
		return transfers;
	}

	/**
	 * Check all the filetransfers if their still valid.
	 */
	public synchronized void checkTransfers() {
		// Check wether filetransfers are still active
		try {
			ArrayList<FiletransferClient> toremove = new
					ArrayList<FiletransferClient>();
			for (FiletransferClient fs: clientfiletransfers) {
				synchronized (fs) {
					/*
					 * If it's not running, then remove it.
					 * if it shouldn't run, stop it and remove it.
					 */
					if (!fs.isRunning()) {
						toremove.add(fs);
					}
					if (!fs.shouldRun()) {
						fs.stop();
						toremove.add(fs);
					}
				}
			}
			for (FiletransferClient t: toremove) {
				clientfiletransfers.remove(t);
			}
		} catch (ConcurrentModificationException cme) {
			Output.err("Concurrent modification! (2)");
		}
		// Check wether filetransfers are still active
		ArrayList<FiletransferServer> toremove = new
				ArrayList<FiletransferServer>();
		for (FiletransferServer fs: serverfiletransfers) {
			synchronized (fs) {
				/*
				 * If it's not running, then remove it.
				 * if it shouldn't run, stop it and remove it.
				 */
				if (!fs.isRunning()) {
					toremove.add(fs);
				}
				if (!fs.shouldRun()) {
					fs.stop();
					toremove.add(fs);
				}
			}
		}
		for (FiletransferServer t: toremove) {
			serverfiletransfers.remove(t);
		}
	}
	
	
	/**
	 * Registers an upload with all the listeners.
	 * @param tr The transfer associated.
	 */
	public synchronized void registerUpload(Transfer tr) {
		FiletransferEvent fe = new FiletransferEvent(state, tr);
		for (TransferListener lis: transferlisteners) {
			lis.registerUpload(fe);
		}
	}
	
	/**
	 * Registers a download with all the listeners.
	 * @param tr The transfer associated.
	 */
	public synchronized void registerDownload(Transfer tr) {
		FiletransferEvent fe = new FiletransferEvent(state, tr);
		for (TransferListener lis: transferlisteners) {
			lis.registerDownload(fe);
		}
	}

	/**
	 * Add a listener to notify when events happen.
	 * @param lis The listener to add.
	 */
	public synchronized void addTransferListener(TransferListener lis) {
		transferlisteners.add(lis);
	}
	
	/**
	 * Remove a listener from the list.
	 * @param lis The listener to remove.
	 */
	public synchronized void removeTransferListener(TransferListener lis) {
		transferlisteners.remove(lis);
	}
}
