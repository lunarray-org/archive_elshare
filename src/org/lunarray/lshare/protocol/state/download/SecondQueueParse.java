package org.lunarray.lshare.protocol.state.download;

import java.util.ArrayList;
import java.util.Set;
import java.util.TreeSet;

import org.lunarray.lshare.protocol.Controls;
import org.lunarray.lshare.protocol.RemoteFile;
import org.lunarray.lshare.protocol.state.download.file.IncompleteFile;
import org.lunarray.lshare.protocol.state.userlist.User;
import org.lunarray.lshare.protocol.state.userlist.UserNotFound;
import org.lunarray.lshare.protocol.tasks.RunnableTask;

/*
 * This does the actual parsing work
 * First get all requesteds
 * requested file is directly requested file
 * 
 * then try to handle queued
 * if queue (for a user) is finished, handle stoppeds
 * 
 * get some very infrequent kick to check queue, just to make sure.
 * 
 */

public class SecondQueueParse implements RunnableTask {

	private boolean ischecking;
	private ArrayList<IncompleteFile> requests;
	private DownloadManager manager;
	
	public SecondQueueParse(DownloadManager m) {
		ischecking = false;
		manager = m;
		requests = new ArrayList<IncompleteFile>();
	}
	
	public void directRequest(IncompleteFile inc) {
		requests.add(inc);
	}
	
	private void download(IncompleteFile f, Controls c, Set<User> available) {
		for (User u: available) {
			if (f.canDownloadFromUser(u)) {
				try {
					RemoteFile i = f.getSourceFromUser(u);
					DownloadHandler h = new DownloadHandler(u, i, f, c, manager);
					h.init();
				} catch (UserNotFound unf) {
					// Ignore
				}
			}
		}
	}
	
	// TODO make regular task to check
	public void runTask(Controls c) {
		while (true) {
			check: {
				if (ischecking) {
					break check;
				} else {
					ischecking = true;
				}
		
				TreeSet<User> available = new TreeSet<User>();
				for (IncompleteFile i: manager.getQueue()) {
					available.addAll(i.getSources());
				}
				for (DownloadHandler t: manager.getTransfers()) {
					available.remove(t.getUser());
				}
				
				for (IncompleteFile f: requests) {
					download(f, c, available);
				}
				requests.clear();
				
				for (IncompleteFile i: manager.getQueue()) {
					if (i.getStatus() == QueueStatus.QUEUED) {
						download(i, c, available);
					}
				}
				
				// TODO checks for regulars
				
				ischecking = false;
			}
			try {
				Thread.sleep(10000);
			} catch (InterruptedException ie) {
				// Ignore
			}		
		}
	}
}
