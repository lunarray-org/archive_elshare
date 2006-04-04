package org.lunarray.lshare.protocol.filelist;

import java.util.List;

import org.lunarray.lshare.protocol.RemoteFile;
import org.lunarray.lshare.protocol.state.sharing.ShareList;

public class FilelistEntry extends RemoteFile {

	private FilelistReceiver receiver;
	private boolean isroot;
	
	public FilelistEntry(FilelistReceiver fr, String p, String n, byte[] h, long lm, long s, boolean root) {
		super(p, n, h, lm, s);
		receiver = fr;
		isroot = root;
	}
	
	public void closeReceiver() {
		receiver.close();
	}
	
	public List<FilelistEntry> getEntries() {
		return receiver.getEntries(getPath() + ShareList.SEPARATOR + getName(), isroot);
	}
}
