package net.jmp.hitormiss.util;

/*
 * (#)Synchronizer.java 0.2.0   05/27/2024
 *
 * @author   Jonathan Parker
 * @version  0.2.0
 * @since    0.2.0
 *
 * MIT License
 *
 * Copyright (c) 2024 Jonathan M. Parker
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

import java.util.Objects;

/**
 * The synchronizer class.
 */
public final class Synchronizer {
    /** The thread being synchronized with. */
    private Thread thread;

    /** Indicator set to true when the synchronized thread has been notified. */
    private boolean notified;

    /**
     * The default constructor.
     */
    public Synchronizer() {
        super();
    }

    /**
     * Method to set the synchronized thread.
     *
     * @param   thread  java.lang.Thread
     */
    public void setThread(final Thread thread) {
        Objects.requireNonNull(thread, "Thread thread is null");

        this.thread = thread;
    }

    /**
     * Method to return the synchronized thread.
     *
     * @return  java.lang.Thread
     */
    public Thread getThread() {
        return this.thread;
    }

    /**
     * Set the notified indicator.
     *
     * @param   notifier boolean
     */
    public synchronized void setNotified(final boolean notifier) {
        this.notified = notifier;
    }

    /**
     * Return the notified indicator.
     *
     * @return  boolean
     */
    public synchronized boolean isNotified() {
        return this.notified;
    }
}
