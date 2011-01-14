package org.valkyriercp.util;

import org.valkyriercp.component.MayHaveMessagableTab;
import org.valkyriercp.component.MessagableTabbedPane;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * A helper class that attaches one component (the overlay) on top of another
 * component.
 *
 * @author oliverh
 */
public class OverlayHelper implements SwingConstants
{
    private final OverlayTargetChangeHandler overlayTargetChangeHandler = new OverlayTargetChangeHandler();

    private final OverlayChangeHandler overlayChangeHandler = new OverlayChangeHandler();

    protected final JComponent overlay;
    protected final JComponent overlayClipper;
    protected final JComponent overlayTarget;

    private final int center;

    private final int xOffset;

    private final int yOffset;

    boolean isUpdating;

    private Runnable overlayUpdater = new OverlayUpdater();

    /**
     * Attaches an overlay to the specified component.
     *
     * @param overlay       the overlay component
     * @param overlayTarget the component over which <code>overlay</code> will be
     *                      attached
     * @param center        position relative to <code>overlayTarget</code> that overlay
     *                      should be centered. May be one of the
     *                      <code>SwingConstants</code> compass positions or
     *                      <code>SwingConstants.CENTER</code>.
     * @param xOffset       x offset from center
     * @param yOffset       y offset from center
     * @see SwingConstants
     */
    public static void attachOverlay(JComponent overlay, JComponent overlayTarget, int center, int xOffset, int yOffset)
    {
        new OverlayHelper(overlay, overlayTarget, center, xOffset, yOffset);
    }

    protected OverlayHelper(JComponent overlay, JComponent overlayTarget, int center, int xOffset, int yOffset)
    {
        this.overlay = overlay;
        this.overlayTarget = overlayTarget;
        this.center = center;
        this.xOffset = xOffset;
        this.yOffset = yOffset;
        this.overlayClipper = new JPanel();
        this.overlayClipper.setLayout(null);
        this.overlayClipper.add(overlay);
        this.overlayClipper.setOpaque(false);
        installListeners();
    }

    final class OverlayChangeHandler implements ComponentListener, PropertyChangeListener
    {
        public void componentHidden(ComponentEvent e)
        {
            hideOverlay();
        }

        public void componentMoved(ComponentEvent e)
        {
            // ignore
        }

        public void componentResized(ComponentEvent e)
        {
            // ignore
        }

        public void componentShown(ComponentEvent e)
        {
            updateOverlay();
        }

        public void propertyChange(PropertyChangeEvent e)
        {
            if ("ancestor".equals(e.getPropertyName()) || "layeredContainerLayer".equals(e.getPropertyName()))
            {
                return;
            }
            updateOverlay();
        }
    }

    class OverlayTargetChangeHandler implements HierarchyListener, HierarchyBoundsListener, ComponentListener
    {
        public void hierarchyChanged(HierarchyEvent e)
        {
            updateOverlay();
        }

        public void ancestorMoved(HierarchyEvent e)
        {
            updateOverlay();
        }

        public void ancestorResized(HierarchyEvent e)
        {
            updateOverlay();
        }

        public void componentHidden(ComponentEvent e)
        {
            hideOverlay();
        }

        public void componentMoved(ComponentEvent e)
        {
            updateOverlay();
        }

        public void componentResized(ComponentEvent e)
        {
            updateOverlay();
        }

        public void componentShown(ComponentEvent e)
        {
            updateOverlay();
        }
    }

    private void installListeners()
    {
        overlayTarget.addHierarchyListener(overlayTargetChangeHandler);
        overlayTarget.addHierarchyBoundsListener(overlayTargetChangeHandler);
        overlayTarget.addComponentListener(overlayTargetChangeHandler);
        overlay.addComponentListener(overlayChangeHandler);
        overlay.addPropertyChangeListener(overlayChangeHandler);
    }

    void updateOverlay()
    {
        if (isUpdating)
        {
            return;
        }
        isUpdating = true;
        // updating the overlay at the end of the event queue to avoid race conditions
        // see RCP-126 (http://opensource.atlassian.com/projects/spring/browse/RCP-216)
        SwingUtilities.invokeLater(overlayUpdater);
    }

