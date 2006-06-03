package org.lunarray.lshare.protocol.state.download;

import org.lunarray.lshare.protocol.Hash;
import org.lunarray.lshare.protocol.RemoteFile;

/**
 * The response of a request for a file transfer.
 * @author Pal Hargitai
 */
public class FileResponse extends RemoteFile {
    /**
     * The port that is to be downloaded to.
     */
    private int port;

    /**
     * The offset of the download.
     */
    private long offset;

    /**
     * Constructs a file response.
     * @param p The path of the file.
     * @param n The name of the file.
     * @param h The hash of the file.
     * @param s The size of the file.
     * @param o The offset of downloading.
     * @param r The port where downloading should occur.
     */
    public FileResponse(String p, String n, Hash h, long s, long o, int r) {
        super(p, n, h, 1, s);
        port = r;
        offset = o;
    }

    /**
     * Get the port at which download should start.
     * @return The port number.
     */
    public int getPort() {
        return port;
    }

    /**
     * Get the offset of the file.
     * @return Get the files offset.
     */
    public long getOffset() {
        return offset;
    }
}
