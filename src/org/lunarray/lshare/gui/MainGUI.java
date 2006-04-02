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

public class MainGUI implements ActionListener {
	
	public static int FRAME_PAD = 30;
	
	private LShare lshare;
	private JFrame frame;
	private JMenuBar menu;
	private JDesktopPane desktop;
	private JMenu winmenu;
	
	private ContactList contactlist;
	private ShareList sharelist;

	public MainGUI(LShare l) {
		frame = new JFrame();
		desktop = new JDesktopPane();
		
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.setTitle("eLShare");
		
		lshare = l;
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent arg0) {
				stop();
			}
		});
		
		contactlist = new ContactList(lshare, this);
		contactlist.getFrame().setVisible(false);
		addFrame(contactlist);
		
		sharelist = new ShareList(lshare);
		sharelist.getFrame().setVisible(false);
		addFrame(sharelist);
		
		initMenu();
		frame.setJMenuBar(menu);
		frame.setContentPane(desktop);
		
		desktop.setMinimumSize(new Dimension(340, 240));
		frame.setMinimumSize(new Dimension(340, 240));
		frame.setPreferredSize(new Dimension(640, 480));
		GraphicsEnvironment ge = GraphicsEnvironment.
				getLocalGraphicsEnvironment();
		frame.setMaximumSize(ge.getMaximumWindowBounds().getSize());
		frame.pack();
		
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
	
	private synchronized void updateDesktop(Dimension framedim) {
		// Check all frames
		for (JInternalFrame i: desktop.getAllFrames()) {
			Dimension d = i.getSize();
			Point p = i.getLocation();
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
	
	private synchronized void updateMenu() {
		winmenu.removeAll();
		for (JInternalFrame i: desktop.getAllFrames()) {
			if (i.isVisible()) {
				winmenu.add(new ShowFrameMenu(i, desktop));
			}
		}
	}
	
	private synchronized void addFrame(GUIFrame f) {
		desktop.add(f.getFrame());
	}
	
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
	
	private JMenuItem addMenuItem(String title, String command) {
		JMenuItem mi = new JMenuItem(title);
		mi.setActionCommand(command);
		mi.addActionListener(this);
		return mi;
	}
	
	public void actionPerformed(ActionEvent arg0) {
		String ac = arg0.getActionCommand();
		if (ac.equals("sharelist")) {
			addShareList();
		} else if (ac.equals("contactlist")) {
			addContactList();
		} else if (ac.equals("challenge")) {
			String nn = JOptionPane.showInputDialog(desktop, "Please " +
					"enter a new e-Mail address: ", lshare.getSettings().
					getUsername(), JOptionPane.QUESTION_MESSAGE);
			if (nn != null) {
				lshare.getSettings().setChallenge(nn);
			}
		} else if (ac.equals("nick")) {
			String nn = JOptionPane.showInternalInputDialog(desktop, "Please " +
					"enter a new nickname: ", lshare.getSettings().
					getUsername(), JOptionPane.QUESTION_MESSAGE);
			if (nn != null) {
				lshare.getSettings().setUsername(nn);
			}
		} else if (ac.equals("quit")) {
			stop();
		} else if (ac.equals("search")) {
			String nn = JOptionPane.showInternalInputDialog(desktop,
					"Enter search query: ", "", JOptionPane.QUESTION_MESSAGE);
			if (nn != null) {
				addSearchList(new StringFilter(nn));
				lshare.getSearchList().searchForString(nn);
			}
		}
	}
	
	public void addContactList() {
		contactlist.getFrame().setVisible(true);
		updateMenu();
	}
	
	public void addShareList() {
		sharelist.getFrame().setVisible(true);
		updateMenu();
	}
	
	public void addFileList(User u) {
		FileList fl = new FileList(lshare, u);
		addFrame(fl);
		updateMenu();
	}
	
	public void addSearchList(SearchFilter f) {
		SearchList sl = new SearchList(lshare, f);
		addFrame(sl);
		updateMenu();
	}
	
	public void start() {
		frame.setVisible(true);
	}
	
	public void stop() {
		frame.setVisible(false);
		lshare.stop();
		frame.dispose();
		System.exit(0);
	}
}
