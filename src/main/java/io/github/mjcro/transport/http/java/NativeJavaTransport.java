package io.github.mjcro.transport.http.java;

import io.github.mjcro.interfaces.experimental.integration.Option;
import io.github.mjcro.interfaces.experimental.integration.http.simple.HttpRequest;
import io.github.mjcro.interfaces.experimental.integration.http.simple.HttpResponse;
import io.github.mjcro.interfaces.experimental.integration.http.simple.HttpTransport;
import io.github.mjcro.transport.InputStreamReadingUtil;
import io.github.mjcro.transport.UnsupportedOptionException;
import io.github.mjcro.transport.http.BasicHeaders;
import io.github.mjcro.transport.http.BasicHttpRequest;
import io.github.mjcro.transport.http.BasicHttpResponse;
import io.github.mjcro.transport.http.java.options.HttpClientBuilderNativeJavaOption;
import io.github.mjcro.transport.http.java.options.HttpRequestBuilderNativeJavaOption;
import io.github.mjcro.transport.http.options.HttpResponseConsumerOption;
import io.github.mjcro.transport.http.options.HttpTelemetryOption;
import io.github.mjcro.transport.options.FailOnUnknownOption;
import io.github.mjcro.transport.options.Options;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Transport implementation over Java 11 native HTTP client.
 */
public class NativeJavaTransport implements HttpTransport {
    private final Option @Nullable [] defaultOptions;

    /**
     * Constructs new native Java transport.
     *
     * @param options Default transport options.
     */
    public NativeJavaTransport(Option @Nullable ... options) {
        this.defaultOptions = options;
    }

    /**
     * Merges given options with default ones provided on transport
     * instantiation and transforms result into options supported by
     * this transport.
     *
     * @param options Additional options.
     * @return Resulting options.
     */
    public Option @Nullable [] getMergedTransformedOptions(Option @Nullable ... options) {
        Option[] mergedOptions = Options.merge(this.defaultOptions, options);
        if (mergedOptions == null || mergedOptions.length == 0) {
            return mergedOptions;
        }

        boolean failOnUnknown = FailOnUnknownOption.isEnabled(mergedOptions, false);
        ArrayList<Option> transformed = new ArrayList<>(mergedOptions.length);
        for (Option option : mergedOptions) {
            if (option instanceof FailOnUnknownOption) {
                continue;
            }
            if (option instanceof HttpTelemetryOption<?> || option instanceof HttpResponseConsumerOption) {
                transformed.add(option);
            } else {
                Optional<HttpRequestBuilderNativeJavaOption> w1 = HttpRequestBuilderNativeJavaOption.transform(option);
                if (w1.isPresent()) {
                    transformed.add(w1.get());
                    continue;
                }

                Optional<HttpClientBuilderNativeJavaOption> w2 = HttpClientBuilderNativeJavaOption.transform(option);
                if (w2.isPresent()) {
                    transformed.add(w2.get());
                    continue;
                }

                if (failOnUnknown) {
                    throw new UnsupportedOptionException(this, option);
                }
            }
        }

        return transformed.toArray(new Option[0]);
    }

    @Override
    public @NonNull HttpResponse send(@NonNull HttpRequest request, Option @Nullable ... options) {
        Option[] mergedOptions = getMergedTransformedOptions(options);

        ArrayList<HttpTelemetryOption<?>> telemetryReceivers = new ArrayList<>(getTelemetryReceivers(mergedOptions));

        HttpResponse response = null;
        HttpRequest rebuiltRequest = null;
        HttpClient client = null;
        Instant start = Instant.now();
        try {
            java.net.http.HttpRequest.Builder b = createRequestBuilder(request);
            b = applyRequestOptions(b, mergedOptions);
            java.net.http.HttpRequest nativeRequest = b.build();

            rebuiltRequest = rebuildRequest(nativeRequest, request);
            HttpClient.Builder clientBuilder = HttpClient.newBuilder();
            clientBuilder = applyClientOptions(clientBuilder, mergedOptions);

            client = clientBuilder.build();
            java.net.http.HttpResponse<InputStream> nativeResponse = client.send(
                    nativeRequest,
                    java.net.http.HttpResponse.BodyHandlers.ofInputStream()
            );
            response = parseResponse(start, nativeResponse);
            nativeResponse.body().close();
            applyResponseConsumers(response, mergedOptions);
            for (HttpTelemetryOption<?> receiver : telemetryReceivers) {
                receiver.onSuccess(rebuiltRequest, response);
            }
            return response;
        } catch (IOException | InterruptedException e) {
            for (HttpTelemetryOption<?> receiver : telemetryReceivers) {
                receiver.onException(rebuiltRequest, response, Duration.between(start, Instant.now()), e);
            }
            throw new RuntimeException(e);
        } catch (RuntimeException e) {
            for (HttpTelemetryOption<?> receiver : telemetryReceivers) {
                receiver.onException(rebuiltRequest == null ? request : rebuiltRequest, response, Duration.between(start, Instant.now()), e);
            }
            throw e;
        } catch (Throwable e) {
            for (HttpTelemetryOption<?> receiver : telemetryReceivers) {
                receiver.onException(rebuiltRequest == null ? request : rebuiltRequest, response, Duration.between(start, Instant.now()), e);
            }
            throw new RuntimeException(e);
        } finally {
            if (client instanceof AutoCloseable) {
                try {
                    ((AutoCloseable) client).close();
                } catch (Exception ignore) {
                    // ignore
                }
            }
        }
    }

