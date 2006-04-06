package org.lunarray.lshare.protocol.state.sharing;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.lunarray.lshare.protocol.Controls;

/**
 * A share entry.
 * @author Pal Hargitai
 */
public class ShareEntry {
	
	/**
	 * The file in the share entry.
	 */
	private File file;
	
	/**
	 * The path in the entry.
	 */
	private String path;
	
	/**
	 * The name of the entry.
	 */
	private String name;
	
	/**
	 * The settings that are to be accessed.
	 */
	private ShareSettings settings;
	
	/**
	 * Constructs a share entry.
	 * @param f The file represented in the entry.
	 * @param n The name of the entry.
	 * @param p The path in which the entry resides.
	 * @param s The settings that are to be accessed.
	 */
	public ShareEntry(File f, String n, String p, ShareSettings s) {
		file = f;
		path = p;
		name = n;
		settings = s;
	}
	
	/**
	 * Checks wether the given entry is a file.
	 * @return True if the entry is a file, false if not.
	 */
	public boolean isFile() {
		return file.isFile();
	}
	
	/**
	 * Checks wether the given entry is a directory.
	 * @return True if the entry is a directory, false if not.
	 */
	public boolean isDirectory() {
		return !isFile();
	}
	
	/**
	 * Gets the name of the entry.
	 * @return The name of the entry.
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Gets the path of the entry.
	 * @return The path of the entry.
	 */
	public String getPath() {
		return path;
	}
	
	/**
	 * Gets the last modified date of the entry.
	 * @return The last modified date of the entry.
	 */
	public long getLastModified() {
		if (file.exists()) {
			if (file.isFile()) {
				return file.lastModified();
			} else {
				return -1;
			}
		} else {
			return 0;
		}
	}

	/**
	 * Gets the size of the entry.
	 * @return The size of the entry.
	 */
	public long getSize() {
		if (file.exists()) {
			if (file.isFile()) {
				return file.length();
			} else {
				return -1;
			}
		} else {
			return 0;
		}
	}
	
	/**
	 * Gets the hash of the entry.
	 * @return The hash of the entry.
	 */
	public byte[] getHash() {
		if (file.isFile()) {
			return settings.getHash(file.getPath());
		} else {
			return ShareSettings.HASH_UNSET;
		}		
	}
	
	/**
	 * Checks wether the hash is an empty hash.
	 * @param h The hash to check.
	 * @return True if the given hash is an empty hash. False if not.
	 */
	public static boolean isEmpty(byte[] h) {
		return equals(h, ShareSettings.HASH_UNSET);
	}
	
	/**
	 * Gets the file represented in this entry.
	 * @return The file.
	 */
	public File getFile() {
		return file;
	}
	
	/**
	 * Gets a hash of the given name.
	 * @param name The name to hash.
	 * @return The hash of the name.
	 */
	protected static byte[] hashName(String name) {
		byte[] md5 = ShareSettings.HASH_UNSET;
		try {
			byte[] nbyte = name.getBytes();
			MessageDigest md = MessageDigest.getInstance(ShareSettings.HASH_ALGO);
			md.update(nbyte);
			md5 = md.digest();
		} catch (NoSuchAlgorithmException nse) {
			Controls.getLogger().fine("Hashing not supported!");
		}
		return md5;
	}
	
	/**
	 * Gets the hash of a specified file.
	 * @param f The file to get the hash of.
	 * @return The hash of the file.
	 */
	protected static byte[] hash(File f) {
		byte[] md5 = ShareSettings.HASH_UNSET;
		try {
			FileInputStream fi = new FileInputStream(f);
			MessageDigest md = MessageDigest.getInstance(ShareSettings.HASH_ALGO);
			byte[] data = new byte[1000];
			int done = 0;
			while (fi.available() > 0) {
				done = fi.read(data);
				md.update(data, 0, done);
			}
			md5 = md.digest();
		} catch (FileNotFoundException e) {
			Controls.getLogger().fine("File not found!");
		} catch (NoSuchAlgorithmException nse) {
			Controls.getLogger().fine("Hashing not supported!");
		} catch (IOException ie) {
			Controls.getLogger().fine("File error!");
		}
		return md5;
	}
	
	/**
	 * Checks if both hashes are equal.
	 * @param h The hash to comare to.
	 * @param j The hash to compare to.
	 * @return True if both hashes are equal, false if not.
	 */
	private static boolean equals(byte[] h, byte[] j) {
		if (h.length == j.length) {
			for (int i = 0; i < h.length; i++) {
				if (h[i] != j[i]) {
					return false;
				}
			}
			return true;
		} else {
			return false;
		}
	}
}
