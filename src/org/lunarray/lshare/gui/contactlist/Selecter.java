package org.lunarray.lshare.gui.contactlist;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;

public class Selecter implements TreeSelectionListener {
	
	private JTree panel;
	private Model model;
	private UserNode selected;
	
	public Selecter(JTree t, Model m) {
		panel = t;
		model = m;
	}

	public void valueChanged(TreeSelectionEvent arg0) {
		if (panel.getSelectionCount() > 0) {
			if (!arg0.getPath().getLastPathComponent().getClass().
					equals(UserNode.class)) {
				panel.getSelectionModel().clearSelection();
			} else {
				selected = (UserNode) arg0.getPath().
						getLastPathComponent();
				// Set menu
				JPopupMenu m = new JPopupMenu();
				m.add("Username: " + selected.getUser().getName());
				m.add("Address: " + selected.getUser().getHostname());
				m.addSeparator();
				JMenuItem ab;
				if (!selected.getUser().isBuddy()) {
					ab = new JMenuItem("Add to buddy list");
				} else {
					ab = new JMenuItem("Remove from buddy list");
				}
				ab.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						if (selected != null) {
							model.toggleBuddy(selected);
						}
					}
				});
				m.add(ab);
				// TODO add: Show Users Filelist
				panel.setComponentPopupMenu(m);
			}
		} else {
			panel.setComponentPopupMenu(null);
		}
	}

}
