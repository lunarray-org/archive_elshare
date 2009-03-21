package org.lunarray.treetable;

import java.awt.Component;
import java.awt.Graphics;

import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.table.TableCellRenderer;

@SuppressWarnings("serial")
public class TreeTableCellRenderer extends JTree implements TableCellRenderer {

    private JTreeTable table;
    private int vrow;
    
    public TreeTableCellRenderer(JTreeTable ta) {
        table = ta;
    }
    
    @Override
    public void setBounds(int x, int y, int w, int h) {
        super.setBounds(x, 0, w, table.getHeight());
    }
    
    @Override
    public void paint(Graphics arg0) {
        arg0.translate(0, -vrow * table.getRowHeight());
        super.paint(arg0);
    }
    
    public Component getTableCellRendererComponent(JTable t, Object val,
            boolean issel, boolean hasf, int row, int col) {
        if (issel) {
            setBackground(t.getSelectionBackground());
        } else {
            setBackground(t.getBackground());
        }
        
        vrow = row;
        
        return this;
    }

}