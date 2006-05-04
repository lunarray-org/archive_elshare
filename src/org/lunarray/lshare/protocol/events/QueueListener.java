package org.lunarray.lshare.protocol.events;

/**
 * A queue listener.
 * @author Pal Hargitai
 */
public interface QueueListener {
    /**
     * Triggered when a queue item has been added.
     * @param e The event associated with the queue item.
     */
    public void queueAdded(QueueEvent e);

    /**
     * Triggered when a queue item has been removed.
     * @param e The event associated with the queue item.
     */
    public void queueRemoved(QueueEvent e);

    /**
     * Triggered when a queue item has been updated.
     * @param e The event associated with the queue item.
     */
    public void queueUpdated(QueueEvent e);
}
