package org.lunarray.lshare.protocol.state.download.file;

import java.io.File;
import java.util.concurrent.LinkedBlockingQueue;

import org.lunarray.lshare.protocol.Controls;
import org.lunarray.lshare.protocol.RemoteFile;
import org.lunarray.lshare.protocol.state.download.DownloadManager;
import org.lunarray.lshare.protocol.state.sharing.ShareEntry;
import org.lunarray.lshare.protocol.state.userlist.User;
import org.lunarray.lshare.protocol.tasks.RunnableTask;

public class FirstQueueParse implements RunnableTask {

	private DownloadManager manager;
	private LinkedBlockingQueue<QueuedItem> files;
	private boolean shouldrun;
	private DownloadFileManager filemanager;
	
	public FirstQueueParse(DownloadManager m, DownloadFileManager f) {
		files = new LinkedBlockingQueue<QueuedItem>();
		manager = m;
		shouldrun = true;
		filemanager = f;
	}
	
	public void toParse(RemoteFile f, User u, File todir) {
		files.add(new QueuedItem(f, u, todir));
	}
	
	public void close() {
		shouldrun = false;
	}
	
	public void recurseIntoDir(String tol, RemoteFile f) {
		
	}
	
	public void runTask(Controls c) {
		run: {
			while (true) {
				try {
					QueuedItem i = files.take();
					// figure file
					if (i.getFile().isFile()) {
						
						File temp = new File(i.getTargetDir().getPath() + File.
								pathSeparator + i.getFile().getName());
						
						IncompleteFile f = filemanager.getFile(temp);

						add: {
							if (f.sizeUnset()) {
								// New file
								f.setHash(i.getFile().getHash());
								f.setSize(i.getFile().getSize());
							} else {
								// Not a new file
								if (f.hashIsEmpty()) {
									// Not new but no hash
									f.setHash(i.getFile().getHash());
								} else {
									// Both hashes are set, now compare
									if (!ShareEntry.equals(f.getHash(), i.
											getFile(). getHash())) {
										// Not the file we're looking for
										break add;
									}
								}
							}
							
							if (i.getFile().getSize() == f.getSize()) {
								f.addSource(i.getUser(), i.getFile());
								manager.enqueue(f);
							}
						}
					} else {
						// do recurse stuff
					}
				} catch (InterruptedException ie) {
					// Ignore
				}
				if (!shouldrun) {
					break run;
				}
			}
		}
	}
	
	private class QueuedItem {
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
	}
}
