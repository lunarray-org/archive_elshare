package org.lunarray.lshare.protocol.state.download.file;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.TreeMap;
import java.util.concurrent.Semaphore;

/**
 * A chunked file.
 * @author Pal Hargitai
 */
public class ChunkedFile {
    /**
     * The minimum chunk size. This is generally {@value}.
     */
    public static final long MIN_CHUNK = 100 * 1024;

    /**
     * All chunks in the file. These chunks should comprise all parts of the
     * file. That is, their converage should be complete.
     */
    private TreeMap<Long, Chunk> chunks;

    /**
     * The size of the file.
     */
    private long size;

    /**
     * The state of the file.
     */
    private ChunkedFileState state;

    /**
     * The file that is to be downloaded to.
     */
    private File file;

    /**
     * The stream for downloading the file to.
     */
    private RandomAccessFile access;

    /**
     * The settings of the file.
     */
    private IncompleteFileSettings settings;

    /**
     * The semaphore for synchronising access.
     */
    private Semaphore fsem;

    /**
     * Constructs a new chunked file.
     * @param s The settings of the file.
     */
    public ChunkedFile(IncompleteFileSettings s) {
        chunks = new TreeMap<Long, Chunk>();
        state = ChunkedFileState.INIT;
        settings = s;
        fsem = new Semaphore(1);
    }

    /**
     * Checks if the file is finished.
     * @return True if the file is finished, false if not.
     */
    public boolean isFinished() {
        return getTodo() == 0;
    }
    
    public boolean inProgress() {
        for (Chunk c: chunks.values()) {
            if (c.isLocked()) {
                return true;
            }
        }
        return false;
    }

    public boolean isEmpty() {
        for (Chunk c: chunks.values()) {
            if (!c.isEmpty()) {
                return false;
            }
        }
        return true;
    }
    
    /**
     * Initialise the file from the backend.
     */
    public void initFromBack() {
        size = settings.getSize();
        file = settings.getLocalTarget();

        // populate all chunks.
        long begin = 0;
        for (Long m : settings.getChunks().keySet()) {
            long mark = m.longValue();
            long end = settings.getChunks().get(m).longValue();

            Chunk c = new Chunk(this, begin, mark, end);
            chunks.put(c.getBegin(), c);

            begin = end;
        }

        state = ChunkedFileState.CLOSED;
    }

    /**
     * Get a chunk. Checks wether there is an unlocked chunk ready for download.
     * Or find a large chunk to split.
     * @return A chunk to download to.
     * @throws IllegalAccessException No available chunk.
     * @throws IllegalStateException The file is not in a state where chunks can
     * be gotten.
     */
    public Chunk getChunk() throws IllegalAccessException,
            IllegalStateException {
        if (state == ChunkedFileState.INIT) {
            throw new IllegalStateException();
        }

        // Lock the file
        try {
            fsem.acquire();
        } catch (InterruptedException ie) {
            throw new IllegalAccessException();
        }

        Chunk toret = null;

        // Get an unlocked chunk
        search: {
            for (Chunk c : chunks.values()) {
                if (!c.isLocked()) {
                    toret = c;
                    break search;
                }
            }
        }

        // Find a big chunk
        search: {
            if (toret != null) {
                break search;
            }

            // Get biggest
            Chunk bgst = null;
            for (Chunk c : chunks.values()) {
                if (bgst == null) {
                    bgst = c;
                } else {
                    if (c.getTodo() > bgst.getTodo()) {
                        bgst = c;
                    }
                }
            }
            if (bgst.getTodo() < (MIN_CHUNK * 2)) {
                break search;
            }

            // Split biggest
            long mid = bgst.getMark() + (bgst.getTodo() / 2);

            Chunk newc = new Chunk(this, mid, mid, bgst.getEnd());
            bgst.setEnd(mid);

            chunks.put(newc.getBegin(), newc);

            toret = newc;
        }

        fsem.release();

        // If there is one, return it, otherwise throw.
        if (toret == null) {
            throw new IllegalAccessException();
        } else {
            return toret;
        }
    }

    /**
     * Get how much downloading is done.
     * @return The amount that is done.
     */
    public long getDone() {
        return getSize() - getTodo();
    }

    /**
     * Get how much downloading is still to be done.
     * @return The amount todo.
     */
    public long getTodo() {
        long todo = 0;
        for (Chunk c : chunks.values()) {
            todo += c.getTodo();
        }
        return todo;
    }

    /**
     * Get the total size of the file.
     * @return The size of the file.
     */
    public long getSize() {
        return size;
    }

    /**
     * Initialise from the front end.
     * @param f The file to download to.
     * @param s The size of the file.
     * @throws IllegalStateException Thrown if the file is already initialised.
     */
    protected void initFromFront(File f, long s) throws IllegalStateException {
        if (state == ChunkedFileState.INIT) {
            size = s;
            file = f;

            Chunk c = new Chunk(this, 0, 0, size);
            chunks.put(c.getBegin(), c);

            state = ChunkedFileState.CLOSED;
        } else {
            throw new IllegalStateException();
        }
    }

    /**
     * Write data to the file.
     * @param b The
     * @param len The length of the data to write.
     * @param mark The mark of the data.
     * @throws IOException Thrown if it could not write.
     * @throws IllegalStateException Thrown if the file is in a bad state for
     * writing.
     */
    protected synchronized void write(byte[] b, int len, long mark)
            throws IOException, IllegalStateException {
        try {
            fsem.acquire();
        } catch (InterruptedException ie) {
            throw new IllegalStateException();
        }

        switch (state) {
        case INIT:
            fsem.release();
            throw new IllegalStateException();
        case CLOSED:
            access = new RandomAccessFile(file, "rw");
            state = ChunkedFileState.OPEN;
            break;
        }
        // Should be fine now start writing
        access.seek(mark);
        access.write(b);
        fsem.release();
    }

    /**
     * Close down the file.
     */
    protected void close() {
        try {
            cleanChunks();

            fsem.acquire();

            if (access != null) {
                access.close();
            }
        } catch (InterruptedException ie) {
            // Ignore and go on
        } catch (IOException ie) {
            // Ignore and go on
        }

        settings.setSize(size);
        settings.setLocalTarget(file);
        settings.clearChunks();
        for (Chunk c : chunks.values()) {
            settings.setChunk(c.getMark(), c.getEnd());
        }
    }

    /**
     * Clean all chunks. That is, merge them forwards and backwards.
     */
    protected void cleanChunks() {
        try {
            fsem.acquire();
        } catch (InterruptedException ie) {
            // Break
            return;
        }
        // cleanup ahead, cleanup back
        for (Chunk c : chunks.values()) {
            if (c.isDone()) {
                // Merge forward.
                if (chunks.containsKey(c.getEnd())) {
                    Chunk next = chunks.get(c.getEnd());

                    chunks.remove(next.getBegin());
                    chunks.remove(c.getBegin());

                    next.setBegin(c.getBegin());
                    chunks.put(next.getBegin(), next);
                }
            } else {
                // Merge backward
                if (chunks.containsKey(c.getEnd())) {
                    Chunk next = chunks.get(c.getEnd());

                    if (next.isEmpty()) {
                        chunks.remove(next.getBegin());

                        c.setEnd(next.getEnd());
                    }
                }
            }
        }

        fsem.release();
    }
}
