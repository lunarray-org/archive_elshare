package org.lunarray.lshare.protocol.events;

/**
 * An upload listener.
 * @author Pal Hargitai
 */
public interface UploadListener {
    /**
     * Triggered when an upload has been added.
     * @param e The event associated with the upload.
     */
    public void uploadAdded(UploadEvent e);

    /**
     * Triggered when an upload has been removed.
     * @param e The event associated with the upload.
     */
    public void uploadRemoved(UploadEvent e);

    /**
     * Triggered when an upload has been updated.
     * @param e The event associated with the upload.
     */
    public void uploadUpdated(UploadEvent e);
}
