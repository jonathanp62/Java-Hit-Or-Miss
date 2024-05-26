package net.jmp.hitormiss.config;

/*
 * (#)Application.java  0.1.0   05/26/2024
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
 * The application configuration class.
 */
public final class Application {
    /** The bucket key prefix. */
    @SerializedName("bucketKeyPrefix")
    private String bucketKeyPrefix;

    /** The initial number of buckets to create. */
    @SerializedName("initialNumberOfBuckets")
    private int initialNumberOfBuckets;

    /**
     * Get the bucket key prefix.
     *
     * @return  java.lang.String
     */
    public String getBucketKeyPrefix() {
        return this.bucketKeyPrefix;
    }

    /**
     * Set the bucket key prefix.
     *
     * @param   bucketKeyPrefix java.lang.String
     */
    public void setBucketKeyPrefix(final String bucketKeyPrefix) {
        this.bucketKeyPrefix = bucketKeyPrefix;
    }

    /**
     * Get the initial number of buckets to create.
     *
     * @return  int
     */
    public int getInitialNumberOfBuckets() {
        return this.initialNumberOfBuckets;
    }

    /**
     * Set the initial number of buckets to create.
     *
     * @param   initialNumberOfBuckets  int
     */
    public void setInitialNumberOfBuckets(final int initialNumberOfBuckets) {
        this.initialNumberOfBuckets = initialNumberOfBuckets;
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

        final Application that = (Application) o;

        return this.initialNumberOfBuckets == that.initialNumberOfBuckets && Objects.equals(this.bucketKeyPrefix, that.bucketKeyPrefix);
    }

    /**
     * The hash-code method.
     *
     * @return  int
     */
    @Override
    public int hashCode() {
        int result = Objects.hashCode(this.bucketKeyPrefix);

        result = 31 * result + this.initialNumberOfBuckets;

        return result;
    }

    /**
     * The to-string method.
     *
     * @return  java.lang.String
     */
    @Override
    public String toString() {
        return "Application{" +
                "bucketKeyPrefix='" + this.bucketKeyPrefix + '\'' +
                ", initialNumberOfBuckets=" + this.initialNumberOfBuckets +
                '}';
    }
}
