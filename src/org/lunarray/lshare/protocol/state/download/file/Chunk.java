package org.lunarray.lshare.protocol.state.download.file;

import java.io.IOException;
import java.util.concurrent.Semaphore;

public class Chunk {

	private ChunkedFile file;
	private Semaphore locksem;
	private long begin;
	private long end;
	
	protected Chunk(ChunkedFile f, long b, long e) {
		file = f;
		locksem = new Semaphore(1);
		begin = b;
		end = e;
	}
	
	public long getBegin() {
		return begin;
	}
	
	public long getEnd() {
		return end;
	}
	
	protected void setBegin(long b) {
		begin = b;
	}
	
	protected void setEnd(long e) {
		end = e;
	}
	
	public long getTodo() {
		return end - begin;
	}
	
	public boolean isDone() {
		return end == begin;
	}
	
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
	}
	
	/**
	 * Asks wether there is a lock on the semaphore, that is. There are no
	 * available permits.
	 * @return True if the chunk is locked, false otherwise.
	 */
	public boolean isLocked() {
		return locksem.availablePermits() == 0;
	}
	
	public void write(byte[] d, int len) throws IOException, InvalidFileStateException {
		if (begin < end) {
			int writable = Long.valueOf(Math.min(len, getTodo())).intValue();
			file.write(d, writable);
			begin += writable;
		}
	}
}
