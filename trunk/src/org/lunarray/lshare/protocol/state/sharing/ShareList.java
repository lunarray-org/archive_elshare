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
package org.lunarray.lshare.protocol.state.sharing;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeMap;

import org.lunarray.lshare.protocol.Controls;
import org.lunarray.lshare.protocol.Hash;
import org.lunarray.lshare.protocol.RemoteFile;

/**
 * The share list for handling shares and files.
 * @author Pal Hargitai
 */
public class ShareList implements ExternalShareList {

    /**
     * The named shares.
     */
    private TreeMap<String, File> shares;

    /**
     * The settings that this list has access to.
     */
    private ShareSettings settings;

    /**
     * True if hashing is going on, false if not.
     */
    private boolean ishashing;

    /**
     * Constructs the share list.
     * @param c The coontrols for access to the protocol.
     */
    public ShareList(Controls c) {
        shares = new TreeMap<String, File>();
        settings = c.getSettings().getShareSettings();
        ishashing = false;
        init();
    }

    /**
     * Registers a share.
     * @param sname The name of the path to register.
     * @param fpath The path to register.
     */
    public void addShare(String sname, File fpath) {
        if (fpath.isDirectory() && !fpath.isHidden()) {
            shares.put(sname, fpath);
            settings.setSharePath(sname, fpath.getPath());
        }
    }

    /**
     * Unregisteres a share.
     * @param sname The name of the path to unregister.
     */
    public void removeShare(String sname) {
        if (shares.containsKey(sname)) {
            shares.remove(sname);
            settings.removeSharePath(sname);
        }
    }

    /**
     * Gets the entry representing the specified file.
     * @param f The file to get the entry for.
     * @return The entry for the file.
     * @throws FileNotFoundException Thrown if a file is not found or shared.
     */
    public ShareEntry getEntryFor(File f) throws FileNotFoundException {
        for (String skey : shares.keySet()) {
            if (f.getPath().startsWith(shares.get(skey).getPath())) {
                String rewritten = "."
                        + RemoteFile.SEPARATOR
                        + skey
                        + f.getPath().substring(
                                shares.get(skey).getPath().length()).replace(
                                File.separator, RemoteFile.SEPARATOR);
                int i = rewritten.lastIndexOf(RemoteFile.SEPARATOR);
                rewritten = rewritten.substring(0, i);
                return new ShareEntry(f, f.getName(), rewritten, settings);
            }
        }
        throw new FileNotFoundException();
    }

    /**
     * Gets the entries matching the specified search string.
     * @param s The search string to get entries from.
     * @return The entries matching the specified search string.
     */
    public List<ShareEntry> getEntriesMatching(String s) {
        LinkedList<ShareEntry> entries = new LinkedList<ShareEntry>();
        for (String skey : shares.keySet()) {
            entries.addAll(getEntriesMatching(s, shares.get(skey), "."
                    + RemoteFile.SEPARATOR + skey));
        }
        return entries;
    }

    /**
     * Gets all children in a certain path.
     * @param path The path to get the entries of.
     * @return The list of entries in the given path.
     * @throws FileNotFoundException Thrown if the path is not found.
     */
    public List<ShareEntry> getChildrenIn(String path)
            throws FileNotFoundException {
        LinkedList<ShareEntry> entries = new LinkedList<ShareEntry>();
        String[] split = path.split(RemoteFile.SEPARATOR);

        if (split.length > 0) {
            if (split[0].equals(".")) {
                if (split.length > 1) {
                    if (shares.containsKey(split[1])) {
                        entries.addAll(fetchDirEntries(split, 2, shares
                                .get(split[1]), path));
                    } else {
                        throw new FileNotFoundException();
                    }
                } else {
                    entries.addAll(getBaseEntries());
                }
            } else {
                throw new FileNotFoundException();
            }
        } else {
            throw new FileNotFoundException();
        }
        return entries;
    }

    /**
     * Gets all the base entries.
     * @return The entries representing the root entries.
     */
    public List<ShareEntry> getBaseEntries() {
        LinkedList<ShareEntry> entries = new LinkedList<ShareEntry>();
        for (String n : shares.keySet()) {
            entries.add(new ShareEntry(shares.get(n), n, ".", settings));
        }
        return entries;
    }

