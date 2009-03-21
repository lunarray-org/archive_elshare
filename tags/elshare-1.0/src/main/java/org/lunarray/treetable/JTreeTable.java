package org.lunarray.treetable;

import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.ListSelectionModel;
import javax.swing.tree.DefaultTreeSelectionModel;

@SuppressWarnings("serial")
public class JTreeTable extends JTable {

    private TreeTableModel model;
    private TreeTableModelAdapter adapter;
    private TreeTableCellRenderer tree;
    private TreeTableSelectionModel ttmodel;
    
    public JTreeTable(TreeTableModel m) {
        model = m;
        adapter = new TreeTableModelAdapter(model);
        
        setModel(adapter);
        
        tree = new TreeTableCellRenderer(this);
        tree.setModel(adapter);
        adapter.setTree(this);
        
        ttmodel = new TreeTableSelectionModel();
        tree.setSelectionModel(ttmodel);
        setSelectionModel(ttmodel.getListModel());
        
        tree.setRowHeight(getRowHeight());
        
        setDefaultEditor(TreeTableModel.class, new TreeTableCellEditor(tree));
        setDefaultRenderer(TreeTableModel.class, tree);
    }
    
    @SuppressWarnings("serial")
    private class TreeTableSelectionModel extends DefaultTreeSelectionModel {
        public ListSelectionModel getListModel() {
            return listSelectionModel;
        }
    }
    
    public int getEditingRow() {
        if (getColumnClass(editingColumn).equals(TreeTableModel.class)) {
            return -1;
        } else {
            return editingRow;
        }
    }
    
    public JTree getTree() {
        return tree;
    }
}
