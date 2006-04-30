package org.lunarray.lshare.gui.incomplete;

import java.awt.Color;
import java.awt.Component;
import java.util.ArrayList;

import javax.swing.JProgressBar;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;

import org.lunarray.lshare.LShare;
import org.lunarray.lshare.gui.GUIUtil;
import org.lunarray.lshare.protocol.events.QueueEvent;
import org.lunarray.lshare.protocol.events.QueueListener;
import org.lunarray.lshare.protocol.state.download.file.IncompleteFile;

public class IncompleteModel implements TableModel, TableCellRenderer,
        QueueListener {

    private ArrayList<TableModelListener> listeners;

    private ArrayList<IncompleteFile> files;

    private ArrayList<JProgressBar> bars;

    public IncompleteModel(LShare ls) {
        listeners = new ArrayList<TableModelListener>();
        files = new ArrayList<IncompleteFile>();
        bars = new ArrayList<JProgressBar>();

        for (IncompleteFile f : ls.getDownloadManager().getIncompleteFiles()) {
            files.add(f);
            JProgressBar j = new JProgressBar();
            j.setMinimum(0);
            if (f.getSize() > Integer.MAX_VALUE) {
                j.setMaximum(Long.valueOf(f.getSize() / Integer.MAX_VALUE)
                        .intValue());
                j.setValue(Long.valueOf(f.getDone() / Integer.MAX_VALUE)
                        .intValue());
            } else {
                j.setMaximum(Long.valueOf(f.getSize()).intValue());
                j.setValue(Long.valueOf(f.getDone()).intValue());
            }
            j.setForeground(Color.BLUE.darker());
            j.setBackground(Color.BLUE);

            bars.add(j);
        }
    }

    public int getRowCount() {
        return files.size();
    }

    public int getColumnCount() {
        return 5;
    }

    public Component getTableCellRendererComponent(JTable arg0, Object arg1,
            boolean arg2, boolean arg3, int arg4, int arg5) {
        if (arg5 == 2) {
            return bars.get(arg4);
        } else {
            return null;
        }
    }

    public String getColumnName(int arg0) {
        switch (arg0) {
        case 0:
            return "Filename";
        case 1:
            return "Status";
        case 2:
            return "Progress";
        case 3:
            return "Size";
        case 4:
            return "Hash";
        default:
            return "";
        }
    }

    public Class<?> getColumnClass(int arg0) {
        if (arg0 == 2) {
            return JProgressBar.class;
        } else {
            return String.class;
        }
    }

    public Object getValueAt(int arg0, int arg1) {
        switch (arg1) {
        case 0:
            return files.get(arg0).getFile().getPath();
        case 1:
            // TODO check for corrupt
            return files.get(arg0).getStatus();
        case 2:
            return bars.get(arg0);
        case 3:
            return GUIUtil.prettyPrint(files.get(arg0).getSize());
        case 4:
            return files.get(arg0).getHash().toString();
        default:
            return "";
        }
    }

    public boolean isCellEditable(int arg0, int arg1) {
        return false;
    }

    public void setValueAt(Object arg0, int arg1, int arg2) {
        // Ignore
    }

    public void addTableModelListener(TableModelListener arg0) {
        listeners.add(arg0);
    }

    public void removeTableModelListener(TableModelListener arg0) {
        listeners.remove(arg0);
    }

    public synchronized void queueAdded(QueueEvent e) {
        if (!files.contains(e.getFile())) {
            files.add(e.getFile());
            JProgressBar j = new JProgressBar();
            j.setStringPainted(true);
            j.setMinimum(0);
            if (e.getFile().getSize() > Integer.MAX_VALUE) {
                j.setMaximum(Long.valueOf(
                        e.getFile().getSize() / Integer.MAX_VALUE).intValue());
                j.setValue(Long.valueOf(
                        e.getFile().getDone() / Integer.MAX_VALUE).intValue());
            } else {
                j.setMaximum(Long.valueOf(e.getFile().getSize()).intValue());
                j.setValue(Long.valueOf(e.getFile().getDone()).intValue());
            }
            j.setForeground(Color.BLUE.darker());
            j.setBackground(Color.BLUE);

            bars.add(j);

            int i = files.indexOf(e.getFile());
            TableModelEvent t = new TableModelEvent(this, i, i,
                    TableModelEvent.ALL_COLUMNS, TableModelEvent.INSERT);
            for (TableModelListener l : listeners) {
                l.tableChanged(t);
            }
        }
    }

    public synchronized void queueRemoved(QueueEvent e) {
        if (files.contains(e.getFile())) {
            int i = files.indexOf(e.getFile());

            TableModelEvent t;

            if (e.getFile().isCorrupt()) {
                files.remove(i);
                bars.remove(i);

                t = new TableModelEvent(this, i, i,
                        TableModelEvent.ALL_COLUMNS, TableModelEvent.DELETE);
            } else {
                t = new TableModelEvent(this, i, i, 1, TableModelEvent.UPDATE);
            }

            for (TableModelListener l : listeners) {
                l.tableChanged(t);
            }
        }
    }

    public synchronized void queueUpdated(QueueEvent e) {
        if (files.contains(e.getFile())) {
            int i = files.indexOf(e.getFile());

            TableModelEvent t = new TableModelEvent(this, i, i, 1,
                    TableModelEvent.UPDATE);
            for (TableModelListener l : listeners) {
                l.tableChanged(t);
            }
        }
    }

    protected void deleteRow(int i) {
        files.remove(i);
        bars.remove(i);

        TableModelEvent t = new TableModelEvent(this, i, i,
                TableModelEvent.ALL_COLUMNS, TableModelEvent.DELETE);

        for (TableModelListener l : listeners) {
            l.tableChanged(t);
        }
    }

    protected IncompleteFile getRow(int i) {
        return files.get(i);
    }

    protected synchronized void updateTable() {
        for (int i = 0; i < files.size(); i++) {

            IncompleteFile f = files.get(i);
            JProgressBar j = bars.get(i);
            if (f.getSize() > Integer.MAX_VALUE) {
                j.setValue(Long.valueOf(f.getDone() / Integer.MAX_VALUE)
                        .intValue());
            } else {
                j.setValue(Long.valueOf(f.getDone()).intValue());
            }

            TableModelEvent t = new TableModelEvent(this, i, i, 1,
                    TableModelEvent.UPDATE);
            TableModelEvent u = new TableModelEvent(this, i, i, 2,
                    TableModelEvent.UPDATE);
            for (TableModelListener l : listeners) {
                l.tableChanged(t);
                l.tableChanged(u);
            }
        }
    }
}
