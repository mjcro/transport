package com.github.mjcro.transport;

import com.github.mjcro.transport.options.Context;
import com.github.mjcro.transport.options.Option;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Duration;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Transport adapter to cache remote response in local FS.
 */
public class LocalFsCachingAdapter implements Transport {
    private final Transport real;
    private final String path;

    /**
     * Constructor.
     *
     * @param transport Real transport to use.
     * @param path      Path to store cache.
     */
    public LocalFsCachingAdapter(Transport transport, String path) {
        this.real = Objects.requireNonNull(transport, "transport");
        this.path = Objects.requireNonNull(path, "path");
    }

    @Override
    public Response call(Request request, Option... options) {
        Context context = Context.create(null, request, options);

        if (!context.isCacheEnabled()) {
            // Cache bypass
            return real.call(request, options);
        }

        try {
            String filename = filename(request, context);
            if (filename != null) {
                // Checking file presence
                Response response = loadFile(filename);
                if (response != null) {
                    return response;
                }
            }

            Response response = real.call(request, options);
            if (filename != null && response != null) {
                saveFile(filename, response);
            }
            return response;
        } catch (IOException | NoSuchAlgorithmException e) {
            throw new RuntimeException(
                    "Unable to perform cache operation",
                    e
            );
        }
    }

    /**
     * Generates file name for given request and transport context.
     *
     * @param request Request object.
     * @param context Transport context.
     * @return File name.
     */
    String filename(Request request, Context context) throws NoSuchAlgorithmException {
        String address = context.formatURLString(request.getAddress());
        String prefix = address.trim().toLowerCase(Locale.ROOT);
        if (prefix.startsWith("https://")) {
            prefix = prefix.substring(8);
        } else if (prefix.startsWith("http://")) {
            prefix = prefix.substring(7);
        }
        prefix = prefix.replaceAll("[^\\w]", "_");
        prefix = prefix.replaceAll("_{2,}", "_");

        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        digest.update(address.getBytes(StandardCharsets.UTF_8));

        return prefix
                + "."
                + bytesToHex(digest.digest())
                + ".res";
    }

    /**
     * Loads response data from file on filesystem.
     *
     * @param filename File name.
     * @return Response object.
     * @throws IOException On read error.
     */
    Response loadFile(String filename) throws IOException {
        Path path = Paths.get(this.path, filename);
        if (!Files.exists(path)) {
            // File not exists, nothing to read
            return null;
        }
        if (!Files.isRegularFile(path)) {
            throw new IOException("Cache file " + path + " is not regular file");
        }
        if (!Files.isReadable(path)) {
            throw new IOException("Cache file " + path + " is not readable");
        }

        int code;
        String url;
        LinkedHashMap<String, String> headers = new LinkedHashMap<>();
        byte[] body;
        long elapsedNano;

        try (FileInputStream fis = new FileInputStream(path.toAbsolutePath().toString())) {
            try (DataInputStream dis = new DataInputStream(fis)) {
                // Version
                int version = dis.readInt();
                if (version != 1) {
                    throw new RuntimeException("Unsupported version " + version);
                }

                // Url
                byte[] urlBytes = new byte[dis.readInt()];
                dis.read(urlBytes);
                url = new String(urlBytes, StandardCharsets.UTF_8);

                // Status code
                code = dis.readInt();

                // Duration
                elapsedNano = dis.readLong();

                // Headers
                int headersCount = dis.readInt();
                for (int i = 0; i < headersCount; i++) {
                    byte[] keyBytes = new byte[dis.readInt()];
                    byte[] valBytes = new byte[dis.readInt()];

                    dis.read(keyBytes);
                    dis.read(valBytes);
                    headers.put(
                            new String(keyBytes, StandardCharsets.UTF_8),
                            new String(valBytes, StandardCharsets.UTF_8)
                    );
                }

                // Body
                body = new byte[dis.readInt()];
                dis.read(body);
            }
        }

        return Response.create(
                headers.entrySet().stream().collect(Collectors.toMap(
                        $ -> $.getKey(),
                        $ -> Collections.singletonList($.getValue())
                )),
                url,
                body,
                code,
                Duration.ofNanos(elapsedNano)
        );
    }

    /**
     * Saves response as file on filesystem.
     *
     * @param filename File name.
     * @param response Response to save.
     * @throws IOException On write error.
     */
    void saveFile(String filename, Response response) throws IOException {
        Path path = Paths.get(this.path, filename);
        if (Files.exists(path)) {
            if (!Files.isRegularFile(path)) {
                throw new IOException("Cache file " + path + " is not regular file");
            }
            if (!Files.isWritable(path)) {
                throw new IOException("Cache file " + path + " is not writable");
            }
        }

        try (FileOutputStream fos = new FileOutputStream(path.toAbsolutePath().toString())) {
            try (DataOutputStream dos = new DataOutputStream(fos)) {
                // Version
                dos.writeInt(1);

                // URL
                byte[] url = filename.getBytes(StandardCharsets.UTF_8);
                dos.writeInt(url.length);
                dos.write(url);

                // Status code
                dos.writeInt(response.getCode());

                // Duration nanoseconds
                dos.writeLong(response.getElapsed().toNanos());

                // Headers
                Map<String, String> headers = response.getFlatHeaders();
                dos.writeInt(headers.size());
                for (Map.Entry<String, String> entry : headers.entrySet()) {
                    byte[] key = entry.getKey().getBytes(StandardCharsets.UTF_8);
                    byte[] val = entry.getValue().getBytes(StandardCharsets.UTF_8);

                    dos.writeInt(key.length);
                    dos.writeInt(val.length);
                    dos.write(key);
                    dos.write(val);
                }

                // Writing body
                byte[] body = response.getBody();
                dos.writeInt(body.length);
                dos.write(body);

                // Done
                dos.flush();
            }
        }
    }

    private static final char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();

    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = HEX_ARRAY[v >>> 4];
            hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
        }
        return new String(hexChars);
    }
}
