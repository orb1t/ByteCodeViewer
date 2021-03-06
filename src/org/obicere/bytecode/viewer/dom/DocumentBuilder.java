package org.obicere.bytecode.viewer.dom;

import org.javacore.Identifiable;
import org.javacore.JCClass;
import org.obicere.bytecode.viewer.context.ClassInformation;
import org.obicere.bytecode.viewer.context.Domain;
import org.obicere.bytecode.viewer.context.DomainAccess;
import org.obicere.bytecode.viewer.dom.style.StyleConstants;
import org.obicere.bytecode.viewer.modeler.Modeler;
import org.obicere.bytecode.viewer.modeler.ModelerSet;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Obicere
 */
public class DocumentBuilder implements DomainAccess {

    private final Domain domain;

    private final Map<String, Object> properties = new HashMap<>();

    private volatile DocumentBuildRequest request;

    public DocumentBuilder(final Domain domain) {
        this.domain = domain;
    }

    @Override
    public Domain getDomain() {
        return domain;
    }

    public List<Block> build(final ClassInformation classInformation) {
        try {
            final int tabSize = domain.getSettingsController().getSettings().getInteger("code.tabSize", 4);
            this.request = new DocumentBuildRequest(tabSize, this, classInformation);

            // this utilizes the style constants, so we must lock on them
            // to avoid styles being changed half way through and looking
            // quite peculiar
            return StyleConstants.submit(request::get);
        } finally {
            properties.clear();
            request = null;
        }
    }

    public <T extends Identifiable> void model(final T element) {
        if (element == null) {
            throw new NullPointerException("element must be non-null");
        }
        final ModelerSet modelers = domain.getModelers();
        final Modeler<? super T> modeler = modelers.get(element.getIdentifier());
        if (modeler != null) {
            modeler.model(element, this);
        }
    }

    public void setProperty(final String key, final Object value) {
        properties.put(key, value);
    }

    public Object getProperty(final String key) {
        return properties.get(key);
    }

    public void openBlock() {
        request.openBlock(new Block());
    }

    public void openCollapsibleBlock() {
        request.openBlock(new Block(true));
    }

    public void closeBlock() {
        request.closeBlock();
    }

    public void closeCollapsibleBlock() {
        request.closeBlock();
    }

    public void add(final String plain) {
        request.submit(StyleConstants.PLAIN, plain);
    }

    public void addKeyword(final String keyword) {
        request.submit(StyleConstants.KEYWORD, keyword);
    }

    public void addComment(final String comment) {
        request.submit(StyleConstants.COMMENT, "// " + comment);
    }

    public void addType(final String type) {
        request.submit(StyleConstants.TYPE, type);
    }

    public void addString(String text) {
        request.submit(StyleConstants.STRING, stringify(text));
    }

    private String stringify(String text) {
        if (text == null) {
            return "null";
        }
        final StringBuilder builder = new StringBuilder();
        final char[] chars = text.toCharArray();
        for (final char c : chars) {
            builder.append(charToString(c));
        }

        text = "\"" + builder + "\"";
        return text;
    }

    private String charToString(final char c) {

        switch (c) {
            case '\0':
                return "\\0";
            case '\\':
                return "\\\\";
            case '\"':
                return "\\\"";
            case '\b':
                return "\\b";
            case '\f':
                return "\\f";
            case '\n':
                return "\\n";
            case '\r':
                return "\\r";
            case '\t':
                return "\\t";
            default:
                if (c < 32 || c >= 127) {
                    // non-ascii-displayable characters
                    final int value = (c & 0xFFFF);
                    final String hex = Integer.toHexString(value);

                    final StringBuilder builder = new StringBuilder(6);
                    // display unicode
                    builder.append('\\');
                    builder.append('u');

                    // 0-pad to 4 character length for \\u####
                    for (int i = hex.length(); i < 4; i++) {
                        builder.append('0');
                    }
                    builder.append(hex);
                    return builder.toString();
                } else {
                    // ascii-displayable characters
                    return String.valueOf(c);
                }
        }
    }

    public void addAnnotation(final String annotation) {
        request.submit(StyleConstants.ANNOTATION, "@" + annotation);
    }

    public void add(final boolean value) {
        request.submit(StyleConstants.KEYWORD, Boolean.toString(value));
    }

    public void add(final byte value) {
        request.submit(StyleConstants.NUMBER, Byte.toString(value));
    }

    public void add(final short value) {
        request.submit(StyleConstants.NUMBER, Short.toString(value));
    }

    public void add(final char value) {
        request.submit(StyleConstants.STRING, "'" + charToString(value) + "'");
    }

    public void add(final int value) {
        request.submit(StyleConstants.NUMBER, Integer.toString(value));
    }

    public void add(final float value) {
        request.submit(StyleConstants.NUMBER, Float.toString(value));
    }

    public void add(final long value) {
        request.submit(StyleConstants.NUMBER, Long.toString(value));
    }

    public void add(final double value) {
        request.submit(StyleConstants.NUMBER, Double.toString(value));
    }

    public void pad(final int size) {
        request.pad(size);
    }

    public void padTabbed(final int soFar, final int size) {
        request.padTabbed(soFar, size);
    }

    public void newLine() {
        request.newLine();
    }

    public void tab() {
        request.tab(1);
    }

    public void tab(final int count) {
        request.tab(count);
    }

    public void comma() {
        request.submit(StyleConstants.PLAIN, ", ");
    }

    public void indent() {
        request.indent();
    }

    public void unindent() {
        request.unindent();
    }
}
