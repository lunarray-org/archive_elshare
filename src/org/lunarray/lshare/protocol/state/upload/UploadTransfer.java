package org.lunarray.lshare.protocol.state.upload;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

import org.lunarray.lshare.protocol.Controls;
import org.lunarray.lshare.protocol.tasks.RunnableTask;

// TODO bandwidth throttle
// TODO give a state
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
     * Contructs a file upload.
     * @param f The file to upload.
     * @param o The offset to uplaod at.
     * @param m The manager that manages this upload.
     */
    public UploadTransfer(File f, long o, UploadManager m) {
        file = f;
        manager = m;
        offset = o;
        done = false;
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
        if (socket == null) {
            return false;
        } else {
            return socket.isConnected();
        }
    }

    /**
     * Close the transfer.
     */
    public void close() {
        try {
            istream.close();
            server.close();
            if (socket != null) {
                socket.close();
            }
        } catch (IOException ie) {
            // Ignore now
        }

        Controls.getLogger().fine("Closed transfer.");
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

                // While there is stuff to transfer, transfer.
                while (istream.available() > 0) {
                    int totrans = Math.min(1024, istream.available());
                    byte[] dat = new byte[totrans];
                    istream.read(dat);
                    ostream.write(dat);
                }
            } catch (IOException ie) {
                // Failed..
                Controls.getLogger().warning("Error uploading file.");
                break run;
            }
        }
        done = true;
        manager.removeTransfer(this);
    }
}