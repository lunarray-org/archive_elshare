package org.lunarray.lshare.protocol.state.download;

import java.io.File;
import java.util.List;

import org.lunarray.lshare.protocol.RemoteFile;
import org.lunarray.lshare.protocol.state.download.file.IncompleteFile;
import org.lunarray.lshare.protocol.state.userlist.User;

public interface ExternalDownloadManager {
	public List<DownloadHandler> getTransfers();
	
	public List<IncompleteFile> getQueue();
	
	public void enqueue(RemoteFile f, User u, File todir);
	
	public void enqueue(RemoteFile f, User u);
}
