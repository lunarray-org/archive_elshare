package org.lunarray.lshare.protocol.state.download.file;

import java.io.IOException;
import java.util.TreeMap;

public class ChunkedFile {

	private TreeMap<Long, Chunk> chunks;
	private long size;
	
//	 TODO
	public ChunkedFile() {
//		 TODO 
	}
	
	protected void write(byte[] b, int len) throws IOException, InvalidFileStateException {
//		 TODO
	}
	
	protected void setSize(long s) throws InvalidFileStateException {
//		 TODO
		if (size > 0) {
			throw new InvalidFileStateException();
		} else {
			size = s;
		}
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
