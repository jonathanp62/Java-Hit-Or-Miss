package net.jmp.hitormiss.config;

/*
 * (#)ProcessUtility.java   0.5.0   06/29/2024
 *
 * @author    Jonathan Parker
 * @version   0.5.0
 * @since     0.5.0
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

import com.google.gson.annotations.SerializedName;

import java.util.Objects;

/**
 * The process utility class of the configuration.
 */
public class ProcessUtility {
    /** The command for Intel architecture. */
    @SerializedName("redis-server")
    private String redisServer;

    /** The command for Apple Silicon architecture. */
    @SerializedName("redis-stack-server")
    private String redisStackServer;

    /**
     * Get the Redis server.
     *
     * @return  java.lang.String
     */
    public String getRedisServer() {
        return this.redisServer;
    }

    /**
     * Set the Redis server.
     *
     * @param   redisServer java.lang.String
     */
    public void setRedisServer(final String redisServer) {
        this.redisServer = redisServer;
    }

    /**
     * Get the Redis stack server.
     *
     * @return  java.lang.String
     */
    public String getRedisStackServer() {
        return this.redisStackServer;
    }

    /**
     * Set the Redis stack server.
     *
     * @param   redisStackServer    java.lang.String
     */
    public void setRedisStackServer(final String redisStackServer) {
        this.redisStackServer = redisStackServer;
    }

    /**
     * The equals method.
     *
     * @param   o   java.lang.Object
     * @return      boolean
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        final ProcessUtility that = (ProcessUtility) o;

        return Objects.equals(this.redisServer, that.redisServer) && Objects.equals(this.redisStackServer, that.redisStackServer);
    }

    /**
     * The hash-code method.
     *
     * @return  int
     */
    @Override
    public int hashCode() {
        int result = Objects.hashCode(this.redisServer);

        result = 31 * result + Objects.hashCode(this.redisStackServer);

        return result;
    }

    /**
     * The to-string method.
     *
     * @return  java.lang.String
     */
    @Override
    public String toString() {
        return "ProcessUtility{" +
                "redisServer='" + this.redisServer + '\'' +
                ", redisStackServer='" + this.redisStackServer + '\'' +
                '}';
    }
}