    /**
     * Applies compatible options to HTTP request builder.
     *
     * @param b       HTTP request builder to apply options on.
     * @param options Options to apply, nullable.
     * @return HTTP request builder.
     */
    private java.net.http.HttpRequest.@NonNull Builder applyRequestOptions(java.net.http.HttpRequest.@NonNull Builder b, Option @Nullable [] options) {
        if (options != null) {
            for (Option option : options) {
                if (option instanceof HttpRequestBuilderNativeJavaOption) {
                    b = ((HttpRequestBuilderNativeJavaOption) option).apply(b);
                }
            }
        }
        return b;
    }

    /**
     * Applies compatible options to HTTP client builder.
     *
     * @param b       HTTP client builder to apply options on.
     * @param options Options to apply, nullable.
     * @return HTTP client builder.
     */
    private java.net.http.HttpClient.@NonNull Builder applyClientOptions(java.net.http.HttpClient.@NonNull Builder b, Option @Nullable [] options) {
        if (options != null) {
            for (Option option : options) {
                if (option instanceof HttpClientBuilderNativeJavaOption) {
                    b = ((HttpClientBuilderNativeJavaOption) option).apply(b);
                }
            }
        }
        return b;
    }

    /**
     * Extracts HTTP telemetry options from given collection.
     *
     * @param options Options to fetch data from, nullable.
     * @return Found telemetry options.
     */
    private @NonNull List<HttpTelemetryOption<?>> getTelemetryReceivers(Option @Nullable [] options) {
        ArrayList<HttpTelemetryOption<?>> telemetryReceivers = new ArrayList<>();
        if (options != null) {
            for (Option option : options) {
                if (option instanceof HttpTelemetryOption<?>) {
                    telemetryReceivers.add((HttpTelemetryOption<?>) option);
                }
            }
        }
        return telemetryReceivers;
    }

    /**
     * Instantiates request builder for given request.
     *
     * @param request Source request.
     * @return HTTP request builder.
     */
    private java.net.http.HttpRequest.@NonNull Builder createRequestBuilder(@NonNull HttpRequest request) {
        java.net.http.HttpRequest.Builder b = java.net.http.HttpRequest.newBuilder();
        if (request.getMethod().equalsIgnoreCase("get")) {
            b = b.GET();
        } else {
            b = b.method(request.getMethod(), java.net.http.HttpRequest.BodyPublishers.ofByteArray(request.getBody()));
        }
        b = b.uri(URI.create(request.getURL()));
        return b;
    }

    /**
     * Rebuilds HTTP request packet using actual data that can be modified by options.
     *
     * @param nativeRequest Java 11 native request.
     * @param source        Original HTTP request.
     * @return New HTTP request build using data from Java 11 native request.
     */
    private @NonNull HttpRequest rebuildRequest(java.net.http.@NonNull HttpRequest nativeRequest, @NonNull HttpRequest source) {
        return new BasicHttpRequest(
                nativeRequest.method(),
                nativeRequest.uri().toString(),
                new BasicHeaders(nativeRequest.headers().map()),
                source.getBody() // TODO
        );
    }

    /**
     * Builds HTTP response instance from Java native response.
     *
     * @param start    HTTP session start instant, used to calculate elapsed time.
     * @param response Java 11 native response.
     * @return HTTP response instance.
     */
    private HttpResponse parseResponse(Instant start, java.net.http.HttpResponse<InputStream> response) throws IOException {
        byte[] body = readResponseBytes(response);

        return new BasicHttpResponse(
                response.statusCode(),
                Duration.between(start, Instant.now()),
                response.uri().toString(),
                new BasicHeaders(response.headers().map()),
                body
        );
    }

    /**
     * Reads response bytes and decodes them if needed.
     *
     * @param response Java 11 native response.
     * @return Body bytes.
     * @throws IOException On read or decryption error.
     */
    private byte[] readResponseBytes(java.net.http.HttpResponse<InputStream> response) throws IOException {
        return InputStreamReadingUtil.readAllUsingContentEncoding(
                response.body(),
                response.headers().firstValue("Content-Encoding").orElse(null)
        );
    }

    /**
     * Applies response consumers.
     *
     * @param response Response to apply consumers on.
     * @param options  Options to extract consumers from.
     */
    private void applyResponseConsumers(@NonNull HttpResponse response, Option @Nullable [] options) {
        if (options != null) {
            for (Option option : options) {
                if (option instanceof HttpResponseConsumerOption) {
                    ((HttpResponseConsumerOption) option).accept(response);
                }
            }
        }
    }
}
