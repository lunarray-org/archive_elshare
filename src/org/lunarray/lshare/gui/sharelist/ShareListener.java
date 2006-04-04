package org.lunarray.lshare.gui.sharelist;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.lunarray.lshare.LShare;

/**
 * The list selection listener to selection in the sharelist model.
 * @author Pal Hargitai
 */
public class ShareListener implements ListSelectionListener {
	
	/**
	 * The model that is associated with this listener.
	 */
	private ShareTable model;
	
	/**
	 * The name of the share to remove, if any.
	 */
	private String name;
	
	/**
	 * The instance of the protocol to communicate with.
	 */
	private LShare lshare;
	
	/**
	 * Constructs a share listener.
	 * @param st The table model to get data from.
	 * @param ls The instance of the procotol to use.
	 */
	public ShareListener(ShareTable st, LShare ls) {
		model = st;
		name = "";
		lshare = ls;
	}
	
	/**
	 * A selection has occured.
	 * @param arg0 The selection event.
	 */
	public void valueChanged(ListSelectionEvent arg0) {
		name = model.getNameAtRow(arg0.getFirstIndex());
	}
	
	/**
	 * Get the name of the share of the selected row.
	 * @return
	 */
	public String getSelectedName() {
		return name;
	}
	
	/**
	 * Remove the selected row.
	 */
	public void removeSelected() {
		if (name.length() > 0) {
			lshare.getShareList().removeShare(name);
			model.refresh();
		}
	}
}
