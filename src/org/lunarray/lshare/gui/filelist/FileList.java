package org.lunarray.lshare.gui.filelist;

import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;

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
		
		frame.setTitle(getTitle());
		frame.getContentPane().add(table);
	}
	
	@Override
	public void close() {
		Object o = model.getRoot();
		if (o.getClass().equals(ListNode.class)) {
			ListNode n = (ListNode)o;
			n.getEntry().closeReceiver();
		}
	}
	
	public String getTitle() {
		return user.getName() + " (" + user.getHostname() + ")";
	}
}
