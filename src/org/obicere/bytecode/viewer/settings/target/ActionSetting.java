package org.obicere.bytecode.viewer.settings.target;

import java.awt.event.ActionListener;

/**
 */
public class ActionSetting extends Setting<Object> {

    public static final String IDENTIFIER = "Action";

    private final ActionListener listener;


    public ActionSetting(final String name, final String descriptor, final ActionListener listener) {
        super(name, descriptor, null);
        this.listener = listener;
    }

    @Override
    public String getIdentifier() {
        return IDENTIFIER;
    }

    @Override
    public Object getDefaultValue() {
        return null;
    }

    public ActionListener getActionListener() {
        return listener;
    }
}
