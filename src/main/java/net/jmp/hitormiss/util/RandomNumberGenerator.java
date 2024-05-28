package net.jmp.hitormiss.util;

/*
 * (#)RandomNumberGenerator.java    0.3.0   05/28/2024
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

import java.util.Random;

import org.slf4j.LoggerFactory;

import org.slf4j.ext.XLogger;

/**
 * The random number generator class.
 */
public final class RandomNumberGenerator {
    /** The logger. */
    private final XLogger logger = new XLogger(LoggerFactory.getLogger(this.getClass().getName()));

    /** The lower limit (inclusive) of random number generation. */
    private final int lowerLimit;

    /** The upper limit (inclusive) of random number generation. */
    private final int upperLimit;

    /** The random number generator. */
    private Random randomGenerator;

    /**
     * The constructor.
     *
     * @param   lowerLimit  int
     * @param   upperLimit  int
     */
    public RandomNumberGenerator(final int lowerLimit, final int upperLimit) {
        super();

        this.lowerLimit = lowerLimit;
        this.upperLimit = upperLimit;
    }

    /**
     * Generate a random number.
     *
     * @return  int
     */
    public int generate() {
        this.logger.entry();

        if (this.randomGenerator == null)
            this.randomGenerator = new Random();

        final int randomNumber = this.randomGenerator.nextInt((this.upperLimit - this.lowerLimit) + 1) + this.lowerLimit;

        this.logger.exit(randomNumber);

        return randomNumber;
    }
}
