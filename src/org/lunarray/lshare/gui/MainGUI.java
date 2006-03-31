package org.lunarray.lshare.gui;

import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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

public class MainGUI {
	
	private LShare lshare;
	private JFrame frame;
	private JMenuBar menu;
	private JDesktopPane desktop;

	public MainGUI(LShare l) {
		frame = new JFrame();
		desktop = new JDesktopPane();
		
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		
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
		
		frame.setMinimumSize(new Dimension(340, 240));
		frame.setPreferredSize(new Dimension(640, 480));
		GraphicsEnvironment ge = GraphicsEnvironment.
				getLocalGraphicsEnvironment();
		frame.setMaximumSize(ge.getMaximumWindowBounds().getSize());
		frame.pack();
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
				ContactList cl = new ContactList(lshare);
				addFrame(cl.getPanel(), cl.getTitle());
			}
		});
		windowm.add(windowmcontacts);
		JMenuItem windowmsharel = new JMenuItem("Show Sharelist");
		windowmsharel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				ShareList sl = new ShareList(lshare);
				addFrame(sl.getPanel(), sl.getTitle());
			}
		});
		windowm.add(windowmsharel);
		menu.add(windowm);		
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
