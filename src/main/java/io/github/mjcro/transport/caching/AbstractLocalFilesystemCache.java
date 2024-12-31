package io.github.mjcro.transport.caching;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Path;
import java.util.Objects;
import java.util.Optional;

/**
 * Abstract local filesystem-based {@link Cache} implementation.
 * Provides resources management and files/paths allocation.
 *
 * @param <Req> Request type.
 * @param <Res> Response type.
 */
public abstract class AbstractLocalFilesystemCache<Req, Res> implements Cache<Req, Res> {
    private final Path basePath;

    /**
     * Constructs new local filesystem cache.
     *
     * @param basePath Base folder to store/read files.
     */
    public AbstractLocalFilesystemCache(Path basePath) {
        this.basePath = Objects.requireNonNull(basePath, "basePath");
    }

    @Override
    public Optional<Res> get(Req request) {
        File file = getFile(request);
        if (file.exists() && file.canRead()) {
            return Optional.of(readFile(file));
        }
        return Optional.empty();
    }

    @Override
    public void put(Req request, Res response) {
        File file = getFile(request);
        if (file.isDirectory()) {
            throw new LocalFilesystemHttpCacheException(file + " is a directory");
        }
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                throw new LocalFilesystemHttpCacheException(e);
            }
        } else if (!file.canWrite()) {
            throw new LocalFilesystemHttpCacheException(file + " is not writable");
        }

        try (FileOutputStream outputStream = new FileOutputStream(file)) {
            writeResponse(outputStream, response);
            outputStream.flush();
        } catch (IOException e) {
            throw new LocalFilesystemHttpCacheException(e);
        }
    }

    /**
     * @return Base path for cache files.
     */
    public Path getBasePath() {
        return basePath;
    }

    /**
     * Reads response from given file.
     * Can be used by external tools to read cached data.
     *
     * @param file File to read data from.
     * @return Response data.
     */
    public Res readFile(File file) {
        try (FileInputStream is = new FileInputStream(file)) {
            return readResponse(is);
        } catch (IOException e) {
            throw new LocalFilesystemHttpCacheException(e);
        }
    }

    /**
     * Resolves file for given request.
     *
     * @param request Request to find file for.
     * @return File for request.
     */
    public File getFile(Req request) {
        String filename = resolveFilename(request);
        if (!getBasePath().toFile().exists()) {
            getBasePath().toFile().mkdirs();
        }
        if (!getBasePath().toFile().isDirectory()) {
            throw new LocalFilesystemHttpCacheException(getBasePath() + " is not a directory");
        }
        return getBasePath().resolve(filename).toFile();
    }

    /**
     * Resolves file name for given request.
     *
     * @param request Request.
     * @return File name.
     */
    public abstract String resolveFilename(Req request);

    /**
     * Reads response from given input stream.
     *
     * @param is Data input stream.
     * @return Response.
     * @throws IOException On IO or data processing error.
     */
    public abstract Res readResponse(InputStream is) throws IOException;

    /**
     * Writes response to given output stream.
     *
     * @param os       Output stream to use.
     * @param response response to write.
     * @throws IOException On IO or data processing error.
     */
    public abstract void writeResponse(OutputStream os, Res response) throws IOException;

    /**
     * Runtime exception thrown on error occurred during local
     * filesystem cache processing.
     */
    public static class LocalFilesystemHttpCacheException extends RuntimeException {
        LocalFilesystemHttpCacheException(IOException cause) {
            super(cause);
        }

        LocalFilesystemHttpCacheException(String message) {
            super(message);
        }
    }
}
