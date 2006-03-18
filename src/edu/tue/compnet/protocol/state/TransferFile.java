package edu.tue.compnet.protocol.state;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.concurrent.Semaphore;

import edu.tue.compnet.Output;
import edu.tue.compnet.protocol.Settings;
import edu.tue.compnet.protocol.State;

/**
 * The class that contains a transferrable file. This file consists of a bunch
 * of chunks. For further information, see the chunk.
 * @see edu.tue.compnet.protocol.state.Chunk
 * @author Pal Hargitai
 * @author Siu-Hong Li
 */
public class TransferFile {
	/** The minimum size of a chunk. */
	public static int MIN_CHUNK_SIZE = 102400;
	
	/*
	 * The chunks: The key of this tree is the beginning value of the chunk.
	 * The value itself is the chunk itself. It's vital to keep the chunks
	 * in check.
	 */
	TreeMap<Integer, Chunk> chunks;
	// The semaphore to lock for writing
	Semaphore semaphore;
	// The file we download to
	File file;
	// The sream we download to
	RandomAccessFile rafile;
	// It's a real file that's just fine
	boolean isvalid;
	// The length of the file
	int length;
	// The settings
	Settings settings;
	// The transfer associated with this.
	Transfer transfer;
	// The hash associated
	byte[] hash;
	// The state
	State state;
	
	/**
	 * The constructor of the transferfile.
	 * @param f The file to be transfered.
	 * @param s The settings to get the incomplete files from.
	 * @param st The state of the protocol.
	 */
	public TransferFile(File f, Settings s, State st) {
		chunks = new TreeMap<Integer, Chunk>();
		semaphore = new Semaphore(1);
		file = f;
		length = 0;
		settings = s;
		transfer = new Transfer();
		transfer.setName(f.getName());
		transfer.setAddress(new ArrayList<InetAddress>());
		transfer.setRate(0);
		hash = HashList.HASH_EMPTY;
		state = st;
	}
	
	/**
	 * Get this file's associated hash
	 * @return The hash associated with this file.
	 */
	public byte[] getHash() {
		return hash;
	}
	
	/**
	 * Set the hash associated with this file.
	 * @param h The hash
	 */
	public void setHash(byte[] h) {
		hash = h;
	}
	
	/**
	 * Gets the file, or atleast it's name.
	 * @return The filename.
	 */
	public File getFile() {
		return file;
	}
	
	/**
	 * Get the transfer associated with this file.
	 * @return The transfer.
	 */
	public Transfer getTransfer() {
		return transfer;
	}

	/**
	 * Checks wether there are any chunks available.
	 * @return True if there are chunks available.
	 */
	public boolean chunksAvailable() {
		boolean ret = false;
		search: {
			/*
			 * Check for all chunks to have something available and are not busy.
			 * Otherwise, check if something can be split up. Else false.
			 */
			for (Chunk c: chunks.values()) {
				if (!c.isLocked() && !c.isFinished()) {
					/*
					 * This one is not locked nor finished so we have a chunk
					 * available
					 */
					ret = true;
					break search;
				}
			}
			/*
			 * Chech if we can split one up
			 */
			for (Chunk c: chunks.values()) {
				if (c.getTodo() > 2 * MIN_CHUNK_SIZE) {
					/*
					 * This one has twice the minimum chunk size left.
					 */
					ret = true;
					break search;
				}
			}
		}
		return ret;
	}
	
	/**
	 * Gets the first chunk in the bunch.
	 * @return The first chunk of the file.
	 */
	public Chunk getFirstChunk() {
		/*
		 * Get the first chunk of the lot. For clients without extensions. 
		 */
		return chunks.get(0);
	}
	
