package edu.tue.compnet.gui;

import java.awt.BorderLayout;
import java.awt.event.*;
import java.net.InetAddress;
import java.util.*;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.AbstractTableModel;

import edu.tue.compnet.Backend;
import edu.tue.compnet.events.*;
import edu.tue.compnet.protocol.state.HashList;

/** 
 * A search pane.
 * @author Pal Hargitai
 * @author Siu-Hong Li
 */
public class SearchPane {
	// This search string this was created for.
	SearchFilter filter;
	// The panel that displays the info.
	JPanel panel;
	// The backend
	Backend backend;
	// The table
	JTable table;
	// The table model
	SearchModel model;
	// The search popup menu
	SearchPopup popup;
	// The main frame
	MainFrame main;
	
	/**
	 * The constructor for the search pane.
	 * @param s The search filter.
	 * @param m The main frame in which it will reside.
	 * @param b The backend.
	 */
	public SearchPane(SearchFilter s, MainFrame m, Backend b) {
		main = m;
		backend = b;
		filter = s;
		panel = new JPanel(new BorderLayout());
		// Add a button to close the panel.
		JButton button = new JButton("Close");
		button.addActionListener(new ButtonListener(this));
		panel.add(button, BorderLayout.SOUTH);
		// Build the model.
		model = new SearchModel();
		table = new JTable(model);
		JScrollPane sp = new JScrollPane(table);
		// Set the selection model
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		DefaultListSelectionModel lsm = new DefaultListSelectionModel();
		lsm.addListSelectionListener(model);
		table.setSelectionModel(lsm);
		// Add to panel
		panel.add(sp, BorderLayout.CENTER);
		// Add a popup menu
		popup = new SearchPopup();
		table.setComponentPopupMenu(popup.getMenu());
		// Add the listener
		backend.getState().getSearchList().addSearchListener(new BackListen(
				model));
	}
	
	/**
	 * This is merely a container to neatly contain a search result. This
	 * should not be used a lot since public access to fields is not
	 * advisable.
	 * @author Pal Hargitai
	 * @author Siu-Hong Li
	 */
	private class Result {
		// The file name, size and date
		public String filename;
		public int size;
		public int date;
		// The user data
		public String username;
		public InetAddress address;		
		// The hash
		public byte[] hash;
	}

	/**
	 * The search popup menu class. Handles the popup menu.
	 * @author Pal Hargitai
	 * @author Siu-Hong Li
	 */
	private class SearchPopup {
		// The menu		
		JPopupMenu menu;
		
		
		/**
		 * The constructor for the popup menu.
		 */
		public SearchPopup() {
			menu = new JPopupMenu();
			menu.setLabel("No selection made");
		}
		
		/**
		 * Get the popup menu.
		 * @return The menu.
		 */
		public JPopupMenu getMenu() {
			return menu;
		}
		
		/**
		 * Download a file.
		 * @param a The action listener.
		 * @param b Action listener for filename
		 * @param c Action listener for hash
		 * @param title The title.
		 */
		public void setDownload(ActionListener a, ActionListener b,
				ActionListener c, String[] title) {
			menu.removeAll();
			for (String t: title) {
				menu.add(t);
			}
			// Download
			JMenuItem me = new JMenuItem("Download");
			menu.add(me);
			me.addActionListener(a);
			menu.addSeparator();
			// More searching
			JMenuItem mf = new JMenuItem("Search for filename");
			menu.add(mf);
			mf.addActionListener(b);
			JMenuItem mg = new JMenuItem("Search for hash");
			menu.add(mg);
			mg.addActionListener(c);		
		}
	}
	
