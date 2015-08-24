package org.obicere.bcviewer.bytecode;

/**
 * @author Obicere
 */
public abstract class BytecodeElement {

    private int start;
    private int end;

    public final int getStart() {
        return start;
    }

    public final int getEnd() {
        return end;
    }

    public final void setBounds(final int start, final int end) {
        if (start < 0 || start >= end) {
            throw new IllegalArgumentException("invalid range: " + start + ", " + end);
        }
        this.start = start;
        this.end = end;
    }

    public final int getLength() {
        return end - start;
    }

    public String toString(final ConstantPool constantPool) {
        final StringBuilder builder = new StringBuilder();

        builder.append("default print: ");
        builder.append(getClass().getSimpleName());
        builder.append(": start=");
        builder.append(start);
        builder.append(", end=");
        builder.append(end);

        return builder.toString();
    }

}