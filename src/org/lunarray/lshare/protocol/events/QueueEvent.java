package org.lunarray.lshare.protocol.events;

import org.lunarray.lshare.protocol.state.download.DownloadManager;
import org.lunarray.lshare.protocol.state.download.file.IncompleteFile;

/**
 * An event in the download queue.
 * @author Pal Hargitai
 */
public class QueueEvent {
    /**
     * The incomplete file added.
     */
    private IncompleteFile file;

    /**
     * The source of the event.
     */
    private DownloadManager source;

    /**
     * Constructs the queue event.
     * @param f The incomplete file of the event.
     * @param m The download manager. Ie. The source of the event.
     */
    public QueueEvent(IncompleteFile f, DownloadManager m) {
        file = f;
        source = m;
    }

    /**
     * Gets the file in the event.
     * @return The file.
     */
    public IncompleteFile getFile() {
        return file;
    }

    /**
     * Gets the source of the event. Ie. The download manager.
     * @return The source of the event.
     */
    public DownloadManager getSource() {
        return source;
    }
}
