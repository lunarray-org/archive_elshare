package org.lunarray.lshare.gui.contactlist;

import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeSelectionModel;
import javax.swing.tree.TreeSelectionModel;

import org.lunarray.lshare.LShare;
import org.lunarray.lshare.gui.GUIFrame;
import org.lunarray.lshare.gui.MainGUI;

/**
 * A standard contact list. Shows all known users in a treelike form.
 * @author Pal Hargitai
 */
public class ContactList extends GUIFrame {
    /**
     * The protocol to access.
     */
    private LShare lshare;

    /**
     * The tree model holding data on the contacts.
     */
    private Model model;

    /**
     * Instanciates the contact list. Sets up the model and the frame.
     * @param ls The protocol that is to be accessed.
     * @param mg The main user interface.
     */
    public ContactList(LShare ls, MainGUI mg) {
        super(mg);

        // Set protocol
        lshare = ls;
        model = new Model(lshare, mg);
        ls.getUserList().addListener(model);

        // Setup tree
        JTree panel = new JTree(model);
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
        panel.getSelectionModel().addTreeSelectionListener(
                new Selecter(panel, model));

        // The frame
        frame.setTitle(getTitle());
        frame.getContentPane().add(scroller);
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
}
