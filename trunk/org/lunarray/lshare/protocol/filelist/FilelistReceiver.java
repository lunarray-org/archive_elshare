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
package org.lunarray.lshare.protocol.filelist;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import org.lunarray.lshare.protocol.Controls;
import org.lunarray.lshare.protocol.Hash;
import org.lunarray.lshare.protocol.packets.PacketUtil;
import org.lunarray.lshare.protocol.state.userlist.User;
import org.lunarray.lshare.protocol.tasks.RunnableTask;

/**
 * A class for receiving file lists from users.<br>
 * Stream 1:<br>
 * Purpose:<br>
 * File browsing.<br>
 * TCP socket.<br>
 * Input:<br>
 * Directory request as follows:<br>
 * 2 fields:<br>
 * 0: 2 bytes, representing an int n<br>
 * 1: n bytes, path name<br>
 * Output:<br>
 * Directory contents as follows:<br>
 * 8 bytes, representing the int n<br>
 * n times:<br>
 * 5 fields:<br>
 * 0: 8 bytes, long, epoch of file modification<br>
 * 1: 8 bytes, long, file size<br>
 * 2: 16 bytes, the hash of the file<br>
 * 3: 1 byte, representing an int n<br>
 * 4: n bytes, file name encoded in UTF-8<br>
 * Constraints:<br>
 * A new directory name may not be sent while a directories content isbeing
 * requested. Multiple clients may connect simultaneously to get the file
 * contents.<br>
 * Note:<br>
 * Root path is given by requesting the '.' path<br>
 * Path elements are seperated with a '/'.<br>
 * Directories will be denoted by having a file size of '-1'.<br>
 * While a connection stands, the contents of any number of directories may be
 * requested.<br>
 * The file hash of a directory will always be an empty hash, ie. just zeroes.
 * @author Pal Hargitai
 */
public class FilelistReceiver {
    /**
     * The user that this file list is requested from.
     */
    private User user;

    /**
     * The socket for receiving a users file list.
     */
    private Socket socket;

    /**
     * The output stream for writing data.
     */
    private OutputStream ostream;

    /**
     * The input stream for reading data.
     */
    private InputStream istream;

    /**
     * Controls to the protocol.
     */
    private Controls controls;

    /**
     * Constructs a file list receiver for receiving a users file list.
     * @param c The controls of the protocol.
     * @param u The user whose file list to get.
     */
    public FilelistReceiver(Controls c, User u) {
        user = u;
        controls = c;
        // Setup stuff
        socket = new Socket();
        try {
            socket.connect(new InetSocketAddress(user.getAddress(),
                    Controls.TCP_PORT));
        } catch (IOException ie) {
            Controls.getLogger().warning("Could not connect to user!");
        }

    }

    /**
     * Gets entries for a specific path.
     * @param path The path to get the entries of.
     * @param isroot True if the root entry is requested.
     * @return The entries to be gotten.
     */
    public synchronized List<FilelistEntry> getEntries(String path,
            boolean isroot) {
        if (isroot) {
            path = ".";
        }

        RecTO timeouthandler = new RecTO();
        timeouthandler.startGet();

        controls.getTasks().backgroundTask(timeouthandler);

        ArrayList<FilelistEntry> ret = new ArrayList<FilelistEntry>();
        run: {
            try {
                write: {
                    Controls.getLogger().finer("Requested path: " + path);
                    ostream = socket.getOutputStream();
                    writeLongString(path);
                }
                read: {
                    istream = socket.getInputStream();
                    long amount = getLong();
                    for (long i = 0; i < amount; i++) {
                        timeouthandler.bump();
                        ret.add(getOne(path));
                    }
                }
            } catch (IOException ie) {
                Controls.getLogger().warning("Could not request path!");
                try {
                    socket.close();
                } catch (IOException i) {
                    // error
                }
                break run;
            }
        }

        timeouthandler.stopGet();
        return ret;
    }