    void putOverlay(final JLayeredPane layeredPane)
    {
        if (overlay.getParent() != overlayClipper)
        {
            JComponent parent = (JComponent) overlay.getParent();
            if (parent != null)
            {
                parent.remove(overlay);
            }
            overlayClipper.add(overlay);
        }
        if (overlayClipper.getParent() != layeredPane)
        {
            JComponent parent = (JComponent) overlayClipper.getParent();
            if (parent != null)
            {
                parent.remove(overlayClipper);
            }
            layeredPane.add(overlayClipper);
            layeredPane.setLayer(overlayClipper, JLayeredPane.PALETTE_LAYER.intValue());
        }
    }

    void positionOverlay(JLayeredPane layeredPane)
    {
        int centerX = xOffset;
        int centerY = yOffset;
        Rectangle overlayTargetBounds = new Rectangle(0, 0, overlayTarget.getWidth(), overlayTarget.getHeight());
        switch (center)
        {
            case SwingConstants.NORTH:
            case SwingConstants.NORTH_WEST:
            case SwingConstants.NORTH_EAST:
                centerY += overlayTargetBounds.y;
                break;
            case SwingConstants.CENTER:
            case SwingConstants.EAST:
            case SwingConstants.WEST:
                centerY += overlayTargetBounds.y + (overlayTargetBounds.height / 2);
                break;
            case SwingConstants.SOUTH:
            case SwingConstants.SOUTH_EAST:
            case SwingConstants.SOUTH_WEST:
                centerY += overlayTargetBounds.y + overlayTargetBounds.height;
                break;
            default:
                throw new IllegalArgumentException("Unknown value for center [" + center + "]");
        }
        switch (center)
        {
            case SwingConstants.WEST:
            case SwingConstants.NORTH_WEST:
            case SwingConstants.SOUTH_WEST:
                centerX += overlayTargetBounds.x;
                break;
            case SwingConstants.CENTER:
            case SwingConstants.NORTH:
            case SwingConstants.SOUTH:
                centerX += overlayTargetBounds.x + (overlayTargetBounds.width / 2);
                break;
            case SwingConstants.EAST:
            case SwingConstants.NORTH_EAST:
            case SwingConstants.SOUTH_EAST:
                centerX += overlayTargetBounds.x + overlayTargetBounds.width;
                break;
            default:
                throw new IllegalArgumentException("Unknown value for center [" + center + "]");
        }
        Dimension size = overlay.getPreferredSize();
        Rectangle newBound = new Rectangle(centerX - (size.width / 2), centerY - (size.height / 2), size.width,
                size.height);
        Rectangle visibleRect = findLargestVisibleRectFor(newBound);

        int offsetx = 0;
        int offsety = 0;

        if (visibleRect != null)
        {
            if (newBound.y < visibleRect.y)
            {
                offsety += visibleRect.y - newBound.y;
            }
            if (newBound.x < visibleRect.x)
            {
                offsetx += visibleRect.x - newBound.x;
            }
            newBound = newBound.intersection(visibleRect);
        }
        else
        {
            newBound.width = newBound.height = 0;
        }
        Point pt = SwingUtilities.convertPoint(overlayTarget, newBound.x, newBound.y, layeredPane);
        newBound.x = pt.x;
        newBound.y = pt.y;
        setOverlayBounds(newBound, offsetx, offsety);
    }

    /**
     * Searches up the component hierarchy to find the largest possible visible
     * rect that can enclose the entire rectangle.
     *
     * @param overlayRect rectangle whose largest enclosing visible rect to find
     * @return largest enclosing visible rect for the specified rectangle
     */
    private Rectangle findLargestVisibleRectFor(final Rectangle overlayRect)
    {
        Rectangle visibleRect = null;
        int curxoffset = 0;
        int curyoffset = 0;
        if (overlayTarget == null)
        {
            return null;
        }

        JComponent comp = overlayTarget;
        do
        {

            visibleRect = comp.getVisibleRect();
            visibleRect.x -= curxoffset;
            visibleRect.y -= curyoffset;
            if (visibleRect.contains(overlayRect))
            {
                return visibleRect;
            }
            curxoffset += comp.getX();
            curyoffset += comp.getY();

            comp = comp.getParent() instanceof JComponent ? (JComponent) comp.getParent() : null;
        }
        while (comp != null && !(comp instanceof JViewport) && !(comp instanceof JScrollPane));


        return visibleRect;
    }

