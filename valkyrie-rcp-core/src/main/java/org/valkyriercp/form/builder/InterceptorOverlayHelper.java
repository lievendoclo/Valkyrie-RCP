package org.valkyriercp.form.builder;

import org.valkyriercp.util.OverlayHelper;

import javax.swing.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * Helper class to attach overlay components to form components. This is needed as some
 * form components are placed inside a scrollpane, and in that case the overlay should be
 * added to the scrollpane and not to the form component itself.
 */
public class InterceptorOverlayHelper {
    private InterceptorOverlayHelper() {
        // static class only
    }

    public static void attachOverlay( final JComponent overlay, final JComponent component, final int center,
            final int xOffset, final int yOffset ) {
        if( component.getParent() == null ) {
            PropertyChangeListener waitUntilHasParentListener = new PropertyChangeListener() {
                public void propertyChange( PropertyChangeEvent e ) {
                    if( component.getParent() != null ) {
                        component.removePropertyChangeListener( "ancestor", this );
                        doAttachOverlay( overlay, component, center, xOffset, yOffset );
                    }
                }
            };
            component.addPropertyChangeListener( "ancestor", waitUntilHasParentListener );
        } else {
            doAttachOverlay( overlay, component, center, xOffset, yOffset );
        }
    }

    private static void doAttachOverlay( JComponent overlay, JComponent component, int center, int xOffset, int yOffset ) {
        JComponent componentToOverlay = hasParentScrollPane( component ) ? getParentScrollPane( component ) : component;
        OverlayHelper.attachOverlay(overlay, componentToOverlay, center, xOffset, yOffset);
    }

    private static JScrollPane getParentScrollPane( JComponent component ) {
        return (JScrollPane) component.getParent().getParent();
    }

    private static boolean hasParentScrollPane( JComponent component ) {
        return component.getParent() != null && component.getParent() instanceof JViewport
                && component.getParent().getParent() instanceof JScrollPane;
    }
}
