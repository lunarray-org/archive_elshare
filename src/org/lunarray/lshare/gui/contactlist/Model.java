package org.lunarray.lshare.gui.contactlist;

import java.util.ArrayList;

import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import org.lunarray.lshare.LShare;
import org.lunarray.lshare.gui.MainGUI;
import org.lunarray.lshare.protocol.events.UserEvent;
import org.lunarray.lshare.protocol.events.UserListener;
import org.lunarray.lshare.protocol.state.userlist.User;

/**
 * A tree model that associates itself to a given instance of the protocol. It
 * will listen to all user events and update itself accordingly.
 * @author Pal Hargitai
 */
public class Model implements TreeModel, UserListener {
    /**
     * The root node of this tree model.
     */
    private Root root;

    /**
     * The listeners that listen to events on this model.
     */
    private ArrayList<TreeModelListener> listener;

    /**
     * The main user interface to be triggered if a userlist is requested.
     */
    private MainGUI gui;

    /**
     * Instanciates the mdoela nd associates itself with it.
     * @param ls The instance of the protocol to use.
     * @param mg The main user interface.
     */
    public Model(LShare ls, MainGUI mg) {
        gui = mg;
        root = new Root(ls);
        listener = new ArrayList<TreeModelListener>();
    }

    /**
     * Get the root node of this model.
     * @return The root node.
     */
    public Root getRoot() {
        return root;
    }

    /**
     * Get the child at the specified inject of the specified object.
     * @param arg0 The object to get the child of.
     * @param arg1 The index of the child.
     * @return The node at the specified index in the specified parent.
     */
    public Object getChild(Object arg0, int arg1) {
        if (arg0 == root) {
            return root.getChildAt(arg1);
        }
        for (int i = 0; i < root.getChildCount(); i++) {
            if (root.getChildAt(i) == arg0) {
                return root.getChildAt(i).getChildAt(arg1);
            }
        }
        return null;
    }

    /**
     * Get the amount of children the object has.
     * @param arg0 The object to get the child count of.
     * @return The amount of children of the node.
     */
    public int getChildCount(Object arg0) {
        if (arg0 == root) {
            return root.getChildCount();
        }
        for (int i = 0; i < root.getChildCount(); i++) {
            if (arg0 == root.getChildAt(i)) {
                return root.getChildAt(i).getChildCount();
            }
        }
        return 0;
    }