    private void setOverlayBounds(Rectangle newBounds, int xoffset, int yoffset)
    {
        final Dimension preferred = overlay.getPreferredSize();
        final Rectangle overlayBounds = new Rectangle(-xoffset, -yoffset, preferred.width, preferred.height);
        if (!overlayBounds.equals(overlay.getBounds()))
        {
            overlay.setBounds(overlayBounds);
        }
        if (!newBounds.equals(overlayClipper.getBounds()))
        {
            overlayClipper.setBounds(newBounds);
        }
    }

    void hideOverlay()
    {
        setOverlayBounds(new Rectangle(0, 0, 0, 0), 0, 0);
    }

    void removeOverlay()
    {
        if (overlay.getParent() != overlayClipper && overlay.getParent() != null)
        {
            overlay.getParent().remove(overlay);
        }
        if (overlayClipper.getParent() != null)
        {
            overlayClipper.getParent().remove(overlayClipper);
        }
    }

    private Container overlayCapableParent;

    protected Container getOverlayCapableParent(JComponent component)
    {
        //if (overlayCapableParent != null)
        //    return overlayCapableParent;
        Component overlayChild = component;
        overlayCapableParent = component.getParent();
        if (overlay instanceof MayHaveMessagableTab)
        {
            MessagableTabbedPane tabbedPane;
            while (overlayCapableParent != null && !(overlayCapableParent instanceof JRootPane))
            {
                if (overlayCapableParent instanceof MessagableTabbedPane)
                {
                    tabbedPane = (MessagableTabbedPane) overlayCapableParent;
                    int tabIndex = tabbedPane.indexOfComponent(overlayChild);
                    ((MayHaveMessagableTab) overlay).setMessagableTab(tabbedPane, tabIndex);
                }

                overlayChild = overlayCapableParent;
                overlayCapableParent = overlayCapableParent.getParent();
            }
        }
        else
        {
            while (overlayCapableParent != null && !(overlayCapableParent instanceof JRootPane))
            {
                overlayCapableParent = overlayCapableParent.getParent();
            }
        }
        return overlayCapableParent;
    }

    protected JLayeredPane getLayeredPane(Container overlayCapableParent)
    {
        if (overlayCapableParent instanceof JRootPane)
        {
            return ((JRootPane) overlayCapableParent).getLayeredPane();
        }
        else
        {
            throw new IllegalArgumentException("Don't know how to handle parent of type ["
                    + overlayCapableParent.getClass().getName() + "].");
        }
    }


    public static class SingleComponentLayoutManager implements LayoutManager
    {
        private Component singleComponent;

        public SingleComponentLayoutManager(Component singleComponent)
        {
            this.singleComponent = singleComponent;
        }

        public void removeLayoutComponent(Component comp)
        {
        }

        public void addLayoutComponent(String name, Component comp)
        {
        }

        public void layoutContainer(Container parent)
        {
            // Fix 5/12/06 AlD: we don't need to base this on the
            // preferred size of the singleComponent or the extentSize
            // of the viewport because the viewport will have already resized
            // the JLayeredPane and taken everything else into consideration.
            // It will have also honored the Scrollable flags, which is
            // something the original code here did not do.
            singleComponent.setBounds(0, 0, parent.getWidth(), parent.getHeight());
        }

        public Dimension minimumLayoutSize(Container parent)
        {
            return singleComponent.getMinimumSize();
        }

        public Dimension preferredLayoutSize(Container parent)
        {
            return singleComponent.getPreferredSize();
        }
    }

    class OverlayUpdater implements Runnable
    {

        public void run()
        {
            try
            {
                Container overlayCapableParent = getOverlayCapableParent(overlayTarget);
                if (overlayCapableParent == null)
                {
                    removeOverlay();
                }
                else if (!overlayTarget.isShowing() || !overlay.isVisible())
                {
                    hideOverlay();
                }
                else
                {
                    JLayeredPane layeredPane = getLayeredPane(overlayCapableParent);
                    if (layeredPane.isVisible() && layeredPane.isShowing())
                    {
                        putOverlay(layeredPane);
                        positionOverlay(layeredPane);
                    }
                }
            }
            finally
            {
                isUpdating = false;
            }
        }
    }
}
