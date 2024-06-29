package net.jmp.hitormiss;

/*
 * (#)RedisError.java   0.5.0   06/29/2024
 *
 * @author   Jonathan Parker
 * @version  0.5.0
 * @since    0.5.0
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

import java.io.Serial;

/**
 * The Redis error class. Only thrown when there is
 * no ability to connect to Redis and the application
 * has no recovery options.
 */
public final class RedisError extends Error {
    /** The serializable version identifier. */
    @Serial
    private static final long serialVersionUID = 5722712449912008505L;

    /**
     * The default constructor.
     */
    private RedisError() {
        throw new UnsupportedOperationException("The default constructor should not be used");
    }

    /**
     * A constructor that takes a message and a throwable.
     *
     * @param   message     java.lang.String
     * @param   throwable   java.lang.Throwable
     */
    RedisError(final String message, final Throwable throwable) {
        super(message, throwable);
    }
}
