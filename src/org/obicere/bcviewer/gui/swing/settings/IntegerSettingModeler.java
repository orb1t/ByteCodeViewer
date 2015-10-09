package org.obicere.bcviewer.gui.swing.settings;

import org.obicere.bcviewer.context.Domain;
import org.obicere.bcviewer.gui.settings.SettingModeler;
import org.obicere.bcviewer.settings.target.Setting;

import javax.swing.JComponent;

/**
 */
public class IntegerSettingModeler implements SettingModeler<Integer, JComponent> {

    private final Domain domain;

    public IntegerSettingModeler(final Domain domain) {
        this.domain = domain;
    }

    @Override
    public JComponent model(final Setting<Integer> setting) {
        return new IntegerSettingPanel(domain, setting);
    }
}
