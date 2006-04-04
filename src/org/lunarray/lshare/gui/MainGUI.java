package org.lunarray.lshare.gui;

import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import org.lunarray.lshare.LShare;
import org.lunarray.lshare.gui.contactlist.ContactList;
import org.lunarray.lshare.gui.filelist.FileList;
import org.lunarray.lshare.gui.main.ShowFrameMenu;
import org.lunarray.lshare.gui.search.SearchFilter;
import org.lunarray.lshare.gui.search.SearchList;
import org.lunarray.lshare.gui.search.StringFilter;
import org.lunarray.lshare.gui.sharelist.ShareList;
import org.lunarray.lshare.protocol.state.userlist.User;

/**
 * The main user interface.
 * @author Pal Hargitai
 */
public class MainGUI implements ActionListener {
	
	/**
	 * The amount of frame padding given for kicking back an internal frame
	 * when it goes beyond the bounds of the viewport of the desktop. This is
	 * {@value} pixels.
	 */
	public final static int FRAME_PAD = 30;
	
	/**
	 * The abstraction of the protocol.
	 */
	private LShare lshare;
	
	/**
	 * The main frame of the gui. 
	 */
	private JFrame frame;
	
	/**
	 * The main menubar of the gui.
	 */
	private JMenuBar menu;
	
	/**
	 * The desktop pane that contains all internal frames.
	 */
	private JDesktopPane desktop;
	
	/**
	 * The window menu that gets updated when a frame comes or disappears.
	 */
	private JMenu winmenu;
	
	/**
	 * The contact list.
	 */
	private ContactList contactlist;
	
	/**
	 * The list of shared directories.
	 */
	private ShareList sharelist;

