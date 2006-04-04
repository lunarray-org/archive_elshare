package org.lunarray.lshare.protocol.filelist;

import java.util.List;

import org.lunarray.lshare.protocol.RemoteFile;
import org.lunarray.lshare.protocol.state.sharing.ShareList;

/**
 * A file entry recieved over a tcp connection.
 * @author Pal Hargitai
 */
public class FilelistEntry extends RemoteFile {

	/**
	 * The filelist receiver for populating file entries.
	 */
	private FilelistReceiver receiver;
	
	/**
	 * True if the root is a root node for populating the correct children.
	 */
	private boolean isroot;
	
	/**
	 * Constructs a file entry.
	 * @param fr The list receiver for getting child entries.
	 * @param p The path that this entry resides in.
	 * @param n The name of this entry.
	 * @param h The hash of this entry.
	 * @param lm The last modified date of this entry.
	 * @param s The size of this entry.
	 * @param root True if this is the root node, false if not.
	 */
	public FilelistEntry(FilelistReceiver fr, String p, String n, byte[] h,
			long lm, long s, boolean root) {
		super(p, n, h, lm, s);
		receiver = fr;
		isroot = root;
	}
	
	/**
	 * Close down the receiver.
	 */
	public void closeReceiver() {
		receiver.close();
	}
	
	/**
	 * Get all children in this entry.
	 * @return The child entries in this entry.
	 */
	public List<FilelistEntry> getEntries() {
		return receiver.getEntries(getPath() + ShareList.SEPARATOR + getName(),
				isroot);
	}
}
