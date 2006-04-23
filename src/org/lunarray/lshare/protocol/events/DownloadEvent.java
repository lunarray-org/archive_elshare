package org.lunarray.lshare.protocol.events;

import org.lunarray.lshare.protocol.state.download.DownloadManager;
import org.lunarray.lshare.protocol.state.download.DownloadHandler;

public class DownloadEvent {
    private DownloadHandler transfer;
    private DownloadManager source;
    
    public DownloadEvent(DownloadHandler t, DownloadManager m) {
        transfer = t;
        source = m;
    }
    
    public DownloadHandler getTransfer() {
        return transfer;
    }
    
    public DownloadManager getSource() {
        return source;
    }
}
