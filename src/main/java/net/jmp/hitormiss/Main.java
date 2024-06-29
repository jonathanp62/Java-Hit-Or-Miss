package net.jmp.hitormiss;

/*
 * (#)Main.java 0.5.0   06/29/2024
 * (#)Main.java 0.4.0   06/14/2024
 * (#)Main.java 0.3.0   05/29/2024
 * (#)Main.java 0.2.0   05/27/2024
 * (#)Main.java 0.1.0   05/25/2024
 *
 * @author   Jonathan Parker
 * @version  0.5.0
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

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import java.nio.file.Files;
import java.nio.file.Paths;

import java.util.Deque;
import java.util.Optional;

import java.util.regex.Pattern;

import net.jmp.hitormiss.data.RequestQueueElement;
import net.jmp.hitormiss.data.RequestType;

import net.jmp.hitormiss.threads.AccessThread;

import net.jmp.hitormiss.util.Synchronizer;

import org.redisson.api.RedissonClient;

import org.slf4j.LoggerFactory;

import org.slf4j.ext.XLogger;

import net.jmp.hitormiss.config.Architecture;
import net.jmp.hitormiss.config.Config;

import net.jmp.hitormiss.data.DataManager;

import net.jmp.hitormiss.threads.StatisticsThread;

/*
 * The application's main class.
 */
public final class Main {
    /** The default configuration file name. */
    private static final String DEFAULT_APP_CONFIG_FILE = "config/config.json";

    /** The logger. */
    private final XLogger logger = new XLogger(LoggerFactory.getLogger(this.getClass().getName()));

    /** A regular expression pattern to get the version from the 'redis-server --version' command. */
    private final Pattern versionPattern = Pattern.compile("(?i)\\.*v=(?<version>.+?)\\s(?-i)");

    /** The data manager. */
    private DataManager dataManager;

    /** The statistics thread object. */
    private StatisticsThread statisticsThreadObject;

    /** The statistics thread. */
    private Thread statisticsThread;

    /**
     * The default constructor.
     */
    private Main() {
        super();
    }

    /**
     * The run method.
     */
    private void run() {
        this.logger.entry();

        this.logger.info("{} {}", Name.NAME_STRING, Version.VERSION_STRING);

        this.getAppConfig().ifPresentOrElse(appConfig -> {
            RedissonClient client = null;

            if (ProcessUtility.isRedisProcessRunning(appConfig.getProcessUtility().getRedisServer()) ||
                ProcessUtility.isRedisProcessRunning(appConfig.getProcessUtility().getRedisStackServer())) {
                try {
                    client = this.getClient(appConfig);

                    this.logServerVersion(appConfig);

                    this.dataManager = new DataManager(appConfig, client);

                    this.dataManager.setupData();
                    this.startStatisticsThread();
                    this.runAccessThread(appConfig, client);
                } catch (final IOException ioe) {
                    this.logger.catching(ioe);
                } finally {
                    this.stopStatisticsThread();

                    // Log the contents of the accumulator buckets

                    if (this.dataManager != null)
                        this.dataManager.teardownData();

                    if (client != null) {
                        Connector.disconnect(client);

                        if (client.isShutdown())
                            this.logger.info("Redisson client has shut down");
                    }
                }
            }
        }, () -> this.logger.error("No configuration found for {}", Name.NAME_STRING));

        this.logger.exit();
    }

    /**
     * Get the application configuration.
     *
     * @return  java.lang.Optional&lt;net.jmp.hitormiss.config.Config&gt;
     */
    private Optional<Config> getAppConfig() {
        this.logger.entry();

        final String configFileName = System.getProperty("app.configurationFile", DEFAULT_APP_CONFIG_FILE);

        Config appConfig = null;

        this.logger.info("Reading the configuration from: {}", configFileName);

        try {
            appConfig = new Gson().fromJson(Files.readString(Paths.get(configFileName)), Config.class);
        } catch (final IOException ioe) {
            this.logger.catching(ioe);
        }

        this.logger.exit(appConfig);

        return Optional.ofNullable(appConfig);
    }

    /**
     * Get the Redisson client.
     *
     * @param   config  net.jmp.hitormiss.config.Config
     * @return          org.redisson.api.RedissonClient
     */
    private RedissonClient getClient(final Config config) {
        this.logger.entry(config);

        assert config != null;

        final var connector = new Connector(
                config.getRedis().getHostName(),
                config.getRedis().getPort(),
                config.getRedis().getProtocol()
        );

        final var client = connector.connect();

        this.logger.exit(client);

        return client;
    }

