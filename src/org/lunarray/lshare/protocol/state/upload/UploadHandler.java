package org.lunarray.lshare.protocol.state.upload;

import java.io.IOException;

import org.lunarray.lshare.protocol.Controls;
import org.lunarray.lshare.protocol.state.download.DownloadRequest;
import org.lunarray.lshare.protocol.state.userlist.User;
import org.lunarray.lshare.protocol.tasks.RunnableTask;

public class UploadHandler implements RunnableTask {

	private UploadManager manager;
	private DownloadRequest request;
	private UploadTransfer transfer;
	private User user;
	
	public UploadHandler(UploadManager m, DownloadRequest r, User u) {
		manager = m;
		request = r;
		user = u;
	}
	
	public void runTask(Controls c) {
		try {
			transfer = new UploadTransfer(c.getState().getShareList().
					getFileForEntry(request), request.getOffset(), manager);

			int port = transfer.init();
			c.getTasks().backgroundTask(transfer);
			// Sleep for a while
			try {
				Thread.sleep(1500);
			} catch (InterruptedException ie) {
				// Ignore
			}
			if (!transfer.isRunning()) {
				transfer.close();
			}
			
			// TODO send response
			port++;
			user.getAddress();
		} catch (IOException ie) {
			transfer.close();
			Controls.getLogger().warning("Could transfer!");
		}
	}
}
