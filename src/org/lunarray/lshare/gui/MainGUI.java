package org.lunarray.lshare.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDesktopPane;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JToolBar;
import javax.swing.SpinnerNumberModel;

import org.lunarray.lshare.LShare;
import org.lunarray.lshare.gui.contactlist.ContactList;
import org.lunarray.lshare.gui.filelist.FileList;
import org.lunarray.lshare.gui.incomplete.IncompleteList;
import org.lunarray.lshare.gui.main.ShowFrameMenu;
import org.lunarray.lshare.gui.search.SearchFilter;
import org.lunarray.lshare.gui.search.SearchList;
import org.lunarray.lshare.gui.search.StringFilter;
import org.lunarray.lshare.gui.sharelist.ShareList;
import org.lunarray.lshare.gui.transfers.TransferList;
import org.lunarray.lshare.protocol.settings.GUISettings;
import org.lunarray.lshare.protocol.state.userlist.User;

/**
 * TODO queues<br>
 * The main user interface.
 * @author Pal Hargitai
 */
public class MainGUI implements ActionListener {

    /**
     * The amount of frame padding given for kicking back an internal frame when
     * it goes beyond the bounds of the viewport of the desktop. This is
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
     * The transferlist.
     */
    private TransferList transferlist;

    /**
     * The incompete files list.
     */
    private IncompleteList incompletelist;

    /**
     * The toolbar.
     */
    private JToolBar bar;

    /**
     * Settings for the GUI.
     */
    private GUISettings set;

