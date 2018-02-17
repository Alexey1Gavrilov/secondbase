package com.github.secondbase.consul.registration;

import com.github.secondbase.consul.ConsulModule;
import com.github.secondbase.consul.ConsulModuleConfiguration;
import com.github.secondbase.core.SecondBase;
import com.github.secondbase.core.SecondBaseException;
import com.github.secondbase.core.config.SecondBaseModule;
import com.github.secondbase.webconsole.HttpWebConsole;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Consul service registration for Prometheus WebConsole widget.
 */
public final class ConsulRegistrationMetricsWebConsole implements SecondBaseModule {

    private static final Logger LOG = LoggerFactory.getLogger(
            ConsulRegistrationMetricsWebConsole.class.getName());

    private final HttpWebConsole webConsole;
    private final ConsulModule consulModule;
    private SecondBase secondBase;

    @Override
    public void load(final SecondBase secondBase) {
        // nothing to configure
        this.secondBase = secondBase;
    }

    @Override
    public void init() throws SecondBaseException {
        final ConsulModuleConfiguration consulModuleConfig = consulModule.getConfig();

        if (!consulModuleConfig.isEnabled()) {
            LOG.info("Consul disabled. Not registering.");
            return;
        }
        if (secondBase.getServiceName().isEmpty()) {
            LOG.info("No service name defined. Nothing to register yet.");
            return;
        }
        if (consulModuleConfig.getServicePort() == 0) {
            LOG.error("Service port needs to be defined in order to register a service in consul.");
            return;
        }
        if (secondBase.getEnvironment().isEmpty()) {
            LOG.error("Environment needs to be defined in order to register a service in consul.");
            return;
        }
        if (consulModuleConfig.getHealthCheckPath().isEmpty()) {
            LOG.error("Health check path needs to be defined in order to register a service in "
                    + "consul.");
            return;
        }
        consulModule.registerServiceInConsul(
                secondBase.getServiceName(),
                webConsole.getPort(),
                secondBase.getEnvironment(),
                "/healthz",
                29L,
                "metrics"
        );
    }

    @Override
    public void shutdown() {
    }

    public ConsulRegistrationMetricsWebConsole(
            final HttpWebConsole webConsole,
            final ConsulModule consulModule) {
        this.webConsole = webConsole;
        this.consulModule = consulModule;
    }
}
