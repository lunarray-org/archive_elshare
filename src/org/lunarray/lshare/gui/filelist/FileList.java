package org.lunarray.lshare.gui.filelist;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.ListSelectionModel;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;

import org.lunarray.lshare.LShare;
import org.lunarray.lshare.gui.GUIFrame;
import org.lunarray.lshare.gui.MainGUI;
import org.lunarray.lshare.protocol.state.userlist.User;

import com.sun.swing.JTreeTable;
import com.sun.swing.JTreeTable.TreeTableCellRenderer;

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
	 * The table used.
	 */
	private JTable table;
	
	private JTree tree;
	private LShare lshare;
	
	/**
	 * Constructs a filelist window.
	 * @param ls The instance of the protocol that is to be used.
	 * @param u The user whose filelist is to be displayed.
	 * @param mg The main user interface that this interface is to be shown on.
	 */
	public FileList(LShare ls, User u, MainGUI mg) {
		super(mg);
		
		lshare = ls;
		
		// Setup model
		user = u;
		model = new ListModel(ls.getUserList(), user, this);
		
		// Setup table
		table = new JTreeTable(model);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		JScrollPane t = new JScrollPane(table);
		
		tree = (TreeTableCellRenderer)table.getCellRenderer(0,0);
		tree.addTreeSelectionListener(new TreeSelectionListener() {
			public void valueChanged(TreeSelectionEvent arg0) {
				if (arg0.getSource() != null) {
					ListNode n = (ListNode)arg0.getPath().getLastPathComponent();
					lshare.getDownloadManager().enqueue(n.getEntry(), user);
				}
			}
		});
		
		// Setup frame
		frame.setTitle(getTitle());
		frame.getContentPane().add(t);
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
	
	/**
	 * The model has been updated, notify table.
	 */
	protected void updatedModel() {
		table.tableChanged(new TableModelEvent(table.getModel()));
	}
}
