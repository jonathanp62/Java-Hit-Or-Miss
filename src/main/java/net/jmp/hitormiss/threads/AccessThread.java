package net.jmp.hitormiss.threads;

/*
 * (#)AccessThread.java 0.3.0   05/29/2024
 * (#)AccessThread.java 0.2.0   05/27/2024
 *
 * @author   Jonathan Parker
 * @version  0.3.0
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
import java.util.UUID;

import net.jmp.hitormiss.config.Config;

import net.jmp.hitormiss.data.DataElement;
import net.jmp.hitormiss.data.RequestQueueElement;
import net.jmp.hitormiss.data.RequestType;

import net.jmp.hitormiss.util.RandomNumberGenerator;

import org.redisson.api.RBucket;
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

    /** The statistics thread. */
    private final StatisticsThread statisticsThread;

    /**
     * The constructor.
     *
     * @param   config              net.jmp.hitormiss.config.Config
     * @param   client              org.redisson.api.RedissonClient
     * @param   statisticsThread    net.jmp.hitormiss.threads.StatisticsThread
     */
    public AccessThread(final Config config, final RedissonClient client, final StatisticsThread statisticsThread) {
        super();

        Objects.requireNonNull(config);
        Objects.requireNonNull(client);
        Objects.requireNonNull(statisticsThread);

        this.config = config;
        this.client = client;
        this.statisticsThread = statisticsThread;
    }

    /**
     * The run method.
     */
    @Override
    public void run() {
        this.logger.entry();

        final int counter = this.config.getApplication().getInitialNumberOfBuckets() * 3;

        final var requestQueue = this.statisticsThread.getRequestQueue();
        final var synchronizer = this.statisticsThread.getSynchronizer();

        final RandomNumberGenerator generator = new RandomNumberGenerator(1, counter);
        final String bucketKeyPrefix = this.config.getApplication().getBucketKeyPrefix();

        for (int i = 0; i < counter; i++) {
            // Get the bucket and determine if it is a hit or miss

            final int keyAsInt = generator.generate();
            final String bucketKey = bucketKeyPrefix + keyAsInt;
            final RBucket<DataElement> bucket = this.client.getBucket(bucketKey);
            final DataElement dataElement = bucket.get();

            if (dataElement != null && this.logger.isDebugEnabled())
                this.logger.debug("Hit on data element: {}", dataElement.toString());
            else {
                this.logger.debug("Miss on key: {}", bucketKey);

                this.persistDataElement(i);
            }

            synchronized (synchronizer) {
                if (dataElement != null)
                    requestQueue.offer(new RequestQueueElement(RequestType.HIT));
                else
                    requestQueue.offer(new RequestQueueElement(RequestType.MISS));

                synchronizer.setNotified(true);
                synchronizer.notifyAll();
            }
        }

        this.logger.info("Access thread is exiting");

        this.logger.exit();
    }

    /**
     * Store the data element that was missed.
     *
     * @param   keyAsInt    int
     */
    private void persistDataElement(final int keyAsInt) {
        this.logger.entry(keyAsInt);

        assert keyAsInt > 0;

        final String bucketKeyPrefix = this.config.getApplication().getBucketKeyPrefix();
        final String bucketKey = bucketKeyPrefix + keyAsInt;
        final String value = UUID.randomUUID().toString();
        final RBucket<DataElement> bucket = this.client.getBucket(bucketKey);

        assert bucket.get() == null;

        bucket.set(new DataElement(keyAsInt, value));

        this.logger.exit();
    }
}
