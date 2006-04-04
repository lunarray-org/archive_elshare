package org.lunarray.lshare.protocol;

import org.lunarray.lshare.protocol.state.sharing.ShareEntry;

/**
 * A remote file or directory representation.
 * @author Pal Hargitai
 */
public abstract class RemoteFile {

	/**
	 * The remote path in which this file or directory resides. 
	 */
	private String path;
	
	/**
	 * The name of this file or directory.
	 */
	private String name;
	
	/**
	 * The hash of this file or none if it's a directory.
	 */
	private byte[] hash;
	
	/**
	 * The last modified date of this file. (Epoch)
	 */
	private long lastmodified;
	
	/**
	 * The size of this file.
	 */
	private long size;
	
	/**
	 * Constructs a remote file or directory representation.
	 * @param p The path in which the file or directory resides.
	 * @param n The name of the file or directory.
	 * @param h The hash of the file or directory.
	 * @param lm The last modified date of the file or directory. (Epoch)
	 * @param s The size of the file or directory.
	 */
	public RemoteFile(String p, String n, byte[] h, long lm, long s) {
		path = p;
		name = n;
		hash = h;
		lastmodified = lm;
		size = s;
	}

	/**
	 * The path in which the file or directory resides.
	 * @return The path.
	 */
	public String getPath() {
		return path;
	}
	
	/**
	 * The name of the file or directory.
	 * @return The name.
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * The hash of the file or unset if for the directory.
	 * @return The hash of the file or unset if for the directory.
	 */
	public byte[] getHash() {
		return hash;
	}
	
	/**
	 * The last modified date or directory.
	 * @return The last modified epcoh date of this file. 0 if this is a
	 * directory. 
	 */
	public long getLastModified() {
		return lastmodified;
	}
	
	/**
	 * The size of the file or directory.
	 * @return Thie file of the size. <0 if this is a directory.
	 */
	public long getSize() {
		return size;
	}
	
	/**
	 * Asks wether this is a directory.
	 * @return True if this is a directory, false if not.
	 */
	public boolean isDirectory() {
		return size < 0;
	}
	
	/**
	 * Asks wether this is a file.
	 * @return True if this is a file, false if not.
	 */
	public boolean isFile() {
		return size >= 0;
	}
	
	/**
	 * Checks if this file has a hash.
	 * @return True if this file has a hash. False if this file does not have
	 * a hash or if this is a directory.
	 */
	public boolean hasHash() {
		if (ShareEntry.isEmpty(hash)) {
			return false;
		} else {
			return true;
		}
	}
}
