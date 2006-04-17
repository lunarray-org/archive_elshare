package org.lunarray.lshare.protocol.state.download;

import java.io.File;

import org.lunarray.lshare.protocol.RemoteFile;
import org.lunarray.lshare.protocol.state.userlist.User;

public class QueuedItem {
	private RemoteFile file;
	private User user;
	private File targetdir;
	
	public QueuedItem(RemoteFile f, User u, File todir) {
		file = f;
		user = u;
		targetdir = todir;
	}
	
	public RemoteFile getFile() {
		return file;
	}
	
	public User getUser() {
		return user;
	}
	
	public File getTargetDir() {
		return targetdir;
	}
	
	public File getTargetFile() {
		return new File(targetdir.getPath() + File.pathSeparator + file.
				getName());
	}
}