    /**
     * Log the server version.
     *
     * @param   config  net.jmp.hitormiss.config.Config
     */
    private void logServerVersion(final Config config) throws IOException {
        this.logger.entry(config);

        assert config != null;

        final var architecture = this.getArchitecture();

        String command;

        switch (architecture) {
            case Architecture.INTEL -> command = config.getRedis().getServerCLI().getCommandIntel();
            case Architecture.APPLE_SILICON -> command = config.getRedis().getServerCLI().getCommandSilicon();
            default -> throw new IllegalStateException("Unsupported architecture: " + architecture);
        }

        final StringBuilder sb = new StringBuilder();
        final Process process = new ProcessBuilder(
                command,
                config.getRedis().getServerCLI().getArgument()
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
                final var matcher = this.versionPattern.matcher(sb.toString());

                if (matcher.find()) {
                    final var version = matcher.group("version");

                    if (version != null)
                        this.logger.info("Redis server {}", version);
                    else {
                        if (this.logger.isWarnEnabled())
                            this.logger.warn("Group 'version' not found in {}", sb.toString());
                    }
                } else {
                    if (this.logger.isWarnEnabled())
                        this.logger.warn("No match on {}", sb.toString());
                }
            } else {
                if (this.logger.isWarnEnabled())
                    this.logger.warn(
                            "Process failed: {}",
                            process.info().commandLine().orElse(
                                    command +
                                            ' ' +
                                            config.getRedis().getServerCLI().getArgument()
                            )
                    );
            }
        } catch (final InterruptedException ie) {
            this.logger.catching(ie);
            Thread.currentThread().interrupt();     // Restore the interrupt status
        }

        this.logger.exit();
    }

    /**
     * Start the statistics thread.
     */
    private void startStatisticsThread() {
        this.logger.entry();

        this.statisticsThreadObject = new StatisticsThread();
        this.statisticsThread = new Thread(this.statisticsThreadObject, "statistics");

        this.statisticsThread.start();

        this.logger.exit();
    }

    /**
     * Run the data access thread.
     *
     * @param   config  net.jmp.hitormiss.config.Config
     * @param   client  org.redisson.api.RedissonClient
     */
    private void runAccessThread(final Config config, final RedissonClient client) {
        this.logger.entry(config, client);

        assert config != null;
        assert client != null;

        final Thread accessThread = new Thread(new AccessThread(config, client, this.statisticsThreadObject), "access");

        accessThread.start();

        try {
            accessThread.join();
        } catch (final InterruptedException ie) {
            this.logger.catching(ie);
            Thread.currentThread().interrupt();
        }

        this.logger.exit();
    }

    /**
     * Stop the statistics thread.
     */
    private void stopStatisticsThread() {
        this.logger.entry();

        if (this.statisticsThreadObject != null) {
            final Synchronizer synchronizer = this.statisticsThreadObject.getSynchronizer();
            final Deque<RequestQueueElement> requestQueue = this.statisticsThreadObject.getRequestQueue();

            synchronized (synchronizer) {
                requestQueue.offer(new RequestQueueElement(RequestType.SHUTDOWN));

                synchronizer.setNotified(true);
                synchronizer.notifyAll();
            }
        }

        if (this.statisticsThread != null) {
            try {
                this.statisticsThread.join();
            } catch (final InterruptedException ie) {
                this.logger.catching(ie);
                Thread.currentThread().interrupt();
            }
        }

        this.logger.exit();
    }

    /**
     * Return the architecture.
     *
     * @return  net.jmp.demo.redis.config.Architecture
     * @throws  java.io.IOException
     */
    private Architecture getArchitecture() throws IOException {
        this.logger.entry();

        Architecture result = Architecture.INTEL;

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
                }
            } else {
                if (this.logger.isWarnEnabled()) {
                    this.logger.warn("Process failed: {}", process.info().commandLine().orElse("/usr/sbin/sysctl -n machdep.cpu.brand_string"));
                }
            }
        } catch (final InterruptedException ie) {
            this.logger.catching(ie);
            Thread.currentThread().interrupt();     // Restore the interrupt status
        }

        this.logger.exit(result);

        return result;
    }

    /**
     * The main method.
     *
     * @param   args    java.lang.String[]
     */
    public static void main(final String[] args) {
        new Main().run();
    }
}
