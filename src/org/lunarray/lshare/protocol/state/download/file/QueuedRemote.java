package org.lunarray.lshare.protocol.state.download.file;

import org.lunarray.lshare.protocol.Hash;
import org.lunarray.lshare.protocol.RemoteFile;

public class QueuedRemote extends RemoteFile {

    public QueuedRemote(String path, String name, Hash h, long size) {
        super(path, name, h, 1, size);
    }

}
