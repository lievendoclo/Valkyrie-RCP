package org.valkyriercp.component;

import javax.swing.*;
import java.awt.*;

/**
 * A useful class for debugging layout problems when using {@link java.awt.GridBagLayout}.
 * When it is displayed, "guidelines" are shown.
 */
public class GridBagLayoutDebugPanel extends JPanel {
    private Color gridColor = Color.red;

    public GridBagLayoutDebugPanel() {
        super(new GridBagLayout());
    }

    /**
     * Change the color used for painting the guidelines
     *
     * @param color
     *            defaults to {@link Color#red}
     */
    public void setGridColor(Color color) {
        gridColor = color;
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        paintGrid(g);
    }

    private void paintGrid(Graphics g) {
        if (!(getLayout() instanceof GridBagLayout))
            return;

        final GridBagLayout layout = (GridBagLayout)getLayout();
        final int[][] layoutDimensions = layout.getLayoutDimensions();
        final int[] columnWidths = layoutDimensions[0];
        final int[] rowHeights = layoutDimensions[1];
        final Point layoutOrigin = layout.getLayoutOrigin();
        final int left = layoutOrigin.x;
        final int top = layoutOrigin.y;
        g.setColor(gridColor);

        int width = 0;
        for (int i = 0; i < columnWidths.length; i++) {
            width += columnWidths[i];
        }

        int height = 0;
        for (int i = 0; i < rowHeights.length; i++) {
            height += rowHeights[i];
        }

        int x = left;
        for (int i = 0; i < columnWidths.length; i++) {
            x += columnWidths[i];
            g.fillRect(x, top, 1, height);
        }

        int y = top;
        for (int i = 0; i < rowHeights.length; i++) {
            y += rowHeights[i];
            g.fillRect(left, y, width, 1);
        }
    }

}
