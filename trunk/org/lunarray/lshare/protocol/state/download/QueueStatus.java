package org.lunarray.lshare.protocol.state.download;

/**
 * The status of a queued item.
 * @author Pal Hargitai
 */
public enum QueueStatus {
    /**
     * Item is queued.
     */
    QUEUED,

    /**
     * Item is running.
     */
    RUNNING,

    /**
     * Item is stopped.
     */
    STOPPED,

    /**
     * Item is done.
     */
    FINISHED,

    /**
     * Item has transferred corruptly.
     */
    CORRUPT
}
