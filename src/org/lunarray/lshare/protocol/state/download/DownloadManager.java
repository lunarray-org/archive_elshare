package org.lunarray.lshare.protocol.state.download;

import java.util.ArrayList;

import org.lunarray.lshare.protocol.Controls;
import org.lunarray.lshare.protocol.RemoteFile;
import org.lunarray.lshare.protocol.state.userlist.User;

/**
 *
 1- add to queue

 2- handle prelim queue to perm queue

 3- check for transfferables

 4- start transfers

 5- transfer

 6- transfer done/canceled

 7- cleanup transfer

 8- check for what has been done

 9- notify listeners

 10- do 3

 11- cleanup when kicked
 * @author Pal Hargitai
 */
public class DownloadManager {

	private ArrayList<IncompleteFile> queue;
	private Controls controls;
	private FirstQueueParse fparse;
	private DownloadSettings settings;
	private DownloadFileManager filemanager;

	public DownloadManager(Controls c) {
		controls = c;
		
		queue = new ArrayList<IncompleteFile>();
		filemanager = new DownloadFileManager();
		
		fparse = new FirstQueueParse(this, filemanager);
		controls.getTasks().backgroundTask(fparse);
		
		/*
		 * Init from back to stopped.
		 * 
		 * directly request these from second queue parser
		 */
	}
	
	/*
	 * get kicked by a finished transfer
	 * this should kick the second queue parser
	 * 
	 * on user signon, kick second queue parser too
	 */
	
	protected void enqueue(IncompleteFile f) {		
		if (!queue.contains(f)) {
			Controls.getLogger().fine("Enqueued: " + f.getRelTarget().getPath());
			queue.add(f);
		}
		// kick for checks
	}
	
	public void enqueue(RemoteFile f, User u) {
		fparse.toParse(f, u, settings.getDownloadDirectory());
	}
	
	public void addSource(RemoteFile f, IncompleteFile i, User u) {
		if (queue.contains(i)) {
			i.addSource(u, f);
		}
		// kick for checks
	}
}