package org.obicere.bytecode.viewer.gui.swing.tree;

import javax.swing.JLabel;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;
import java.awt.Component;

public class ByteCodeTreeCellRenderer extends DefaultTreeCellRenderer {

    @Override
    public Component getTreeCellRendererComponent(final JTree tree, final Object value, final boolean sel, final boolean expanded, final boolean leaf, final int row, final boolean hasFocus) {
        final JLabel component = (JLabel) super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);

        if (value instanceof ByteCodeTreeNode) {
            final ByteCodeTreeNode node = (ByteCodeTreeNode) value;
            component.setIcon(node.getIcon());
            component.setDisabledIcon(node.getDisabledIcon());
        }
        return component;
    }
}