	/**
	 * Gets the last ready chunk.
	 * @return The chunk.
	 */
	public Chunk getLastReadyChunk() {
		/*
		 * Get the last ready chunk of the lot. For clients with extensions.
		 */
		// Make sure things don't change here
		try {
			semaphore.acquire();
		} catch (InterruptedException ie) {
			// Something went wrong, let's not mess up
			return null;
		}
		/*
		 * Check for all chunks to have something available and are not busy.
		 * Otherwise, check if something can be split up. Else false.
		 */
		Chunk toreturn = null;
		// Reverse order check
		chunksearch: {
			// If there are more than 1 chunks, search
			if (chunks.size() > 1) {
				Integer k;
				SortedMap<Integer, Chunk> s = chunks;
				do {
					k = s.lastKey();
					Chunk c = s.get(k);
					if (!c.isLocked() && !c.isFinished()) {
						/*
						 * This one is not locked nor finished so we have a chunk
						 * available
						 */
						toreturn = c;
						break chunksearch;
					}
					s.headMap(k);
				} while (s.size() > 1);
			}
			// Check the first chunk
			if (chunks.size() > 0) {
				Chunk c = chunks.get(chunks.firstKey());
				if (!c.isLocked() && !c.isFinished()) {
					toreturn = c;
					break chunksearch;
				}
			}
			// Reverse order check for a large one
			Integer k;
			SortedMap<Integer, Chunk> s = chunks;
			// Splitting for more than 1 chunks
			if (chunks.size() > 1) {
				do {
					k = s.lastKey();
					Chunk c = s.get(k);
					if (c.getTodo() > 2 * MIN_CHUNK_SIZE) {
						/*
						 * This one has twice the minimum chunk size left.
						 * split it up
						 */
						int nb = c.getMark() + c.getTodo() / 2;
						int ne = c.getEnd();
						// Set new place
						c.setEnd(nb);
						// New chunk
						Chunk n = new Chunk(this, nb, nb, ne);
						// Add chunk to right places
						chunks.put(n.getBegin(), n);
						toreturn = n;
						break chunksearch;
					}
					s.headMap(k);
				} while (s.size() > 1);
			}
		}
		// Release
		semaphore.release();
		return toreturn;
	}
	
	/**
	 * Lock a chunk.
	 * @param c
	 */
	public void lockChunk(Chunk c) {
		/*
		 * Lock the chunk
		 */
		c.lock();
	}
	
	/**
	 * Release the lock on the chunk, and enlarge it to maximum size.
	 * @param c The chunk to release.
	 */
	public void releaseChunk(Chunk c) {
		// Make sure things don't change here
		try {
			semaphore.acquire();
			/*
			 * Release the chunk and if can be merge with others.
			 * Merge forward and backwards.
			 */
			/*
			 * Since use of these chunks should be as prescibed, it's not
			 * needed to enlarge it beyond the next and previous item.
			 */
			// First the one before it
			SortedMap<Integer, Chunk> firsts = chunks.headMap(c.getBegin());
			if (firsts.size() > 0) {
				Chunk before = firsts.get(firsts.firstKey());
				// before should be safe, see if we can merge
				if (before.isFinished() && !before.isLocked()) {
					/*
					 * Remove old one, set beginning of new one, remove new
					 * one add it with new key.
					 */
					c.setBegin(before.getBegin());
					chunks.remove(c.getBegin());
					chunks.put(c.getBegin(), c);
				}
			}
			// The last one
			SortedMap<Integer, Chunk> lasts = chunks.tailMap(c.getBegin() + 
					1);
			if (lasts.size() > 0) {
				Chunk after = lasts.get(lasts.firstKey());
				// after should be safe
				if (after.isEmpty() && !after.isLocked()) {
					/*
					 * Remove old one, set end of new one
					 */
					c.setEnd(after.getEnd());
					chunks.remove(after.getBegin());
				}
			}
		} catch (InterruptedException ie) {
			// Something went wrong, we don't want to try anymore.
		}
		// Finally release it
		c.unlock();
		semaphore.release();
	}
	
