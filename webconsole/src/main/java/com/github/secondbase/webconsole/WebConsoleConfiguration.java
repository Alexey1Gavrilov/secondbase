package com.github.secondbase.webconsole;

import com.github.secondbase.flags.Flag;

/**
 * Configuration parameters for the WebConsole.
 */
public class WebConsoleConfiguration {

    @Flag(
            name = "webconsole-port",
            description = "The port used by the webconsole (default 0 will select an available "
                    + "port between X and Y)")
    private int port = 5060;

    @Flag(
            name = "webconsole-shutdown-delay",
            description = "Time, in seconds, from requesting shutdown on the webconsole until "
            + "the server stops forcefully")
    private int stopTimeout = 0;

    public int getPort() {
        return port;
    }

    public int getStopTimeout() {
        return stopTimeout;
    }
}
