package org.lunarray.lshare.gui;

import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;

import org.lunarray.lshare.LShare;
import org.lunarray.lshare.gui.filelist.ListModel;
import org.lunarray.lshare.protocol.state.userlist.User;

import com.sun.swing.JTreeTable;

public class FileList {

	private LShare lshare;
	private User user;
	private ListModel model;
	private JComponent table;
	
	public FileList(LShare ls, User u) {
		lshare = ls;
		user = u;
		model = new ListModel(lshare.getUserList(), user);
		
		JTable t = new JTreeTable(model);
		t.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table = new JScrollPane(t);
	}

	public JComponent getPanel() {
		return table;
	}
	
	public String getTitle() {
		return user.getName() + " (" + user.getHostname() + ")";
	}
}
