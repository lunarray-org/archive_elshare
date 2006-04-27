package org.lunarray.lshare.gui.transfers;

import java.awt.Color;
import java.io.File;

import javax.swing.JProgressBar;

import org.lunarray.lshare.protocol.RemoteFile;
import org.lunarray.lshare.protocol.state.upload.UploadTransfer;
import org.lunarray.lshare.protocol.state.userlist.User;

public class UploadItem implements TransferItem {

    private UploadTransfer upload;
    
    private JProgressBar progressbar;

    public UploadItem(UploadTransfer dh) {
        upload = dh;
        progressbar = new JProgressBar();

        progressbar.setStringPainted(true);
        progressbar.setBackground(Color.RED);
        progressbar.setForeground(Color.RED.darker());
        
        progressbar.setMinimum(0);
        if (getSize() > Integer.MAX_VALUE) {
            progressbar.setMaximum(Long.valueOf(getSize() / Integer.MAX_VALUE)
                    .intValue());
            progressbar.setValue(Long.valueOf(getDone() / Integer.MAX_VALUE)
                    .intValue());
        } else {
            progressbar.setMaximum(Long.valueOf(getSize()).intValue());
            progressbar.setValue(Long.valueOf(getDone()).intValue());
        }
    }
    
    public Object getTransfer() {
        return upload;
    }
    
    public String getStatus() {
        if (upload.isDone()) {
            return "Finished";
        } else {
            if (upload.isRunning()) {
                return "Transferring";
            } else {
                return "Connecting";
            }
        }
    }
    
    public File getLocal() {
        return upload.getFile();
    }
    
    public long getTodo() {
        return upload.getTodo();
    }

    public long getDone() {
        return upload.getDone();
    }

    public long getSize() {
        return upload.getSize();
    }

    public RemoteFile getRemoteEntry() {
        return upload.getRequest();
    }

    public User getRemoteUser() {
        return upload.getUser();
    }

    public void updateBar() {
        if (getSize() > Integer.MAX_VALUE) {
            progressbar.setValue(Long.valueOf(getDone() / Integer.MAX_VALUE)
                    .intValue());
        } else {
            progressbar.setValue(Long.valueOf(getDone()).intValue());
        }
    }

    public JProgressBar getProgressBar() {
        return progressbar;
    }
}
