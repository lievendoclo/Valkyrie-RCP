package org.valkyriercp.component;

import org.valkyriercp.form.builder.InterceptorOverlayHelper;

import javax.swing.*;
import java.awt.*;

/**
 * Default overlay service implementation based on Spring RCP <code>OverlayHelper</code>.
 *
 * @author <a href = "mailto:julio.arguello@gmail.com" >Julio Argüello (JAF)</a>
 */
public class DefaultOverlayService implements OverlayService, SwingConstants {

    /**
     * The default insets to be used.
     */
    private static final Insets DEFAULT_INSETS = new Insets(0, 0, 0, 0);

    /**
     * {@inheritDoc}
     */
    @Override
    public Boolean isOverlayInstalled(JComponent targetComponent, JComponent overlay) {

        // TODO
        return Boolean.FALSE;
    }

    /**
     * {@inheritDoc}
     *
     * @see #installOverlay(JComponent, JComponent, int, Insets)
     */
    @Override
    public Boolean installOverlay(JComponent targetComponent, JComponent overlay) {

        this.installOverlay(targetComponent, overlay, SwingConstants.NORTH_WEST, DefaultOverlayService.DEFAULT_INSETS);

        return Boolean.TRUE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Boolean installOverlay(JComponent targetComponent, JComponent overlay, int position, Insets insets) {

        if (insets == null) { // TODO FIXME
            insets = new Insets(0, 0, 0, 0);
        }

        InterceptorOverlayHelper.attachOverlay(overlay, targetComponent, position, insets.left, insets.top);

        return Boolean.TRUE;
    }

    /**
     * {@inheritDoc}
     *
     * @see #uninstallOverlay(JComponent, JComponent, Insets)
     */
    public Boolean uninstallOverlay(JComponent targetComponent, JComponent overlay) {

        this.uninstallOverlay(targetComponent, overlay, null);

        return Boolean.TRUE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Boolean uninstallOverlay(JComponent targetComponent, JComponent overlay, Insets insets) {

        overlay.setVisible(Boolean.FALSE);

        return Boolean.TRUE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Boolean hideOverlay(JComponent targetComponent, JComponent overlay) {

        overlay.setVisible(Boolean.FALSE);

        return Boolean.TRUE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Boolean showOverlay(JComponent targetComponent, JComponent overlay) {

        overlay.setVisible(Boolean.TRUE);

        return Boolean.TRUE;
    }
}
