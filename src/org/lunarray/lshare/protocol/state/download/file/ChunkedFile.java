package org.lunarray.lshare.protocol.state.download.file;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.TreeMap;
import java.util.concurrent.Semaphore;

public class ChunkedFile {

	public static final long MIN_CHUNK = 100 * 1024;
	
	private TreeMap<Long, Chunk> chunks;
	private long size;
	private ChunkedFileState state;
	private File file;
	private RandomAccessFile access;
	private IncompleteFileSettings settings;
	private Semaphore fsem;
	
	public ChunkedFile(IncompleteFileSettings s) {
		chunks = new TreeMap<Long, Chunk>();
		state = ChunkedFileState.INIT;
		settings = s;
		fsem = new Semaphore(1);
	}
	
	public boolean isFinished() {
		return getTodo() == 0;
	}
	
	public void initFromBack() {
		size = settings.getSize();
		file = settings.getLocalTarget();
		
		long begin = 0;
		for (Long m: settings.getChunks().keySet()) {
			long mark = m.longValue();
			long end = settings.getChunks().get(m).longValue();
			
			Chunk c = new Chunk(this, begin, mark, end);
			chunks.put(c.getBegin(), c);
			
			begin = end;
		}
		
		state = ChunkedFileState.CLOSED;
	}
	
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
		for (Chunk c: chunks.values()) {
			settings.setChunk(c.getMark(), c.getEnd());
		}
	}
	
	protected void cleanChunks() {
		try {
			fsem.acquire();
		} catch (InterruptedException ie) {
			// Break
			return;
		}
		// Cleanup empties
		
		// cleanup ahead, cleanup back
		for (Chunk c: chunks.values()) {
			if (c.isDone()) {
				if (chunks.containsKey(c.getEnd())) {
					Chunk next = chunks.get(c.getEnd());
					
					chunks.remove(next.getBegin());
					chunks.remove(c.getBegin());
					
					next.setBegin(c.getBegin());
					chunks.put(next.getBegin(), next);
				}
			} else {
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
	
	public Chunk getChunk() throws IllegalAccessException, IllegalStateException {
		if (state == ChunkedFileState.INIT) {
			throw new IllegalStateException();
		}
		
		try {
			fsem.acquire();
		} catch (InterruptedException ie) {
			throw new IllegalAccessException();
		}
		
		Chunk toret = null;
		
		search: {
			for (Chunk c: chunks.values()) {
				if (!c.isLocked()) {
					toret = c;
					break search;
				}
			}
		}
		
		search: {
			if (toret != null) {
				break search;
			}
			
			Chunk bgst = null;
			for (Chunk c: chunks.values()) {
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
		
		if (toret == null) {
			throw new IllegalAccessException();
		} else {
			return toret;
		}		
	}
	
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
	
	protected synchronized void write(byte[] b, int len, long mark) throws IOException, IllegalStateException {
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
	
	public long getDone() {
		return getSize() - getTodo();
	}
	
	public long getTodo() {
		long todo = 0;
		for (Chunk c: chunks.values()) {
			todo += c.getTodo();
		}
		return todo;
	}
	
	public long getSize() {
		return size;
	}
}
