package org.lunarray.treetable;

import javax.swing.event.TreeModelListener;

public interface TreeTableModel {

    public Object getRoot();
    public Class getColumnClass(int column);
    public int getColumnCount();
    public String getColumnName(int column);
    public boolean isLeaf(Object arg0);
    public Object getChild(Object arg0, int arg1);
    public int getChildCount(Object arg0);
    public int getIndexOfChild(Object arg0, Object arg1);
    public Object getValueAt(Object node, int column);
    public void setValueAt(Object node, Object val, int column);
    public boolean isEditable(Object node, int column);
    public void removeTreeModelListener(TreeModelListener arg0);
    public void addTreeModelListener(TreeModelListener arg0);
}