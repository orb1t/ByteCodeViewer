package org.obicere.bcviewer.settings.target;

import java.awt.event.ActionListener;

/**
 */
public class ActionSetting extends Setting<Object> {

    private final ActionListener listener;


    public ActionSetting(final String name, final String descriptor, final ActionListener listener) {
        super(name, descriptor, null);
        this.listener = listener;
    }

    @Override
    public Object getValue() {
        return null;
    }

    public ActionListener getActionListener() {
        return listener;
    }
}