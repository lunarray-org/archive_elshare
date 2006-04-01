package org.lunarray.lshare.gui;

import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JComponent;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import org.lunarray.lshare.LShare;
import org.lunarray.lshare.gui.main.ShowFrameMenu;
import org.lunarray.lshare.protocol.state.userlist.User;

public class MainGUI {
	
	public static int FRAME_PAD = 30;
	
	private LShare lshare;
	private JFrame frame;
	private JMenuBar menu;
	private JDesktopPane desktop;
	private JMenu winmenu;

	public MainGUI(LShare l) {
		frame = new JFrame();
		desktop = new JDesktopPane();
		
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.setTitle("eLShare");
		
		lshare = l;
		frame.addWindowListener(new WindowListener() {
			public void windowActivated(WindowEvent arg0) {}
			public void windowClosed(WindowEvent arg0) {}
			public void windowClosing(WindowEvent arg0) {
				stop();
			}
			public void windowDeactivated(WindowEvent arg0) {}
			public void windowDeiconified(WindowEvent arg0) {}
			public void windowIconified(WindowEvent arg0) {}
			public void windowOpened(WindowEvent arg0) {}
		});
		
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
			winmenu.add(new ShowFrameMenu(i, desktop));
		}
	}
	
	private synchronized void addFrame(JComponent p, String t) {
		JInternalFrame icl = new JInternalFrame();
		icl.getContentPane().add(p);
		icl.setTitle(t);
		icl.setMinimumSize(new Dimension(320, 240));
		icl.setPreferredSize(new Dimension(320, 240));
		icl.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
		icl.pack();
		icl.setLocation(0, 0);
		icl.setVisible(true);
		icl.setResizable(true);
		icl.setClosable(true);
		icl.setMaximizable(true);
		icl.setIconifiable(true);
		
		desktop.add(icl);
	}
	
	public void initMenu() {
		menu = new JMenuBar();
		// File
		JMenu filem = new JMenu("File");
		JMenuItem filemquit = new JMenuItem("Quit");
		filemquit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				stop();
			}
		});
		filem.add(filemquit);
		menu.add(filem);
		// Settings
		JMenu settingsm = new JMenu("Settings");
		JMenuItem settingsmchangenick = new JMenuItem("Change Nickname");
		settingsmchangenick.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String nn = JOptionPane.showInternalInputDialog(desktop, "Please " +
						"enter a new nickname: ", lshare.getSettings().
						getUsername(), JOptionPane.QUESTION_MESSAGE);
				if (nn != null) {
					lshare.getSettings().setUsername(nn);
				}
			}
		});
		settingsm.add(settingsmchangenick);
		JMenuItem settingsmchangechal = new JMenuItem("Change e-Mail address");
		settingsmchangechal.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String nn = JOptionPane.showInputDialog(desktop, "Please " +
						"enter a new e-Mail address: ", lshare.getSettings().
						getUsername(), JOptionPane.QUESTION_MESSAGE);
				if (nn != null) {
					lshare.getSettings().setChallenge(nn);
				}
			}
		});
		settingsm.add(settingsmchangechal);
		menu.add(settingsm);
		// Window
		JMenu windowm = new JMenu("Window");
		JMenuItem windowmcontacts = new JMenuItem("Show Contactlist");
		windowmcontacts.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				addContactList();
			}
		});
		windowm.add(windowmcontacts);
		JMenuItem windowmsharel = new JMenuItem("Show Sharelist");
		windowmsharel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				addShareList();
			}
		});
		windowm.add(windowmsharel);
		menu.add(windowm);
		// Winlist
		winmenu = new JMenu("View");
		menu.add(winmenu);
	}
	
	public void addContactList() {
		ContactList cl = new ContactList(lshare, this);
		addFrame(cl.getPanel(), cl.getTitle());
		updateMenu();
	}
	
	public void addShareList() {
		ShareList sl = new ShareList(lshare);
		addFrame(sl.getPanel(), sl.getTitle());
		updateMenu();
	}
	
	public void addFileList(User u) {
		FileList fl = new FileList(lshare, u);
		addFrame(fl.getPanel(), fl.getTitle());
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
