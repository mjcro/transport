package io.github.mjcro.transport;

import org.apache.commons.compress.compressors.brotli.BrotliCompressorInputStream;
import org.apache.commons.compress.compressors.deflate.DeflateCompressorInputStream;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;
import java.util.Optional;
import java.util.zip.GZIPInputStream;

public class InputStreamReadingUtil {
    /**
     * Reads all data from given input stream then closes it.
     *
     * @param is Source input stream, nullable.
     * @return Data from input stream.
     * @throws IOException On any IO error.
     */
    public static byte @NonNull [] readAll(@Nullable InputStream is) throws IOException {
        if (is == null) {
            return new byte[0];
        }
        try (is) {
            return is.readAllBytes();
        }
    }

    /**
     * Reads all data from given input stream then closes it.
     * Performs GZIP decompression.
     *
     * @param is Source input stream, nullable.
     * @return Data from input stream.
     * @throws IOException On any IO error.
     */
    public static byte @NonNull [] readAllGzip(@Nullable InputStream is) throws IOException {
        return readAll(is == null ? null : new GZIPInputStream(is));
    }

    /**
     * Reads all data from given input stream then closes it.
     * Performs Deflate decompression.
     *
     * @param is Source input stream, nullable.
     * @return Data from input stream.
     * @throws IOException On any IO error.
     */
    public static byte @NonNull [] readAllDeflate(@Nullable InputStream is) throws IOException {
        return readAll(is == null ? null : new DeflateCompressorInputStream(is));
    }

    /**
     * Reads all data from given input stream then closes it.
     * Performs Brotli decompression.
     *
     * @param is Source input stream, nullable.
     * @return Data from input stream.
     * @throws IOException On any IO error.
     */
    public static byte @NonNull [] readAllBrotli(@Nullable InputStream is) throws IOException {
        return readAll(is == null ? null : new BrotliCompressorInputStream(is));
    }

    /**
     * Reads all data from given input stream then closes it.
     * Automatically detects required decompressor using content encoding data.
     *
     * @param is              Source input stream, nullable.
     * @param contentEncoding Content encoding, nullable.
     * @return Data from input stream.
     * @throws IOException On any IO error or if content encoding is not supported.
     */
    public static byte @NonNull [] readAllUsingContentEncoding(
            @Nullable InputStream is,
            @Nullable String contentEncoding
    ) throws IOException {
        if (is == null) {
            return readAll(null);
        }

        switch (Optional.ofNullable(contentEncoding).map($ -> $.toLowerCase(Locale.ENGLISH)).orElse("")) {
            case "gzip":
                return readAllGzip(is);
            case "deflate":
                return readAllDeflate(is);
            case "brotli":
            case "br":
                return readAllBrotli(is);
            case "":
                return readAll(is);
            default:
                throw new UnsupportedContentEncodingException(contentEncoding);
        }
    }

    private InputStreamReadingUtil() {
    }

    public static final class UnsupportedContentEncodingException extends IOException {
        private UnsupportedContentEncodingException(@Nullable String contentEncoding) {
            super("Unsupported content encoding " + contentEncoding);
        }
    }
}
