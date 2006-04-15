package org.lunarray.lshare.protocol.state.download.file;

import java.io.IOException;
import java.util.TreeMap;

public class ChunkedFile {

	private TreeMap<Long, Chunk> chunks;
	private long size;
	
	protected void write(byte[] b, int len) throws IOException {
		
	}
	
	protected void setSize(long s) {
		// Bla
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
