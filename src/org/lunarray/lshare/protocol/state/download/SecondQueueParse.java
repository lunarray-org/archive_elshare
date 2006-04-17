package org.lunarray.lshare.protocol.state.download;

import java.util.ArrayList;
import java.util.TreeSet;

import org.lunarray.lshare.protocol.Controls;
import org.lunarray.lshare.protocol.state.download.file.IncompleteFile;
import org.lunarray.lshare.protocol.state.userlist.User;
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
 * TODO request transfers and update incomplete files for it
 * TODO new class for handling file transfers being handled, timeout handler?
 */

public class SecondQueueParse implements RunnableTask {

	private boolean ischecking;
	private ArrayList<IncompleteFile> requests;
	private DownloadManager manager;
	
	public SecondQueueParse(DownloadManager m) {
		ischecking = false;
		manager = m;
	}
	
	public void directRequest(IncompleteFile inc) {
		requests.add(inc);
	}
	
	public void runTask(Controls c) {
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

			if (!requests.isEmpty()) {
				// TODO do requests
			}
			// TODO checks for regulars
			
			ischecking = false;
		}
	}
}
