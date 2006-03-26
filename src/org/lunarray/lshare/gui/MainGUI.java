package org.lunarray.lshare.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;

import org.lunarray.lshare.LShare;

public class MainGUI {
	
	private LShare lshare;
	private JFrame frame;
	private JMenuBar menu;
	private JTabbedPane tabs;

	public MainGUI(LShare l) {
		frame = new JFrame();
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
		frame.getContentPane().setLayout(new BorderLayout());
		frame.getContentPane().add(menu, BorderLayout.NORTH);
		
		ContactList cl = new ContactList(lshare);
		
		tabs = new JTabbedPane();
		tabs.add(cl.getPanel(), cl.getTitle());
		
		frame.getContentPane().add(tabs, BorderLayout.CENTER);
		
		frame.setMinimumSize(new Dimension(340, 240));
		frame.setPreferredSize(new Dimension(640, 480));
		GraphicsEnvironment ge = GraphicsEnvironment.
				getLocalGraphicsEnvironment();
		frame.setMaximumSize(ge.getMaximumWindowBounds().getSize());
		frame.pack();
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
				String nn = JOptionPane.showInputDialog(frame, "Please " +
						"enter a new nickname: ", lshare.getSettings().
						getUsername());
				lshare.getSettings().setUsername(nn);
			}
		});
		settingsm.add(settingsmchangenick);
		JMenuItem settingsmchangechal = new JMenuItem("Change e-Mail address");
		settingsmchangechal.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String nn = JOptionPane.showInputDialog(frame, "Please " +
						"enter a new e-Mail address: ", lshare.getSettings().
						getUsername());
				lshare.getSettings().setChallenge(nn);
			}
		});
		settingsm.add(settingsmchangechal);
		menu.add(settingsm);
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
