package org.lunarray.lshare.protocol.state.upload;

import org.lunarray.lshare.protocol.Hash;
import org.lunarray.lshare.protocol.RemoteFile;

/** A request for uploading a file.
 * @author Pal Hargitai
 */
public class UploadRequest extends RemoteFile {
    /** The offset at which to start uploading.
     */
    private long offset;
    
    /** Constructs the upload request.
     * @param p The path of the file.
     * @param n The name of the file.
     * @param h The hash of the file.
     * @param s The size of the file.
     * @param o The offset at which to start uploading.
     */
    public UploadRequest(String p, String n, Hash h, long s, long o) {
        super(p, n, h, 1, s);
    }
    
    /** The offset at which to start uploading.
     * @return The offset.
     */
    public long getOffset() {
        return offset;
    }
}