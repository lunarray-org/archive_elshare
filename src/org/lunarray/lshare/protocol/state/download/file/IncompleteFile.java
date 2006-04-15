package org.lunarray.lshare.protocol.state.download.file;

import java.io.File;

import org.lunarray.lshare.protocol.RemoteFile;
import org.lunarray.lshare.protocol.state.sharing.ShareEntry;

public class IncompleteFile {
//	 TODO

	private byte[] hash;
	private ChunkedFile file;
	
	public IncompleteFile(File f) {
//		 TODO
		file = new ChunkedFile();
	}
	
	public boolean matches(RemoteFile f) {
		if (file.getSize() > 0 && f.getSize() != file.getSize()) {
			return false;
		}
		if (ShareEntry.isEmpty(hash)) {
			return true;
		} else {
			return ShareEntry.equals(hash, f.getHash());
		}
	}
	
	protected void setHash(byte[] h) throws IllegalArgumentException {
		if (ShareEntry.isEmpty(hash)) {
			hash = h;
		} else {
			if (ShareEntry.equals(hash, h)) {
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
