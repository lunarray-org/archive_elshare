package org.lunarray.lshare.protocol.events;

import org.lunarray.lshare.protocol.state.download.DownloadManager;
import org.lunarray.lshare.protocol.state.download.file.IncompleteFile;

public class QueueEvent {
    private IncompleteFile file;
    private DownloadManager source;
    
    public QueueEvent(IncompleteFile f, DownloadManager m) {
        file = f;
        source = m;
    }
    
    public IncompleteFile getFile() {
        return file;
    }
    
    public DownloadManager getSource() {
        return source;
    }
}