	/**
	 * Try to enlarge the chunk with anything that may come after it. This
	 * should be used with caution!
	 * @param c The chunk to enlarge.
	 */
	public void enlargeChunk(Chunk c) {
		/*
		 * If the chunk is the very first chunk, then we may need to increase
		 * the size of the chunk.
		 */
		try {
			semaphore.acquire();
			// The last one
			SortedMap<Integer, Chunk> lasts = chunks.tailMap(c.getBegin() + 
					1);
			if (lasts.size() > 0) {
				Chunk after = lasts.get(lasts.firstKey());
				// after should be safe
				if (!after.isLocked()) {
					/*
					 * Remove old one, set end of new one
					 */
					c.setEnd(after.getEnd());
					chunks.remove(after.getBegin());
				}
			}
		} catch (InterruptedException ie) {
			// Something went wrong, we don't want to try anymore.
		}
		semaphore.release();
	}
	
	/**
	 * Gets the length of the entire file.
	 * @return The length of the file.
	 */
	public int getLength() {
		return length;
	}
	
	/**
	 * Gets the amount of bytes to still write.
	 * @return The amount of bytes writable.
	 */
	public int getTodo() {
		/*
		 * Get the info on much is still to be downloaded. Sum of todos of
		 * chunks.
		 */
		int todo = 0;
		for (Chunk c: chunks.values()) {
			todo += c.getTodo();
		}
		return todo;
	}
	
	/**
	 * Wether the file is finished, that is, nothing more to do.
	 * @return True if it's finished.
	 */
	public boolean isFinished() {
		return getTodo() == 0;
	}

	/**
	 * Asks wether the file is a new file.
	 * @return True if the file is new.
	 */
	public boolean isNew() {
		// If the file doesn't exist, it's new.
		return !file.exists();
	}
	
	/**
	 * If the file is a new file, the length will have to be specified.
	 * @param len The length of the file.
	 */
	public void setLength(int len) {
		length = len;
		transfer.setLength(length);
	}
	
	/**
	 * Asks wether the file is a complete file. That is, it's not new and
	 * it's not incomplete.
	 * @return True if the file is complete.
	 */
	public boolean isComplete() {
		/*
		 * If the file isn't new, so a file exists, and the file isn't
		 * incomplete, that is. It's not managed. Then it's a complete file.
		 * Ask for overwriting.
		 */
		return !isNew() && !isIncomplete();
	}
	
	/**
	 * Asks wether a file is an incomplete file. That is, it's been partially
	 * downloaded before.
	 * @return Wether it's an incomplete file.
	 */
	public boolean isIncomplete() {
		/*
		 * Check wether file exists, there is a node that holds info on it and
		 * check for chunks being there.
		 */
		if (isNew()) {
			return false;
		}
		/*
		 * Check for size
		 */
		int size = settings.getInt(getSettingsLoc(), "size", 0);
		return size > 0;
	}
	
	/**
	 * Simply asks wether the file is writable.
	 * @return True if the file is writable.
	 */
	public boolean isWritable() {
		return file.canWrite();
	}
	
	/**
	 * Get the location of the settings for this file.
	 * @return The location.
	 */
	private String getSettingsLoc() {
		return "/files/" + file.getName();
	}
	
	/**
	 * Initisalise as though it's a new file.
	 */
	private void initNew() {
		try {
			file.createNewFile();
			Chunk c = new Chunk(this, 0, 0, length);
			chunks.put(c.getBegin(), c);
		} catch (IOException ie) {
			// Nothing we can do here.
		}
	}
	
	/**
	 * Initialise for it being an incomplete file. Get settings from settings.
	 */
	private synchronized void initIncomplete() {
		/* 
		 * Init for an incomplete file
		 */
		String loc = getSettingsLoc() + "/chunks";
		// Init size
		length = settings.getInt(getSettingsLoc(), "size", length);
		// Init hash
		hash = settings.getByteArray(getSettingsLoc(), "hash", HashList.
				HASH_EMPTY);
		// Init chunks
		String [] ckeys = settings.getKeys(loc);
		// A map with mark => end
		TreeMap<Integer, Integer> markend = new TreeMap<Integer, Integer>();
		for (String skey: ckeys) {
			try {
				int key = Integer.parseInt(skey);
				int val = settings.getInt(loc, skey, key);
				markend.put(Integer.valueOf(key), Integer.valueOf(val));
			} catch (NumberFormatException nfe) {
				// Ignore this one
			}
		}
		// The beginning is obviously 0;
		int begin = 0;
		/*
		 * Note that the keyset is ascending, so we can assume that the marks
		 * and thus the ends are ascending too.
		 */
		for (Integer mark: markend.keySet()) {
			// The mark
			/*
			 * Basic checks
			 */
			Integer end = markend.get(mark);
			if (begin <= mark && mark <= end) {
				Chunk c = new Chunk(this, begin, mark, end);
				chunks.put(c.getBegin(), c);
				begin = end;
			}
		}
	}
	
