package org.obicere.bytecode.viewer.io;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 */
public class FileSource implements Source {

    private static final int BUFFER_SIZE = 1024; // 1 KiB

    private final String file;

    private volatile InputStream open;

    public FileSource(final String file) {
        this.file = file;
    }

    public FileSource(final File file) {
        this.file = file.getAbsolutePath();
    }

    @Override
    public InputStream open() throws IOException {
        close();

        final InputStream open = new FileSourceFileInputStream(asFile());
        this.open = open;
        return open;
    }

    @Override
    public byte[] read() throws IOException {
        final FileInputStream stream = new FileInputStream(asFile());
        final ByteArrayOutputStream file = new ByteArrayOutputStream();

        final byte[] buffer = new byte[BUFFER_SIZE];
        while (true) {
            final int read = stream.read(buffer);
            if (read <= 0) {
                break;
            }
            file.write(buffer, 0, read);
        }

        stream.close();
        return file.toByteArray();
    }

    @Override
    public boolean exists() {
        return asFile().exists();
    }

    @Override
    public String getPath() {
        return file;
    }

    @Override
    public Source getSibling(final String fileName) {
        final File thisFile = asFile();
        final File parent = thisFile.getParentFile();
        return new FileSource(new File(parent, fileName).getAbsolutePath());
    }

    private File asFile() {
        return new File(file);
    }

    @Override
    public void close() throws IOException {
        if (open != null) {
            open.close();
            open = null;
        }
    }

    private class FileSourceFileInputStream extends FileInputStream {

        public FileSourceFileInputStream(final File file) throws FileNotFoundException {
            super(file);
        }

        @Override
        public void close() throws IOException {
            FileSource.this.close();
        }
    }
}
