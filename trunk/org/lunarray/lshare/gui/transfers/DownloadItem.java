package org.lunarray.lshare.gui.transfers;

import java.awt.Color;
import java.io.File;

import javax.swing.JProgressBar;

import org.lunarray.lshare.protocol.RemoteFile;
import org.lunarray.lshare.protocol.state.download.DownloadHandler;
import org.lunarray.lshare.protocol.state.userlist.User;

/**
 * An item representing a download.
 * @author Pal Hargitai
 */
public class DownloadItem implements TransferItem {

    /**
     * The download to represent.
     */
    private DownloadHandler transfer;

    /**
     * The progressbar.
     */
    private JProgressBar progressbar;

    /**
     * Given a transfer, constructs the item.
     * @param dh The transfer.
     */
    public DownloadItem(DownloadHandler dh) {
        transfer = dh;
        // Setup bar
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

    /**
     * Get the local file.
     * @return The local file.
     */
    public File getLocal() {
        return transfer.getFile().getFile();
    }

    /**
     * Update the progressbar.
     */
    public void updateBar() {
        progressbar.setValue(Long.valueOf(
                getSize() > Integer.MAX_VALUE ? getDone() / Integer.MAX_VALUE
                        : getDone()).intValue());
    }

    /**
     * Get the transfer.
     */
    public Object getTransfer() {
        return transfer;
    }

    /**
     * Get the status of the transfer.
     */
    public String getStatus() {
        return transfer.getStatus().toString();
    }

    /**
     * Get the progressbar.
     */
    public JProgressBar getProgressBar() {
        return progressbar;
    }

    /**
     * Get the amount todo.
     */
    public long getTodo() {
        return transfer.getFile().getTodo();
    }

    /**
     * Get the amount done.
     */
    public long getDone() {
        return transfer.getFile().getDone();
    }

    /**
     * Get the size.
     */
    public long getSize() {
        return transfer.getFile().getSize();
    }

    /**
     * Get the remote entry.
     */
    public RemoteFile getRemoteEntry() {
        return transfer.getRemoteEntry();
    }

    /**
     * Get the remote user.
     */
    public User getRemoteUser() {
        return transfer.getUser();
    }

    /**
     * Cancel the transfer.
     */
    public void cancel() {
        transfer.cancel();
    }
}
