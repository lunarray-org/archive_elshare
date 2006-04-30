package org.lunarray.lshare.gui.transfers;

import java.awt.Component;
import java.util.ArrayList;

import javax.swing.JProgressBar;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;

import org.lunarray.lshare.gui.GUIUtil;
import org.lunarray.lshare.protocol.events.DownloadEvent;
import org.lunarray.lshare.protocol.events.DownloadListener;
import org.lunarray.lshare.protocol.events.UploadEvent;
import org.lunarray.lshare.protocol.events.UploadListener;

public class TransferModel implements TableModel, UploadListener,
        DownloadListener, TableCellRenderer {

    private ArrayList<TransferItem> transferitems;

    private ArrayList<TableModelListener> listeners;

    public TransferModel() {
        transferitems = new ArrayList<TransferItem>();
        listeners = new ArrayList<TableModelListener>();
    }

    // This is only for the
    public Component getTableCellRendererComponent(JTable arg0, Object arg1, 
            boolean arg2, boolean arg3, int arg4, int arg5) {
        if (arg5 == 3) {
            return transferitems.get(arg4).getProgressBar();
        } else {
            return null;
        }
    }
    
    public int getRowCount() {
        return transferitems.size();
    }

    public int getColumnCount() {
        // User
        // Remoteentry
        // Status
        // Progress
        // Localentry
        // Size
        return 6;
    }

    public String getColumnName(int arg0) {
        switch (arg0) {
        case 0:
            return "User";
        case 1:
            return "Remote file";
        case 2:
            return "Status";
        case 3:
            return "Progress";
        case 4:
            return "Filename";
        case 5:
            return "Size";
        default:
            return "";
        }
    }

    public Class<?> getColumnClass(int arg0) {
        if (arg0 == 3) {
            return JProgressBar.class;
        } else {
            return String.class;
        }
    }

    public Object getValueAt(int arg1, int arg0) {
        if (arg1 >= 0 && arg1 < transferitems.size()) {
            switch (arg0) {
            case 0:
                return transferitems.get(arg1).getRemoteUser().toString();
            case 1:
                return transferitems.get(arg1).getRemoteEntry().toString();
            case 2:
                return transferitems.get(arg1).getStatus();
            case 3:
                return transferitems.get(arg1).getProgressBar();
            case 4:
                return transferitems.get(arg1).getLocal();
            case 5:
                return GUIUtil.prettyPrint(transferitems.get(arg1).getSize());
            default:
                return "";
            }
        } else {
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

    public synchronized void downloadAdded(DownloadEvent e) {
        DownloadItem t = new DownloadItem(e.getTransfer());
        transferitems.add(t);

        int i = transferitems.indexOf(t);

        TableModelEvent ev = new TableModelEvent(this, i, i,
                TableModelEvent.ALL_COLUMNS, TableModelEvent.INSERT);
        for (TableModelListener l : listeners) {
            l.tableChanged(ev);
        }
    }

    public synchronized void downloadRemoved(DownloadEvent e) {
        TransferItem torem = null;
        search: {
            for (TransferItem t : transferitems) {
                if (t.getTransfer().equals(e.getTransfer())) {
                    torem = t;
                    break search;
                }
            }
        }
        if (torem != null) {
             int i = transferitems.indexOf(torem);
             transferitems.remove(torem);
            
             TableModelEvent ev = new TableModelEvent(this, i, i,
             TableModelEvent.ALL_COLUMNS, TableModelEvent.DELETE);
             for (TableModelListener l : listeners) {
                 l.tableChanged(ev);
             }
        }
    }

    public synchronized void downloadUpdated(DownloadEvent e) {
        TransferItem torem = null;
        search: {
            for (TransferItem t : transferitems) {
                if (t.getTransfer().equals(e.getTransfer())) {
                    torem = t;
                    break search;
                }
            }
        }
        if (torem != null) {
            // Update
            int i = transferitems.indexOf(torem);

            TableModelEvent ev = new TableModelEvent(this, i, i,
                    TableModelEvent.ALL_COLUMNS, TableModelEvent.UPDATE);
            for (TableModelListener l : listeners) {
                l.tableChanged(ev);
            }
        }
    }

    public synchronized void uploadAdded(UploadEvent e) {
        UploadItem t = new UploadItem(e.getTransfer());
        transferitems.add(t);

        int i = transferitems.indexOf(t);

        TableModelEvent ev = new TableModelEvent(this, i, i,
                TableModelEvent.ALL_COLUMNS, TableModelEvent.INSERT);
        for (TableModelListener l : listeners) {
            l.tableChanged(ev);
        }

    }

    public synchronized void uploadRemoved(UploadEvent e) {
        TransferItem torem = null;
        search: {
            for (TransferItem t : transferitems) {
                if (t.getTransfer().equals(e.getTransfer())) {
                    torem = t;
                    break search;
                }
            }
        }
        if (torem != null) {
             int i = transferitems.indexOf(torem);
             transferitems.remove(torem);
            
             TableModelEvent ev = new TableModelEvent(this, i, i,
             TableModelEvent.ALL_COLUMNS, TableModelEvent.DELETE);
             for (TableModelListener l : listeners) {
                 l.tableChanged(ev);
             }
        }
    }

    public synchronized void uploadUpdated(UploadEvent e) {
        TransferItem torem = null;
        search: {
            for (TransferItem t : transferitems) {
                if (t.getTransfer().equals(e.getTransfer())) {
                    torem = t;
                    break search;
                }
            }
        }
        if (torem != null) {
            // Update
            int i = transferitems.indexOf(torem);

            TableModelEvent ev = new TableModelEvent(this, i, i,
                    TableModelEvent.ALL_COLUMNS, TableModelEvent.UPDATE);
            for (TableModelListener l : listeners) {
                l.tableChanged(ev);
            }
        }
    }

    protected TransferItem getRow(int i) {
        return transferitems.get(i);
    }
   
    protected void updateTable() {
        for (int i = 0; i < transferitems.size(); i++ ) {
            transferitems.get(i).updateBar();
            TableModelEvent ev = new TableModelEvent(this, i, i,
                    3, TableModelEvent.UPDATE);
            for (TableModelListener l : listeners) {
                l.tableChanged(ev);
            }
        }
    }
}
