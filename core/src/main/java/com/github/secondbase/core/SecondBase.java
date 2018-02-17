package com.github.secondbase.core;

import com.github.secondbase.core.config.SecondBaseModule;
import com.github.secondbase.flags.Flag;
import com.github.secondbase.flags.Flags;
import java.util.Arrays;

/**
 * Coordination class for SecondBase modules. Handles Flags parsing and cooperation between modules.
 */
@SuppressWarnings("FieldCanBeLocal")
public class SecondBase {

    @Flag(
            name = "service-name",
            description = "Name of service"
    )
    private String serviceName = "";

    @Flag(
            name = "service-environment",
            description = "The environment the service runs in"
    )
    private String environment = "testing";
    private final SecondBaseModule[] modules;

    private Flags flags;

    /**
     * Initiates SecondBase with a set of {@link SecondBaseModule}.
     * @param args command line arguments to be parsed
     * @throws SecondBaseException if a module fails to start
     */
    public SecondBase(final String[] args, final SecondBaseModule[] modules)
            throws SecondBaseException{
        this(args, modules, new Flags());
    }

    /**
     * Initiates SecondBase with a set of {@link SecondBaseModule} and custom {@link Flags}.
     * @param args command line arguments to be parsed
     * @param flags preloaded {@link Flags}
     * @throws SecondBaseException if a module fails to start
     */
    public SecondBase(
            final String[] args,
            final SecondBaseModule[] modules,
            final Flags flags)
            throws SecondBaseException {
        this.flags = flags;
        this.modules = modules;
        flags.loadOpts(this);
        for(final SecondBaseModule module : modules) {
            module.load(this);
        }
        flags.parse(args);

        if (flags.helpFlagged()) {
            flags.printHelp(System.out);
            System.exit(0);
        }
        if (flags.versionFlagged()) {
            flags.printVersion(System.out);
            System.exit(0);
        }

        for (final SecondBaseModule module : modules) {
            module.init();
        }
    }

    /**
     * Get Flags instance used by Base.
     * @return {@link Flags} object
     */
    public Flags getFlags() {
        return flags;
    }


    /**
     * Release resources allocated by this instance.
     */
    public void shutdown() {
        Arrays.stream(modules).forEach(SecondBaseModule::shutdown);
    }

    public String getServiceName() {
        return serviceName;
    }

    public String getEnvironment() {
        return environment;
    }
}
