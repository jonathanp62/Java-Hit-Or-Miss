package net.jmp.hitormiss.data;

/*
 * (#)DataManager.java  0.3.0   05/29/2024
 * (#)DataManager.java  0.1.0   05/26/2024
 *
 * @author   Jonathan Parker
 * @version  0.3.0
 * @since    0.1.0
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

import java.util.concurrent.atomic.AtomicInteger;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.redisson.api.RBucket;
import org.redisson.api.RKeys;
import org.redisson.api.RedissonClient;

import org.slf4j.LoggerFactory;

import org.slf4j.ext.XLogger;

import net.jmp.hitormiss.config.Config;

/**
 * The data manager class.
 */
public final class DataManager {
    /** The logger. */
    private final XLogger logger = new XLogger(LoggerFactory.getLogger(this.getClass().getName()));

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
    public DataManager(final Config config, final RedissonClient client) {
        super();

        Objects.requireNonNull(config);
        Objects.requireNonNull(client);

        this.config = config;
        this.client = client;
    }

    /**
     * Set up the initial data in order
     * to provide cache hits and misses.
     */
    public void setupData() {
        this.logger.entry();

        this.setupDataElementBuckets();
        this.setupAccumulatorBuckets();

        this.logger.exit();
    }

    /**
     * Set up the data element buckets.
     */
    private void setupDataElementBuckets() {
        this.logger.entry();

        final String bucketKeyPrefix = this.config.getApplication().getBucketKeyPrefix();
        final int initialNumberOfBuckets = this.config.getApplication().getInitialNumberOfBuckets();

        this.logger.info("Creating {} buckets to start with", initialNumberOfBuckets);

        for (int i = 0; i < this.config.getApplication().getInitialNumberOfBuckets(); i++) {
            final String key = bucketKeyPrefix + i;
            final String value = UUID.randomUUID().toString();
            final RBucket<DataElement> bucket = this.client.getBucket(key);

            bucket.setIfAbsent(new DataElement(i, value));
        }

        this.logger.exit();
    }

    /**
     * Set up the accumulator buckets.
     */
    private void setupAccumulatorBuckets() {
        this.logger.entry();

        final var hitsBucketName = this.config.getApplication().getAccumulatorBucketNameForHits();
        final var missesBucketName = this.config.getApplication().getAccumulatorBucketNameForMisses();

        final RBucket<Integer> hitsBucket = this.client.getBucket(hitsBucketName);
        final RBucket<Integer> missesBucket = this.client.getBucket(missesBucketName);

        hitsBucket.set(0);
        missesBucket.set(0);

        this.logger.exit();
    }

    /**
     * Tear down the data used to
     * provide cache hits and misses.
     */
    public void teardownData() {
        this.logger.entry();

        this.teardownAccumulatorBuckets();
        this.teardownDataElementBuckets();

        this.logger.exit();
    }

    /**
     * Tear down the data element buckets.
     */
    private void teardownDataElementBuckets() {
        this.logger.entry();

        final Pattern pattern = Pattern.compile("^" + this.config.getApplication().getBucketKeyPrefix() + "\\d+$");
        final RKeys keys = this.client.getKeys();

        final AtomicInteger deleteCountOK = new AtomicInteger(0);
        final AtomicInteger deleteCountNotOK = new AtomicInteger(0);

        keys.getKeys().forEach(key -> {
            final Matcher matcher = pattern.matcher(key);

            if (matcher.matches()) {
                if (!this.deleteBucket(key)) {
                    deleteCountNotOK.incrementAndGet();
                } else {
                    deleteCountOK.incrementAndGet();
                }
            }
        });

        this.logger.info("{} buckets deleted OK", deleteCountOK.get());
        this.logger.info("{} buckets failed to be deleted", deleteCountNotOK.get());

        this.logger.exit();
    }

    /**
     * Tear down the accumulator buckets.
     */
    private void teardownAccumulatorBuckets() {
        this.logger.entry();

        final var hitsBucketName = this.config.getApplication().getAccumulatorBucketNameForHits();
        final var missesBucketName = this.config.getApplication().getAccumulatorBucketNameForMisses();

        this.deleteBucket(missesBucketName);
        this.deleteBucket(hitsBucketName);

        this.logger.exit();
    }

    /**
     * Delete the specified bucket.
     * True is returned if the
     * bucket was deleted okay.
     *
     * @param   key java.lang.String
     * @return      boolean
     */
    private boolean deleteBucket(final String key) {
        this.logger.entry(key);

        assert key != null;

        boolean result = false;

        if (!this.client.getBucket(key).delete())
            this.logger.error("Failed to delete bucket '{}'", key);
        else
            result = true;

        this.logger.exit(result);

        return result;
    }
}
