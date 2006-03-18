package edu.tue.compnet.gui;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.*;
import java.util.Timer;

import javax.swing.*;

import edu.tue.compnet.Backend;
import edu.tue.compnet.events.*;
import edu.tue.compnet.protocol.state.HashList;

/**
 * This class will hold the main frame, it will also manage this main frame.
 * @author Pal Hargitai
 * @author Siu-Hong Li
 */
public class MainFrame {
	/** The string of the show uploads */
	public static String SETTINGS_SHOW_UPLOADS = "showuploads";
	
	// Backend
	Backend backend;
	// The main panel
	JFrame main;
	// The main menu
	MainMenu menu;
	// The tabbed pane
	JTabbedPane tabs;
	// The contact list
	ContactList contacts;
	// The file chooser
	JFileChooser chooser;
	// Active searches
	ArrayList<SearchPane> searches;
	// The file transfers
	ArrayList<FiletransferWindow> filetransfers;
	// Timed updates
	Timer utimer;
	// Show uploads
	boolean showuploads;
	
	/**
	 * The constructor of the main frame, will set up most things here.
	 * @param b The backend it will connect to.
	 */
	public MainFrame(Backend b) {
		// Set the backend
		backend = b;
		// Set the timer
		utimer = new Timer();
		// Searches
		searches = new ArrayList<SearchPane>();
		// File transfers
		filetransfers = new ArrayList<FiletransferWindow>();
		// Set the main frame
		main = new JFrame("Computer networks");
		main.addWindowListener(new WindowListener() {
			public void windowActivated(WindowEvent e) {}
			public void windowOpened(WindowEvent e) {}
			public void windowDeactivated(WindowEvent e) {}
			public void windowDeiconified(WindowEvent e) {}
			public void windowIconified(WindowEvent e) {}
			public void windowClosed(WindowEvent e) {}
			public void windowClosing(WindowEvent e) {
				backend.quit();
				System.exit(0);
			}
		});
		// init the chooser
		chooser = new JFileChooser();
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		
		/*
		 * The frame will have a tabbed pane, these tabs will allow for some
		 * basic controlls. The main tab will not be closable and will contain
		 * a list of users. Additionally, there will be a menu bar with
		 * different actions.
		 */
		// Set layout
		main.getContentPane().setLayout(new BorderLayout());
		// Do the menu bar
		showuploads = backend.getState().getSettings().getBool("/",
				SETTINGS_SHOW_UPLOADS, false);
		menu = new MainMenu(showuploads, backend.getState().
				getStateSettings().getCheckHashes());
		menu.addListener(new MenuListen(this));
		main.getContentPane().add(menu.getMenuBar(), BorderLayout.NORTH);
		// Add the tabbed pane
		tabs = new JTabbedPane();
		main.getContentPane().add(tabs);
		// Add the contact list
		contacts = new ContactList(backend, this);
		tabs.add("Contacts", contacts.getTree());
		
		/**
		 * Add a listener that will handle notices and questions.
		 */
		MainListener c = new MainListener(this);
		// The updating thread
		utimer.scheduleAtFixedRate(new TimerTask() {
			// update
			public void run() {
				for (FiletransferWindow ft: filetransfers) {
					ft.update();
				}
			}
		}, 0, 1000);
		backend.getState().getQuery().addQueryListener(c);
		backend.getState().getFiletransferList().addTransferListener(c);
	}
	
	/**
	 * Remove a filetransfer window from the tabs.
	 * @param ft The filetransfer to remove.
	 */
	public void removeFiletransfer(FiletransferWindow ft) {
		tabs.remove(ft.getPanel());
		filetransfers.remove(ft);
	}
	
	/**
	 * Gets the main frame. 
	 * @return The frame.
	 */
	public JFrame getFrame() {
		return main;
	}

	/**
	 * A class to hold most menu listen information. Neatly
	 * @author Pal Hargitai
	 * @author Siu-Hong Li
	 */
	private class MenuListen implements MenuListener {
		// The main frame
		MainFrame main;
		
		public MenuListen(MainFrame m) {
			main = m;
		}
		
		/**
		 * Quit the application.
		 */
		public void quit() {
			backend.quit();
			System.exit(0);
		}
		
		/**
		 * Change the nickname.
		 */
		public void changeNickname() {
			String newnick = JOptionPane.showInputDialog(main.getFrame(),
					"Change nickname", backend.getState().getStateSettings().
					getNickname());
			if (newnick != null) {
				if (newnick.length() > 0) {
					backend.getState().getStateSettings().setNickname(newnick);
				}
			}
		}
		
		/**
		 * Change Incoming folder
		 */
		public void changeIncomingFolder() {
			if (backend.getState().getStateSettings().getIncomingDirectory()
					!= null) {
				chooser.setCurrentDirectory(backend.getState().
						getStateSettings().getIncomingDirectory());
			}
			int ret = chooser.showSaveDialog(main.getFrame());
			if (ret == JFileChooser.APPROVE_OPTION) {
				File chosen = chooser.getSelectedFile();
				if (chosen.isDirectory() && chosen.canWrite()) {
					backend.getState().getStateSettings().
					setIncomingDirectory(chosen);
					JOptionPane.showMessageDialog(main.getFrame(),
							"Incoming directory set to: " +	chosen.
							toString());
				}
			}
		}
		
