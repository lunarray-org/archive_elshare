package org.lunarray.lshare.protocol.events;

import org.lunarray.lshare.protocol.state.upload.UploadManager;
import org.lunarray.lshare.protocol.state.upload.UploadTransfer;

/**
 * An upload event.
 * @author Pal Hargitai
 */
public class UploadEvent {
    /**
     * The transfer in the event.
     */
    private UploadTransfer transfer;

    /**
     * The source of the event.
     */
    private UploadManager source;

    /**
     * Constructs an upload event.
     * @param t The transfer of the event.
     * @param m The manager of the event.
     */
    public UploadEvent(UploadTransfer t, UploadManager m) {
        transfer = t;
        source = m;
    }

    /**
     * Gets the transfer.
     * @return The transfer.
     */
    public UploadTransfer getTransfer() {
        return transfer;
    }

    /**
     * Gets the source of the event. That is, the upload manager.
     * @return The source of the event.
     */
    public UploadManager getSource() {
        return source;
    }
}
