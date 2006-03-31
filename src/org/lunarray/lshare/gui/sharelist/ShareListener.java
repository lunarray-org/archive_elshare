package org.lunarray.lshare.gui.sharelist;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.lunarray.lshare.LShare;

public class ShareListener implements ListSelectionListener {
	
	private ShareTable model;
	private String name;
	private LShare lshare;
	
	public ShareListener(ShareTable st, LShare ls) {
		model = st;
		name = "";
		lshare = ls;
	}
	
	public void valueChanged(ListSelectionEvent arg0) {
		name = model.getNameAtRow(arg0.getFirstIndex());
	}
	
	public String getSelectedName() {
		return name;
	}
	
	public void removeSelected() {
		if (name.length() > 0) {
			lshare.getShareList().removeShare(name);
			model.refresh();
		}
	}
}
