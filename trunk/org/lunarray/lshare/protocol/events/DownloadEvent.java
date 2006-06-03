package org.lunarray.lshare.protocol.events;

import org.lunarray.lshare.protocol.state.download.DownloadManager;
import org.lunarray.lshare.protocol.state.download.DownloadHandler;

/**
 * A download event.
 * @author Pal Hargitai
 */
public class DownloadEvent {
    /**
     * The transfer in the event.
     */
    private DownloadHandler transfer;

    /**
     * The source of the event.
     */
    private DownloadManager source;

    /**
     * Constructs a download event.
     * @param t The transfer of the event.
     * @param m The manager of the event.
     */
    public DownloadEvent(DownloadHandler t, DownloadManager m) {
        transfer = t;
        source = m;
    }

    /**
     * Gets the transfer.
     * @return The transfer.
     */
    public DownloadHandler getTransfer() {
        return transfer;
    }

    /**
     * Gets the source of the event. That is, the download manager.
     * @return The source of the event.
     */
    public DownloadManager getSource() {
        return source;
    }
}
