package org.lunarray.lshare.protocol.state.download.file;

import java.io.File;
import java.util.ArrayList;
import java.util.Set;
import java.util.TreeMap;

import org.lunarray.lshare.protocol.RemoteFile;
import org.lunarray.lshare.protocol.state.download.DownloadTransfer;
import org.lunarray.lshare.protocol.state.download.FileResponse;
import org.lunarray.lshare.protocol.state.download.QueueStatus;
import org.lunarray.lshare.protocol.state.sharing.ShareEntry;
import org.lunarray.lshare.protocol.state.sharing.ShareSettings;
import org.lunarray.lshare.protocol.state.userlist.User;
import org.lunarray.lshare.protocol.state.userlist.UserNotFound;

public class IncompleteFile {

	private QueueStatus status;
	private TreeMap<User, RemoteFile> users;
	private byte[] hash;
	private ArrayList<DownloadTransfer> transfers;
	private ChunkedFile file;
	
	protected IncompleteFile(File f) {
		users = new TreeMap<User, RemoteFile>();
		transfers = new ArrayList<DownloadTransfer>(); 
		status = QueueStatus.QUEUED;
		hash = ShareSettings.HASH_UNSET;
		// file = new ChunkedFile(f);
	}
	
	protected void setSize(long s) {
		file.setSize(s);
	}
	
	protected void setHash(byte[] h) {
		hash = h;
	}
	
	public void addSource(User u, RemoteFile f) {
		if (!users.containsKey(u)) {
			if (ShareEntry.equals(hash, f.getHash()));
			users.put(u, f);
		}
	}
	
	public void removeSource(User u) {
		if (users.containsKey(u)) {
			users.remove(u);
		}
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
	
	public long getTodo() {
		return file.getTodo(); // Blaat
	}
	
	public long getSize() {
		return file.getSize();
	}
	
	public byte[] getHash() {
		return hash;
	}
	
	public void initDownload(FileResponse r) {
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
		return getSize() < 0;
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
