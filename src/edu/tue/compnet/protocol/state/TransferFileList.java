package edu.tue.compnet.protocol.state;

import java.io.File;
import java.util.ArrayList;

import edu.tue.compnet.protocol.Settings;
import edu.tue.compnet.protocol.State;

/**
 * Managed the transferfile list, ensures that only one instance for a file
 * exists.
 * @author Pal Hargitai
 * @author Siu-Hong Li
 */
public class TransferFileList {
	// The transferfiles
	ArrayList<TransferFile> flist;
	// The settings
	Settings settings;
	// The state
	State state;
	
	/**
	 * The constructor of the list.
	 * @param s The settings.
	 * @param st The state of the protocol.
	 */
	public TransferFileList(Settings s, State st) {
		flist = new ArrayList<TransferFile>();
		settings = s;
		state = st;
	}
	
	/**
	 * Gets a transferfile with the file f. If need be, create a new
	 * transferfile with the file f.
	 * @param f The file for which to get a transferfile.
	 * @return The transferfile associated with the file.
	 */
	public synchronized TransferFile getFile(File f) {
		TransferFile ret = null;
		search: {
			// Find one, else, make one
			for (TransferFile tfile: flist) {
				if (tfile.getFile().equals(f)) {
					ret = tfile;
					break search;
				}
			}
			
			TransferFile t = new TransferFile(f, settings, state);
			flist.add(t);
			ret = t;
			break search;
		}
		return ret;
	}
	
	/**
	 * Close a file and remove it from here.
	 * @param f The file to close.
	 */
	public synchronized void closeFile(TransferFile f) {
		f.close();
		flist.remove(f);
	}
	
	/**
	 * Entirely close down things.
	 */
	public synchronized void close() {
		for (TransferFile t: flist) {
			t.close();
		}
	}
}
