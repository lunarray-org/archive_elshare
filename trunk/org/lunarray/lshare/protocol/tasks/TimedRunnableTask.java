package org.lunarray.lshare.protocol.tasks;

import java.util.TimerTask;

import org.lunarray.lshare.protocol.Controls;

/**
 * A timed runnable task.
 * @author Pal Hargitai
 */
public abstract class TimedRunnableTask extends TimerTask implements
        RunnableTask {
    /**
     * The controls to the protocol.
     */
    private Controls controls;

    /**
     * Gets the delay of between executions of the task.
     * @return The delay between executions of the task.
     */
    public abstract int getDelay();

    /**
     * Set the controls of the protocol.
     * @param c The controls of the protocol.
     */
    public void setControls(Controls c) {
        controls = c;
    }

    @Override
    /**
     * Runs the task.
     */
    public void run() {
        runTask(controls);
    }

    /**
     * Runs the actual task.
     * @param c The controls to the protocol.
     */
    public abstract void runTask(Controls c);
}
