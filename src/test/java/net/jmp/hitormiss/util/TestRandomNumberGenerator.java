package net.jmp.hitormiss.util;

/*
 * (#)TestRandomNumberGenerator.java    0.3.0   05/28/2024
 *
 * @author   Jonathan Parker
 * @version  0.3.0
 * @since    0.3.0
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

import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * The random number generator test class.
 */
public class TestRandomNumberGenerator {
    @Test
    public void testOneAndOneThousand() {
        final RandomNumberGenerator rng = new RandomNumberGenerator(1, 1_000);

        for (int i = 0; i < 1_000; i++) {
            final int result = rng.generate();

            assertTrue(result > 0);
            assertTrue(result <= 1_000);
        }
    }

    @Test
    public void testOneAndTenThousand() {
        final RandomNumberGenerator rng = new RandomNumberGenerator(1, 10_000);

        for (int i = 0; i < 10_000; i++) {
            final int result = rng.generate();

            assertTrue(result > 0);
            assertTrue(result <= 10_000);
        }
    }
}
