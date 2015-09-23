package org.obicere.bcviewer.dom;

import org.obicere.bcviewer.bytecode.ClassFile;
import org.obicere.bcviewer.bytecode.ConstantPool;
import org.obicere.bcviewer.context.Domain;
import org.obicere.bcviewer.context.DomainAccess;
import org.obicere.bcviewer.dom.ui.DocumentRenderer;

import java.util.HashMap;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Obicere
 */
public class DocumentBuilder implements DomainAccess {

    private final Domain domain;

    private final ReentrantLock lock = new ReentrantLock();

    private final ColorResourcePool colorPool;

    private final FontResourcePool fontPool;

    private final AttributesResourcePool attributesPool;

    private final PaddingCache padding = PaddingCache.getPaddingCache();

    private volatile int tabSize = 4;

    private volatile ClassFile classFile;

    private volatile Document document;

    private final HashMap<String, Object> properties = new HashMap<>();

    public DocumentBuilder(final Domain domain) {
        this.domain = domain;
        this.colorPool = new ColorResourcePool(this);
        this.fontPool = new FontResourcePool(this);
        this.attributesPool = new AttributesResourcePool();

        // TODO: move to the settings value
        fontPool.setBaseFont("Courier new", 14);

        attributesPool.updateFonts(fontPool);
        attributesPool.updateColors(colorPool);
    }

    public Document build(final DocumentRenderer renderer, final ClassFile classFile, final Modeler classFileModeler) {
        if (renderer == null) {
            throw new NullPointerException("cannot render document to null renderer");
        }

        if (classFileModeler == null) {
            throw new NullPointerException("cannot model with a null modeler.");
        }
        try {
            lock.lock();

            this.classFile = classFile;
            this.document = new Document(renderer);

            classFileModeler.model(this, document.getRoot());

            return document;
        } finally {

            // clear the current-working objects
            this.classFile = null;
            this.document = null;

            properties.clear();
            lock.unlock();
        }
    }

    public void setWorkingClass(final ClassFile file){
        this.classFile = file;
    }

    public boolean isBusy() {
        return lock.isLocked();
    }

    void notifyFontChange() {
        attributesPool.updateFonts(fontPool);
    }

    void notifyColorChange() {
        attributesPool.updateColors(colorPool);
    }

    public ConstantPool getConstantPool() {
        return classFile.getConstantPool();
    }

    public ClassFile getClassFile() {
        return classFile;
    }

    public Document getDocument() {
        return document;
    }

    public AttributesResourcePool getAttributesPool() {
        return attributesPool;
    }

    public ColorResourcePool getColorPool() {
        return colorPool;
    }

    public FontResourcePool getFontPool() {
        return fontPool;
    }

    public int getTabSize() {
        return tabSize;
    }

    public void setTabSize(final int tabSize) {
        if (tabSize <= 0) {
            throw new IllegalArgumentException("illegal tab size. Must be positive.");
        }
        this.tabSize = tabSize;
    }

    public String getPadding(final int minimum) {
        return getPadding(0, minimum);
    }

    public String getPadding(final int sizeSoFar, final int minimum) {
        return padding.getPadding(getPaddingSize(sizeSoFar, minimum));
    }

    public int getPaddingSize(final int minimum) {
        return getPaddingSize(0, minimum);
    }

    public int getPaddingSize(final int sizeSoFar, final int minimum) {
        if (sizeSoFar < 0) {
            throw new IllegalArgumentException("current size cannot be negative.");
        }
        if (sizeSoFar > minimum) {
            return 0;
        }
        return minimum - sizeSoFar;
    }

    public String getTabbedPadding(final int minimum) {
        return getTabbedPadding(0, minimum);
    }

    public String getTabbedPadding(final int sizeSoFar, final int minimum) {
        return padding.getPadding(getTabbedPaddingSize(sizeSoFar, minimum));
    }

    public int getTabbedPaddingSize(final int minimum) {
        return getTabbedPaddingSize(0, minimum);
    }

    public int getTabbedPaddingSize(final int sizeSoFar, int minimum) {
        if (sizeSoFar < 0) {
            throw new IllegalArgumentException("current size cannot be negative.");
        }
        if (minimum < sizeSoFar) {
            return tabSize * (sizeSoFar / tabSize) + tabSize - sizeSoFar;
        }
        final int remainder = minimum % tabSize;
        if (remainder == 0) {
            return minimum - sizeSoFar;
        } else {
            return (minimum - sizeSoFar) + (tabSize - remainder);
        }
    }

    public void addMarker(final Marker marker) {
        document.addMarker(marker);
    }

    public void setProperty(final String key, final Object value) {
        properties.put(key, value);
    }

    public Object getProperty(final String key) {
        return properties.get(key);
    }

    @Override
    public Domain getDomain() {
        return domain;
    }
}
