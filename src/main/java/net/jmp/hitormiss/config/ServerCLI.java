package net.jmp.hitormiss.config;

/*
 * (#)ServerCLI.java    0.4.0   06/14/2024
 * (#)ServerCLI.java    0.1.0   05/25/2024
 *
 * @author    Jonathan Parker
 * @version   0.4.0
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

public final class ServerCLI {
    /** The command for Intel architecture. */
    @SerializedName("command-intel")
    private String commandIntel;

    /** The command for Apple Silicon architecture. */
    @SerializedName("command-silicon")
    private String commandSilicon;

    /** The argument. */
    @SerializedName("argument")
    private String argument;

    /**
     * Get the command for Intel.
     *
     * @return  java.lang.String
     */
    public String getCommandIntel() {
        return this.commandIntel;
    }

    /**
     * Set the command for Intel.
     *
     * @param   commandIntel    java.lang.String
     */
    public void setCommandIntel(final String commandIntel) {
        this.commandIntel = commandIntel;
    }

    /**
     * Get the command for Apple Silicon.
     *
     * @return  java.lang.String
     */
    public String getCommandSilicon() {
        return this.commandSilicon;
    }

    /**
     * Set the command for Apple Silicon.
     *
     * @param   commandSilicon  java.lang.String
     */
    public void setCommandSilicon(final String commandSilicon) {
        this.commandSilicon = commandSilicon;
    }

    /**
     * Get the argument.
     *
     * @return  java.lang.String
     */
    public String getArgument() {
        return this.argument;
    }

    /**
     * Set the argument.
     *
     * @param   argument    java.lang.String
     */
    public void setArgument(final String argument) {
        this.argument = argument;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        final ServerCLI serverCLI = (ServerCLI) o;

        return Objects.equals(this.commandIntel, serverCLI.commandIntel) && Objects.equals(this.commandSilicon, serverCLI.commandSilicon) && Objects.equals(this.argument, serverCLI.argument);
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(this.commandIntel);

        result = 31 * result + Objects.hashCode(this.commandSilicon);
        result = 31 * result + Objects.hashCode(this.argument);

        return result;
    }

    @Override
    public String toString() {
        return "ServerCLI{" +
                "commandIntel='" + this.commandIntel + '\'' +
                "commandSilicon='" + this.commandSilicon + '\'' +
                ", argument='" + this.argument + '\'' +
                '}';
    }
}
