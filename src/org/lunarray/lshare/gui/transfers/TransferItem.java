package org.lunarray.lshare.gui.transfers;

import java.io.File;

import javax.swing.JProgressBar;

import org.lunarray.lshare.protocol.RemoteFile;
import org.lunarray.lshare.protocol.state.userlist.User;

/**
 * A representation of a transferring item.
 * @author Pal Hargitai
 */
public interface TransferItem {

    /**
     * Get the amount todo.
     * @return The amount todo.
     */
    public long getTodo();

    /**
     * Get the amount done.
     * @return The amount done.
     */
    public long getDone();

    /**
     * Get the total size of the transfer.
     * @return The total size.
     */
    public long getSize();

    /**
     * Get the remote entry.
     * @return The remote entry.
     */
    public RemoteFile getRemoteEntry();

    /**
     * Get the remote user.
     * @return The remote user.
     */
    public User getRemoteUser();

    /**
     * Update the progressbar.
     */
    public void updateBar();

    /**
     * Get the progressbar.
     * @return The progressbar.
     */
    public JProgressBar getProgressBar();

    /**
     * Get the local file.
     * @return The local file.
     */
    public File getLocal();

    /**
     * Get the status of the transfer.
     * @return The status of the transfer.
     */
    public String getStatus();

    /**
     * Get the transfer itself.
     * @return The transfer.
     */
    public Object getTransfer();

    /**
     * Cancel the transfer.
     */
    public void cancel();
}