    /**
     * Close the connection and cleanup the socket.
     */
    public void close() {
        try {
            socket.close();
        } catch (IOException ie) {
            Controls.getLogger().fine("Could not close socket!");
        }
        Controls.getLogger().finer("Closed socket!");

    }

    /**
     * Get the root node for the entries.
     * @return The root entry.
     */
    public FilelistEntry getRoot() {
        return new FilelistEntry(this, ".", user.getName(), Hash.getUnset(), -1,
                -1, true);
    }

    /**
     * Get a specified number of bytes.
     * @param a The amount of bytes to get.
     * @return The bytes gotten.
     * @throws IOException If the socket could not be read from.
     */
    private byte[] get(int a) throws IOException {
        byte[] dat = new byte[a];
        int todo = a;
        int done = 0;
        while (todo > 0) {
            int read = istream.read(dat, done, todo);
            done += read;
            todo -= read;
        }
        return dat;
    }

    /**
     * Get a single file entry in a given path.
     * @param p The path the entry is from.
     * @return A single file entry from a given path.
     * @throws IOException If the socket could not be read from.
     */
    private FilelistEntry getOne(String p) throws IOException {

        long ad = getLong();
        long size = getLong();
        Hash hash = getHash();
        String name = getShortString();

        Controls.getLogger().finer("Data for: " + name);
        return new FilelistEntry(this, p, name, hash, ad, size, false);
    }

    /**
     * Gets a short string.
     * @return The string with a maximum length of 255.
     * @throws IOException Thrown if read wrong.
     */
    private String getShortString() throws IOException {
        return PacketUtil.decode(get(get(1)[0])).trim();
    }

    /**
     * Gets a long.
     * @return The long.
     * @throws IOException Thrown if read wrong.
     */
    private long getLong() throws IOException {
        return PacketUtil.byteArrayToLong(get(8), 0);
    }

    /**
     * Gets a hash.
     * @return The hash.
     * @throws IOException Thrown if read wrong.
     */
    private Hash getHash() throws IOException {
        return new Hash(get(Hash.length()));
    }

    /**
     * Writes a long string.
     * @param s The string to write.
     * @throws IOException Thrown if written wrong.
     */
    private void writeLongString(String s) throws IOException {
        byte[] sdat = PacketUtil.encode(s);
        byte[] slen = new byte[2];
        PacketUtil.shortUToByteArray(sdat.length, slen, 0);
        ostream.write(slen);
        ostream.write(sdat);
    }

    /**
     * A timeout thread to ensure that a get does not go on indefinitely.
     * @author Pal Hargitai
     */
    private class RecTO implements RunnableTask {
        /**
         * The allocated time to get a single item.
         */
        public int NEXT = 1000;

        /**
         * The timestamp at which a socket is concidered corrupt.
         */
        private long nextstamp;

        /**
         * True if there are items to be gotten, false if not.
         */
        private boolean shouldget;

        /**
         * Constructs a timeout type thread.
         */
        public RecTO() {
            nextstamp = System.currentTimeMillis() + NEXT;
            shouldget = false;
        }

        /**
         * Bumps the timestamp.
         */
        public void bump() {
            nextstamp = System.currentTimeMillis() + NEXT;
        }

        /**
         * Allow this thread to check for validity of the socket.
         */
        public void startGet() {
            shouldget = true;
        }

        /**
         * Disallows this thread from checking socket validity.
         */
        public void stopGet() {
            shouldget = false;
        }

        /**
         * Checks the socket for validity.
         * @param c The controls of the protocol.
         */
        public void runTask(Controls c) {
            run: {
                while (true) {
                    // Check if the time has passed
                    if (nextstamp - System.currentTimeMillis() < 0) {
                        if (!shouldget) {
                            break run;
                        }

                        shouldget = false;
                        // Time has passed and the socket should now close.
                        close();
                    } else {
                        try {
                            Thread
                                    .sleep(nextstamp
                                            - System.currentTimeMillis());
                        } catch (InterruptedException ie) {
                            // Ignore
                        }
                    }

                    if (!shouldget) {
                        break run;
                    }
                }
            }
        }
    }
}
