package edu.tue.compnet.gui;

import java.awt.event.*;
import java.util.ArrayList;

import javax.swing.*;

/**
 * The main menubar of the main frame. This will setup the menubar and allow
 * listeners to listen for menubar events.
 * @author Pal Hargitai
 * @author Siu-Hong Li
 */
public class MainMenu {
	// The main menubar that this is about
	JMenuBar menubar;
	// The listeners
	ArrayList<MenuListener> listeners;
	
	/**
	 * The constructor of the main menu bar.
	 * @param showup True to show uploads, false if not.
	 * @param hashes True to strictly check hashes.
	 */
	public MainMenu(boolean showup, boolean hashes) {
		listeners = new ArrayList<MenuListener>();
		/*
		 * Setup the file
		 */
		menubar = new JMenuBar();
		// file
		JMenu file = new JMenu("File");
		// change name
		JMenuItem change = new JMenuItem("Change Nickname");
		change.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				for (MenuListener l: listeners) {
					l.changeNickname();
				}
			}
		});
		file.add(change);
		//quit
		JMenuItem quit = new JMenuItem("Quit");
		quit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				for (MenuListener l: listeners) {
					l.quit();
				}
			}
		});
		file.add(quit);
		// fin file
		menubar.add(file);
		
		JMenu edit = new JMenu("Edit");
		// incoming folder
		JMenuItem incoming = new JMenuItem("Change Incoming Folder");
		incoming.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				for (MenuListener l: listeners) {
					l.changeIncomingFolder();
				}
			}
		});
		edit.add(incoming);
		// share folder
		JMenuItem share = new JMenuItem("Change Sharing Folder");
		share.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				for (MenuListener l: listeners) {
					l.changeShareFolder();
				}
			}
		});
		edit.add(share);
		// search
		JMenuItem search = new JMenuItem("Search for filename");
		search.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				for (MenuListener l: listeners) {
					l.search(false);
				}
			}
		});
		edit.add(search);
		JMenuItem searchfilter = new JMenuItem("Search for filename " +
				"(Filtered)");
		searchfilter.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				for (MenuListener l: listeners) {
					l.search(true);
				}
			}
		});
		edit.add(searchfilter);
		edit.addSeparator();
		// Show uploads
		JCheckBoxMenuItem douploads = new JCheckBoxMenuItem("Display uploads");
		douploads.setState(showup);
		douploads.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JCheckBoxMenuItem j = (JCheckBoxMenuItem)arg0.getSource();
				for (MenuListener l: listeners) {
					l.showUploads(j.getState());
				}				
			}
		});
		edit.add(douploads);
		// Allow empty hashes
		JCheckBoxMenuItem checkhashes = new JCheckBoxMenuItem("Allows unhashed downloads");
		checkhashes.setState(hashes);
		checkhashes.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JCheckBoxMenuItem j = (JCheckBoxMenuItem)arg0.getSource();
				for (MenuListener l: listeners) {
					l.showUploads(j.getState());
				}				
			}
		});
		edit.add(checkhashes);
		//fin edit
		menubar.add(edit);
		
	}
	
	/**
	 * Get the menubar, filled and well.
	 * @return The menubar
	 */
	public JMenuBar getMenuBar() {
		return menubar;
	}

	/**
	 * Adds a listener that listenes to menu events.
	 * @param lis Adds the listener.
	 */
	public void addListener(MenuListener lis) {
		listeners.add(lis);
	}
	
	/**
	 * Removes a listener.
	 * @param lis The listener to be removed.
	 */
	public void removeListener(MenuListener lis) {
		listeners.remove(lis);
	}
}
