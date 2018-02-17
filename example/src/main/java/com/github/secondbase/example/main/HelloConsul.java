package com.github.secondbase.example.main;

import com.github.secondbase.consul.ConsulModule;

/**
 * Example of how to use the {@link ConsulModule} to register a service.
 */
public final class HelloConsul {
    private HelloConsul() {}

    public static void main(final String[] args) throws Exception {
        final String serviceName = "myservice";
        final int servicePort = 8080;
        final String environment = "testing";
        final String healthCheckPath = "/health";
        final long healthCheckIntervalSec = 29L;
        final String[] tags = {"tagone", "tagtwo"};

        // Manually register a service in consul
        final ConsulModule consulModule = new ConsulModule();
        consulModule.getConfig().setHost("localhost:8500");

        consulModule.registerServiceInConsul(
                serviceName,
                servicePort,
                environment,
                healthCheckPath,
                healthCheckIntervalSec,
                tags
        );

        Thread.sleep(1000);
        consulModule.shutdown();
    }
}
