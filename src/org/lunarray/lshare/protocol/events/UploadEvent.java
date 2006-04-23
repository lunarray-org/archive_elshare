package org.lunarray.lshare.protocol.events;

import org.lunarray.lshare.protocol.state.upload.UploadManager;
import org.lunarray.lshare.protocol.state.upload.UploadTransfer;

public class UploadEvent {
    private UploadTransfer transfer;
    private UploadManager source;
    
    public UploadEvent(UploadTransfer t, UploadManager m) {
        transfer = t;
        source = m;
    }
    
    public UploadTransfer getTransfer() {
        return transfer;
    }
    
    public UploadManager getSource() {
        return source;
    }
}
