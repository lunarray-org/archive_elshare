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

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Collections;
import java.util.List;

import org.lunarray.lshare.protocol.Controls;
import org.lunarray.lshare.protocol.Hash;
import org.lunarray.lshare.protocol.packets.PacketUtil;
import org.lunarray.lshare.protocol.state.sharing.ShareEntry;

/**
 * A class for sending file entries.<br>
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
public class FilelistSender extends Thread {
    /**
     * The client socket for sending file entries.
     */
    private Socket socket;

    /**
     * Access to the protocol.
     */
    private Controls controls;

    /**
     * The input stream from which data is read.
     */
    private InputStream istream;

    /**
     * The output stream to which data is written.
     */
    private OutputStream ostream;

    /**
     * The constructor for the file sender.
     * @param g The thread group in which this thread will run.
     * @param c The controls for the protocol.
     * @param s The socket that this is to communicate to.
     */
    public FilelistSender(ThreadGroup g, Controls c, Socket s) {
        super(g, "filelistsender");
        controls = c;
        socket = s;
    }

    /**
     * The main code for sending file entries.
     */
    public void run() {
        run: {
            // Set streams
            try {
                istream = socket.getInputStream();
                ostream = socket.getOutputStream();
            } catch (IOException ie) {
                Controls.getLogger().severe("Could not get streams!");
                break run;
            }
            while (true) {
                String path = "";
                read: {
                    // read and write data
                    try {
                        path = readLongString();
                    } catch (IOException ie) {
                        Controls.getLogger().fine("Could not read!");
                        break run;
                    }
                }
                Controls.getLogger().finer("Requested " + path);

                // Get entries
                List<ShareEntry> entries = Collections.emptyList();
                try {
                    entries = controls.getState().getShareList().getChildrenIn(
                            path);
                } catch (FileNotFoundException ne) {
                    // We can safely ignore this.
                }

                // Write length
                try {
                    writeLong(entries.size());
                } catch (IOException ie) {
                    Controls.getLogger().fine("Cannot write!");
                    break run;
                }
                try {
                    // Write entries
                    for (ShareEntry e : entries) {
                        send(e);
                    }
                } catch (IOException ie) {
                    Controls.getLogger().fine("Cannot write!");
                    break run;
                }
                yield();
            }
        }
        // Close socket
        try {
            socket.close();
        } catch (IOException ie) {
            Controls.getLogger().finer("Closed socket");
        }
    }

    /**
     * Closes the socket.
     */
    public void close() {
        try {
            socket.close();
        } catch (IOException ie) {
            Controls.getLogger().severe("Could not close client socket!");
        }
    }

    /**
     * Send a file entry over the socket.
     * @param d The entry to send.
     * @throws IOException Incase a transferring an entry fails.
     */
    private void send(ShareEntry d) throws IOException {
        writeLong(d.getLastModified());
        writeLong(d.getSize());
        writeHash(d.getHash());
        writeShortString(d.getName());
    }

    /**
     * Read a long string.
     * @return The read string.
     * @throws IOException Thrown if the data could not be read.
     */
    private String readLongString() throws IOException {
        int slen = PacketUtil.byteArrayToShortU(get(2), 0);
        return PacketUtil.decode(get(slen)).trim();
    }

    /**
     * Write a hash.
     * @param h The hash to write.
     * @throws IOException Thrown if the data could not be written.
     */
    private void writeHash(Hash h) throws IOException {
        ostream.write(h.getBytes());
    }

    /**
     * Write a short string.
     * @param s The string to be written.
     * @throws IOException Thrown if the data could not be written.
     */
    private void writeShortString(String s) throws IOException {
        byte[] sdat = PacketUtil.encode(s);
        byte[] slen = {
            (byte) Math.min(sdat.length, 255)
        };
        ostream.write(slen);
        ostream.write(sdat, 0, slen[0]);
    }

    /**
     * Write a long.
     * @param l The long to be written.
     * @throws IOException Thrown if the data could not be written.
     */
    private void writeLong(long l) throws IOException {
        byte[] dat = new byte[8];
        PacketUtil.longToByteArray(l, dat, 0);
        ostream.write(dat);
    }

    /**
     * Gets a certain amount of bytes.
     * @param a The amount of bytes to get.
     * @return An a amount of bytes in an array.
     * @throws IOException In case recieving an file fails.
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
}
