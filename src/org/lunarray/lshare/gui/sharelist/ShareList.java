package org.lunarray.lshare.gui.sharelist;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;

import org.lunarray.lshare.LShare;
import org.lunarray.lshare.gui.GUIFrame;
import org.lunarray.lshare.gui.MainGUI;

/**
 * Shows a list of currently shared directories.
 * @author Pal Hargitai
 */
public class ShareList extends GUIFrame implements ActionListener {

	/**
	 * The textfield for setting the name of the share.
	 */
	private JTextField name;
	
	/**
	 * The textfield for setting the location of the share.
	 */
	private JTextField loc;
	
	/**
	 * The instance of the protocol to communicate with.
	 */
	private LShare lshare;
	
	/**
	 * The table model representing all known shares.
	 */
	private ShareTable model;
	
	/**
	 * The selection listener for selecting a share for removal.
	 */
	private ShareListener slis;

	/**
	 * Constructs a sharelist frame.
	 * @param ls The instance of the protocol to assocate with.
	 * @param mg The window this frame resides in.
	 */
	public ShareList(LShare ls, MainGUI mg) {
		super(mg);
		// Setup Panel
		lshare = ls;
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		
		JPanel info = new JPanel();
		info.setLayout(new GridBagLayout());
		
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 0;
		gbc.gridwidth = 1;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		info.add(new JLabel("Name:"), gbc);
		
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.weightx = 0;
		gbc.gridwidth = 1;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		info.add(new JLabel("Location:"), gbc);
		
		gbc.gridx = 1;
		gbc.gridy = 0;
		gbc.weightx = 1;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		name = new JTextField();
		info.add(name, gbc);
		
		gbc.gridx = 1;
		gbc.gridy = 1;
		gbc.weightx = 1;
		gbc.gridwidth = 1;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		loc = new JTextField();
		info.add(loc, gbc);
		
		gbc.gridx = 2;
		gbc.gridy = 1;
		gbc.weightx = 0;
		gbc.gridwidth = 1;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		JButton floc = new JButton("Browse");
		floc.setActionCommand("browse");
		floc.addActionListener(this);
		info.add(floc, gbc);
		
		gbc.gridx = 0;
		gbc.gridy = 2;
		gbc.weightx = 0;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		JButton add = new JButton("Add Directory");
		add.setActionCommand("add");
		add.addActionListener(this);
		info.add(add, gbc);
		panel.add(info, BorderLayout.NORTH);
		
		// Setup model and table
		model = new ShareTable(lshare);
		JTable shares = new JTable();
		slis = new ShareListener(model, lshare);
		shares.setModel(model);
		shares.getSelectionModel().addListSelectionListener(slis);
		shares.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		JScrollPane sp = new JScrollPane(shares);
		panel.add(sp, BorderLayout.CENTER);
		
		JButton rem = new JButton("Remove Selected");
		rem.setActionCommand("remove");
		rem.addActionListener(this);
		
		panel.add(rem, BorderLayout.SOUTH);

		// Setup the frame
		frame.add(panel);
		frame.setTitle(getTitle());
	}
	
	/**
	 * The action listener to respond to the button presses.
	 * @param arg0 The event that triggered this.
	 */
	public void actionPerformed(ActionEvent arg0) {
		String ac = arg0.getActionCommand();
		if (ac.equals("add")) {
			if (loc.getText() != null) {
				File f = new File(loc.getText());
				if (f.exists() && f.isDirectory() && !f.isHidden()) {
					lshare.getShareList().addShare(name.getText(), f);
					model.refresh();
				}
			}
		} else if (ac.equals("browse")) {
			JFileChooser fc = new JFileChooser();
			fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			int res = fc.showDialog(frame, "Select Share Folder");
			if (res == JFileChooser.APPROVE_OPTION) {
				loc.setText(fc.getSelectedFile().getPath());
			}
		} else if (ac.equals("remove")) {
			slis.removeSelected();
		}
	}
	
	@Override
	/**
	 * Hides the frame.
	 */
	public void close() {
		frame.setVisible(false);
	}
	
	/**
	 * Gets the title of the frame.
	 * @return The title of the frame.
	 */
	public String getTitle() {
		return "Shared Directories";
	}
}
