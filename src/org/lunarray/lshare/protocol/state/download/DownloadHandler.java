package org.lunarray.lshare.protocol.state.download;

import org.lunarray.lshare.protocol.Controls;
import org.lunarray.lshare.protocol.RemoteFile;
import org.lunarray.lshare.protocol.packets.download.RequestOut;
import org.lunarray.lshare.protocol.state.download.file.Chunk;
import org.lunarray.lshare.protocol.state.download.file.IncompleteFile;
import org.lunarray.lshare.protocol.state.userlist.User;

public class DownloadHandler {
	
	private User user;
	private RemoteFile remote;
	private IncompleteFile incomplete;
	private Controls controls;
	private DownloadManager manager;
	private DownloadTransfer transfer;
	private Chunk chunk;
	private DownloadHandlerStatus status;
	
	public DownloadHandler(User u, RemoteFile f, IncompleteFile i, 
			Controls c, DownloadManager m) {
		user = u;
		remote = f;
		incomplete = i;
		controls = c;
		manager = m;
		status = DownloadHandlerStatus.INIT;
	}
	
	public void close() {
		transfer.close();
		manager.removeDownloadHandler(this);
	}
	
	public User getUser() {
		return user;
	}
	
	protected DownloadHandlerStatus getStatus() {
		return status;
	}
	
	// TODO handle response
	
	public void init() {
		if (incomplete.getSources().contains(user)) {
			// Is a valid source
			for (DownloadHandler d: manager.getTransfers()) {
				if (d.getUser().equals(user)) {
					manager.removeDownloadHandler(this);
					return;
				}
			}
			
			// No downloads from the user yet
			// We can start request
			try {
				chunk = incomplete.getChunk();
				RequestOut ro = new RequestOut(user, remote, chunk.getMark());
				controls.getUDPTransport().send(ro);
				status = DownloadHandlerStatus.CONNECTING;
				controls.getTasks().enqueueMultiTask(new DownloadTimeout(this,
						ro));
			} catch (IllegalAccessException iae) {
				// No chunk
			}
		} else {
			manager.removeDownloadHandler(this);
		}
	}
}
