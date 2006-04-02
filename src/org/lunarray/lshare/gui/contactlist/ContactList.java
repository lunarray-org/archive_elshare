package org.lunarray.lshare.gui.contactlist;

import javax.swing.JInternalFrame;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeSelectionModel;
import javax.swing.tree.TreeSelectionModel;

import org.lunarray.lshare.LShare;
import org.lunarray.lshare.gui.GUIFrame;
import org.lunarray.lshare.gui.MainGUI;

public class ContactList extends GUIFrame {
	
	private LShare lshare;
	private JTree panel;
	private JScrollPane scroller;
	private Model model;

	public ContactList(LShare ls, MainGUI mg) {
		super();
		
		frame.setTitle(getTitle());		
		
		lshare = ls;
		model = new Model(lshare, mg);
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
		
		frame.getContentPane().add(scroller);
		frame.setDefaultCloseOperation(JInternalFrame.HIDE_ON_CLOSE);
	}
	
	public String getTitle() {
		return "Contact List";
	}
}