    /**
     * Instanciates the main user interface.
     * @param l The instance of the protocol that it may use.
     */
    public MainGUI(LShare l) {
        frame = new JFrame();
        desktop = new JDesktopPane();
        lshare = l;
        set = lshare.getSettings().getSettingsForGUI();
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

        transferlist = new TransferList(lshare, this);
        transferlist.getFrame().setVisible(false);
        addFrame(transferlist);

        incompletelist = new IncompleteList(lshare, this);
        incompletelist.getFrame().setVisible(false);
        addFrame(incompletelist);

        // Init of the menu bar.
        initMenu();
        frame.setJMenuBar(menu);

        // Init of the toolbar
        initToolBar();

        // Set the panel
        JPanel mp = new JPanel(new BorderLayout());
        frame.setContentPane(mp);
        mp.add(desktop, BorderLayout.CENTER);
        mp.add(bar, BorderLayout.NORTH);

        // Size setting
        desktop.setMinimumSize(new Dimension(340, 240));
        frame.setMinimumSize(new Dimension(340, 240));
        frame.setPreferredSize(new Dimension(640, 480));
        GraphicsEnvironment ge = GraphicsEnvironment
                .getLocalGraphicsEnvironment();
        frame.setMaximumSize(ge.getMaximumWindowBounds().getSize());
        frame.pack();

        /*
         * When a window is resized, so is it's desktop. This allows the desktop
         * to be updated and the frames to stick in it.
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

        // Check challenge
        if (lshare.getSettings().getChallenge().equals("")) {
            // Prompt for unput
            String nc = "";
            while (nc.equals("")) {
                nc = JOptionPane.showInputDialog(frame,
                        "Input (unique) e-Mail address");
            }
            lshare.getSettings().setChallenge(nc);
        }
    }

    /**
     * Updates the window menu with all visible windows.
     */
    public synchronized void updateMenu() {
        winmenu.removeAll();
        for (JInternalFrame i : desktop.getAllFrames()) {
            if (i.isVisible()) {
                winmenu.add(new ShowFrameMenu(i, desktop));
            }
        }
    }

    /**
     * Get a button to be put on the toolbar.
     * @param name The name of the button.
     * @param icon The icon of the button.
     * @param action The actioncommand associated with it.
     * @return The button to be put on the toolbar.
     */
    public JButton addToolButton(String name, String icon, String action) {
        JButton but = new JButton();
        // but.setText(name); <- Use icon
        // but.setIcon(new ImageIcon(icon));
        but.setIcon(new ImageIcon(ClassLoader.getSystemResource(icon)));
        but.addActionListener(this);
        but.setActionCommand(action);
        return but;
    }

    /**
     * Initialise the toolbar.
     */
    public void initToolBar() {
        bar = new JToolBar();
        bar.setFloatable(false);
        bar.add(addToolButton("Contact List", "icons/system-users.png",
                "contactlist"));
        bar.add(addToolButton("Share list", "icons/system-file-manager.png",
                "sharelist"));
        bar.add(addToolButton("Transfer list",
                "icons/network-transmit-receive.png", "transferlist"));
        bar.add(addToolButton("Incomplete file list",
                "icons/text-x-generic.png", "incompletelist"));
        bar.addSeparator();
        bar.add(addToolButton("Search", "icons/system-search.png", "search"));
    }

    /**
     * Initialises the menu.
     */
    public void initMenu() {
        menu = new JMenuBar();
        // File
        JMenu filem = new JMenu("File");
        filem.add(addMenuItem("Search", "search"));
        filem.add(addMenuItem("Quit", "quit"));
        menu.add(filem);
        // Settings
        JMenu settingsm = new JMenu("Settings");
        settingsm.add(addMenuItem("Change Nickname", "nick"));
        settingsm.add(addMenuItem("Change e-Mail address", "challenge"));
        settingsm.add(addMenuItem("Set download directory", "downdir"));
        settingsm.addSeparator();
        settingsm.add(addMenuItem("Set upload slots", "upslots"));
        settingsm.add(addMenuItem("Set upload rate", "uprate"));
        menu.add(settingsm);
        // Window
        JMenu windowm = new JMenu("Window");
        windowm.add(addMenuItem("Show contactlist", "contactlist"));
        windowm.add(addMenuItem("Show sharelist", "sharelist"));
        windowm.add(addMenuItem("Show transfers", "transferlist"));
        windowm.add(addMenuItem("Show incomplete files", "incompletelist"));
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
            String nn = JOptionPane.showInputDialog(desktop, "Please "
                    + "enter a new e-Mail address: ", lshare.getSettings()
                    .getUsername(), JOptionPane.QUESTION_MESSAGE);
            if (nn != null) {
                lshare.getSettings().setChallenge(nn);
            }
        } else if (ac.equals("nick")) {
            // Allow a user to enter a new nickname.
            String nn = JOptionPane.showInternalInputDialog(desktop,
                    "Please enter a new nickname: ", lshare.getSettings()
                            .getUsername(), JOptionPane.QUESTION_MESSAGE);
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
        } else if (ac.equals("transferlist")) {
            // Show the transferlist
            addTransferList();
        } else if (ac.equals("incompletelist")) {
            // Show the incompletelist
            addIncompleteList();
        } else if (ac.equals("downdir")) {
            // Set a new download directory
            JFileChooser f = new JFileChooser(lshare.getSettings()
                    .getDownloadDir());
            f.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            int res = f.showOpenDialog(frame);
            if (res == JFileChooser.APPROVE_OPTION) {
                if (f.getSelectedFile().isDirectory()) {
                    lshare.getSettings().setDownloadDir(f.getSelectedFile());
                }
            }
        } else if (ac.equals("upslots")) {
            // Create a spinner
            int slots = lshare.getUploadManager().getSlots();
            JSpinner spin = new JSpinner(new SpinnerNumberModel(slots, 1, 10, 1));
            int ret = JOptionPane.showInternalConfirmDialog(desktop, spin,
                    "Set amount of slots", JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.QUESTION_MESSAGE);
            if (ret == JOptionPane.OK_OPTION) {
                Object o = spin.getValue();
                if (o instanceof Integer) {
                    Integer val = (Integer)o;
                    lshare.getUploadManager().setSlots(val);
                }
            }
        } else if (ac.equals("uprate")) {
            int rate = lshare.getUploadManager().getRate();
            JSpinner spin = new JSpinner(new SpinnerNumberModel(rate, 100, 100000, 1));
            int ret = JOptionPane.showInternalConfirmDialog(desktop, spin,
                    "Set amount of slots", JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.QUESTION_MESSAGE);
            if (ret == JOptionPane.OK_OPTION) {
                Object o = spin.getValue();
                if (o instanceof Integer) {
                    Integer val = (Integer)o;
                    lshare.getUploadManager().setRate(val);
                }
            }
        }
    }

    /**
     * Add the transferlist. That is, make it visible.
     */
    public void addTransferList() {
        transferlist.getFrame().setVisible(true);
        updateMenu();
    }

    /**
     * Add the incompletefile list. That is, make it visible.
     */
    public void addIncompleteList() {
        incompletelist.getFrame().setVisible(true);
        updateMenu();
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
        addFrame(new FileList(lshare, u, this));
        updateMenu();
    }

    /**
     * Shows a window with search results.
     * @param f The filter to apply to the search results.
     */
    public void addSearchList(SearchFilter f) {
        addFrame(new SearchList(lshare, f, this));
        updateMenu();
    }

    /**
     * Shows the user interface
     */
    public void start() {
        frame.setLocation(set.getInt("/main", "x", 0), set.getInt("/main", "y",
                0));
        frame.setSize(set.getInt("/main", "w", 640), set.getInt("/main", "h",
                480));

        frame.setVisible(true);
    }

    /**
     * Stops the user interface and the underlying protocol.
     */
    public void stop() {
        set.setInt("/main", "x", frame.getX());
        set.setInt("/main", "y", frame.getY());
        set.setInt("/main", "w", frame.getWidth());
        set.setInt("/main", "h", frame.getHeight());

        for (JInternalFrame i : desktop.getAllFrames()) {
            i.dispose();
        }

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
        for (JInternalFrame i : desktop.getAllFrames()) {
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