    /**
     * Checks wether the given object is a leaf.
     * @param arg0 The object to check wether it's a leaf.
     * @return True if the node is a leaf, false if not.
     */
    public boolean isLeaf(Object arg0) {
        if (arg0.getClass().equals(UserNode.class)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Model requires this, ignore.
     */
    public void valueForPathChanged(TreePath arg0, Object arg1) {
        // Ignore
    }

    /**
     * Get the index of the given child.
     * @param arg0 The parent.
     * @param arg1 The child node to get the index of.
     * @return The index of the child in the given parent.
     */
    public int getIndexOfChild(Object arg0, Object arg1) {
        if (arg0 == root) {
            if (arg1.getClass().equals(TreeNode.class)) {
                return root.getIndex((TreeNode) arg1);
            } else {
                return -1;
            }
        }
        for (int i = 0; i < root.getChildCount(); i++) {
            if (arg0 == root.getChildAt(i)) {
                return root.getChildAt(i).getIndex(arg1);
            }
        }
        return -1;
    }

    /**
     * Registers a listener.
     * @param arg0 The listener to register.
     */
    public void addTreeModelListener(TreeModelListener arg0) {
        listener.add(arg0);
    }

    /**
     * Removes a listener.
     * @param arg0 The listener to remove.
     */
    public void removeTreeModelListener(TreeModelListener arg0) {
        listener.remove(arg0);
    }

    /**
     * A user signoff event.
     * @param e The user event.
     */
    public void signoff(UserEvent e) {
        if (e.getUser().isBuddy()) {
            fireInsert(root.getChildAt(2).addUser(e.getUser()));
            int i = root.getChildAt(0).getIndex(
                    root.getChildAt(0).findUser(e.getUser()));
            fireRemove(root.getChildAt(0).removeUser(e.getUser()), i);
        } else {
            int i = root.getChildAt(1).getIndex(
                    root.getChildAt(1).findUser(e.getUser()));
            fireRemove(root.getChildAt(1).removeUser(e.getUser()), i);
        }
    }

    /**
     * A user signon event.
     * @param e The user event.
     */
    public void signon(UserEvent e) {
        if (e.getUser().isBuddy()) {
            int i = root.getChildAt(2).getIndex(
                    root.getChildAt(2).findUser(e.getUser()));
            fireRemove(root.getChildAt(2).removeUser(e.getUser()), i);
            fireInsert(root.getChildAt(0).addUser(e.getUser()));
        } else {
            fireInsert(root.getChildAt(1).addUser(e.getUser()));
        }
    }

    /**
     * A user update event.
     * @param e The user event.
     */
    public void update(UserEvent e) {
        UserNode n;
        if (e.getUser().isBuddy()) {
            n = root.getOnlineBuddies().findUser(e.getUser());
            root.getOnlineBuddies().resort(n);
        } else {
            n = root.getOnlineMisc().findUser(e.getUser());
            root.getOnlineMisc().resort(n);
        }
        if (n != null) {
            fireUpdate(n);
        }
    }

    /**
     * Toggle the status of a buddy. That is, a buddy gets unset and a regular
     * user becomes a buddy.
     * @param u The user to toggle.
     */
    public void toggleBuddy(UserNode u) {
        for (int i = 0; i < root.getChildCount(); i++) {
            int j = root.getChildAt(i).getIndex(u);
            UserNode n = root.getChildAt(i).removeUser(u.getUser());
            if (n != null) {
                if (j > 0) {
                    fireRemove(n, j);
                } else {
                    fireRemove(n, 0);
                }
            }
        }

        if (u.getUser().isBuddy()) {
            u.getUser().unsetBuddy();

            if (u.getUser().isOnline()) {
                fireInsert(root.getOnlineMisc().addUser(u.getUser()));
            }
        } else {
            u.getUser().setBuddy();

            fireInsert(root.getOnlineBuddies().addUser(u.getUser()));
        }
    }

    /**
     * Triggers showing of a filelist of a user.
     * @param u The user to show the filelist of.
     */
    protected void showUserList(User u) {
        gui.addFileList(u);
    }

    /**
     * Fires an update of a node to the models listeners.
     * @param n The node that has been updated.
     */
    private void fireUpdate(UserNode n) {
        TreeModelEvent e = genEvent(n, n.getParent().getIndex(n));

        for (TreeModelListener l : listener) {
            l.treeStructureChanged(e);
        }
    }

    /**
     * Fires an insert of a node to the models listeners.
     * @param n The node that has been inserted.
     */
    private void fireInsert(UserNode n) {
        TreeModelEvent e = genEvent(n, n.getParent().getIndex(n));

        for (TreeModelListener l : listener) {
            l.treeNodesInserted(e);
        }
    }

    /**
     * Fires a remove of a node to the models listeners.
     * @param n The node that has been removed.
     * @param i The nodes former index.
     */
    private void fireRemove(UserNode n, int i) {
        if (n != null) {
            int[] j = {
                i
            };
            Object[] children = {
                n
            };
            Object[] p = {
                    root, n.getParent()
            };

            TreeModelEvent e = new TreeModelEvent(n.getParent(), p, j, children);

            for (TreeModelListener l : listener) {
                l.treeNodesRemoved(e);
            }
        }
    }

    /**
     * Generates a TreeModelEvent.
     * @param n The node for the event.
     * @param i The index of the event.
     * @return The event that can be fired to the listeners.
     */
    private TreeModelEvent genEvent(UserNode n, int i) {
        int[] j = {
            i
        };
        Object[] children = {
            n
        };
        Object[] p = {
                root, n.getParent()
        };

        return new TreeModelEvent(n.getParent(), p, j, children);
    }
}
