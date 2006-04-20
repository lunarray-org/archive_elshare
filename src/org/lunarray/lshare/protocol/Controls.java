package org.lunarray.lshare.protocol;

import java.io.IOException;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import org.lunarray.lshare.protocol.packets.user.SignOffOut;

/**
 * Standard controls that should be known throughout the system.
 * @author Pal Hargitai
 */
public class Controls {
    /**
     * The UDP port that UDP traffic will be sent and received on. This is port
     * {@value}.
     */
    public final static int UDP_PORT = 7400;

    /**
     * The TCP port for filelist handling. This is port {@value}.
     */
    public final static int TCP_PORT = 7400;

    /**
     * The maximum packet size for sending over UDP. The MTU size of {@value}.
     */
    public final static int UDP_MTU = 1400;

    /**
     * The UDP transport.
     */
    private UDPTransport utrans;

    /**
     * The TCP transport for file list sharing.
     */
    private TCPSharesTransport tstrans;

    /**
     * The protocol state.
     */
    private State state;

    /**
     * The settings for the state.
     */
    private Settings settings;

    /**
     * Task handling for background tasks.
     */
    private Tasks tasks;

    /**
     * The threadgroup in which most threads of this protocol will reside in.
     */
    private ThreadGroup lsgroup;

    /**
     * Instanciates the controls and sets up the protocol and all it's
     * supporting functionality.
     */
    public Controls() {
        // Init logger
        Handler ha;
        try {
            // Try to use a file handler.
            ha = new FileHandler("lshare.log");
        } catch (IOException ie) {
            // We assume that something went wrong, use a console handler.
            ha = new ConsoleHandler();
        }
        ha.setFormatter(new SimpleFormatter());
        ha.setLevel(Level.ALL);
        Logger ls = Logger.getLogger("lshare");
        // >TEMP
        Handler hb = new ConsoleHandler();
        hb.setLevel(Level.FINER);
        hb.setFormatter(new SimpleFormatter());
        ls.addHandler(hb);
        // <TEMP
        ls.setLevel(Level.ALL);
        ls.addHandler(ha);

        lsgroup = new ThreadGroup("lshare");
        settings = new Settings(this);
        utrans = new UDPTransport(this);
        tstrans = new TCPSharesTransport(this);
        tasks = new Tasks(this);
        state = new State();
        state.init(this);
    }

    /**
     * Start the protocol and it's subsystems.
     */
    public void start() {
        tstrans.init();
        getUDPTransport().init();
        getTasks().start();
    }

    /**
     * Stop the protocol and it's subsystems.
     */
    public void stop() {
        tstrans.close();
        getTasks().stop();
        // Send logout
        SignOffOut soo = new SignOffOut();
        getUDPTransport().send(soo);

        getUDPTransport().close();
        getState().commit();
        // controls.getSettings().commit();
    }

    /**
     * Get the settings that are available for this protocol.
     * @return The settings.
     */
    public Settings getSettings() {
        return settings;
    }

    /**
     * Get the state of the protocol.
     * @return The state.
     */
    public State getState() {
        return state;
    }

    /**
     * Get the UDP transport for this protocol.
     * @return The UDP Transport.
     */
    public UDPTransport getUDPTransport() {
        return utrans;
    }

    /**
     * Get the tasks for this protocol.
     * @return The tasks.
     */
    public Tasks getTasks() {
        return tasks;
    }

    /**
     * Gets the logger that may be used for this instance of the protocol.
     * @return The logger.
     */
    public static Logger getLogger() {
        return LogManager.getLogManager().getLogger("lshare");
    }

    /**
     * Gets the threadgroup for this instance of the protocol.
     * @return The threadgroup all threads of this instance should reside in.
     */
    protected ThreadGroup getThreadGroup() {
        return lsgroup;
    }
}
