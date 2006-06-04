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

import org.lunarray.lshare.protocol.Controls;
import org.lunarray.lshare.protocol.packets.download.RequestOut;
import org.lunarray.lshare.protocol.tasks.TimeoutTask;

/**
 * The timeout for a download handle.
 * @author Pal Hargitai
 */
public class DownloadTimeout extends TimeoutTask {
    /**
     * The delay between timeouts. This is {@value} milliseconds.
     */
    public final static int DELAY = 1000;

    /**
     * The handler that this timeout is for.
     */
    private DownloadHandler handler;

    /**
     * An outgoing request.
     */
    private RequestOut packet;

    /**
     * Constructs a timeout for a download handler.
     * @param h The handler for downloading.
     * @param p The packet for retransmission.
     */
    public DownloadTimeout(DownloadHandler h, RequestOut p) {
        handler = h;
        packet = p;
    }

    @Override
    /**
     * Get the delay between a timeout.
     * @return The delay.
     */
    public int getDelay() {
        return DELAY;
    }

    @Override
    /**
     * Get the amount of times that this may timeout.
     * @return The amount of times it may timeout.
     */
    public int amount() {
        return 3;
    }

    @Override
    /**
     * Handle a single timeout.
     * @param c The controls to the protocol.
     */
    public void timedOut(Controls c) {
        c.getUDPTransport().send(packet);
        Controls.getLogger().fine(
                "Timedout request to: " + packet.getTarget().getHostAddress());
    }

    @Override
    /**
     * Cleanup this timeout as it is no longer relevant.
     * @param c The controls to the protocol.
     */
    public void cleanupFinished(Controls c) {
        // Do nothing, just let it clean up
        Controls.getLogger().finer(
                "Running: " + packet.getTarget().getHostAddress());
    }

    @Override
    /**
     * Clean up this timeout as all available timeouts have passed.
     * @param c The controls to the protocol.
     */
    public void cleanupTimedout(Controls c) {
        handler.close();
        Controls.getLogger().fine(
                "Cleaning up: " + packet.getTarget().getHostAddress());
    }

    @Override
    /**
     * Checks wether this timeout is still relevant.
     * @param c The controls to the protocol.
     * @return False if the timeout is still relevant, true if not.
     */
    public boolean finished(Controls c) {
        switch (handler.getStatus()) {
        case CONNECTING:
            return false;
        default:
            return true;
        }
    }
}
