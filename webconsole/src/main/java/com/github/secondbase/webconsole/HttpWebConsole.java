package com.github.secondbase.webconsole;

import com.github.secondbase.core.SecondBase;
import com.github.secondbase.core.SecondBaseException;
import com.github.secondbase.core.config.SecondBaseModule;
import com.github.secondbase.webconsole.widget.Widget;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A webserver for hosting secondbase servlets using Sun's {@link HttpServer}.
 */
public final class HttpWebConsole implements SecondBaseModule {

    private static final Logger LOG = LoggerFactory.getLogger(HttpWebConsole.class);
    private final HttpServer server;
    private final Widget[] widgets;
    private final WebConsoleConfiguration webConsoleConfiguration;

    /**
     * Basic /healthz endpoint, returning 200 OK.
     */
    private final class HealthzHandler implements HttpHandler {
        private static final String healthyMsg = "Healthy";

        @Override
        public void handle(final HttpExchange t) throws IOException {
            final byte[] response = healthyMsg.getBytes();
            t.sendResponseHeaders(200, response.length);
            final OutputStream os = t.getResponseBody();
            os.write(response);
            os.close();
        }
    }

    /**
     * Set up the webconsole without widgets using port from {@link WebConsoleConfiguration}.
     *
     * @throws IOException if server can't start on a given port
     */
    public HttpWebConsole() throws IOException {
        this(new Widget[]{});
    }

    /**
     * Set up the webconsole with the given widgets.
     *
     * @param widgets to use
     * @throws IOException if the server can't start on a given port
     */
    public HttpWebConsole(final Widget[] widgets) throws IOException {
        server = HttpServer.create();
        server.createContext("/healthz", new HealthzHandler());
        this.widgets = widgets;
        webConsoleConfiguration = new WebConsoleConfiguration();
    }

    /**
     * Load WebConsole Flags and set the secondbase webconsole to "this".
     *
     * @param secondBase module coordinator
     */
    @Override
    public void load(final SecondBase secondBase) {
        secondBase.getFlags().loadOpts(webConsoleConfiguration);
    }

    @Override
    public void init() throws SecondBaseException {
        try {
            start();
        } catch (final IOException e) {
            throw new SecondBaseException("Could not start webconsole.", e);
        }
        Runtime.getRuntime().addShutdownHook(new Thread(this::shutdown));
    }

    public void start() throws IOException {
        if (webConsoleConfiguration.getPort() == 0) {
            return;
        }
        final int useSystemDefaultBacklog = 0;
        server.bind(
                new InetSocketAddress(webConsoleConfiguration.getPort()),
                useSystemDefaultBacklog);
        LOG.info("Starting webconsole on port " + webConsoleConfiguration.getPort());
        for (final Widget widget : widgets) {
            LOG.info("Adding webconsole widget " + widget.getPath());
            server.createContext(widget.getPath(), widget.getServlet());
        }
        server.start();
    }

    public void shutdown() {
        if (webConsoleConfiguration.getPort() == 0) {
            return;
        }
        LOG.info("Shutting down webconsole.");
        server.stop(webConsoleConfiguration.getStopTimeout());
    }

    public int getPort() {
        return webConsoleConfiguration.getPort();
    }

    /**
     * Get the server implementation.
     *
     * @return HttpServer
     */
    public HttpServer getServer() {
        return server;
    }
}
