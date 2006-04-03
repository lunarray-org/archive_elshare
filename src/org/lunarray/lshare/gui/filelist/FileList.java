package org.lunarray.lshare.gui.filelist;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;

import org.lunarray.lshare.LShare;
import org.lunarray.lshare.gui.GUIFrame;
import org.lunarray.lshare.gui.MainGUI;
import org.lunarray.lshare.protocol.state.userlist.User;

import com.sun.swing.JTreeTable;

/**
 * Shows a filelist of a specific user. Allows browsing throught that file
 * list.
 * @author Pal Hargitai
 */
public class FileList extends GUIFrame {

	/**
	 * The user whose filelist is displayed.
	 */
	private User user;
	
	/**
	 * The list model that allows showing of the file list.
	 */
	private ListModel model;

	/**
	 * Constructs a filelist window.
	 * @param ls The instance of the protocol that is to be used.
	 * @param u The user whose filelist is to be displayed.
	 * @param mg The main user interface that this interface is to be shown on.
	 */
	public FileList(LShare ls, User u, MainGUI mg) {
		super(mg);
		
		// Setup model
		user = u;
		model = new ListModel(ls.getUserList(), user);
		
		// Setup table
		JTable t = new JTreeTable(model);
		t.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		JScrollPane table = new JScrollPane(t);
		
		// Setup frame
		frame.setTitle(getTitle());
		frame.getContentPane().add(table);
	}
	
	@Override
	/**
	 * Closes the list and disposes the frame.
	 */
	public void close() {
		Object o = model.getRoot();
		if (o.getClass().equals(ListNode.class)) {
			ListNode n = (ListNode)o;
			n.getEntry().closeReceiver();
		}
		frame.dispose();
	}
	
	/**
	 * Gets the title that the frame is to be set to.
	 * @return The title of the frame.
	 */
	public String getTitle() {
		return user.getName() + " (" + user.getHostname() + ")";
	}
}
