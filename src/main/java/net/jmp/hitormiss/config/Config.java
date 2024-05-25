package net.jmp.hitormiss.config;

/*
 * (#)Config.java   0.1.0   05/25/2024
 *
 * @author    Jonathan Parker
 * @version   0.1.0
 * @since     0.1.0
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
 * The configuration class.
 */
public final class Config {
    /** The Redis component. */
    @SerializedName("redis")
    private Redis redis;

    /**
     * Get the Redis component
     *
     * @return  net.jmp.demo.redis.Redis
     */
    public Redis getRedis() {
        return this.redis;
    }

    /**
     * Set the Redis component.
     *
     * @param   redis   net.jmp.demo.redis.Redis
     */
    public void setRedis(final Redis redis) {
        this.redis = redis;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        final Config config = (Config) o;

        return Objects.equals(this.redis, config.redis);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.redis);
    }

    /**
     * The to-string method.
     *
     * @return  java.lang.String
     */
    @Override
    public String toString() {
        return "Config{" +
                "redis=" + this.redis +
                '}';
    }
}
