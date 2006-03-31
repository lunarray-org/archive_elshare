package org.lunarray.lshare.protocol.state.sharing;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import org.lunarray.lshare.protocol.Controls;

public class SharedFile {
	
	private File file;
	private List<String> path;
	private ShareSettings settings;
	
	public SharedFile(File f, List<String> p, ShareSettings s) {
		file = f;
		path = p;
		settings = s;
	}
	
	public String getName() {
		return file.getName();
	}
	
	public List<String> getSplitPath() {
		return path;
	}
	
	public String getPath() {
		List<String> l = getSplitPath();
		String rebuilt = "";
		for (String s: l) {
			rebuilt += "/" + s;
		}
		
		if (rebuilt.length() > 0) {
			return rebuilt.substring(1);
		} else {
			return rebuilt;
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
		return settings.getHash(file.getPath());
	}
	
	public static boolean equals(byte[] h, byte[] j) {
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
	
	public static boolean isEmpty(byte[] h) {
		return equals(h, ShareSettings.HASH_UNSET);
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
}
