package org.lunarray.lshare.gui.transfers;

import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import org.lunarray.lshare.LShare;
import org.lunarray.lshare.gui.GUIFrame;
import org.lunarray.lshare.gui.MainGUI;

public class TransferList extends GUIFrame {
    
    private JTable table;
    private TransferModel model;
    private Timer timer;
    
    public TransferList(LShare ls, MainGUI mg) {
        super(mg);
        
        timer = new Timer();
        
        model = new TransferModel();
        ls.getDownloadManager().addTransferListener(model);
        ls.getUploadManager().addListener(model);
        table = new JTable(model);
        table.setDefaultRenderer(JProgressBar.class, model);
        
        JScrollPane sp = new JScrollPane(table);
        
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                model.updateTable();
            }
        }, 0, 1000);
        
        // The frame
        frame.setTitle(getTitle());
        frame.add(sp);
    }
    
    @Override
    /**
     * Hides the frame.
     */
    public void close() {
        frame.setVisible(false);
        timer.cancel();
    }

    /**
     * Gets the title of the frame.
     * @return The title of the frame.
     */
    public String getTitle() {
        return "Transfers";
    }
}
