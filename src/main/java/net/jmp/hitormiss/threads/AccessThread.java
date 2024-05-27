package net.jmp.hitormiss.threads;

/*
 * (#)AccessThread.java 0.2.0   05/27/2024
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

import net.jmp.hitormiss.config.Config;

import net.jmp.hitormiss.util.Synchronizer;

import org.redisson.api.RedissonClient;
import org.slf4j.LoggerFactory;

import org.slf4j.ext.XLogger;

/**
 * The thread that access Redis buckets like a cache.
 */
public final class AccessThread implements Runnable {
    /** The logger. */
    private final XLogger logger = new XLogger(LoggerFactory.getLogger(this.getClass().getName()));

    /** The configuration. */
    private final Config config;

    /** The Redisson client. */
    private final RedissonClient client;

    /**
     * The constructor.
     *
     * @param   config                  net.jmp.hitormiss.config.Config
     * @param   client                  org.redisson.api.RedissonClient
     * @param   statisticsSynchronizer  net.jmp.hitormiss.util.Synchronizer
     */
    public AccessThread(final Config config, final RedissonClient client, final Synchronizer statisticsSynchronizer) {
        super();

        this.config = config;
        this.client = client;
    }

    /**
     * The run method.
     */
    @Override
    public void run() {
        this.logger.entry();

        final int counter = this.config.getApplication().getInitialNumberOfBuckets() * 3;

        for (int i = 0; i < counter; i++) {
            ;
        }

        this.logger.exit();
    }
}
