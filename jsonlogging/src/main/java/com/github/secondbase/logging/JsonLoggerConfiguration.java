package com.github.secondbase.logging;

import com.github.secondbase.flags.Flag;

public class JsonLoggerConfiguration {

    @Flag(
            name = "datacenter",
            description = "The datacenter the service runs in (dc1|dc2)."
    )
    private String datacenter;

    @Flag(
            name = "request-logger-class-name",
            description = "Name of the request logger class."
    )
    private String requestLoggerClassName;

    public String getDatacenter() {
        return datacenter;
    }

    public String getRequestLoggerClassName() {
        return requestLoggerClassName;
    }
}
