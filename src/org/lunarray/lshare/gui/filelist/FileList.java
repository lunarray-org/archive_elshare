package org.lunarray.lshare.gui.filelist;

import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;

import org.lunarray.lshare.LShare;
import org.lunarray.lshare.gui.GUIFrame;
import org.lunarray.lshare.protocol.state.userlist.User;

import com.sun.swing.JTreeTable;

public class FileList extends GUIFrame {

	private LShare lshare;
	private User user;
	private ListModel model;
	private JComponent table;
	
	public FileList(LShare ls, User u) {
		super();
		
		lshare = ls;
		user = u;
		model = new ListModel(lshare.getUserList(), user);
		
		JTable t = new JTreeTable(model);
		t.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table = new JScrollPane(t);
		
		frame.addInternalFrameListener(new InternalFrameListener() {
			public void internalFrameActivated(InternalFrameEvent arg0) {}
			public void internalFrameClosed(InternalFrameEvent arg0) {}
			public void internalFrameClosing(InternalFrameEvent arg0) {
				Object o = model.getRoot();
				if (o.getClass().equals(ListNode.class)) {
					ListNode n = (ListNode)o;
					n.getEntry().closeReceiver();
				}
			}
			public void internalFrameDeactivated(InternalFrameEvent arg0) {}
			public void internalFrameDeiconified(InternalFrameEvent arg0) {}
			public void internalFrameIconified(InternalFrameEvent arg0) {}
			public void internalFrameOpened(InternalFrameEvent arg0) {}
		});
		
		frame.setTitle(getTitle());
		frame.getContentPane().add(table);
	}
	
	public String getTitle() {
		return user.getName() + " (" + user.getHostname() + ")";
	}
}
