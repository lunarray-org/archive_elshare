package org.lunarray.lshare.protocol.events;

public interface DownloadListener {
    public void downloadAdded(DownloadEvent e);
    public void downloadRemoved(DownloadEvent e);
    public void downloadUpdated(DownloadEvent e);
}
