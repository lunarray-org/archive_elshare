package org.lunarray.lshare.protocol.state.sharing;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.lunarray.lshare.protocol.Controls;

public class ShareEntry {

	
	private File file;
	private String path;
	private String name;
	private ShareSettings settings;
	
	public ShareEntry(File f, String n, String p, ShareSettings s) {
		file = f;
		path = p;
		name = n;
		settings = s;
	}
	
	public boolean isFile() {
		return file.isFile();
	}
	
	public boolean isDirectory() {
		return !isFile();
	}
	
	public String getName() {
		return name;
	}
	
	public String getPath() {
		return path;
	}
	
	public long getLastModified() {
		if (file.isFile()) {
			if (file.exists()) {
				return file.lastModified();
			} else {
				return 0;
			}
		} else {
			return -1;
		}
	}

	public long getSize() {
		if (file.isFile()) {
			if (file.exists()) {
				return file.length();
			} else {
				return 0;
			}
		} else {
			return -1;
		}
	}
	
	public byte[] getHash() {
		if (file.isFile()) {
			return settings.getHash(file.getPath());
		} else {
			return ShareSettings.HASH_UNSET;
		}		
	}
	
	public static boolean isEmpty(byte[] h) {
		return equals(h, ShareSettings.HASH_UNSET);
	}
	
	public File getFile() {
		return file;
	}
	
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
