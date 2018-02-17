package com.github.secondbase.consul;

import com.github.secondbase.flags.Flag;

/**
 * Configuration parameters for the WebConsole.
 */
public class ConsulModuleConfiguration {
    /**
     * Location of Consul server.
     */
    @Flag (
            name = "consul-host",
            description="Consul host[:port]")
    private String host = "localhost:8500";

    @Flag(
            name = "service-port",
            description = "Port of service to register in consul"
    )
    private int servicePort = 0;

    @Flag(
            name = "consul-health-check-path",
            description = "Http path for consul to run health check towards"
    )
    private String healthCheckPath = "";

    @Flag(
            name = "consul-tags",
            description = "Comma separated list of tags to register with the service"
    )
    private String tags = "";

    @Flag(
            name = "consul-health-check-interval",
            description = "Interval, in seconds, between health checks performed by consul"
    )
    private long healthCheckIntervalSec = 29L;

    @Flag(
            name = "enable-consul",
            description = "Set to enable consul integration"
    )
    private boolean enabled = false;


    public String getHost() {
        return host;
    }

    public int getServicePort() {
        return servicePort;
    }

    public String getHealthCheckPath() {
        return healthCheckPath;
    }

    public String getTags() {
        return tags;
    }

    public long getHealthCheckIntervalSec() {
        return healthCheckIntervalSec;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setHost(final String host) {
        this.host = host;
    }
}
