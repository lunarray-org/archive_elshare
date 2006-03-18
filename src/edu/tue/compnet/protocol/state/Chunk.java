package edu.tue.compnet.protocol.state;

import java.util.concurrent.Semaphore;

/**
 * A chunk is a part of a file. The chunk has 3 important variables these are
 * the beginning, mark and end. Assume that the beginning of chunk t is lt,
 * the mark of chunk t is mt and the end of chunk t is kt. There are n chunks
 * Then these will hold:<br>
 * <ul>
 * 	<li>lt &lt;= mt &lt;= kt</li>
 * 	<li>k(t-1) == lt</li>
 * 	<li>kt == l(t+1)</li>
 * 	<li>k0 == 0</li>
 * 	<li>ln == file.length</li>
 * 	<li>mt &lt; kt =&gt; an amount of kt - mt is still to be downloaded</li>
 * 	<li>mt == kt =&gt; an amount of 0 is still to be downloaded, ie. the chunk
 * is finished</li>
 * 	<li>lt == mt =&gt; an amount of 0 is downloaded, ie. the chunk is
 * fresh</li>
 * 	<li>kt == lt =&th; the chunk is empty.</li>
 * 	<li>kt - lt == the chunk size</li>
 * </ul>
 * @author Pal Hargitai
 * @author Siu-Hong Li
 */
public class Chunk {
	// The begin position of the chunk
	int begin;
	// The current position relative to the file
	int mark;
	// The end position of the chunk
	int end;
	// The file
	TransferFile file;
	// The lock on this chunk
	Semaphore locksem;
	
	/**
	 * The constructor of the chunk. 
	 * @param f The file it's in.
	 * @param b The beginning.
	 * @param m The mark
	 * @param e The end.
	 */
	public Chunk(TransferFile f, int b, int m, int e) {
		// The semaphore that allows locking
		locksem = new Semaphore(1);
		// Set the vars
		begin = b;
		mark = m;
		end = e;
		file = f;
	}

	/**
	 * Get the amount of to download bytes. kt - mt.
	 * @return The amount todo.
	 */
	public int getTodo() {
		return end - mark;
	}
	
	/**
	 * The amount downloaded mt - lt.
	 * @return The amount done.
	 */
	public int getDone() {
		return mark - begin;
	}
	
	/**
	 * Checks wether the chunk is finished.
	 * @return True if the chunk is finishe.
	 */
	public boolean isFinished() {
		return !(mark < end);
	}
	
	/**
	 * Checks wether this chunk is empty.
	 * @return True if no bytes have been downloaded here.
	 */
	public boolean isEmpty() {
		return !(begin < mark);
	}
	
	/**
	 * Writes a given array to the file. Offset in file is the mark.
	 * @param data The data to write.
	 * @param length The length of the data.
	 */
	public void write(byte[] data, int length) {
		if (mark < end) {
			/*
			 * We don't want the chunk to write outside it's bounds.
			 * Write the given data to the file
			 * Mark moves written length on.
			 */
			int writable = Math.min(length, getTodo());
			file.write(data, writable, mark);
			mark += writable;
			
			// Update transfer
			file.getTransfer().setTodo(file.getTodo());
		}
	}
	
	/**
	 * Set the beginning to the given value. The file should check wether this
	 * turns out well.
	 * @param nb The new beginning.
	 */
	protected void setBegin(int nb) {
		// We presume the file checked this.
		begin = nb;
	}
	
	/**
	 * Set the end to the given value. The file should check wether this turns
	 * out well.
	 * @param nb The new beginning.
	 */
	protected void setEnd(int nb) {
		// We presume the file checked this.
		end = nb;
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
	
	/**
	 * Gets the beginning of this chunk, lt.
	 * @return The beginning of this chunk, lt.
	 */
	public int getBegin() {
		return begin;
	}
	
	/**
	 * Gets the end of this chunk, kt.
	 * @return The end of this chunk, kt.
	 */
	public int getEnd() {
		return end;
	}
	
	/**
	 * Gets the current mark in the file, that is. The current cursor position
	 * of this chunk, mt.
	 * @return The place in the file, mt.
	 */
	public int getMark() {
		/*
		 * This should check wether the given mark is the current mark.
		 */
		return mark;
	}
	
	/**
	 * Gets the transfer file this chunk is associated with.
	 * @return The file.
	 */
	public TransferFile getTransferFile() {
		return file;
	}
	
	/**
	 * Get the length of the entire chunk.
	 * @return The length.
	 */
	public int getLength() {
		return getEnd() - getBegin();
	}
	
	/**
	 * Get the name of the file
	 * @return The file name.
	 */
	public String getName() {
		return file.getFile().getName();
	}
}
