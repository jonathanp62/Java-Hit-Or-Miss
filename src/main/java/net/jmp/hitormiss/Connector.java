package net.jmp.hitormiss;

/*
 * (#)Connector.java    0.3.0   05/31/2024
 * (#)Connector.java    0.1.0   05/25/2024
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

import org.redisson.Redisson;

import org.redisson.api.RedissonClient;

import org.redisson.config.Config;

import org.slf4j.LoggerFactory;

import org.slf4j.ext.XLogger;

/**
 * The connector class. This class connects
 * to Redis and returns a client as well as
 * taking a client and shutting it down.
 */
final class Connector {
    /** The logger. */
    private final XLogger logger = new XLogger(LoggerFactory.getLogger(this.getClass().getName()));

    /** The name of the Redis host. */
    private final String hostName;

    /** The port that the Redis host is listening on. */
    private final int port;

    /** The Redis protocol. */
    private final String protocol;

    /**
     * The constructor.
     *
     * @param   hostName    java.lang.String
     * @param   port        int
     * @param   protocol    java.lang.String
     */
    Connector(final String hostName, final int port, final String protocol) {
        super();

        Objects.requireNonNull(hostName);
        Objects.requireNonNull(protocol);

        if (port <= 0)
            throw new IllegalArgumentException("Port must be greater than 0");

        this.hostName = hostName;
        this.port = port;
        this.protocol = protocol;
    }

    /**
     * Connect to Redis.
     *
     * @return  org.redisson.api.RedissonClient
     */
    RedissonClient connect() {
        this.logger.entry();

        final var config = new Config();

        config.useSingleServer().setAddress(this.protocol + this.hostName + ":" + this.port);

        final var client = Redisson.create(config);

        this.logger.info("Redisson client ID: {}", client.getId());

        this.logger.exit(client);

        return client;
    }

    /**
     * Disconnect from Redis.
     *
     * @param   client  org.redisson.api.RedissonClient
     */
    static void disconnect(final RedissonClient client) {
        if (client != null) {
            client.shutdown();
        }
    }
}
