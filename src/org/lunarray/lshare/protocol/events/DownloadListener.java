package org.lunarray.lshare.protocol.events;

/**
 * A download listener.
 * @author Pal Hargitai
 */
public interface DownloadListener {
    /**
     * Triggered when a download has been added.
     * @param e The event associated with the download.
     */
    public void downloadAdded(DownloadEvent e);

    /**
     * Triggered when a download has been removed.
     * @param e The event associated with the download.
     */
    public void downloadRemoved(DownloadEvent e);

    /**
     * Triggered when a download has been updated.
     * @param e The event associated with the download.
     */
    public void downloadUpdated(DownloadEvent e);
}
