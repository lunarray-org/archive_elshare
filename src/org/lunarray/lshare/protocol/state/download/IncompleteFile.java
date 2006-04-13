package org.lunarray.lshare.protocol.state.download;

import java.io.File;
import java.util.ArrayList;
import java.util.Set;
import java.util.TreeMap;

import org.lunarray.lshare.protocol.RemoteFile;
import org.lunarray.lshare.protocol.state.sharing.ShareEntry;
import org.lunarray.lshare.protocol.state.sharing.ShareSettings;
import org.lunarray.lshare.protocol.state.userlist.User;
import org.lunarray.lshare.protocol.state.userlist.UserNotFound;

public class IncompleteFile {

	private File target;
	private QueueStatus status;
	private TreeMap<User, RemoteFile> users;
	private long size;
	private byte[] hash;
	private ArrayList<DownloadTransfer> transfers;
	
	protected IncompleteFile(File f) {
		users = new TreeMap<User, RemoteFile>();
		transfers = new ArrayList<DownloadTransfer>(); 
		status = QueueStatus.QUEUED;
		target = f;
		size = -1;
		hash = ShareSettings.HASH_UNSET;
	}
	
	protected void setSize(long s) {
		size = s;
	}
	
	protected void setHash(byte[] h) {
		hash = h;
	}
	
	protected void addSource(User u, RemoteFile f) {
		if (!users.containsKey(u)) {
			if (ShareEntry.equals(hash, f.getHash()));
			users.put(u, f);
		}
	}
	
	protected void removeSource(User u) {
		if (users.containsKey(u)) {
			users.remove(u);
		}
	}
	
	public File getRelTarget() {
		return target;
	}
	
	public void setStatus(QueueStatus s) {
		status = s;
	}
	
	public QueueStatus getStatus() {
		return status;
	}
		
	public Set<User> getSources() {
		return users.keySet();
	}
	
	public long getDone() {
		return 0; // Blaat
	}
	
	public long getSize() {
		return size;
	}
	
	public byte[] getHash() {
		return hash;
	}
	
	public void initDownload(User u, int port) {
		// Bla bla
	}
	
	public RemoteFile getEntryFromUser(User u) throws UserNotFound {
		if (users.containsKey(u)) {
			return users.get(u);
		} else {
			throw new UserNotFound();
		}
	}
	
	public boolean hashIsEmpty() {
		return ShareEntry.isEmpty(hash);
	}
	
	public boolean sizeUnset() {
		return size < 0;
	}
	
	public boolean canDownloadFrom(User u) {
		if (users.containsKey(u)) {
			for (DownloadTransfer t: transfers) {
				if (!t.getAddress().equals(u.getAddress())) {
					return true;
				}
			}
		}
		return false;
	}
}
