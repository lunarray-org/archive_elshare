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
package org.lunarray.lshare.protocol.state.upload;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

import org.lunarray.lshare.protocol.Controls;
import org.lunarray.lshare.protocol.state.userlist.User;
import org.lunarray.lshare.protocol.tasks.RunnableTask;

/**
 * Uploads a file from a specific offset onward.
 * @author Pal Hargitai
 */
public class UploadTransfer implements RunnableTask {
    /**
     * The file to upload.
     */
    private File file;

    /**
     * The offset to start uploading at.
     */
    private long offset;

    /**
     * The input stream of the file.
     */
    private FileInputStream istream;

    /**
     * The socket to which the user is connecting.
     */
    private Socket socket;

    /**
     * The server socket that a user may connect to.
     */
    private ServerSocket server;

    /**
     * The upload manager that manages this upload.
     */
    private UploadManager manager;

    /**
     * A variable for checking wether a transfer is done. Set to true if the
     * transfer is done, false if not.
     */
    private boolean done;

    /**
     * The total size of the transfer.
     */
    private long size;

    /**
     * The amount to be transferred.
     */
    private long todo;

    /**
     * The request associated with the transfer.
     */
    private UploadRequest req;

    /**
     * The user the transfer is dealing with.
     */
    private User user;

    /**
     * Set to true if the transfer should run, false if not.
     */
    private boolean shouldrun;

    /**
     * Contructs a file upload.
     * @param f The file to upload.
     * @param o The offset to uplaod at.
     * @param m The manager that manages this upload.
     * @param r The request that is associated with it.
     * @param u The user the transfer is with.
     */
    public UploadTransfer(File f, long o, UploadManager m, UploadRequest r,
            User u) {
        user = u;
        req = r;
        file = f;
        manager = m;
        offset = o;
        done = false;
        size = file.length();
        todo = size - offset;
        shouldrun = true;
    }

    /**
     * Initialises the transfer.
     * @return The port that the transfer is bound to. Ie, the port that the
     * other party may connect to.
     * @throws IOException If the file cannot be read or no socket can be bound
     * to.
     */
    public int init() throws IOException {
        // Setup file
        istream = new FileInputStream(file);
        istream.skip(offset);
        // Setup socket
        bind: {
            // Try to bind to any socket within the socket range.
            for (int i = UploadManager.BEGIN_PORT; i < UploadManager.END_PORT; i++) {
                server = new ServerSocket();
                try {
                    server.bind(new InetSocketAddress(i));
                    break bind;
                } catch (IOException ie) {
                    // try again
                }
            }
        }

        Controls.getLogger().fine("Started transfer.");

        // Check if a proper socket is bound to.
        if (server.getLocalPort() >= UploadManager.BEGIN_PORT
                && server.getLocalPort() <= UploadManager.END_PORT) {
            return server.getLocalPort();
        } else {
            throw new IOException();
        }
    }

    /**
     * Checks wether this upload is done.
     * @return True if the upload is done, false if not.
     */
    public boolean isDone() {
        return done;
    }

    /**
     * Checks wether this upload is running.
     * @return True if the upload is running, false if not.
     */
    public boolean isRunning() {
        return socket == null ? false : socket.isConnected();
    }

    /**
     * Cancels the transfer.
     */
    public void cancel() {
        shouldrun = false;
    }

    /**
     * Close the transfer.
     */
    public void close() {
        try {
            istream.reset();
            istream.close();
            server.close();
            if (socket != null) {
                socket.shutdownOutput();
                socket.close();
            }
        } catch (IOException ie) {
            // Ignore now
        }

        Controls.getLogger().fine("Closed transfer.");
    }

    /**
     * Gets the file being transferred.
     * @return The file being transferred.
     */
    public File getFile() {
        return file;
    }

    /**
     * Gets the total size of the file.
     * @return The total size of the file.
     */
    public long getSize() {
        return size;
    }

    /**
     * Gets the amount to be transferred.
     * @return The amount to be transferred.
     */
    public long getTodo() {
        return todo;
    }

    /**
     * Gets the transferred amount.
     * @return The amount transferred.
     */
    public long getDone() {
        return size - todo;
    }

    /**
     * Gets the request associated with this transfer.
     * @return The request associated with the transfer.
     */
    public UploadRequest getRequest() {
        return req;
    }

    /**
     * Gets the user associated with this transfer.
     * @return The user associated with this transfer.
     */
    public User getUser() {
        return user;
    }

    /**
     * Runs the actual transfer.
     * @param c The controls to the protocol.
     */
    public void runTask(Controls c) {
        run: {
            try {
                // Accept a transfer.
                manager.addTransfer(this);
                socket = server.accept();
                OutputStream ostream = socket.getOutputStream();

                manager.statusUpdate(this);

                // While there is stuff to transfer, transfer.
                int tokenamount = manager.getTokenValue();
                while (istream.available() > 0) {
                    if (tokenamount <= 0) {
                        manager.getToken();
                        tokenamount = manager.getTokenValue();
                    } else {
                        tokenamount--;
                    }
                    int totrans = Math.min(1024, istream.available());
                    byte[] dat = new byte[totrans];
                    istream.read(dat);
                    ostream.write(dat);

                    todo -= totrans;

                    if (!shouldrun) {
                        break run;
                    }
                }
            } catch (IOException ie) {
                // Failed..
                Controls.getLogger().warning("Error uploading file.");
                break run;
            }
        }
        close();
        done = true;
        manager.removeTransfer(this);
    }
}