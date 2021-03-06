package org.obicere.bytecode.viewer.settings.target;

/**
 */
public class FloatSetting extends NumberSetting<Float> {

    public static final String IDENTIFIER = "Float";

    public FloatSetting(final String name, final String descriptor, final Float value) {
        this(name, descriptor, value, Float.MIN_VALUE, Float.MAX_VALUE);
    }

    public FloatSetting(final String name, final String descriptor, final Float value, final float minimum, final float maximum) {
        super(name, descriptor, value, minimum, maximum);
    }

    @Override
    public String getIdentifier() {
        return IDENTIFIER;
    }
}
