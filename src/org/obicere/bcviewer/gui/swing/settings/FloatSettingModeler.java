package org.obicere.bcviewer.gui.swing.settings;

import org.obicere.bcviewer.context.Domain;
import org.obicere.bcviewer.gui.settings.SettingModeler;
import org.obicere.bcviewer.settings.target.Setting;

import javax.swing.JComponent;

/**
 */
public class FloatSettingModeler implements SettingModeler<Float, JComponent> {

    private final Domain domain;

    public FloatSettingModeler(final Domain domain) {
        this.domain = domain;
    }

    @Override
    public JComponent model(final Setting<Float> setting) {
        return new FloatSettingPanel(domain, setting);
    }
}