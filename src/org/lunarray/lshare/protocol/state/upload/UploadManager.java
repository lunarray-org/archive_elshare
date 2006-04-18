package org.lunarray.lshare.protocol.state.upload;

import java.util.ArrayList;
import java.util.List;

import org.lunarray.lshare.protocol.Controls;
import org.lunarray.lshare.protocol.state.download.DownloadRequest;
import org.lunarray.lshare.protocol.state.userlist.User;

// TODO slot limit
public class UploadManager {

	public final static int BEGIN_PORT = 7401;
	public final static int END_PORT = 7500;
	
	private ArrayList<UploadTransfer> uploads;
	private Controls controls;
	
	public UploadManager(Controls c) {
		uploads = new ArrayList<UploadTransfer>();
		controls = c;
	}
	
	protected void addTransfer(UploadTransfer t) {
		if (!uploads.contains(t)) {
			uploads.add(t);
		}
	}
	
	protected void removeTransfer(UploadTransfer t) {
		if (uploads.contains(t)) {
			uploads.remove(t);
		}
	}

	public void close() {
		for (UploadTransfer t: uploads) {
			t.close();
		}
	}
	
	public List<UploadTransfer> getUploads() {
		return uploads;
	}
	
	public void processRequest(User u, DownloadRequest f) {
		UploadHandler h = new UploadHandler(this, f, u);
		controls.getTasks().backgroundTask(h);
	}
}
