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
package org.lunarray.lshare.protocol.state.download;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

import org.lunarray.lshare.protocol.Controls;
import org.lunarray.lshare.protocol.state.download.file.Chunk;
import org.lunarray.lshare.protocol.state.userlist.User;
import org.lunarray.lshare.protocol.tasks.RunnableTask;

/**
 * A download transfer.
 * @author Pal Hargitai
 */
public class DownloadTransfer implements RunnableTask {
    /**
     * The chunk to download.
     */
    private Chunk chunk;

    /**
     * The user to download from.
     */
    private User user;

    /**
     * The handler that this transfer resides in.
     */
    private DownloadHandler handler;

    /**
     * The port to connect to.
     */
    private int port;

    /**
     * The socket. Represents the connection between this user and the user
     * hosting the file.
     */
    private Socket socket;

    /**
     * The input stream of the socket.
     */
    private InputStream istream;

    /**
     * Set to true if the transfer should run. False if not.
     */
    private boolean shouldrun;

    /**
     * Constructs a download transfer.
     * @param c The chunk to download to.
     * @param u The user to download from.
     * @param h The handler in which this resides.
     * @param p The port to connect to.
     */
    public DownloadTransfer(Chunk c, User u, DownloadHandler h, int p) {
        chunk = c;
        user = u;
        handler = h;
        port = p;
        shouldrun = true;
    }

    /**
     * Runs the transfer.
     * @param c The controls to the protocol.
     */
    public void runTask(Controls c) {
        // Actually download
        run: {
            while (!chunk.isDone()) {
                // download some
                try {
                    int toread = Long.valueOf(
                            Math.min(Math.min(1024, chunk.getTodo()), istream
                                    .available())).intValue();

                    byte[] data = new byte[toread];

                    istream.read(data);
                    chunk.write(data, toread);
                } catch (IOException ie) {
                    Controls.getLogger().warning("Error transferring file.");
                    break run;
                }
                if (!shouldrun) {
                    break run;
                }
            }
        }
        handler.done();
    }

    /**
     * Cancel the transfer.
     */
    public synchronized void cancel() {
        shouldrun = false;
    }

    /**
     * Initialises the transfer.
     * @throws IOException Thrown if the socket or it's stream can't be opened.
     */
    public synchronized void init() throws IOException {
        socket = new Socket(user.getAddress(), port);
        istream = socket.getInputStream();
    }

    /**
     * Closes the transfer.
     */
    public synchronized void close() {
        try {
            if (socket != null) {
                socket.close();
            }
            chunk.unlock();
        } catch (IOException ie) {
            // Ignore
        }
    }

    /**
     * Gets the user to connect to.
     * @return The user.
     */
    public User getUser() {
        return user;
    }
}
