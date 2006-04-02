package org.lunarray.lshare.protocol.filelist;

import java.util.List;

import org.lunarray.lshare.protocol.RemoteFile;
import org.lunarray.lshare.protocol.state.sharing.SharedDirectory;

public class FilelistEntry extends RemoteFile {

	private FilelistReceiver receiver;
	
	public FilelistEntry(FilelistReceiver fr, String p, String n, byte[] h, long lm, long s) {
		super(p, n, h, lm, s);
		receiver = fr;
	}
	
	public void closeReceiver() {
		receiver.close();
	}
	
	public List<FilelistEntry> getEntries() {
		if (getPath().equals(".")) {
			return receiver.getEntries(getPath());
		} if (getPath().equals("")) {
			return receiver.getEntries(getName());
		} else {
			return receiver.getEntries(getPath() + SharedDirectory.SEPARATOR + getName());
		}
	}
}