		/**
		 * Change Share folder
		 */
		public void changeShareFolder() {
			if (backend.getState().getStateSettings().getShareDirectory() !=
					null) {
				chooser.setCurrentDirectory(backend.getState().
						getStateSettings().getShareDirectory());
			}
			int ret = chooser.showOpenDialog(main.getFrame());
			if (ret == JFileChooser.APPROVE_OPTION) {
				File chosen = chooser.getSelectedFile();
				if (chosen.isDirectory() && chosen.canRead()) {
					backend.getState().getStateSettings().setShareDirectory(
							chosen);
					JOptionPane.showMessageDialog(main.getFrame(),
							"Share directory set to: " + chosen.toString());
				}
			}
		}
		
		/**
		 * Start a search for a filename
		 */
		public void search(boolean filter) {
			String searchs = JOptionPane.showInputDialog(main.getFrame(),
					"Search for");
			if (searchs != null) {
				if (searchs.length() > 0) {
					SearchFilter fil;
					if (filter) {
						fil = new BuddyFilter(searchs, backend.getState());
					} else {
						fil = new RegularFilter(searchs);
					}
					SearchPane spane = new SearchPane(fil, main, backend);
					searches.add(spane);
					tabs.add(spane.getPanel(), spane.getTitle());
					backend.getState().getTasks().searchFor(searchs,
							HashList.HASH_EMPTY);
				}
			}
		}
		
		/**
		 * Show uploads, or not
		 */
		public void showUploads(boolean yn) {
			showuploads = yn;
			backend.getState().getSettings().setBool("/",
					SETTINGS_SHOW_UPLOADS, showuploads);
		}
		
		/**
		 * To check hashes, or not
		 */
		public void checkHashes(boolean yn) {
			backend.getState().getStateSettings().setCheckHashes(yn);
		}
	}
	
	/**
	 * Open a search windows with a specifiek filter.
	 * @param f The filter for the search window.
	 */
	public void search(SearchFilter f) {
		SearchPane spane = new SearchPane(f, this, backend);
		searches.add(spane);
		tabs.add(spane.getPanel(), spane.getTitle());
	}
	
	/**
	 * Removes a search pane.
	 * @param s The search pane to be removed.
	 */
	public void removeSearch(SearchPane s) {
		tabs.remove(s.getPanel());
		searches.remove(s);
	}
	
	/**
	 * Show the userinterface.
	 */
	public void startGUI() {
		contacts.populateList();
		main.setMinimumSize(new Dimension(100,100));
		main.setMaximumSize(new Dimension(10000,10000));
		main.setPreferredSize(new Dimension(500,500));
		main.setVisible(true);
		main.pack();
	}

	/**
	 * This class is the main listener for the GUI. It handles most functions
	 * kicked here regarding notices and questions. 
	 * @author Pal Hargitai
	 * @author Siu-Hong Li
	 */
	private class MainListener implements QueryListener, TransferListener {
	 	// The frame
	 	MainFrame mframe;
	 	
	 	/**
	 	 * The constructor of the listener of this main frame.
	 	 * @param mf The frame.
	 	 */
	 	public MainListener(MainFrame mf) {
	 		mframe = mf;
	 	}
	 
		/**
		 * Ask a question.
		 */
		public boolean ask(edu.tue.compnet.events.Event e, String title) {
			int res = JOptionPane.showConfirmDialog(main, e.getMessage(),
					title, JOptionPane.YES_NO_OPTION);
			if (res == JOptionPane.YES_OPTION) {
				return true;
			}
			return false;
		}
		
		public Answer askForTristate(edu.tue.compnet.events.Event e,
				String title) {
			int res = JOptionPane.showConfirmDialog(main, e.getMessage(),
					title, JOptionPane.YES_NO_OPTION);
			if (res == JOptionPane.YES_OPTION) {
				return QueryListener.Answer.YES;
			} else {
				return QueryListener.Answer.NO;
			}
		}
		
		/**
		 * Give user a notice screen.
		 */
		public void notice(edu.tue.compnet.events.Event e) {
			JOptionPane.showMessageDialog(main, e.getMessage());
		}
		
		/**
		 * Handle a file transferring event
		 */
		public void registerDownload(FiletransferEvent e) {
			add: {
				// Search wether this transfer is already being done.
				for (FiletransferWindow w: filetransfers) {
					if (w.getTransfer() == e.getTransfer()) {
						break add;
					}
				}
				FiletransferWindow fw = new FiletransferWindow(mframe, true,
						e.getTransfer());
				filetransfers.add(fw);
				tabs.add(fw.getPanel(), fw.getTitle());
			}
		}
		
		/**
		 * Handle a file transferring event
		 */
		public void registerUpload(FiletransferEvent e) {
			if (showuploads) {
				FiletransferWindow fw = new FiletransferWindow(mframe ,	false,
						e.getTransfer());
				filetransfers.add(fw);
				tabs.add(fw.getPanel(), fw.getTitle());
			}
		}
	}
}
