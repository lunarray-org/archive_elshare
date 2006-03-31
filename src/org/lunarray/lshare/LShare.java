package org.lunarray.lshare;

import org.lunarray.lshare.protocol.Controls;
import org.lunarray.lshare.protocol.ExternalSettings;
import org.lunarray.lshare.protocol.state.sharing.ExternalShareList;
import org.lunarray.lshare.protocol.state.userlist.ExternalUserList;

public class LShare {
	
	private Controls controls;

	public LShare() {
		
		// Init protocol
		controls = new Controls();
		//craete TCPtransport
		
		/*
		 * thread: stack together network, create tasks
		 * thread: task handler processes those
		 * thread: maintain file list
		 */
	}
	
	public ExternalUserList getUserList() {
		return controls.getState().getUserList();
	}
	
	public ExternalSettings getSettings() {
		return controls.getSettings();
	}
	
	public ExternalShareList getShareList() {
		return controls.getState().getShareList();
	}
	
	public void start() {
		controls.start();
	}
	
	public void stop() {
		controls.stop();
		//controls.getSettings().commit();
	}
}
