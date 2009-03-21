package org.lunarray.treetable;

import java.awt.Component;
import java.util.EventObject;

import javax.swing.AbstractCellEditor;
import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.table.TableCellEditor;

@SuppressWarnings("serial")
public class TreeTableCellEditor extends AbstractCellEditor implements TableCellEditor {

    private JTree tree;
    
    public TreeTableCellEditor(JTree t) {
        tree = t;
    }

    @Override
    public boolean isCellEditable(EventObject arg0) {
        return true;
    }
    
    @Override
    public boolean shouldSelectCell(EventObject arg0) {
        return false;
    }
    
    @Override
    public boolean stopCellEditing() {
        return true;
    }
    
    public Object getCellEditorValue() {
        return null;
    }
    
    public Component getTableCellEditorComponent(JTable arg0, Object arg1,
            boolean arg2, int arg3, int arg4) {
        return tree;
    }
}
