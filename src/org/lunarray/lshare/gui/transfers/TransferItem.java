package org.lunarray.lshare.gui.transfers;

import java.io.File;

import javax.swing.JProgressBar;

import org.lunarray.lshare.protocol.RemoteFile;
import org.lunarray.lshare.protocol.state.userlist.User;

public interface TransferItem {

    public long getTodo();

    public long getDone();

    public long getSize();

    public RemoteFile getRemoteEntry();

    public User getRemoteUser();

    public void updateBar();

    public JProgressBar getProgressBar();

    public File getLocal();
    
    public String getStatus();
    
    public Object getTransfer();
    
    public void cancel();
}