	/**
	 * Instanciates the main user interface.
	 * @param l The instance of the protocol that it may use.
	 */
	public MainGUI(LShare l) {
		frame = new JFrame();
		desktop = new JDesktopPane();
		lshare = l;
		// Close ops
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.setTitle("eLShare");
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent arg0) {
				stop();
			}
		});
		
		// The singleton windows.
		contactlist = new ContactList(lshare, this);
		contactlist.getFrame().setVisible(false);
		addFrame(contactlist);
		
		sharelist = new ShareList(lshare, this);
		sharelist.getFrame().setVisible(false);
		addFrame(sharelist);
		
		// Init of the menu bar.
		initMenu();
		frame.setJMenuBar(menu);
		frame.setContentPane(desktop);
		
		// Size setting
		desktop.setMinimumSize(new Dimension(340, 240));
		frame.setMinimumSize(new Dimension(340, 240));
		frame.setPreferredSize(new Dimension(640, 480));
		GraphicsEnvironment ge = GraphicsEnvironment.
				getLocalGraphicsEnvironment();
		frame.setMaximumSize(ge.getMaximumWindowBounds().getSize());
		frame.pack();
		
		/*
		 * When a window is resized, so is it's desktop. This allows the 
		 * desktop to be updated and the frames to stick in it.
		 */
		frame.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent arg0) {
				Dimension n = arg0.getComponent().getSize();
				Dimension f = frame.getMinimumSize();
				Dimension fd = new Dimension(n.width, n.height);
				if (n.height < f.height) {
					fd.height = f.height;
				}
				if (n.width < f.width) {
					fd.width = f.width;
				}
				frame.setSize(fd);
				
				updateDesktop(fd);
				
				super.componentResized(arg0);
			}
		});
	}
	
	/**
	 * Updates the window menu with all visible windows.
	 */
	public synchronized void updateMenu() {
		winmenu.removeAll();
		for (JInternalFrame i: desktop.getAllFrames()) {
			if (i.isVisible()) {
				winmenu.add(new ShowFrameMenu(i, desktop));
			}
		}
	}
	
	/**
	 * Initialises the menu.
	 */
	public void initMenu() {
		menu = new JMenuBar();
		// File
		JMenu filem = new JMenu("File");
		filem.add(addMenuItem("Search", "search"));
		filem.add(addMenuItem("Quit", "stop"));
		menu.add(filem);
		// Settings
		JMenu settingsm = new JMenu("Settings");
		settingsm.add(addMenuItem("Change Nickname", "nick"));
		settingsm.add(addMenuItem("Change e-Mail address", "challenge"));
		menu.add(settingsm);
		// Window
		JMenu windowm = new JMenu("Window");
		windowm.add(addMenuItem("Show contactlist", "contactlist"));
		windowm.add(addMenuItem("Show sharelist", "sharelist"));
		menu.add(windowm);
		// Winlist
		winmenu = new JMenu("View");
		menu.add(winmenu);
	}
	
	/**
	 * The main action handler for menu actions.
	 */
	public void actionPerformed(ActionEvent arg0) {
		String ac = arg0.getActionCommand();
		if (ac.equals("sharelist")) {
			// Show the sharelist
			addShareList();
		} else if (ac.equals("contactlist")) {
			// Show the contactlist
			addContactList();
		} else if (ac.equals("challenge")) {
			// Allow the user to enter a new challenge.
			String nn = JOptionPane.showInputDialog(desktop, "Please " +
					"enter a new e-Mail address: ", lshare.getSettings().
					getUsername(), JOptionPane.QUESTION_MESSAGE);
			if (nn != null) {
				lshare.getSettings().setChallenge(nn);
			}
		} else if (ac.equals("nick")) {
			// Allow a user to enter a new nickname.
			String nn = JOptionPane.showInternalInputDialog(desktop, "Please " +
					"enter a new nickname: ", lshare.getSettings().
					getUsername(), JOptionPane.QUESTION_MESSAGE);
			if (nn != null) {
				lshare.getSettings().setUsername(nn);
			}
		} else if (ac.equals("quit")) {
			// Stop the application
			stop();
		} else if (ac.equals("search")) {
			// Search for something
			String nn = JOptionPane.showInternalInputDialog(desktop,
					"Enter search query: ", "", JOptionPane.QUESTION_MESSAGE);
			if (nn != null) {
				addSearchList(new StringFilter(nn));
				lshare.getSearchList().searchForString(nn);
			}
		}
	}
	
	/**
	 * Shows the contact list
	 */
	public void addContactList() {
		contactlist.getFrame().setVisible(true);
		updateMenu();
	}
	
	/**
	 * Shows the shared directories list
	 */
	public void addShareList() {
		sharelist.getFrame().setVisible(true);
		updateMenu();
	}
	
	/**
	 * Shows the file list for a specified user.
	 * @param u The user to show the filelist of.
	 */
	public void addFileList(User u) {
		FileList fl = new FileList(lshare, u, this);
		addFrame(fl);
		updateMenu();
	}
	
	/**
	 * Shows a window with search results.
	 * @param f The filter to apply to the search results.
	 */
	public void addSearchList(SearchFilter f) {
		SearchList sl = new SearchList(lshare, f, this);
		addFrame(sl);
		updateMenu();
	}
	
	/**
	 * Shows the user interface
	 */
	public void start() {
		frame.setVisible(true);
	}
	
	/**
	 * Stops the user interface and the underlying protocol.
	 *
	 */
	public void stop() {
		frame.setVisible(false);
		lshare.stop();
		frame.dispose();
		System.exit(0);
	}

	/**
	 * Created a standard menu item with a given label and action command.
	 * @param title The label of the menu item.
	 * @param command The action command triggered when the item is clicked on.
	 * @return A menu item with the given title and action command.
	 */
	private JMenuItem addMenuItem(String title, String command) {
		JMenuItem mi = new JMenuItem(title);
		mi.setActionCommand(command);
		mi.addActionListener(this);
		return mi;
	}
	
	/**
	 * Updates the desktop to a new given dimension.
	 * @param framedim The new dimension of the desktop.
	 */
	private synchronized void updateDesktop(Dimension framedim) {
		// Check all frames
		for (JInternalFrame i: desktop.getAllFrames()) {
			Dimension d = i.getSize();
			Point p = i.getLocation();
			// Checks bounds
			if (p.x > framedim.width - FRAME_PAD) {
				p.x = 0;
			} else if (p.x + d.width < 0 + FRAME_PAD) {
				p.x = 0;
			}
			if (p.y > framedim.height - FRAME_PAD) {
				p.y = 0;
			} else if (p.y < 0) {
				p.y = 0;
			}
			
			i.setLocation(p);
		}
	}

	/**
	 * Adds a frame to the desktop.
	 * @param f The frame to add to the desktop.
	 */
	private synchronized void addFrame(GUIFrame f) {
		desktop.add(f.getFrame());
	}
}
