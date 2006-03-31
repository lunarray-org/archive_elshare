package org.lunarray.lshare.gui;

import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeSelectionModel;
import javax.swing.tree.TreeSelectionModel;

import org.lunarray.lshare.LShare;
import org.lunarray.lshare.gui.contactlist.Model;
import org.lunarray.lshare.gui.contactlist.Selecter;

public class ContactList {
	
	private LShare lshare;
	private JTree panel;
	private JScrollPane scroller;
	private Model model;

	public ContactList(LShare ls) {
		lshare = ls;
		model = new Model(lshare);
		panel = new JTree(model);
		scroller = new JScrollPane(panel);
		
		scroller.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		
		panel.setRootVisible(false);
		for (int i = 0; i < panel.getRowCount(); i++) {
			panel.expandRow(i);
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
		return scroller;
	}
}
