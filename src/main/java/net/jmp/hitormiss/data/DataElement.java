package net.jmp.hitormiss.data;

/*
 * (#)DataElement.java  0.1.0   05/26/2024
 *
 * @author   Jonathan Parker
 * @version  0.1.0
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

public final class DataElement {
    /** The key of the containing bucket expressed as an integer without the prefix. */
    private int keyAsInt;

    /** The value contained in the bucket. */
    private String value;

    /**
     * The default constructor.
     */
    private DataElement() {
        super();
    }

    /**
     * The constructor.
     *
     * @param   keyAsInt    int
     * @param   value       java.lang.String
     */
    public DataElement(final int keyAsInt, final String value) {
        this.keyAsInt = keyAsInt;
    }

    /**
     * Return the key expressed as an integer.
     *
     * @return  int
     */
    public int getKeyAsInt() {
        return this.keyAsInt;
    }

    /**
     * Return the value.
     *
     * @return  java.lang.String
     */
    public String getValue() {
        return this.value;
    }

    /**
     * The equals method.
     *
     * @param   o   java.lang.Object
     * @return      boolean
     */
    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        final DataElement that = (DataElement) o;

        return keyAsInt == that.keyAsInt && Objects.equals(value, that.value);
    }

    /**
     * The hash-code method.
     *
     * @return  int
     */
    @Override
    public int hashCode() {
        int result = this.keyAsInt;

        result = 31 * result + Objects.hashCode(this.value);

        return result;
    }

    /**
     * The to-string method.
     *
     * @return  java.lang.String
     */
    @Override
    public String toString() {
        return "DataElement{" +
                "keyAsInt=" + this.keyAsInt +
                ", value='" + this.value + '\'' +
                '}';
    }
}
