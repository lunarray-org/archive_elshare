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
package org.lunarray.lshare.gui.filelist;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.table.TableColumnModel;
import javax.swing.tree.TreePath;

import org.lunarray.lshare.gui.GUIUtil;
import org.lunarray.lshare.protocol.state.userlist.ExternalUserList;
import org.lunarray.lshare.protocol.state.userlist.User;
import org.lunarray.treetable.JTreeTable;
import org.lunarray.treetable.TreeTableModel;

/*import com.sun.swing.AbstractTreeTableModel;
import com.sun.swing.JTreeTable;
import com.sun.swing.TreeTableModel;*/


/**
 * A list model for showing a users file list.
 * @author Pal Hargitai
 */
public class ListModel //extends AbstractTreeTableModel
    implements
        TreeTableModel, MouseListener, TreeExpansionListener {
    /**
     * The model listeners.
     */
    private ArrayList<TreeModelListener> listeners;

    /**
     * The file list that this model is used in.
     */
    private FileList list;

    /**
     * The header of the column.
     */
    private JTreeTable table;

    /**
     * Used for synchronisation. Certain events are ignored when sorting.
     */
    private boolean sorting;

    private ListNode root;
    
    /**
     * Constructs a list.
     * @param l The userlist that the filelisti s fetched from.
     * @param u The user that the filelist is to be fetched from.
     * @param f The file list that this model is used in.
     */
    public ListModel(ExternalUserList l, User u, FileList f) {
        /*super(new ListNode(l.getFilelist(u), null, null));
        ((ListNode) getRoot()).setModel(this);
        ((ListNode) getRoot()).setExpanded(true);*/
        
        root = new ListNode(l.getFilelist(u), null, null);
        root.setModel(this);
        root.setExpanded(true);

        
        listeners = new ArrayList<TreeModelListener>();
        sorting = false;
        list = f;
    }

    /**
     * Set the table hearder.
     * @param h The header of the table.
     */
    public void setTable(JTreeTable h) {
        if (table != null) {
            table.getTableHeader().removeMouseListener(this);
            table.getTree().removeTreeExpansionListener(this);
        }
        table = h;
        table.getTableHeader().addMouseListener(this);
        table.getTree().addTreeExpansionListener(this);
    }

    /**
     * Triggered if a tree node has collapsed. Will inform the node that such
     * has happened.
     * @param arg0 The node that has been collapsed.
     */
    public void treeCollapsed(TreeExpansionEvent arg0) {
        if (!sorting) {
            Object o = arg0.getPath().getLastPathComponent();
            if (o instanceof ListNode) {
                ((ListNode) o).setExpanded(false);
            }
        }
    }

    /**
     * Triggered if a tree node has expanded. Will inform the node that such has
     * happened.
     * @param arg0 The node that has been expanded.
     */
    public void treeExpanded(TreeExpansionEvent arg0) {
        if (!sorting) {
            Object o = arg0.getPath().getLastPathComponent();
            if (o instanceof ListNode) {
                ((ListNode) o).setExpanded(true);
            }
        }
    }

    /**
     * A mouse click has occured on the header. Set the column to sort.
     * @param arg0 The mouse event.
     */
    public void mouseClicked(MouseEvent arg0) {
        sorting = true;

        TableColumnModel cm = table.getTableHeader().getColumnModel();
        int viewcol = cm.getColumnIndexAtX(arg0.getX());
        int actualcol = cm.getColumn(viewcol).getModelIndex();
        switch (actualcol) {
        case 1:
            ((ListNode) getRoot()).switchOnType(ListNode.SortType.SIZE);
            break;
        case 2:
            ((ListNode) getRoot()).switchOnType(ListNode.SortType.DATE);
            break;
        case 3:
            ((ListNode) getRoot()).switchOnType(ListNode.SortType.HASH);
            break;
        case 0:
        default:
            ((ListNode) getRoot()).switchOnType(ListNode.SortType.NAME);
            break;
        }

        Object[] p = {
            getRoot()
        };
        TreeModelEvent e = new TreeModelEvent(this, p);
        for (TreeModelListener l : listeners) {
            l.treeStructureChanged(e);
        }

        ((ListNode) getRoot()).checkExpansion(table.getTree(), 0);
        sorting = false;
    }

    /**
     * Mouse over.
     * @param arg0 The event.
     */
    public void mouseEntered(MouseEvent arg0) {
        // Ignore
    }

    /**
     * Mouse off.
     * @param arg0 The event.
     */
    public void mouseExited(MouseEvent arg0) {
        // Ignore
    }

    /**
     * Mouse press.
     * @param arg0 The event.
     */
    public void mousePressed(MouseEvent arg0) {
        // Ignore
    }

    /**
     * Mouse release.
     * @param arg0 The event.
     */
    public void mouseReleased(MouseEvent arg0) {
        // Ignore
    }

    /**
     * Removes a model listener.
     * @param arg0 The listener to be removed.
     */
    public void removeTreeModelListener(TreeModelListener arg0) {
        listeners.remove(arg0);
    }

    /**
     * Adds a model listener.
     * @param arg0 The listener to be added.
     */
    public void addTreeModelListener(TreeModelListener arg0) {
        listeners.add(arg0);
    }

    /**
     * Gets a the child at of the specified node at the specified index.
     * @param arg0 The parent.
     * @param arg1 The index of the child.
     * @return The child.
     */
    public Object getChild(Object arg0, int arg1) {
        if (arg0 instanceof ListNode) {
            return ((ListNode) arg0).get(arg1);
        } else {
            return null;
        }
    }

    /**
     * Gets the amount of children the node has.
     * @param arg0 The node to get the count of.
     */
    public int getChildCount(Object arg0) {
        if (arg0 instanceof ListNode) {
            return ((ListNode) arg0).size();
        } else {
            return 0;
        }
    }

    /**
     * Gets the index of the child in the parent.
     * @param arg0 The parent.
     * @param arg1 The child.
     * @return The index of the child in the parent.
     */
    public int getIndexOfChild(Object arg0, Object arg1) {
        if (arg0 instanceof ListNode && arg1 instanceof ListNode) {
            return ((ListNode) arg0).getIndex((ListNode) arg1);
        } else {
            return -1;
        }
    }

    /**
     * Gets the class of the spcified column.
     * @param column The column whose class to get.
     * @return The class of the specified column.
     */
    public Class getColumnClass(int column) {
        switch (column) {
        case 0:
            return TreeTableModel.class;
        default:
            return String.class;
        }
    }

    /**
     * Gets the amount of columns.
     * @return The amount of columns. (Generally 4)
     */
    public int getColumnCount() {
        return 4;
    }

    /**
     * Gets the name of the specified column.
     * @param column The column index.
     * @return The name of the specified column.
     */
    public String getColumnName(int column) {
        switch (column) {
        case 0:
            return "Name";
        case 1:
            return "Size";
        case 2:
            return "Last Modified Date";
        case 3:
            return "File hash";
        default:
            return "";
        }
    }

    /**
     * Gets the value of the node in the specific column.
     * @param node The node to get the value of.
     * @param column The column to get the value of.
     * @return The value at the specified node and column.
     */
    public Object getValueAt(Object node, int column) {
        if (node instanceof ListNode) {
            ListNode n = (ListNode) node;
            switch (column) {
            case 0:
                return n.getName();
            case 1:
                if (n.getFileSize() >= 0) {
                    return GUIUtil.prettyPrint(n.getFileSize());
                } else {
                    return "";
                }
            case 2:
                if (n.getLastModified() < 0) {
                    return "";
                } else {
                    return new Date(n.getLastModified());
                }
            case 3:
                return n.getHash();
            default:
                return "";
            }
        } else {
            return null;
        }
    }

    /**
     * Checks wether the specified node is a leaf.
     * @param arg0 The node to check.
     * @return True if the node is a leaf, false if not.
     */
    public boolean isLeaf(Object arg0) {
        if (arg0 instanceof ListNode) {
            return ((ListNode) arg0).isLeaf();
        } else {
            return false;
        }
    }

    /**
     * Updates the given node, ie. tells listeners that children have bene
     * inserted.
     * @param n The node to update.
     */
    protected void updateNode(ListNode n) {
        TreePath p = new TreePath(getPathToRoot(n, 0));

        TreeModelEvent e = new TreeModelEvent(n, p);
        for (TreeModelListener l : listeners) {
            l.treeStructureChanged(e);
        }

        list.updatedModel();
    }

    /**
     * Gets the path from this node to the root node.
     * @param node The node to get the path of.
     * @param depth The current depth.
     * @return The path from the given node to the root.
     */
    protected ListNode[] getPathToRoot(ListNode node, int depth) {
        if (node == null) {
            if (depth == 0) {
                return null;
            }
            return new ListNode[depth];
        }
        ListNode[] path = getPathToRoot(node.getParent(), depth + 1);
        path[path.length - depth - 1] = node;
        return path;
    }
    
    public Object getRoot() {
        return root;
    }
    
    public boolean isEditable(Object node, int column) {
        return false;
    }
    
    public void setValueAt(Object node, Object val, int column) {
    }
}
