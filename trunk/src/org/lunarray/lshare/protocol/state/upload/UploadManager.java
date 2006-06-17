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

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Semaphore;

import org.lunarray.lshare.protocol.Controls;
import org.lunarray.lshare.protocol.events.UploadEvent;
import org.lunarray.lshare.protocol.events.UploadListener;
import org.lunarray.lshare.protocol.state.userlist.User;
import org.lunarray.lshare.protocol.tasks.RunnableTask;

/**
 * A manager for handling uploads.
 * @author Pal Hargitai
 */
public class UploadManager implements ExternalUploadManager {
    /**
     * The first port to bind to. This is port ${value};
     */
    public final static int BEGIN_PORT = 7401;

    /**
     * The last port to bind to. This is port ${value};
     */
    public final static int END_PORT = 7500;

    /**
     * The interval between token additions.
     */
    public int interval;

    /**
     * The amount of tokens. This is: ${value};
     */
    public static final int AMOUNT = 100;

    /**
     * Bandwidth limiting. This should give some kind of tokenbucket
     * implementation.
     */
    private Semaphore ratesem;

    /**
     * Rate value. The amount of bytes available with one token.
     */
    private int rateval;

    /**
     * All registered uploads.
     */
    private List<UploadTransfer> uploads;

    /**
     * The controls to the protocol.
     */
    private Controls controls;

    /**
     * The upload settings.
     */
    private UploadSettings settings;

    /**
     * A synchornisation variable for controlling running of the token adder.
     */
    private boolean shouldrun;

    /**
     * The listeners of this manager.
     */
    private List<UploadListener> listeners;

    /**
     * Constructs an upload manager.
     * @param c The controls to the protocol.
     */
    public UploadManager(Controls c) {
        controls = c;
        settings = controls.getSettings().getUploadSettings();
        interval = 1000 / AMOUNT;
        rateval = settings.getUpRate() / AMOUNT;
        ratesem = new Semaphore(AMOUNT);
        shouldrun = true;
        c.getTasks().backgroundTask(new TokenAdder());
        listeners = new LinkedList<UploadListener>();
    }

    /**
     * Sets the amount of available upload slots.
     * @param s The new amount of upload slots.
     */
    public void setSlots(int s) {
        settings.setSlots(s);
    }

    /**
     * Gets the amount of available upload slots.
     * @return The amount of upload slots.
     */
    public int getSlots() {
        return settings.getSlots();
    }

    /**
     * Set the download rate.
     * @param r The new download rate.
     */
    public void setRate(int r) {
        settings.setUpRate(r);
        rateval = settings.getUpRate() / AMOUNT;
    }

    /**
     * Get the download rate.
     * @return The download rate.
     */
    public int getRate() {
        return settings.getUpRate();
    }

    /**
     * Closes all uploads.
     */
    public synchronized void close() {
        shouldrun = false;
        for (UploadTransfer t : uploads) {
            t.close();
        }
    }

    /**
     * Gets a list of all uploads.
     * @return All known uploads.
     */
    public List<UploadTransfer> getUploads() {
        return uploads;
    }

    /**
     * Processes a request for a filetransfer.
     * @param u The user the request originated from.
     * @param f The request for a filetransfer.
     */
    public void processRequest(User u, UploadRequest f) {
        if (settings.getSlots() > uploads.size()) {
            UploadHandler h = new UploadHandler(this, f, u);
            controls.getTasks().backgroundTask(h);
        } else {
            // TODO send user busy
        }
    }

    /**
     * Add a listener.
     * @param lis The listener to add.
     */
    public void addListener(UploadListener lis) {
        listeners.add(lis);
    }

    /**
     * Remove a listener.
     * @param lis The listener to remove.
     */
    public void removeListener(UploadListener lis) {
        listeners.remove(lis);
    }

    /**
     * Updates the status of a transfer.
     * @param t The upload that has been updated.
     */
    protected void statusUpdate(UploadTransfer t) {
        if (uploads.contains(t)) {
            UploadEvent e = new UploadEvent(t, this);
            for (UploadListener lis : listeners) {
                lis.uploadUpdated(e);
            }
        }
    }

    /**
     * Adds an upload.
     * @param t The upload to be added.
     */
    protected void addTransfer(UploadTransfer t) {
        if (!uploads.contains(t)) {
            uploads.add(t);

            UploadEvent e = new UploadEvent(t, this);
            for (UploadListener lis : listeners) {
                lis.uploadAdded(e);
            }
        }
    }

    /**
     * Removes an upload.
     * @param t The upload to be removed.
     */
    protected void removeTransfer(UploadTransfer t) {
        if (uploads.contains(t)) {
            uploads.remove(t);

            UploadEvent e = new UploadEvent(t, this);
            for (UploadListener lis : listeners) {
                lis.uploadRemoved(e);
            }
        }
    }

    /**
     * Get a single token for upload.
     */
    protected void getToken() {
        try {
            ratesem.acquire();
        } catch (InterruptedException ie) {
            // Should not happen, but just continue.
        }
    }

    /**
     * Get the value for a token.
     * @return The value of the token in kbytes.
     */
    protected int getTokenValue() {
        return rateval;
    }

    /**
     * The adder of tokens.
     * @author Pal Hargitai
     */
    private class TokenAdder implements RunnableTask {
        /**
         * Regularly adds tokens.
         * @param c The controls to the protocol.
         */
        public void runTask(Controls c) {
            run: {
                while (true) {
                    if (ratesem.availablePermits() < AMOUNT) {
                        ratesem.release();
                    }
                    try {
                        Thread.sleep(interval);
                    } catch (InterruptedException ie) {
                        // Ignore
                    }
                    if (!shouldrun) {
                        break run;
                    }
                }
            }
        }
    }
}
