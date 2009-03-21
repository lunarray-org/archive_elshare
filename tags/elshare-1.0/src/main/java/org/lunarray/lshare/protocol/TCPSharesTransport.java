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
package org.lunarray.lshare.protocol;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;

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
    private List<FilelistSender> senders;

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
        senders = new LinkedList<FilelistSender>();
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
