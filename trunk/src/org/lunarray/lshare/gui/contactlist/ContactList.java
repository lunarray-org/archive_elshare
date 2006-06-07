/*
 * eLShare allows you to share.
 * Copyright (C) 2006 Pal Hargitai
 * E-Mail: pal@lunarray.org
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package org.lunarray.lshare.gui.contactlist;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultTreeSelectionModel;
import javax.swing.tree.TreeSelectionModel;

import org.lunarray.lshare.LShare;
import org.lunarray.lshare.gui.GUIFrame;
import org.lunarray.lshare.gui.MainGUI;

/**
 * A standard contact list. Shows all known users in a treelike form.
 * @author Pal Hargitai
 */
public class ContactList extends GUIFrame implements ActionListener,
        TreeSelectionListener {
    /**
     * The protocol to access.
     */
    private LShare lshare;

    /**
     * The currently selected node.
     */
    private UserNode selected;

    /**
     * The tree model holding data on the contacts.
     */
    private Model model;

    /**
     * The button to remove a buddy.
     */
    private JButton buttonrembuddy;

    /**
     * The button to add a buddy.
     */
    private JButton buttonaddbuddy;

    /**
     * The button to fetch a users filelist.
     */
    private JButton buttonfilelist;

    /**
     * The tree of the buddylist.
     */
    private JTree panel;

    /**
     * Instanciates the contact list. Sets up the model and the frame.
     * @param ls The protocol that is to be accessed.
     * @param mg The main user interface.
     */
    public ContactList(LShare ls, MainGUI mg) {
        super(mg, ls);

        // Set protocol
        lshare = ls;
        model = new Model(lshare, mg);
        ls.getUserList().addListener(model);

        JPanel mp = new JPanel(new BorderLayout());

        // Setup tree
        panel = new JTree(model);
        JScrollPane scroller = new JScrollPane(panel);
        scroller
                .setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        panel.setRootVisible(false);

        // Expand tree
        for (int i = 0; i < panel.getRowCount(); i++) {
            panel.expandRow(i);
        }

        // Set selectiion
        panel.setSelectionModel(new DefaultTreeSelectionModel());
        panel.getSelectionModel().setSelectionMode(
                TreeSelectionModel.SINGLE_TREE_SELECTION);
        panel.getSelectionModel().addTreeSelectionListener(this);

        // Set buttons
        buttonrembuddy = new JButton();
        buttonrembuddy.setActionCommand("buddy");
        buttonrembuddy.addActionListener(this);
        buttonrembuddy.setEnabled(false);
        // buttonrembuddy.setText("Remove Buddy"); <- Use icon
        buttonrembuddy.setIcon(new ImageIcon(ClassLoader
                .getSystemResource("content/icons/list-remove.png")));
        buttonaddbuddy = new JButton();
        buttonaddbuddy.setActionCommand("buddy");
        buttonaddbuddy.addActionListener(this);
        buttonaddbuddy.setEnabled(false);
        // buttonaddbuddy.setText("Add Buddy"); <- Use icon
        buttonaddbuddy.setIcon(new ImageIcon(ClassLoader
                .getSystemResource("content/icons/list-add.png")));
        buttonfilelist = new JButton();
        buttonfilelist.setActionCommand("filelist");
        buttonfilelist.addActionListener(this);
        buttonfilelist.setEnabled(false);
        // buttonfilelist.setText("Get filelist"); <- Use icon
        buttonfilelist.setIcon(new ImageIcon(ClassLoader
                .getSystemResource("content/icons/folder-remote.png")));

        // Set toolbar
        JToolBar bar = new JToolBar();
        bar.setFloatable(false);
        bar.add(buttonaddbuddy);
        bar.add(buttonrembuddy);
        bar.addSeparator();
        bar.add(buttonfilelist);

        // Set panel
        mp.add(bar, BorderLayout.NORTH);
        mp.add(scroller, BorderLayout.CENTER);

        // The frame
        frame.setTitle(getTitle());
        frame.getContentPane().add(mp);
    }

    /**
     * When a treeselection occurs, this is triggered.
     * @param arg0 The event associated with the selection.
     */
    public void valueChanged(TreeSelectionEvent arg0) {
        // See if anything is selected.
        if (panel.getSelectionCount() > 0) {
            // Checks if the component is a user
            if (!(arg0.getPath().getLastPathComponent() instanceof UserNode)) {
                panel.getSelectionModel().clearSelection();
                selected = null;
            } else {
                // It's a usernode
                selected = (UserNode) arg0.getPath().getLastPathComponent();
            }
        } else {
            // Unselect
            selected = null;
        }
        if (selected == null) {
            disableButtons();
        } else {
            enableButtons(selected.getUser().isBuddy());
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

    @Override
    /**
     * The close action for the frame. Just makes the frame invisible.
     */
    public void close() {
        frame.setVisible(false);
    }

    /**
     * Gets the title of this frame.
     * @return The title of this frame.
     */
    public String getTitle() {
        return "Contact List";
    }

    /**
     * Disable the buttons.
     */
    private void disableButtons() {
        buttonaddbuddy.setEnabled(false);
        buttonrembuddy.setEnabled(false);
        buttonfilelist.setEnabled(false);
    }

    /**
     * Enable the buttons.
     * @param isbuddy Wether the user is a buddy.
     */
    private void enableButtons(boolean isbuddy) {
        buttonfilelist.setEnabled(true);
        buttonaddbuddy.setEnabled(!isbuddy);
        buttonrembuddy.setEnabled(isbuddy);
    }
}
