package org.lunarray.lshare.protocol.state.download.file;

import org.lunarray.lshare.protocol.Hash;
import org.lunarray.lshare.protocol.RemoteFile;

/**
 * A remote file that is localy queued.
 * @author Pal Hargitai
 */
public class QueuedRemote extends RemoteFile {
    /**
     * Constructs a queued entry.
     * @param path The path of the file.
     * @param name The name of the file.
     * @param h The hash of the file.
     * @param size The size of the file.
     */
    public QueuedRemote(String path, String name, Hash h, long size) {
        super(path, name, h, 1, size);
    }
}
