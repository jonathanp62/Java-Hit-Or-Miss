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

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.concurrent.atomic.AtomicInteger;

import net.jmp.hitormiss.config.Config;

import net.jmp.hitormiss.data.RequestQueueElement;
import net.jmp.hitormiss.data.RequestType;

import net.jmp.hitormiss.util.Synchronizer;

import org.redisson.api.RedissonClient;

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

    /** The request queue. */
    private final Deque<RequestQueueElement> requestQueue = new ArrayDeque<>();

    /** The configuration. */
    private final Config config;

    /** The Redisson client. */
    private final RedissonClient client;

    /**
     * The constructor.
     *
     * @param   config  net.jmp.hitormiss.config.Config
     * @param   client  org.redisson.api.RedissonClient
     */
    public StatisticsThread(final Config config, final RedissonClient client) {
        super();

        this.config = config;
        this.client = client;
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
     * Return the request queue.
     *
     * @return  java.util.Deque&lt;net.jmp.hitormiss.data.RequestQueueElement&gt;
     */
    public Deque<RequestQueueElement> getRequestQueue() {
        return this.requestQueue;
    }

    /**
     * The run method.
     */
    @Override
    public void run() {
        this.logger.entry();

        boolean shutdown = false;

        final AtomicInteger hits = new AtomicInteger(0);
        final AtomicInteger misses = new AtomicInteger(0);

        while (!shutdown) {
            synchronized (this.synchronizer) {
                if (!this.synchronizer.isNotified()) {
                    try {
                        this.synchronizer.wait();
                    } catch (final InterruptedException ie) {
                        this.logger.catching(ie);
                        Thread.currentThread().interrupt();     // Restore the interrupt status
                    }
                }

                this.synchronizer.setNotified(false);

                shutdown = this.processRequestQueue(hits, misses);
            }
        }

        this.logger.info("Hits  : {}", hits.get());
        this.logger.info("Misses: {}", misses.get());

        this.logger.info("Statistics thread is exiting");

        this.logger.exit();
    }

    /**
     * Process the request queue. Note that hits
     * and misses are updated and hence not final.
     * True is returned if shutdown was requested.
     *
     * @param   hits    Integer
     * @param   misses  Integer
     * @return          boolean
     */
    private boolean processRequestQueue(final AtomicInteger hits, final AtomicInteger misses) {
        this.logger.entry(hits, misses);

        boolean shutdown = false;

        while (this.requestQueue.peek() != null) {
            final var requestElement = this.requestQueue.poll();

            if (requestElement.getRequestType() == RequestType.HIT)
                hits.incrementAndGet();

            if (requestElement.getRequestType() == RequestType.MISS)
                misses.incrementAndGet();

            if (requestElement.getRequestType() == RequestType.SHUTDOWN)
                shutdown = true;
        }

        this.logger.exit(shutdown);

        return shutdown;
    }
}
