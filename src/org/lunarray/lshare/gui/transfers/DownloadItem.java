package org.lunarray.lshare.gui.transfers;

import java.awt.Color;
import java.io.File;

import javax.swing.JProgressBar;

import org.lunarray.lshare.protocol.RemoteFile;
import org.lunarray.lshare.protocol.state.download.DownloadHandler;
import org.lunarray.lshare.protocol.state.userlist.User;

public class DownloadItem implements TransferItem {

    private DownloadHandler transfer;

    private JProgressBar progressbar;

    public DownloadItem(DownloadHandler dh) {
        transfer = dh;
        progressbar = new JProgressBar();

        progressbar.setStringPainted(true);
        progressbar.setBackground(Color.GREEN);
        progressbar.setForeground(Color.GREEN.darker());
        
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
    
    public File getLocal() {
        return transfer.getFile().getFile();
    }

    public void updateBar() {
        if (getSize() > Integer.MAX_VALUE) {
            progressbar.setValue(Long.valueOf(getDone() / Integer.MAX_VALUE)
                    .intValue());
        } else {
            progressbar.setValue(Long.valueOf(getDone()).intValue());
        }
    }
    
    public Object getTransfer() {
        return transfer;
    }
    
    public String getStatus() {
        return transfer.getStatus().toString();
    }

    public JProgressBar getProgressBar() {
        return progressbar;
    }

    public long getTodo() {
        return transfer.getFile().getTodo();
    }

    public long getDone() {
        return transfer.getFile().getDone();
    }

    public long getSize() {
        return transfer.getFile().getSize();
    }

    public RemoteFile getRemoteEntry() {
        return transfer.getRemoteEntry();
    }

    public User getRemoteUser() {
        return transfer.getUser();
    }
}
