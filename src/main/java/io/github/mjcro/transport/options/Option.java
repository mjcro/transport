package io.github.mjcro.transport.options;

import io.github.mjcro.transport.AsyncTransport;
import io.github.mjcro.transport.Telemetry;
import io.github.mjcro.transport.Transport;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.function.Consumer;

/**
 * Defines transport behaviour.
 */
public interface Option extends Consumer<Context> {
    /**
     * Merges all option data into single array.
     *
     * @param chunks Chunks to merge.
     * @return Resulting options array.
     */
    static Option[] merge(Option[]... chunks) {
        if (chunks == null || chunks.length == 0) {
            return new Option[0];
        }

        // Calculating len
        int len = 0;
        for (Option[] chunk : chunks) {
            if (chunk != null) {
                len += chunk.length;
            }
        }

        // Only one chunk present
        if (chunks.length == 1 || chunks[0].length == len) {
            return chunks[0] == null ? new Option[0] : chunks[0];
        }

        Option[] result = new Option[len];
        int index = 0;
        for (Option[] chunk : chunks) {
            if (chunk != null && chunk.length > 0) {
                System.arraycopy(chunk, 0, result, index, chunk.length);
                index += chunk.length;
            }
        }
        return result;
    }

    /**
     * Appends one or more options to exising options array.
     *
     * @param options Existing options array.
     * @param append  Options to append.
     * @return Resulting options array.
     */
    static Option[] append(Option[] options, Option... append) {
        if (append == null || append.length == 0) {
            // Nothing to append
            return options == null
                    ? new Option[0]
                    : options;
        }
        if (options == null || options.length == 0) {
            // No previously set options
            return append;
        }

        Option[] result = new Option[options.length + append.length];
        System.arraycopy(options, 0, result, 0, options.length);
        System.arraycopy(append, 0, result, options.length, append.length);
        return result;
    }

    /**
     * @param value HTTP method.
     * @return Option setting HTTP method.
     */
    static Option httpMethod(String value) {
        return new SetMethod(value);
    }

    /**
     * @return Option setting HTTP GET method.
     */
    static Option httpGet() {
        return httpMethod("GET");
    }

    /**
     * @return Option setting HTTP POST method.
     */
    static Option httpPost() {
        return httpMethod("POST");
    }

    /**
     * @param value Timeout value.
     * @return Option setting HTTP POST method.
     */
    static Option timeout(Duration value) {
        return new SetTimeout(value);
    }

    /**
     * @param name   Header name.
     * @param values Header values.
     * @return Option setting header.
     */
    static Option header(String name, String... values) {
        if (values != null && values.length > 0) {
            return header(name, Arrays.asList(values));
        } else {
            return new VoidOption();
        }
    }

    /**
     * @param name   Header name.
     * @param values Header values.
     * @return Option setting header.
     */
    static Option header(String name, List<String> values) {
        return new SetHeader(name, values);
    }

    /**
     * @param value User agent value.
     * @return Option setting header.
     */
    static Option userAgent(String value) {
        return header("User-Agent", value);
    }

    /**
     * @return Random user agent.
     */
    static Option randomUserAgent() {
        try {
            String ua = new String(
                    Files.readAllBytes(Paths.get(Option.class.getResource("/agents.txt").toURI())),
                    StandardCharsets.UTF_8
            );
            String[] agents = Arrays.stream(ua.split("\n"))
                    .filter($ -> !$.isEmpty())
                    .toArray(String[]::new);
            return userAgent(agents[new Random().nextInt(agents.length)]);
        } catch (URISyntaxException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @param value True to enable HTTP/2
     * @return Option setting HTTP/2 mode.
     */
    static Option http2(boolean value) {
        return new SetHTTP2(value);
    }

    /**
     * @return Option enabling HTTP/2 mode
     */
    static Option http2() {
        return http2(true);
    }

    /**
     * @param value True to enable redirects.
     * @return Option setting redirects mode.
     */
    static Option allowRedirects(boolean value) {
        return new SetAllowRedirects(value);
    }

    /**
     * @return Option enabling redirects.
     */
    static Option allowRedirects() {
        return allowRedirects(true);
    }

    /**
     * @param value True to enable caching.
     * @return Option setting caching mode.
     */
    static Option useCaching(boolean value) {
        return new SetCaching(value);
    }

    /**
     * @return Option enabling caching.
     */
    static Option useCaching() {
        return useCaching(true);
    }

    /**
     * @param consumer Telemetry consumer to use, nullable.
     * @return Option setting telemetry consumer.
     */
    static Option telemetryConsumer(Consumer<Telemetry> consumer) {
        return new SetTelemetryConsumer(consumer);
    }

    /**
     * @param key   Metadata key.
     * @param value Metadata value.
     * @return Option setting metadata.
     */
    static Option setMetadata(String key, Object value) {
        return new SetMetadata(key, value);
    }

    /**
     * Creates currying adapter that will inject provided options
     * to all requests made using this transport.
     *
     * @param transport Transport to wrap.
     * @param options   Options to append.
     * @return Wrapped transport.
     */
    static Transport curry(Transport transport, Option... options) {
        if (options == null || options.length == 0) {
            return transport;
        }
        if (transport instanceof OptionCurryTransport) {
            OptionCurryTransport oct = (OptionCurryTransport) transport;
            return new OptionCurryTransport(oct.real, Option.merge(oct.options, options));
        }

        return new OptionCurryTransport(transport, options);
    }

    /**
     * Creates currying adapter that will inject provided options
     * to all requests made using this transport.
     *
     * @param transport Transport to wrap.
     * @param options   Options to append.
     * @return Wrapped transport.
     */
    static AsyncTransport curry(AsyncTransport transport, Option... options) {
        if (options == null || options.length == 0) {
            return transport;
        }
        if (transport instanceof OptionCurryAsyncTransport) {
            OptionCurryAsyncTransport oct = (OptionCurryAsyncTransport) transport;
            return new OptionCurryAsyncTransport(oct.real, Option.merge(oct.options, options));
        }

        return new OptionCurryAsyncTransport(transport, options);
    }
}
