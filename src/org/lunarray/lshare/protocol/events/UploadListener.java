package org.lunarray.lshare.protocol.events;

public interface UploadListener {
    public void uploadAdded(UploadEvent e);
    public void uploadRemoved(UploadEvent e);
    public void uploadUpdated(UploadEvent e);
}
