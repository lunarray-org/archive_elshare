package org.lunarray.lshare.gui.contactlist;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;

/**
 * A tree selection listener that listens to the contactlist. Updates the panels
 * popup menu.
 * @author Pal Hargitai
 */
public class Selecter implements TreeSelectionListener, ActionListener {

    /**
     * The tree to edit the popup menu of.
     */
    private JTree panel;

    /**
     * The model to trigger incase of a buddy change.
     */
    private Model model;

    /**
     * The currently selected node.
     */
    private UserNode selected;

    /**
     * Constructs the selection listener.
     * @param t The tree that it listens to.
     * @param m The model that it can use.
     */
    public Selecter(JTree t, Model m) {
        panel = t;
        model = m;
    }

    /**
     * Triggered in case of a select or unselect.
     * @param arg0 The event that triggered.
     */
    public void valueChanged(TreeSelectionEvent arg0) {
        // See if anything is selected.
        if (panel.getSelectionCount() > 0) {
            // Checks if the component is a user
            if (!arg0.getPath().getLastPathComponent().getClass().equals(
                    UserNode.class)) {
                panel.getSelectionModel().clearSelection();
                panel.setComponentPopupMenu(null);
            } else {
                // It's a usernode
                selected = (UserNode) arg0.getPath().getLastPathComponent();
                // Set menu
                JPopupMenu m = new JPopupMenu();
                m.add("Username: " + selected.getUser().getName());
                m.add("Address: " + selected.getUser().getHostname());
                m.addSeparator();
                JMenuItem ab;
                // Buddy toggle item
                if (!selected.getUser().isBuddy()) {
                    ab = new JMenuItem("Add to buddy list");
                } else {
                    ab = new JMenuItem("Remove from buddy list");
                }
                ab.setActionCommand("buddy");
                ab.addActionListener(this);
                m.add(ab);
                // The filelist item
                JMenuItem gfl = new JMenuItem("Get File list");
                gfl.setActionCommand("filelist");
                gfl.addActionListener(this);
                m.add(gfl);
                panel.setComponentPopupMenu(m);
            }
        } else {
            // Unselect
            panel.setComponentPopupMenu(null);
        }
    }

    /**
     * The action triggered if an item on the menu is clicked.
     * @param arg0 The action that triggered the call of this event.
     */
    public void actionPerformed(ActionEvent arg0) {
        String ac = arg0.getActionCommand();
        if (ac.equals("filelist")) {
            if (selected != null) {
                model.showUserList(selected.getUser());
            }
        } else if (ac.equals("buddy")) {
            if (selected != null) {
                model.toggleBuddy(selected);
            }
        }
    }
}
