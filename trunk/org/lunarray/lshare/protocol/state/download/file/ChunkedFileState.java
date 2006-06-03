package org.lunarray.lshare.protocol.state.download.file;

/**
 * Allows representing the state a chunked file is in.
 * @author Pal Hargitai
 */
public enum ChunkedFileState {

    /**
     * The file is initialising.
     */
    INIT,

    /**
     * The file is closed. Ie. Not yet writable.
     */
    CLOSED,

    /**
     * The file is opened. Ie. Writable.
     */
    OPEN
}
