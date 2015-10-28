package org.obicere.bcviewer.gui.swing.editor;

import org.obicere.bcviewer.bytecode.ClassFile;
import org.obicere.bcviewer.concurrent.ClassCallback;
import org.obicere.bcviewer.concurrent.ClassLoaderService;
import org.obicere.bcviewer.concurrent.ClassModelerService;
import org.obicere.bcviewer.context.ClassInformation;
import org.obicere.bcviewer.context.Domain;
import org.obicere.bcviewer.context.DomainAccess;
import org.obicere.bcviewer.dom.Block;
import org.obicere.bcviewer.dom.DocumentBuilder;
import org.obicere.bcviewer.dom.awt.QuickWidthFont;
import org.obicere.bcviewer.dom.gui.swing.JDocumentArea;
import org.obicere.bcviewer.gui.EditorPanel;
import org.obicere.bcviewer.gui.FrameManager;
import org.obicere.utility.io.FileSource;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.List;
import java.util.logging.Level;

/**
 * @author Obicere
 */
public class SwingEditorPanel extends JPanel implements EditorPanel, DomainAccess {

    private final Domain domain;

    private final JDocumentArea documentArea;

    private final DocumentBuilder builder;

    private volatile ClassInformation classInformation;

    private volatile ClassFile loadedClassFile;

    public SwingEditorPanel(final Domain domain) {
        super(new BorderLayout(10, 10));
        this.domain = domain;
        this.builder = new DocumentBuilder(domain);

        this.documentArea = new JDocumentArea(domain);

        final JScrollPane editorScroll = new JScrollPane(documentArea);

        add(editorScroll);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(830, 620);
    }

    @Override
    public ClassFile getClassFile() {
        return loadedClassFile;
    }

    @Override
    public void setClassInformation(final ClassInformation classInformation) {
        this.classInformation = classInformation;
        setClassFile(classInformation.getRootClass());
    }

    @Override
    public ClassInformation getClassInformation() {
        return classInformation;
    }

    private void setClassFile(final ClassFile classFile) {
        if (classFile == null) {
            return;
        }
        this.loadedClassFile = classFile;
    }

    @Override
    public void setBlocks(final List<Block> blocks) {
        if (blocks == null) {
            final String className = classInformation.getRootClass().getName();
            domain.getLogger().log(Level.WARNING, "Failed to load class: " + className);
            return;
        }
        documentArea.getBlocks().clear();
        blocks.forEach(documentArea::addBlock);
        repaint();
    }

    @Override
    public DocumentBuilder getBuilder() {
        return builder;
    }

    @Override
    public void setFont(final QuickWidthFont font) {
        documentArea.setFont(font);
    }

    @Override
    public void reload() {
        final ClassModelerService service = domain.getClassModelerService();

        final ClassCallback callback = new ClassCallback(this);

        service.postRequest(callback, builder, classInformation);
    }

    @Override
    public void hardReload() {
        classInformation.clear();

        final ClassLoaderService service = domain.getClassLoaderService();

        final ClassCallback callback = new ClassCallback(this);
        final FileSource fileSource = classInformation.getFileSource();

        service.postRequest(callback, fileSource);
    }

    @Override
    public void close() {
        final FrameManager manager = domain.getGUIManager().getFrameManager();
        manager.getEditorManager().closeEditorPanel(getName());
    }

    @Override
    public Domain getDomain() {
        return domain;
    }
}
