package org.lunarray.treetable;

import java.util.LinkedList;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;
import javax.swing.event.TreeModelListener;
import javax.swing.table.TableModel;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

public class TreeTableModelAdapter implements TreeModel, TableModel {

    private TreeTableModel model;

    private LinkedList<TableModelListener> listeners;

    private JTreeTable table;

    public TreeTableModelAdapter(TreeTableModel m) {
        model = m;
        listeners = new LinkedList<TableModelListener>();
    }

    public void setTree(JTreeTable t) {
        table = t;

        table.getTree().addTreeExpansionListener(new TreeExpansionListener() {
            public void treeCollapsed(TreeExpansionEvent arg0) {
                fireExpandChange(arg0, TableModelEvent.DELETE);
            }

            public void treeExpanded(TreeExpansionEvent arg0) {
                fireExpandChange(arg0, TableModelEvent.INSERT);
            }
        });
    }

    public Object getRoot() {
        return model.getRoot();
    }

    public Object getChild(Object arg0, int arg1) {
        return model.getChild(arg0, arg1);
    }

    public int getChildCount(Object arg0) {
        return model.getChildCount(arg0);
    }

    public boolean isLeaf(Object arg0) {
        return model.isLeaf(arg0);
    }

    public void valueForPathChanged(TreePath arg0, Object arg1) {
        // Ignore this
    }

    public int getIndexOfChild(Object arg0, Object arg1) {
        return model.getIndexOfChild(arg0, arg1);
    }

    public void addTreeModelListener(TreeModelListener arg0) {
        model.addTreeModelListener(arg0);
    }

    public void removeTreeModelListener(TreeModelListener arg0) {
        model.removeTreeModelListener(arg0);
    }

    public int getRowCount() {
        return table.getTree().getRowCount();
    }

    public int getColumnCount() {
        return model.getColumnCount();
    }

    public String getColumnName(int arg0) {
        return model.getColumnName(arg0);
    }

    public Class<?> getColumnClass(int arg0) {
        return model.getColumnClass(arg0);
    }

    public boolean isCellEditable(int arg0, int arg1) {
        // return model.isEditable(getNodeForRow(arg0), arg1);
        if (table.getColumnClass(arg1).equals(
                TreeTableModel.class)) {
            return true;
        } else {
            return model.isEditable(getNodeForRow(arg0), arg1);
        }
    }

    public Object getValueAt(int arg0, int arg1) {
        return model.getValueAt(getNodeForRow(arg0), arg1);
    }

    public void setValueAt(Object arg0, int arg1, int arg2) {
        // Check to see if editing is possible
        if (model.isEditable(getNodeForRow(arg1), arg2)) {
            model.setValueAt(getNodeForRow(arg1), arg0, arg2);
        }
    }

    public void addTableModelListener(TableModelListener arg0) {
        listeners.add(arg0);
    }

    public void removeTableModelListener(TableModelListener arg0) {
        listeners.remove(arg0);
    }

    private Object getNodeForRow(int row) {
        TreePath p = table.getTree().getPathForRow(row);
        return p.getLastPathComponent();
    }

    private void fireExpandChange(TreeExpansionEvent e, int type) {
        TreePath o = e.getPath().getParentPath();
        TableModelEvent event;
        if (o != null) {
            event = new TableModelEvent(this, table.getTree().getRowForPath(o),
                    table.getTree().getRowForPath(o)
                            + model.getChildCount(o.getLastPathComponent()),
                    TableModelEvent.ALL_COLUMNS, type);
        } else {
            event = new TableModelEvent(this, 0,
                    table.getTree().getRowCount() - 1,
                    TableModelEvent.ALL_COLUMNS, type);
        }
        for (TableModelListener l : listeners) {
            l.tableChanged(event);
        }
    }
}
