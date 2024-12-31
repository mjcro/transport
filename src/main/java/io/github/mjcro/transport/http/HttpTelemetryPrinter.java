package io.github.mjcro.transport.http;

import io.github.mjcro.interfaces.experimental.integration.Headers;
import io.github.mjcro.interfaces.experimental.integration.Telemetry;
import io.github.mjcro.interfaces.experimental.integration.TelemetryConsumer;
import io.github.mjcro.interfaces.experimental.integration.http.simple.HttpRequest;
import io.github.mjcro.interfaces.experimental.integration.http.simple.HttpResponse;

import java.io.PrintStream;
import java.time.Duration;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * {@link TelemetryConsumer} implementation that works with HTTP telemetry.
 * <p>
 * Outputs telemetry data into given {@link PrintStream}.
 */
public class HttpTelemetryPrinter implements TelemetryConsumer<HttpRequest, HttpResponse, Object, TemporalAccessor> {
    private final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss").withZone(ZoneOffset.UTC);
    private final PrintStream printStream;
    private final boolean detailed;

    /**
     * Constructs new telemetry printer.
     *
     * @param printStream Print stream to output data into.
     * @param detailed    If true - headers and body will also be displayed.
     */
    public HttpTelemetryPrinter(PrintStream printStream, boolean detailed) {
        this.printStream = Objects.requireNonNull(printStream, "printStream");
        this.detailed = detailed;
    }

    /**
     * Constructs new telemetry printer that outputs information into standard output.
     *
     * @param detailed If true - headers and body will also be displayed.
     */
    public HttpTelemetryPrinter(boolean detailed) {
        this(System.out, detailed);
    }

    @Override
    public void accept(Telemetry<? extends HttpRequest, ? extends HttpResponse, ?, ? extends TemporalAccessor> t) {
        printStream.printf(
                Locale.ROOT, "%s [%.3fs] %s %s -> %s\n",
                timeFormatter.format(t.getCreatedAt()),
                t.getElapsed().orElse(Duration.ZERO).toMillis() / 1000.f,
                t.getRequest().getMethod(),
                t.getRequest().getURL(),
                t.getResponse().map(HttpResponse::getURL).orElse("null")
        );
        if (t.hasException()) {
            Throwable exception = t.getException().get();
            printStream.println("!!! " + exception.getClass().getName());
            printStream.println("!!! " + exception.getMessage());
        }
        if (detailed) {
            printHeaders(">>> ", t.getRequest().getHeaders());
            if (t.getRequest().isBodyPresent()) {
                printStream.println(t.getRequest().getBodyString());
            }

            t.ifHasResponse((Consumer<HttpResponse>) $ -> {
                printHeaders("<<< ", $.getHeaders());
                if ($.isBodyPresent()) {
                    printStream.println($.getBodyString());
                }
            });
        }
    }

    private void printHeaders(String prefix, Headers headers) {
        for (Map.Entry<String, List<String>> values : headers) {
            for (String value : values.getValue()) {
                printStream.println(prefix + values.getKey() + " := " + value);
            }
        }
    }
}