	/**
	 * This is a table model to implement showing results and handling the
	 * selection in a neat way.
	 * @author Pal Hargitai
	 * @author Siu-Hong Li
	 */
	private class SearchModel extends AbstractTableModel 
	implements ListSelectionListener {
		/**
		 * The serial UID, for whatever it is..
		 */
		private static final long serialVersionUID = -5920893101340228376L;
		// The rows
		ArrayList<Result> results;
		// The selected row
		int selected;
		
		/**
		 * The constructor for a search model
		 */
		public SearchModel() {
			results = new ArrayList<Result>();
			selected = -1;
		}
		
		/**
		 * Get the name of a column, if invalid, just return something empty.
		 * @param i The column id.
		 * @return The column name.
		 */
		public String getColumnName(int i) {
			switch (i) {
			case 0:
				return "Filename";
			case 1:
				return "Filesize";
			case 2:
				return "Date of modification";
			case 3:
				return "Username";
			case 4:
				return "User address";
			case 5:
				return HashList.HASH_ALGO + " Hash";
			default:
				return "";
			}
		}
		
		/**
		 * Add a data row.
		 * @param r The data to add
		 */
		public void addData(Result r) {
			add: {
				// Search for similar
				for (Result re: results) {
					if (r.address.equals(re.address)) {
						if (r.filename.equals(re.filename)) {
							// We found similar, update
							if (r.date > re.date) {
								re.date = r.date;
							}
							if (r.size > re.size) {
								re.size = r.size;
							}
							if (HashList.equals(HashList.HASH_EMPTY,
									re.hash)) {
								re.hash = r.hash;
							}
							// We break out of adding
							break add;
						}
					}
				}
				// Update / add
				results.add(r);
			}
			fireTableDataChanged();
		}
		
		/**
		 * Asks wether a cell is editable.
		 * @param arg0 The row.
		 * @param arg1 The column.
		 * @return By default false, we don't allow editing.
		 */
		public boolean isCellEditable(int arg0, int arg1) {
			return false;
		}
		
		/**
		 * The column count, ie. 5.
		 * @return The amount of columns.
		 */
		public int getColumnCount() {
			return 6;
		}
		
		/**
		 * Get the amount of rows.
		 * @return The amount of rows.
		 */
		public int getRowCount() {
			return results.size();
		}
		
		/**
		 * Get the value at a place.
		 * @param arg0 The row.
		 * @param arg1 The column
		 * @return The string at a given row and column.
		 */
		public String getValueAt(int arg0, int arg1) {
			Result r = results.get(arg0);
			switch (arg1) {
			case 0:
				return r.filename;
			case 1:
				if (r.size < 1) {
					return "";
				} else {
					return Integer.valueOf(r.size).toString();
				}
			case 2:
				if (r.date < 1) {
					return "";
				} else {
					Date d = new Date((long)r.date * 1000);
					return d.toString();
				}
			case 3:
				return r.username;
			case 4:
				return r.address.getHostName();
			case 5:
				return HashList.toString(r.hash);
			default:
				return "";
			}
		}
		
		/**
		 * A selection change event, just select the first one.
		 */
		public void valueChanged(ListSelectionEvent arg0) {
			if (selected != arg0.getFirstIndex()) {
				// Updated popup menu
				int i = arg0.getFirstIndex();
				if (i >= 0 && i < results.size()) {
					Result res = results.get(i);
					String[] t = {"File: " + res.filename, "From: " + res.
							username};
					popup.setDownload(new ActionListener() {
						public void actionPerformed(ActionEvent arg0) {
							if (selected >= 0) {
								Result res = results.get(selected);
								backend.getState().getTasks().requestFile(res.
										filename, res.address, res.size);
							}
						}
					}, new ActionListener() {
						public void actionPerformed(ActionEvent arg0) {
							if (selected >= 0) {
								Result res = results.get(selected);
								main.search(new StrictFilter(res.filename));
								backend.getState().getTasks().searchFor(res.
										filename, HashList.HASH_EMPTY);
							}
						}
					}, new ActionListener() {
						public void actionPerformed(ActionEvent arg0) {
							if (selected >= 0) {
								Result res = results.get(selected);
								main.search(new HashFilter(res.hash));
								backend.getState().getTasks().searchFor(res.
										filename, res.hash);
							}
						}
					},t);
				}
			}
			selected = arg0.getFirstIndex();
		}
	}
	
	/**
	 * This class is the backend listener.
	 * @author Pal Hargitai
	 * @author Siu-Hong Li
	 */
	private class BackListen implements SearchListener {
		// The table model
		SearchModel model;
		
		/**
		 * The constructor of the backend listener, primarilly handles search
		 * events.
		 * @param m The table model.
		 */
		public BackListen(SearchModel m) {
			model = m;
		}
		
		/**
		 * Process result
		 */
		public void searchResult(SearchEvent e) {
			if (filter.isAllowed(e)) {
				Result res = new Result();
				res.filename = e.getName();
				res.size = e.getSize();
				res.date = e.getDate();
				res.username = e.getUserName();
				res.address = e.getAddress();
				res.hash = e.getHash();
				model.addData(res);
				table.updateUI();
			}
		}
	}
	
	/**
	 * Implements a listener for the close button.
	 * @author Pal Hargitai
	 * @author Siu-Hong Li
	 */
	private class ButtonListener implements ActionListener {
		// The search panel
		SearchPane search;
		
		/**
		 * The constructor of a button listener.
		 * @param m The main frame.
		 * @param s The search pane.
		 */
		public ButtonListener(SearchPane s) {
			search = s;
		}
		
		/**
		 * Removes this from the search
		 */
		public void actionPerformed(ActionEvent arg0) {
			main.removeSearch(search);
		}
	}
	
	/**
	 * Gets the panel associated.
	 * @return The panel.
	 */
	public JPanel getPanel() {
		return panel;
	}

	/**
	 * Gets the title of this pane, used when inserting the tab.
	 * @return The title of this pane.
	 */
	public String getTitle() {
		return filter.getName();
	}
}
