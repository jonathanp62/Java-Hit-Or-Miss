package net.jmp.hitormiss;

/*
 * (#)RedisServerVersionLogger.java 0.5.0   06/29/2024
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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import java.util.Objects;

import java.util.regex.Pattern;

import net.jmp.hitormiss.config.Architecture;
import net.jmp.hitormiss.config.Config;

import org.slf4j.LoggerFactory;

import org.slf4j.ext.XLogger;

/**
 * The class to detect and log the Redis server version.
 */
public class RedisServerVersionLogger {
    /** The logger. */
    private final XLogger logger = new XLogger(LoggerFactory.getLogger(this.getClass().getName()));

    /** The application configuration. */
    private final Config config;

    /** A regular expression pattern to get the version from the 'redis-server --version' command. */
    private final Pattern versionPattern = Pattern.compile("(?i)\\.*v=(?<version>.+?)\\s(?-i)");

    /**
     * The default constructor.
     */
    private RedisServerVersionLogger() {
        super();

        this.config = null;
    }

    /**
     * A constructor that takes the application configuration.
     *
     * @param   config  net.jmp.hitormiss.config.Config
     */
    RedisServerVersionLogger(final Config config) {
        super();

        this.config = Objects.requireNonNull(config);
    }

    /**
     * Log the Redis server version.
     *
     * @throws  java.io.IOException When an I/O exception occurs handling any of the processes
     */
    void logRedisServerVersion() throws IOException {
        this.logger.entry();

        assert this.config != null;

        final String command = this.getCommand();
        final StringBuilder sb = new StringBuilder();

        final Process process = new ProcessBuilder(
                command,
                this.config.getRedis().getServerCLI().getArgument()
        ).redirectErrorStream(true)
         .start();

        try (final var processOutputReader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            String line;

            while ((line = processOutputReader.readLine()) != null) {
                sb.append(line);
            }

            process.waitFor();

            if (process.exitValue() == 0) {
                this.handleServerCliProcessOK(sb.toString());
            } else {
                this.handleServerCliProcessNotOK(command, process);
            }
        } catch (final InterruptedException ie) {
            this.logger.catching(ie);
            Thread.currentThread().interrupt();     // Restore the interrupt status
        }

        this.logger.exit();
    }

    /**
     * Handle the output from the 'OK'
     * response from the server CLI command.
     *
     * @param   output  java.lang.String
     */
    private void handleServerCliProcessOK(final String output) {
        this.logger.entry(output);

        assert output != null;

        final var matcher = this.versionPattern.matcher(output);

        if (matcher.find()) {
            final var version = matcher.group("version");

            if (version != null)
                this.logger.info("Redis server {}", version);
            else {
                if (this.logger.isWarnEnabled())
                    this.logger.warn("Group 'version' not found in {}", output);
            }
        } else {
            if (this.logger.isWarnEnabled())
                this.logger.warn("No match on {}", output);
        }

        this.logger.exit();
    }

    /**
     * Handle the output from the 'Not OK'
     * response from the server CLI command.
     *
     * @param   command java.lang.String
     * @param   process java.lang.Process
     */
    private void handleServerCliProcessNotOK(final String command, final Process process) {
        this.logger.entry(command, process);

        assert command != null;
        assert process != null;

        assert this.config != null;

        if (this.logger.isWarnEnabled()) {
            this.logger.warn(
                    "Process failed: {}",
                    process.info().commandLine().orElse(
                            command +
                                    ' ' +
                                    config.getRedis().getServerCLI().getArgument()
                    )
            );
        }

        this.logger.exit();
    }

    /**
     * Get the command to execute in the process.
     *
     * @return  java.lang.String
     * @throws  java.io.IOException When an I/O exception occurs handling the process that gets the architecture
     */
    private String getCommand() throws IOException {
        this.logger.entry();

        assert this.config != null;

        final var architecture = this.getArchitecture();

        String command;

        switch (architecture) {
            case Architecture.INTEL -> command = this.config.getRedis().getServerCLI().getCommandIntel();
            case Architecture.APPLE_SILICON -> command = this.config.getRedis().getServerCLI().getCommandSilicon();
            case NOT_AVAILABLE -> throw new IllegalStateException("Architecture is not available");
            default -> throw new IllegalStateException("Unsupported architecture: " + architecture);
        }

        this.logger.exit(command);

        return command;
    }

    /**
     * Return the architecture.
     *
     * @return  net.jmp.demo.redis.config.Architecture
     * @throws  java.io.IOException When an I/O exception occurs handling the process
     */
    private Architecture getArchitecture() throws IOException {
        this.logger.entry();

        Architecture result;

        final StringBuilder sb = new StringBuilder();
        final Process process = new ProcessBuilder(
                "/usr/sbin/sysctl",
                "-n",
                "machdep.cpu.brand_string"
        )
                .redirectErrorStream(true)
                .start();

        try (final var processOutputReader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            String line;

            while ((line = processOutputReader.readLine()) != null) {
                sb.append(line);
            }

            process.waitFor();

            if (process.exitValue() == 0) {
                if (sb.toString().equals("Apple M2 Max")) {
                    result = Architecture.APPLE_SILICON;
                } else if (sb.toString().equals("Intel(R) Core(TM) i7-4578U CPU @ 3.00GHz")) {
                    result = Architecture.INTEL;
                } else {
                    throw new IllegalStateException("Unsupported architecture: " + sb.toString());
                }
            } else {
                result = Architecture.NOT_AVAILABLE;

                if (this.logger.isWarnEnabled()) {
                    this.logger.warn("Process failed: {}", process.info().commandLine().orElse("/usr/sbin/sysctl -n machdep.cpu.brand_string"));
                }
            }
        } catch (final InterruptedException ie) {
            result = Architecture.NOT_AVAILABLE;

            this.logger.catching(ie);
            Thread.currentThread().interrupt();     // Restore the interrupt status
        }

        this.logger.exit(result);

        return result;
    }
}
