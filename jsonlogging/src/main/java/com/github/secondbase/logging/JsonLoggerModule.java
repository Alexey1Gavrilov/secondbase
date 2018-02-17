package com.github.secondbase.logging;

import com.github.secondbase.core.SecondBase;
import com.github.secondbase.core.config.SecondBaseModule;
import com.google.common.base.Strings;
import java.util.LinkedList;
import java.util.List;
import org.slf4j.bridge.SLF4JBridgeHandler;

public class JsonLoggerModule implements SecondBaseModule {
    private SecondBase secondBase;
    private final JsonLoggerConfiguration jsonLoggerConfig = new JsonLoggerConfiguration();

    @Override
    public void load(final SecondBase secondBase) {
        this.secondBase = secondBase;
        secondBase.getFlags().loadOpts(jsonLoggerConfig);
    }

    @Override
    public void init() {
        SLF4JBridgeHandler.removeHandlersForRootLogger();
        SLF4JBridgeHandler.install();
        final List<String> keyList = new LinkedList<>();
        final List<String> valueList = new LinkedList<>();
        if (!Strings.isNullOrEmpty(secondBase.getServiceName())) {
            keyList.add("service");
            valueList.add(secondBase.getServiceName());
        }
        if (!Strings.isNullOrEmpty(secondBase.getEnvironment())) {
            keyList.add("environment");
            valueList.add(secondBase.getEnvironment());
        }
        if (!Strings.isNullOrEmpty(jsonLoggerConfig.getDatacenter())) {
            keyList.add("datacenter");
            valueList.add(jsonLoggerConfig.getDatacenter());
        }
        SecondBaseLogger.setupLoggingStdoutOnly(
                keyList.toArray(new String[] {}),
                valueList.toArray(new String[] {}),
                jsonLoggerConfig.getDatacenter(),
                true);
    }
}