	/**
	 * Closes everything down, cleans up chunks and saves to settings.
	 */
	protected synchronized void close() {
		/* 
		 * Clean up everything to the settings.
		 * Clean up chunks, save to settings if not finished
		 * if finished, cleanup settings and remove reference to this file.
		 */
		if (rafile != null) {
			if (isFinished()) {
				settings.removeNode(getSettingsLoc());
				// Check for hash
				byte[] h = HashList.hash(file);
				if (!HashList.equals(h, HashList.HASH_EMPTY) &&
						!HashList.equals(hash, HashList.HASH_EMPTY)) {
					// hash isn't empty, now just compare
					if (!HashList.equals(h, hash)) {
						boolean del = state.getQuery().askListeners(
								"Hash of file '" + file.getName() + "' does" +
								"not match. Do you wish to delete this file?",
								"Hash error");
						if (del) {
							file.delete();
						}
					}
				}
			} else {
				// Commit size
				settings.setInt(getSettingsLoc(), "size", length);
				// Commit hash
				settings.setByteArray(getSettingsLoc(), "hash", hash);
				// Commit chunks
				String loc = getSettingsLoc() + "/chunks";
				/*
				 * Clearn previous chunk info and add the new chunk info.
				 */
				for (String key: settings.getKeys(loc)) {
					settings.remove(loc, key);
				}
				for (Chunk c: chunks.values()) {
					settings.setInt(loc, Integer.valueOf(c.getMark()).
							toString(), c.getEnd());
				}
			}
			// Chunks should be setup by now, close the stream.
			try {
				rafile.close();
			} catch (IOException ie) {
				// Error closing file, shouldn't be important.
			}
		}
	}
	
	/**
	 * Initialises the class and sets it up so that it can be written to.
	 */
	public void initWrite() {
		/*
		 * Initialise this file for writing.
		 */
		if (isNew()) {
			initNew();
		} else if (isIncomplete()) {
			initIncomplete();
		} else {
			// This shouldn't have been called in the firstplace
			return;
		}
		// Setup the standard stuff
		try {
			rafile = new RandomAccessFile(file, "rw");
			isvalid = true;
		} catch (FileNotFoundException fnfe) {
			// File not found
			isvalid = false;
		}
		
		transfer.setLength(length);
		transfer.setTodo(getTodo());
	}
	
	/**
	 * Wether this file can be downloaded, and where it can be downloaded to.
	 * @return True if the file is valid, false if it isn't.
	 */
	public boolean isValid() {
		return isvalid;
	}

	/**
	 * Try to write the given data to the file at the given offset.
	 * @param data The data to write.
	 * @param len The actual length of the data.
	 * @param offset The offset in the file where to data should be put.
	 */
	protected void write(byte[] data, int len, int offset) {
		if (offset + len > length) {
			// This shouldn't be written, it's out of bounds.
			return;
		}
		// Lock semaphore
		try {
			semaphore.acquire();
			// Write a bit of the thing
			try {
				// First seek the place
				rafile.seek(offset);
				// Then write
				rafile.write(data, 0, len);
			} catch (IOException ie) {
				// Nothing we can do here, we don't retry.
			}
			// Release the file
			semaphore.release();
		} catch (InterruptedException ie) {
			// Writing went foobar
			Output.err("Error while writing to file!");
		}
	}
}
