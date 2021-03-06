package org.obicere.bytecode.viewer.gui.swing.menu;

import org.obicere.bytecode.viewer.context.Domain;
import org.obicere.bytecode.viewer.gui.EditorPanel;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

/**
 */
public class ViewMenu extends JMenu {

    public ViewMenu(final Domain domain) {
        super("View");
        setMnemonic('V');

        final JMenuItem close = new JMenuItem("Close Tab");
        final JMenuItem reload = new JMenuItem("Reload");
        final JMenuItem hardReload = new JMenuItem("Hard Reload");

        close.addActionListener(e -> {
            final EditorPanel panel = domain.getGUIManager().getFrameManager().getEditorManager().getOpenEditorPanel();

            if (panel != null) {
                panel.close();
            }
        });

        reload.addActionListener(e -> {
            final EditorPanel panel = domain.getGUIManager().getFrameManager().getEditorManager().getOpenEditorPanel();

            if (panel != null) {
                panel.reload();
            }
        });
        hardReload.addActionListener(e -> {
            final EditorPanel panel = domain.getGUIManager().getFrameManager().getEditorManager().getOpenEditorPanel();
            if (panel != null) {
                panel.hardReload();
            }
        });

        add(close);
        add(reload);
        add(hardReload);
    }
}
