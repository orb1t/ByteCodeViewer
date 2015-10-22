package org.obicere.bcviewer.gui.swing;

import org.obicere.bcviewer.configuration.Icons;
import org.obicere.bcviewer.context.Domain;
import org.obicere.bcviewer.gui.EditorPanel;
import org.obicere.bcviewer.gui.EditorPanelManager;
import org.obicere.bcviewer.gui.swing.editor.SwingEditorPanel;

import javax.swing.ButtonModel;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.border.EmptyBorder;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;

/**
 * @author Obicere
 */
public class SwingEditorPanelManager implements EditorPanelManager {

    private final JPanel editorArea;

    private final CardLayout contentLayout;

    private final JTabbedPane tabbedPane;

    private final String tabbedPaneName = "Tabbed";

    private final JPanel dropPane;

    private final String dropPaneName = "Drop";

    private final Domain domain;

    private final HashMap<String, SwingEditorPanel> addedPanels = new HashMap<>();

    public SwingEditorPanelManager(final Domain domain) {

        this.domain = domain;
        this.contentLayout = new CardLayout();
        this.editorArea = new JPanel(contentLayout);
        this.tabbedPane = new JTabbedPane();
        this.dropPane = new JPanel();
    }

    public JComponent getContent() {

        final JPanel content = new JPanel(new BorderLayout());

        final JLabel icon = new JLabel(domain.getIcons().getIcon(Icons.ICON_DARK_256));
        icon.setBorder(new EmptyBorder(128, 128, 128, 128));

        dropPane.setLayout(new BorderLayout());
        dropPane.add(icon);

        editorArea.add(tabbedPaneName, tabbedPane);
        editorArea.add(dropPaneName, dropPane);

        contentLayout.show(editorArea, dropPaneName);

        content.add(editorArea);

        return content;
    }

    @Override
    public EditorPanel getEditorPanel(final String className) {
        return addedPanels.get(className);
    }

    @Override
    public EditorPanel[] getEditorPanels() {
        final ArrayList<EditorPanel> panels = new ArrayList<>(tabbedPane.getComponentCount());
        for (final Component component : tabbedPane.getComponents()) {
            if (component instanceof EditorPanel) {
                panels.add((EditorPanel) component);
            }
        }
        return panels.toArray(new EditorPanel[panels.size()]);
    }

    @Override
    public boolean hasEditorPanel(final String className) {
        return getEditorPanel(className) != null;
    }

    @Override
    public EditorPanel displayEditorPanel(final String className) {
        final SwingEditorPanel panel = (SwingEditorPanel) getEditorPanel(className);
        if (panel != null) {
            display(panel, className);
            return panel;
        }
        return null;
    }

    @Override
    public EditorPanel createEditorPanel(final String className) {
        if (addedPanels.containsKey(className)) {
            return addedPanels.get(className);
        }

        final SwingEditorPanel panel = new SwingEditorPanel(domain);

        display(panel, className);

        addedPanels.put(className, panel);

        return panel;
    }

    private void display(final SwingEditorPanel panel, final String className) {
        if (panel == null || className == null) {
            return;
        }
        int index = tabbedPane.indexOfComponent(panel);
        if(index < 0) {

            index = tabbedPane.getTabCount();

            panel.setName(className);

            if (tabbedPane.getTabCount() == 0) {
                contentLayout.show(editorArea, tabbedPaneName);
            }

            tabbedPane.addTab(className, panel);

            final JPanel tabPanel = new JPanel(new FlowLayout(FlowLayout.LEADING, 5, 0));

            tabPanel.setOpaque(false);
            tabPanel.add(new JLabel(className));
            tabPanel.add(new TabCloseButton(className));

            tabbedPane.setTabComponentAt(index, tabPanel);
        }
        tabbedPane.setSelectedIndex(index);
    }

    private DefaultMutableTreeNode getNodeWithObject(final DefaultMutableTreeNode node, final String object){
        final Enumeration enumeration = node.children();
        while(enumeration.hasMoreElements()){
            final DefaultMutableTreeNode child = (DefaultMutableTreeNode) enumeration.nextElement();
            if(child.getUserObject().equals(object)){
                return child;
            }
        }
        return null;
    }

    @Override
    public EditorPanel removeEditorPanel(final String className) {
        final SwingEditorPanel component = (SwingEditorPanel) getEditorPanel(className);
        if (component != null) {
            final int index = tabbedPane.indexOfTab(className);
            tabbedPane.removeTabAt(index);

            if (tabbedPane.getTabCount() == 0) {
                contentLayout.show(editorArea, dropPaneName);
            }
            return component;
        }
        return null;
    }

    @Override
    public EditorPanel getOpenEditorPanel() {
        return (EditorPanel) tabbedPane.getSelectedComponent();
    }

    private class TabCloseButton extends JButton {

        public TabCloseButton(final String className) {
            setPreferredSize(new Dimension(7, 7));
            setFocusable(false);
            setBorderPainted(false);
            setOpaque(false);
            setContentAreaFilled(false);
            addActionListener(e -> removeEditorPanel(className));
        }

        @Override
        protected void paintComponent(final Graphics g) {

            final ButtonModel model = getModel();
            if (model.isRollover()) {
                g.setColor(Color.DARK_GRAY);
            } else {

                g.setColor(Color.GRAY);
            }
            if (model.isPressed()) {
                g.translate(1, 1);
            }
            final Graphics2D g2 = (Graphics2D) g;

            final BasicStroke stroke = new BasicStroke(2, BasicStroke.CAP_ROUND, BasicStroke.JOIN_BEVEL);

            final int length = 3;
            final int mid = getWidth() / 2;
            g2.setStroke(stroke);
            g2.drawLine(mid - length, mid - length, mid + length, mid + length);
            g2.drawLine(mid - length, mid + length, mid + length, mid - length);
        }
    }
}