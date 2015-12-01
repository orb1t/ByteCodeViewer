package org.obicere.bcviewer.concurrent;

import org.obicere.bcviewer.context.Domain;
import org.obicere.bcviewer.context.DomainAccess;
import org.obicere.bcviewer.gui.EditorPanel;
import org.obicere.bcviewer.gui.EditorPanelManager;
import org.obicere.bcviewer.gui.FrameManager;
import org.obicere.bcviewer.gui.GUIManager;
import org.obicere.bcviewer.util.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.Enumeration;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 */
public class FileLoaderService implements DomainAccess {

    private static final int THREAD_POOL_COUNT = 2;

    private final ExecutorService fileLoaderExecutorService = Executors.newFixedThreadPool(THREAD_POOL_COUNT);

    private final Domain domain;

    public FileLoaderService(final Domain domain) {
        this.domain = domain;
    }

    public void postRequest(final Path... files) {
        if (files == null) {
            return;
        }
        final FileLoadRequest newRequest = new FileLoadRequest(files);
        fileLoaderExecutorService.submit(newRequest);
    }

    @Override
    public Domain getDomain() {
        return domain;
    }

    private class FileLoadRequest implements Callable<Void> {

        private final Path[] paths;

        public FileLoadRequest(final Path[] paths) {
            this.paths = paths;
        }

        @Override
        public Void call() throws Exception {
            final GUIManager guiManager = domain.getGUIManager();
            final FrameManager frameManager = guiManager.getFrameManager();
            final EditorPanelManager editorPanels = frameManager.getEditorManager();
            for (final Path path : paths) {
                final File file = path.toFile();
                if (!file.exists() || !file.canRead()) {
                    continue;
                }
                if (file.isDirectory()) {
                    final File[] files = file.listFiles();
                    if(files == null){
                        continue;
                    }
                    final Path[] paths = new Path[files.length];

                    for(int i =0; i < files.length; i++){
                        paths[i] = files[i].toPath();
                    }
                    postRequest(paths);
                    continue;
                }
                final String extension = FileUtils.getFileExtension(path.toString());
                if (extension == null) {
                    continue;
                }

                switch (extension) {
                    case ".class":
                        loadClass(editorPanels, path);
                        break;
                    case ".zip":
                    case ".jar":
                        loadArchive(editorPanels, path);
                }
            }

            return null;
        }
    }

    private void loadClass(final EditorPanelManager editorPanels, final Path path) {
        final ClassLoaderService service = domain.getClassLoaderService();

        final String name = path.toString();
        final String className = FileUtils.getFileName(name);

        if (editorPanels.hasEditorPanel(className)) {
            editorPanels.displayEditorPanel(className);
            return;
        }
        final EditorPanel panel = editorPanels.createEditorPanel();
        service.postRequest(new ClassCallback(panel), path);
    }

    private void loadArchive(final EditorPanelManager editorPanels, final Path path) {
        try {
            final FileSystem system = FileSystems.newFileSystem(path, null);

            final ZipFile zip = new ZipFile(path.toFile());
            final Enumeration<? extends ZipEntry> entries = zip.entries();
            while (entries.hasMoreElements()) {
                final ZipEntry entry = entries.nextElement();
                final String name = entry.getName();
                final String extension = FileUtils.getFileExtension(name);
                if (extension != null && extension.equals(".class")) {

                    final Path newPath = system.getPath(name);
                    loadClass(editorPanels, newPath);
                }
            }

            system.close();
        } catch (final IOException e) {
            domain.getLogger().log(Level.SEVERE, e.getMessage(), e);
        }
    }
}
