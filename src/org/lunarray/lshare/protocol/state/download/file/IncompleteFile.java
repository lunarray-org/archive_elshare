package org.lunarray.lshare.protocol.state.download.file;

import java.io.File;

import org.lunarray.lshare.protocol.Hash;
import org.lunarray.lshare.protocol.RemoteFile;

public class IncompleteFile {
//	 TODO

	private Hash hash;
	private ChunkedFile file;
	
	public IncompleteFile(File f) {
//		 TODO
		file = new ChunkedFile();
	}
	
	public boolean matches(RemoteFile f) {
		if (file.getSize() > 0 && f.getSize() != file.getSize()) {
			return false;
		}
		if (hash.isEmpty()) {
			return true;
		} else {
			return hash.equals(f.getHash());
		}
	}
	
	protected void setHash(Hash h) throws IllegalArgumentException {
		if (hash.isEmpty()) {
			hash = h;
		} else {
			if (hash.equals(h)) {
				// do nothing
			} else {
				throw new IllegalArgumentException();
			}
		}
	}
	
	protected void setSize(long s) throws InvalidFileStateException {
		file.setSize(s);
	}
}
