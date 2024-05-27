package net.jmp.hitormiss.util;

/*
 * (#)TestSynchronizer.java 0.2.0   05/27/2024
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

/**
 * The synchronizer test class.
 */
public class TestSynchronizer {
    /** The synchronizer. */
    private Synchronizer synchronizer;

    /**
     * Method to run before each test.
     */
    @Before
    public void init() {
        this.synchronizer = new Synchronizer();
    }

    /**
     * Test setting the thread.
     */
    @Test
    public void testSetThread() {
        this.synchronizer.setThread(Thread.currentThread());
        assertEquals(Thread.currentThread(), this.synchronizer.getThread());
    }

    /**
     * Test setting the thread to null.
     */
    @Test(expected = NullPointerException.class)
    public void testSetThreadToNyll() {
        this.synchronizer.setThread(null);
    }

    /**
     * Test getting the notification state.
     */
    @Test
    public void testIsNotified() {
        assertFalse(this.synchronizer.isNotified());
    }

    /**
     * Test setting the notification state.
     */
    @Test
    public void testSetNotified() {
        this.synchronizer.setNotified(true);
        assertTrue(this.synchronizer.isNotified());
    }
}
