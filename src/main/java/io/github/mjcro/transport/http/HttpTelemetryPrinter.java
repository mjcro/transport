package io.github.mjcro.transport.http;

import io.github.mjcro.interfaces.experimental.integration.Headers;
import io.github.mjcro.interfaces.experimental.integration.Telemetry;
import io.github.mjcro.interfaces.experimental.integration.TelemetryConsumer;
import io.github.mjcro.interfaces.experimental.integration.http.simple.HttpRequest;
import io.github.mjcro.interfaces.experimental.integration.http.simple.HttpResponse;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.io.PrintStream;
import java.time.Duration;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.Temporal;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

/**
 * {@link TelemetryConsumer} implementation that works with HTTP telemetry.
 * <p>
 * Outputs telemetry data into given {@link PrintStream}.
 */
public class HttpTelemetryPrinter implements TelemetryConsumer<HttpRequest, HttpResponse, Object, Temporal> {
    private final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss").withZone(ZoneOffset.UTC);
    private final PrintStream printStream;
    private final boolean detailed;

    /**
     * Constructs new telemetry printer.
     *
     * @param printStream Print stream to output data into.
     * @param detailed    If true - headers and body will also be displayed.
     */
    public HttpTelemetryPrinter(@NonNull PrintStream printStream, boolean detailed) {
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
    public void accept(@NonNull Telemetry<? extends HttpRequest, ? extends HttpResponse, ?, ? extends Temporal> t) {
        printStream.printf(
                Locale.ROOT, "%s %s [%.3fs] %s %s -> %s\n",
                timeFormatter.format(t.getCreatedAt()),
                t.getResponse().map(HttpResponse::getStatusCode).map(String::valueOf).orElse("..."),
                t.getElapsed().orElse(Duration.ZERO).toMillis() / 1000.f,
                t.getRequest().getMethod(),
                t.getRequest().getURL(),
                t.getResponse().map(HttpResponse::getURL).orElse("null")
        );
        if (t.getException().isPresent()) {
            Throwable exception = t.getException().get();
            printStream.println("!!! " + exception.getClass().getName());
            printStream.println("!!! " + exception.getMessage());
        }
        if (detailed) {
            printRequestDetails(t.getRequest());
            printResponseDetails(t.getResponse().orElse(null));
        }
    }

    public void printRequestDetails(@Nullable HttpRequest request) {
        if (request != null) {
            printHeaders(">>> ", request.getHeaders());
            if (request.isBodyPresent()) {
                printStream.println(request.getBodyString());
            }
        }
    }

    public void printResponseDetails(@Nullable HttpResponse response) {
        if (response != null) {
            printStream.println(response.getStatusCode());
            printHeaders("<<< ", response.getHeaders());
            if (response.isBodyPresent()) {
                printStream.println(response.getBodyString());
            }
        }
    }

    private void printHeaders(@NonNull String prefix, @NonNull Headers headers) {
        for (Map.Entry<String, List<String>> values : headers) {
            for (String value : values.getValue()) {
                printStream.println(prefix + values.getKey() + " := " + value);
            }
        }
    }
}
