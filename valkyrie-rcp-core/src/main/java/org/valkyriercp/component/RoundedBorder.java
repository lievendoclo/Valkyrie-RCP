package org.valkyriercp.component;

import javax.swing.border.AbstractBorder;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

/**
 * A rounded border implementation copied from <code>com.publicobject.misc.swing</code>.
 *
 * @author <a href = "mailto:julio.arguello@gmail.com" >Julio Argüello (JAF)</a>
 */
public class RoundedBorder extends AbstractBorder {

    /**
     * This is a <code>Serializable</code> class.
     */
    private static final long serialVersionUID = 728818763812538126L;

    /**
     * The insets.
     */
    private Insets insets;

    /**
     * The stroke.
     */
    private Stroke stroke;

    /**
     * The stroke color.
     */
    private Color strokeColor;

    /**
     * The arc.
     */
    private int arc;

    /**
     * The stroke width.
     */
    private float strokeWidth;

    /**
     * Simple rounded border with no outline.
     *
     * @param arc
     *            the arc.
     */
    public RoundedBorder(int arc) {

        this.arc = arc;

        final int i = (int) (arc / Math.PI) / 2;
        this.insets = new Insets(i, i, i, i);
    }

    /**
     * Rounded border with an outline.
     *
     * @param arc
     *            the arc.
     * @param strokeWidth
     *            width of the outline.
     * @param color
     *            color of the outline.
     */
    public RoundedBorder(int arc, float strokeWidth, Color color) {

        this.arc = arc;
        int i = (int) ((arc / Math.PI) + ((strokeWidth * 2) / (Math.PI)));
        this.insets = new Insets(i, i, i, i);
        this.stroke = new BasicStroke(strokeWidth);
        this.strokeColor = color;
        this.strokeWidth = strokeWidth;
    }

    /**
     * {@inheritDoc}
     */
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {

        final Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // In real code optimize by preserving the rect between calls
        if (stroke != null) {
            final int i = (int) strokeWidth / 2;
            final RoundRectangle2D.Float rect = //
            new RoundRectangle2D.Float(i, i, width - strokeWidth, height - strokeWidth, arc, arc);
            g2.translate(x, y);
            g2.setColor(c.getBackground());
            g2.fill(rect);

            g2.setColor(strokeColor);
            g2.setStroke(stroke);
            g2.draw(rect);
        } else {
            final RoundRectangle2D.Float rect = new RoundRectangle2D.Float(0, 0, width, height, arc, arc);
            g2.translate(x, y);
            g2.setColor(c.getBackground());
            g2.fill(rect);
        }
    }

    /*
     * boring stuff from here on...
     */

    /**
     * {@inheritDoc}
     */
    public Insets getBorderInsets(Component c) {

        return this.insets;
    }

    /**
     * {@inheritDoc}
     */
    public Insets getBorderInsets(Component c, Insets insets) {

        return this.insets;
    }

    /**
     * {@inheritDoc}
     */
    public Rectangle getInteriorRectangle(Component c, int x, int y, int width, int height) {

        return RoundedBorder.getInteriorRectangle(c, this, x, y, width, height);
    }

    /**
     * Gets the interior rectangle.
     *
     * @param c
     *            the target component.
     * @param b
     *            the border.
     * @param x
     *            the x coordinate.
     * @param y
     *            the y coordinate.
     * @param width
     *            the width.
     * @param height
     *            the height.
     * @return the rectangle.
     */
    public static Rectangle getInteriorRectangle(Component c, Border b, int x, int y, int width, int height) {

        final Insets insets;
        if (b != null) {
            insets = b.getBorderInsets(c);
        } else {
            insets = new Insets(0, 0, 0, 0);
        }

        return new Rectangle(x + insets.left, //
                y + insets.top, //
                width - insets.right - insets.left, //
                height - insets.top - insets.bottom);
    }
}