    /**
     * Gets a file for a given remote entry.
     * @param f The entry to get the file for.
     * @return The file presented in the entry.
     * @throws FileNotFoundException Thrown if the file is not found.
     */
    public File getFileForEntry(RemoteFile f) throws FileNotFoundException {
        List<ShareEntry> shares = getChildrenIn(f.getPath());

        for (ShareEntry s : shares) {
            if (s.getName().equals(f.getName())) {
                // name matches
                if (s.getHash().equals(f.getHash())
                        && s.getSize() == f.getSize()) {
                    // file seems to match
                    return s.getFile();
                }
            }
        }
        throw new FileNotFoundException();
    }

    /**
     * Cleans up all shared files and rechecks all hashes.
     * @param sset The share settings to use.
     */
    protected synchronized void hash(ShareSettings sset) {
        if (ishashing) {
            return;
        }
        ishashing = true;
        // Cleanup
        LinkedList<String> torem = new LinkedList<String>();
        for (String s : sset.getFilesInPath()) {
            File n = new File(s);
            if (!n.exists() || !isInShares(n)) {
                torem.add(s);
            }
        }
        for (String s : torem) {
            sset.removePath(s);
        }
        // Hashes
        for (String s : shares.keySet()) {
            hash(shares.get(s));
        }
        ishashing = false;
    }

    /**
     * Gets all entries in the given path.
     * @param path The path to check for.
     * @param depth The depth the path is currently recursed in.
     * @param f The file whose entries are to be checked or given.
     * @param sp The path that is searched for.
     * @return The entries in the given path.
     * @throws FileNotFoundException Thrown if the path is not found.
     */
    private List<ShareEntry> fetchDirEntries(String[] path, int depth, File f,
            String sp) throws FileNotFoundException {
        if (path.length <= depth) {
            LinkedList<ShareEntry> entries = new LinkedList<ShareEntry>();
            // Last element, get children
            for (File e : f.listFiles()) {
                if (!e.isHidden()) {
                    entries.add(new ShareEntry(e, e.getName(), sp, settings));
                }
            }
            return entries;
        } else {
            // Just get the children of the matching element
            for (File e : f.listFiles()) {
                if (e.isDirectory() && !e.isHidden()
                        && e.getName().equals(path[depth])) {
                    return fetchDirEntries(path, depth + 1, e, sp);
                }
            }
            throw new FileNotFoundException();
        }
    }

    /**
     * Initialises the shares.
     */
    private void init() {
        for (String s : settings.getShareNames()) {
            File f = new File(settings.getSharePath(s));
            if (f.exists() && f.isDirectory() && !f.isHidden()) {
                shares.put(s, f);
            } else {
                settings.removeSharePath(s);
            }
        }
    }

    /**
     * Checks if a given file is in the share.
     * @param f The file to check.
     * @return True if the file is shared.
     */
    private boolean isInShares(File f) {
        for (File s : shares.values()) {
            if (f.getPath().startsWith(s.getPath())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Gets all entries in the given path to match the search string.
     * @param s The search string to match.
     * @param f The directory to search in.
     * @param path The path these entries reside in.
     * @return The entries in this directory that matches the search string.
     */
    private List<ShareEntry> getEntriesMatching(String s, File f, String path) {
        LinkedList<ShareEntry> entries = new LinkedList<ShareEntry>();
        for (File e : f.listFiles()) {
            if (!e.isHidden() && e.getName().contains(s)) {
                entries.add(new ShareEntry(e, e.getName(), path, settings));
            }
            if (e.isDirectory()) {
                entries.addAll(getEntriesMatching(s, e, path
                        + RemoteFile.SEPARATOR + e.getName()));
            }
        }
        return entries;
    }

    /**
     * Hashes the specified file or any child entries.
     * @param f The file and it's children to hash.
     */
    private void hash(File f) {
        for (File fl : f.listFiles()) {
            // Hash
            if (fl.isDirectory() && !fl.isHidden()) {
                // Hash children
                hash(fl);
            } else if (fl.isFile() && !fl.isHidden() && fl.canRead()) {
                if (settings.getAccessDate(fl.getPath()) < fl.lastModified()) {
                    // Update hash
                    Hash h = new Hash(fl);
                    Hash n = new Hash(fl.getPath());

                    settings.setData(fl.getPath(), h, n, fl.lastModified());
                }
            }
        }
    }
}
