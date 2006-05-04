package org.lunarray.lshare.gui.filelist;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;
import javax.swing.ListSelectionModel;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;

import org.lunarray.lshare.LShare;
import org.lunarray.lshare.gui.GUIFrame;
import org.lunarray.lshare.gui.MainGUI;
import org.lunarray.lshare.protocol.state.userlist.User;

import com.sun.swing.JTreeTable;

/**
 * TODO Rewrite standard TableModel.
 * Shows a filelist of a specific user. Allows browsing throught that file list.
 * @author Pal Hargitai
 */
public class FileList extends GUIFrame implements TreeSelectionListener,
        ActionListener {
    /**
     * The user whose filelist is displayed.
     */
    private User user;

    /**
     * The list model that allows showing of the file list.
     */
    private ListModel model;

    /**
     * The table used.
     */
    private JTreeTable table;

    /**
     * The download button.
     */
    private JButton download;

    /**
     * The download to.. button.
     */
    private JButton downloadto;

    /**
     * The controls of the protocol.
     */
    private LShare lshare;

    /**
     * The selected node.
     */
    private ListNode selected;

    /**
     * Constructs a filelist window.
     * @param ls The instance of the protocol that is to be used.
     * @param u The user whose filelist is to be displayed.
     * @param mg The main user interface that this interface is to be shown on.
     */
    public FileList(LShare ls, User u, MainGUI mg) {
        super(mg);

        lshare = ls;

        // Setup model
        user = u;
        model = new ListModel(ls.getUserList(), user, this);

        // Setup table
        table = new JTreeTable(model);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getTree().addTreeSelectionListener(this);

        JScrollPane t = new JScrollPane(table);

        // Set the toolbar
        JToolBar bar = new JToolBar();
        download = new JButton();
        download.setActionCommand("download");
        download.addActionListener(this);
        download.setEnabled(false);
        // download.setText("Download"); <- Use icon
        download.setIcon(new ImageIcon("icons/document-save.png"));
        bar.add(download);
        downloadto = new JButton();
        downloadto.setActionCommand("downloadto");
        downloadto.addActionListener(this);
        downloadto.setEnabled(false);
        // downloadto.setText("Download To"); <- Use icon
        downloadto.setIcon(new ImageIcon("icons/document-save-as.png"));
        bar.add(downloadto);

        // Set the main panel
        JPanel mp = new JPanel(new BorderLayout());
        mp.add(bar, BorderLayout.NORTH);
        mp.add(t, BorderLayout.CENTER);

        // Setup frame
        frame.setTitle(getTitle());
        frame.getContentPane().add(mp);
    }

    /**
     * Triggered if an action is performed.
     * @param arg0 The event associated with the action.
     */
    public void actionPerformed(ActionEvent arg0) {
        if (arg0.getActionCommand().equals("download")) {
            lshare.getDownloadManager().enqueue(selected.getEntry(), user);
        } else if (arg0.getActionCommand().equals("downloadto")) {
            JFileChooser fc = new JFileChooser();
            fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            int res = fc.showDialog(frame, "Download to directory...");
            if (res == JFileChooser.APPROVE_OPTION) {
                lshare.getDownloadManager().enqueue(selected.getEntry(), user,
                        fc.getSelectedFile());
            }
        }
    }

    /**
     * Triggered if a list selection occurs.
     * @param arg0 The event associated with the selection.
     */
    public void valueChanged(TreeSelectionEvent arg0) {
        if (arg0.getSource() != null) {
            if (arg0.getPath().getLastPathComponent() instanceof ListNode) {
                ListNode n = (ListNode) arg0.getPath().getLastPathComponent();
                selected = n;
            } else {
                selected = null;
            }
        } else {
            selected = null;
        }
        setButtonsEnabled(selected != null);
    }

    @Override
    /**
     * Closes the list and disposes the frame.
     */
    public void close() {
        Object o = model.getRoot();
        if (o instanceof ListNode) {
            ListNode n = (ListNode) o;
            n.getEntry().closeReceiver();
        }
        frame.dispose();
    }

    /**
     * Gets the title that the frame is to be set to.
     * @return The title of the frame.
     */
    public String getTitle() {
        return user.getName() + " (" + user.getHostname() + ")";
    }

    /**
     * The model has been updated, notify table.
     */
    protected void updatedModel() {
        table.tableChanged(new TableModelEvent(table.getModel()));
    }

    /**
     * Enable or disable the buttons.
     * @param enabled True to enable them, false to disable them.
     */
    private void setButtonsEnabled(boolean enabled) {
        download.setEnabled(enabled);
        downloadto.setEnabled(enabled);
    }
}
