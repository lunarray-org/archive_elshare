package org.lunarray.lshare.protocol.state.download.file;

import java.io.IOException;
import java.util.concurrent.Semaphore;

/**
 * A single chunk of a file.
 * @author Pal Hargitai
 */
public class Chunk {
    /**
     * The file that this chunk resides in.
     */
    private ChunkedFile file;

    /**
     * The semaphore for locking access to this file.
     */
    private Semaphore locksem;

    /**
     * The beginning of this chunk in the file.
     */
    private long begin;

    /**
     * The end of this chunk in the file.
     */
    private long end;

    /**
     * The mark to which is downloaded.
     */
    private long mark;

    /**
     * Creates a chunk
     * @param f The file that this chunk is for.
     * @param b The beginning of this chunk.
     * @param m The mark of this chunk.
     * @param e The end of this chunk.
     */
    protected Chunk(ChunkedFile f, long b, long m, long e) {
        file = f;
        locksem = new Semaphore(1);
        begin = b;
        mark = m;
        end = e;
    }

    /**
     * Writes a piece of data to the file.
     * @param d The data to write.
     * @param len The length of the data.
     * @throws IOException Thrown if writing failed.
     * @throws IllegalStateException Thrown if the file is in a bad state for
     * writing.
     */
    public void write(byte[] d, int len) throws IOException,
            IllegalStateException {
        if (mark < end) {
            int writable = Long.valueOf(Math.min(len, getTodo())).intValue();
            file.write(d, writable, mark);
            mark += writable;
        }
    }

    /**
     * Get the beginning of the chunk.
     * @return The beginning.
     */
    public long getBegin() {
        return begin;
    }

    /**
     * Get the end of the chunk.
     * @return The end.
     */
    public long getEnd() {
        return end;
    }

    /**
     * Get the mark of this chunk.
     * @return The mark.
     */
    public long getMark() {
        return mark;
    }

    /**
     * Get the amount of data still to download to this chunk.
     * @return The amount to still download.
     */
    public long getTodo() {
        return end - mark;
    }

    /**
     * Check if the chunk is done.
     * @return True if the chunk is done, false if not.
     */
    public boolean isDone() {
        return end <= mark;
    }

    /**
     * Check if this chunk has not yet downloaded anything.
     * @return True if the download is yet to download anything, false if not.
     */
    public boolean isEmpty() {
        return mark <= begin;
    }

    /**
     * Get the file that this chunk is part of.
     * @return The file.
     */
    public ChunkedFile getFile() {
        return file;
    }

    /**
     * Acquire a lock on the chunk.
     */
    public void lock() {
        try {
            locksem.acquire();
        } catch (InterruptedException ie) {
            // The lock couldn't be acquired
        }
    }

    /**
     * Release the lock on the chunk.
     */
    public void unlock() {
        locksem.release();
        file.cleanChunk(this);
    }

    /**
     * Asks wether there is a lock on the semaphore, that is. There are no
     * available permits.
     * @return True if the chunk is locked, false otherwise.
     */
    public boolean isLocked() {
        return locksem.availablePermits() == 0;
    }

    /**
     * Set the mark of this chunk.
     * @param m The new mark.
     */
    protected void setMark(long m) {
        mark = m;
    }

    /**
     * Set the beginning of this chunk.
     * @param b The new beginning.
     */
    protected void setBegin(long b) {
        begin = b;
    }

    /**
     * Set the end of this chunk.
     * @param e The new end.
     */
    protected void setEnd(long e) {
        end = e;
    }
}
