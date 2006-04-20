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
                int toread = Long.valueOf(Math.min(chunk.getTodo(), 1024))
                        .intValue();

                byte[] data = new byte[toread];

                try {
                    istream.read(data);
                    chunk.write(data, toread);
                } catch (IOException ie) {
                    Controls.getLogger().warning("Error transferring file.");
                    break run;
                }
            }
        }
        handler.done();
    }

    /**
     * Initialises the transfer.
     * @throws IOException Thrown if the socket or it's stream can't be opened.
     */
    public void init() throws IOException {
        socket = new Socket(user.getAddress(), port);
        istream = socket.getInputStream();
    }

    /**
     * Closes the transfer.
     */
    public void close() {
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
