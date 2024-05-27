package net.jmp.hitormiss.threads;

/*
 * (#)StatisticsThread.java 0.2.0   05/27/2024
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

import net.jmp.hitormiss.util.Synchronizer;

import org.slf4j.LoggerFactory;

import org.slf4j.ext.XLogger;

/**
 * The thread that captures and persists statistics.
 */
public final class StatisticsThread implements Runnable {
    /** The logger. */
    private final XLogger logger = new XLogger(LoggerFactory.getLogger(this.getClass().getName()));

    /** The synchronizer. */
    private final Synchronizer synchronizer = new Synchronizer();

    /**
     * The default constructor.
     */
    public StatisticsThread() {
        super();
    }

    /**
     * Return the synchronizer.
     *
     * @return  net.jmp.hitormiss.util.Synchronizer
     */
    public Synchronizer getSynchronizer() {
        return this.synchronizer;
    }

    /**
     * The run method.
     */
    @Override
    public void run() {
        this.logger.entry();

        while (true) {
            synchronized (this.synchronizer) {
                if (!this.synchronizer.isNotified()) {
                    try {
                        this.synchronizer.wait();
                        this.synchronizer.setNotified(false);

                        break;
                    } catch (final InterruptedException ie) {
                        this.logger.catching(ie);
                        this.synchronizer.setNotified(false);
                        Thread.currentThread().interrupt();     // Restore the interrupt status
                    }
                } else {
                    this.synchronizer.setNotified(true);
                }
            }
        }

        this.logger.exit();
    }
}
