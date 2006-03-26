package org.lunarray.lshare.gui;

import javax.swing.JComponent;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeSelectionModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import org.lunarray.lshare.LShare;
import org.lunarray.lshare.gui.contactlist.Model;
import org.lunarray.lshare.gui.contactlist.Selecter;

public class ContactList {
	
	private LShare lshare;
	private JTree panel;
	//private DefaultTreeModel model;
	private Model model;

	public ContactList(LShare ls) {
		lshare = ls;
		model = new Model(lshare);
		panel = new JTree(model);
		panel.setRootVisible(false);
		for (int i = 0; i < model.getRoot().getChildCount(); i++) {
			Object[] p = {model.getRoot(), model.getRoot().getChildAt(i)};
			panel.expandPath(new TreePath(p));
		}
		
		panel.setSelectionModel(new DefaultTreeSelectionModel());
		panel.getSelectionModel().setSelectionMode(TreeSelectionModel.
				SINGLE_TREE_SELECTION);
		panel.getSelectionModel().addTreeSelectionListener(new Selecter(panel, 
				model));
	}
	
	public String getTitle() {
		return "Contact List";
	}

	public JComponent getPanel() {
		return panel;
	}
}
