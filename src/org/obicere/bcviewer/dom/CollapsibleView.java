package org.obicere.bcviewer.dom;

import java.awt.Graphics;
import java.awt.Rectangle;

/**
 */
public class CollapsibleView extends View<CollapsibleElement> {

    public CollapsibleView(final CollapsibleElement element) {
        super(element);
    }

    @Override
    protected void paintSelf(final Graphics g, final Rectangle bounds) {

    }

    @Override
    protected void paintChildren(final Graphics g) {
        if (!element.isCollapsed()) {
            super.paintChildren(g);
        }
    }

    @Override
    protected Rectangle layoutSelf(final int x, final int y) {
        return new Rectangle();
    }

    @Override
    protected Rectangle layoutChildren(final Rectangle parent) {
        if (element.isCollapsed()) {
            return new Rectangle();
        } else {
            return super.layoutChildren(parent);
        }
    }
}