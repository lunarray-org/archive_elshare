/*
 * eLShare allows you to share.
 * Copyright (C) 2006 Pal Hargitai
 * E-Mail: pal@lunarray.org
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package org.lunarray.lshare.protocol.state.download.file;

import java.io.File;
import java.util.Set;
import java.util.TreeMap;

import org.lunarray.lshare.protocol.Controls;
import org.lunarray.lshare.protocol.Hash;
import org.lunarray.lshare.protocol.RemoteFile;
import org.lunarray.lshare.protocol.state.download.QueueStatus;
import org.lunarray.lshare.protocol.state.userlist.User;
import org.lunarray.lshare.protocol.state.userlist.UserNotFound;

/**
 * A single incomplete file.
 * @author Pal Hargitai
 */
public class IncompleteFile {
    /**
     * The hash of the file.
     */
    private Hash hash;

    /**
     * The chunked representation of this file.
     */
    private ChunkedFile file;

    /**
     * The settings associated with this file.
     */
    private IncompleteFileSettings settings;

    /**
     * The controls to the protocol.
     */
    private Controls controls;

    /**
     * The sources to this file.
     */
    private TreeMap<User, RemoteFile> sources;

    /**
     * True if the file is corrupt, false if it's ok.
     */
    private boolean corrupt;

    /**
     * Constructs an incomplete file.
     * @param s The settings for this file.
     * @param c The controls to the protocol.
     */
    public IncompleteFile(IncompleteFileSettings s, Controls c) {
        settings = s;
        controls = c;
        sources = new TreeMap<User, RemoteFile>();
        file = new ChunkedFile(settings);
        hash = Hash.getUnset();
        corrupt = false;
    }

    /**
     * Gets the reference to the file on the filesystem.
     * @return The file.
     */
    public File getFile() {
        return file.getFile();
    }

    /**
     * Checks wether the file is corrupt.
     * @return True if the file is corrupt, false if not.
     */
    public boolean isCorrupt() {
        return corrupt;
    }

    /**
     * The the file integrity.
     */
    public void checkIntegrity() {
        if (hash.isEmpty()) {
            Hash h = new Hash(file.getFile());
            corrupt = !hash.equals(h);
        }
    }

    /**
     * Physically removes the file. Use with care!
     */
    public void delete() {
        settings.removeFile();
        file.close();
        file.getFile().delete();
    }

    /**
     * Get the files hash.
     * @return The hash of the file.
     */
    public Hash getHash() {
        return hash;
    }

    /**
     * Gets the status of this file.
     * @return The status of this file.
     */
    public QueueStatus getStatus() {
        if (file.isFinished()) {
            return corrupt ? QueueStatus.CORRUPT : QueueStatus.FINISHED;
        } else if (file.inProgress()) {
            return QueueStatus.RUNNING;
        } else if (file.isEmpty()) {
            return QueueStatus.QUEUED;
        } else {
            return QueueStatus.STOPPED;
        }
    }

    /**
     * Gets all sources of this file.
     * @return The sources of this file.
     */
    public Set<User> getSources() {
        return sources.keySet();
    }

    /**
     * Get the remote entry associated with this file.
     * @param u The user to get the remote entry of.
     * @return The remote entry of the given user.
     * @throws UserNotFound Thrown if the user doesn't exist.
     */
    public RemoteFile getSourceFromUser(User u) throws UserNotFound {
        if (sources.containsKey(u)) {
            return sources.get(u);
        } else {
            throw new UserNotFound();
        }
    }

    /**
     * Checks wether the given user is a valid source of this file.
     * @param u The user to check.
     * @return True if the user is a valid source, false if not.
     */
    public boolean canDownloadFromUser(User u) {
        return sources.containsKey(u);
    }

    /**
     * Adds a given user as a source for this file.
     * @param u The user to add as a source.
     * @param f The remote entry of this user.
     * @throws IllegalArgumentException Thrown if the file does not match the
     * signature of this file.
     */
    public void addSource(User u, RemoteFile f) throws IllegalArgumentException {
        if (f.getSize() == getSize()) {
            if (hash.isEmpty()) {
                hash = f.getHash();

                sources.put(u, f);
            } else {
                if (hash.equals(f.getHash())) {
                    sources.put(u, f);
                } else {
                    throw new IllegalArgumentException();
                }
            }
        } else {
            throw new IllegalArgumentException();
        }
    }

    /**
     * Removes the use as a source.
     * @param u The user to remove as a source.
     */
    public void removeSource(User u) {
        if (sources.containsKey(u)) {
            sources.remove(u);
        }
    }

    /**
     * Gets a chunk of the file.
     * @return A free chunk of the file.
     * @throws IllegalAccessException Thrown if there is no available chunk.
     */
    public Chunk getChunk() throws IllegalAccessException {
        return file.getChunk();
    }

    /**
     * Gets the size of this file.
     * @return The size of this file.
     */
    public long getSize() {
        return file.getSize();
    }

    /**
     * Gets the amount of bytes to download.
     * @return The amount of bytes to still download.
     */
    public long getTodo() {
        return file.getTodo();
    }

    /**
     * Gets the amount of the file that is done.
     * @return The finished amount.
     */
    public long getDone() {
        return file.getDone();
    }

    /**
     * Check if the remote entry matches this files signature.
     * @param f The file to compare.
     * @return True if the file matches, false if not.
     */
    public boolean matches(RemoteFile f) {
        if (file.getSize() > 0 && f.getSize() != file.getSize()) {
            return false;
        }
        if (hash.isEmpty()) {
            return true;
        } else {
            return hash.equals(f.getHash());
        }
    }

    /**
     * Initialise the file from known settings.
     */
    protected void initFromSettings() {
        file.initFromBack();
        hash = settings.getHash();

        for (String uc : settings.getSources()) {
            User u = controls.getState().getUserList().findUserByChallenge(uc);
            if (u != null) {
                String path = settings.getSourcePath(uc);
                String name = settings.getSourceName(uc);

                QueuedRemote r = new QueuedRemote(path, name, hash, file
                        .getSize());

                sources.put(u, r);
            }
        }
    }

    /**
     * Closes down this file.
     */
    protected void close() {
        // Close file
        file.close();
        if (file.isFinished()) {
            settings.removeFile();
        } else {
            settings.setHash(hash);
            for (User u : sources.keySet()) {
                RemoteFile f = sources.get(u);

                if (u.isBuddy()) {
                    settings.setSource(u.getChallenge(), f.getPath(), f
                            .getName());
                }
            }
        }
    }

    /**
     * Sets the hash of this file.
     * @param h The hash to set it to.
     * @throws IllegalArgumentException Thrown if the hash is already set but
     * doesn't match.
     */
    protected void setHash(Hash h) throws IllegalArgumentException {
        if (hash.isEmpty()) {
            hash = h;
        } else {
            if (!hash.equals(h)) {
                throw new IllegalArgumentException();
            }
        }
    }

    /**
     * Give arguments to init this file with.
     * @param s The size of the file.
     * @param f The target of this file.
     * @throws IllegalStateException Thrown if the file is in an invalid state.
     */
    protected void initFromFront(long s, File f) throws IllegalStateException {
        file.initFromFront(f, s);
    }
}
