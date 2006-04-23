package org.lunarray.lshare.protocol.events;

public interface QueueListener {
    public void queueAdded(QueueEvent e);
    public void queueRemoved(QueueEvent e);
    public void queueUpdated(QueueEvent e);
}
