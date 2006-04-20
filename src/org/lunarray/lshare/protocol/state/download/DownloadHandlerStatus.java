package org.lunarray.lshare.protocol.state.download;

/**
 * Represents the status of a download handler.
 * @author Pal Hargitai
 */
public enum DownloadHandlerStatus {
    /**
     * Handler is initialising.
     */
    INIT,

    /**
     * Handler is connecting.
     */
    CONNECTING,

    /**
     * The transfer is running.
     */
    RUNNING
}
