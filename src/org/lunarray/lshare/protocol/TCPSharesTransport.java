package org.lunarray.lshare.protocol;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import org.lunarray.lshare.protocol.filelist.FilelistSender;

/**
 * A server socket for sharing a filelist.
 * @author Pal Hargitai
 */
public class TCPSharesTransport extends Thread {
    /**
     * The server sockets that accepts connections.
     */
    private ServerSocket server;

    /**
     * The running variable to control behavior of the server.
     */
    private boolean run;

    /**
     * The threadgroup in which client threads will reside.
     */
    private ThreadGroup tgroup;

    /**
     * The client threads.
     */
    private ArrayList<FilelistSender> senders;

    /**
     * Access to the protocol.
     */
    private Controls controls;

    /**
     * Instanciates the transport.
     * @param c The controlls for access to the protocol.
     */
    public TCPSharesTransport(Controls c) {
        super(c.getThreadGroup(), "tcptransport");
        run = true;
        controls = c;
        senders = new ArrayList<FilelistSender>();
        tgroup = new ThreadGroup(c.getThreadGroup(), "filelisters");
    }

    /**
     * Initialises the socket and sets up the thread.
     */
    public void init() {
        try {
            server = new ServerSocket(Controls.TCP_PORT);
            // Start running
            run = true;
            start();
        } catch (IOException ie) {
            Controls.getLogger().severe("Cannot instanciate socket!");
        }
    }

    /**
     * Handling connections.
     */
    public void run() {
        if (server.isBound()) {
            while (run) {
                try {
                    Socket s = server.accept();
                    FilelistSender l = new FilelistSender(tgroup, controls, s);
                    senders.add(l);
                    l.start();
                } catch (IOException ie) {
                    if (run) {
                        Controls.getLogger().severe(
                                "Cannot listen for connections!");
                    }
                }
            }
        } else {
            Controls.getLogger().severe("Socket unbound!");
        }
    }

    /**
     * Close down the clients and clean up the socket.
     */
    public void close() {
        for (FilelistSender l : senders) {
            l.close();
        }
        run = false;
        try {
            server.close();
        } catch (Exception ie) {
            Controls.getLogger().severe("Cannot close socket!");
        }
        Controls.getLogger().fine("Closed TCP socket and connections");
    }
}
