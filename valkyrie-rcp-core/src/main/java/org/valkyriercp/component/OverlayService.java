package org.valkyriercp.component;

import javax.swing.*;
import java.awt.*;

/**
 * This interface provide a way for installing and uninstalling overlay components.
 * <p>
 * It's also a way for decoupling this functionality initially provided by <code>OverlayHelper</code>.
 *
 * @author <a href = "mailto:julio.arguello@gmail.com" >Julio Argüello (JAF)</a>
 */
public interface OverlayService {

    /**
     * Returns if the overlay is currently installed into the target component.
     *
     * @param targetComponent
     *            the target component.
     * @param overlay
     *            the overlay.
     * @return <code>true</code> if installed and <code>false</code> in other case.
     */
    Boolean isOverlayInstalled(JComponent targetComponent, JComponent overlay);

    /**
     * Installs an overlay on top of an overlayable component.
     *
     * @param targetComponent
     *            the target component.
     * @param overlay
     *            the overlay.
     *
     * @return <code>true</code> if success and <code>false</code> in other case.
     */
    Boolean installOverlay(JComponent targetComponent, JComponent overlay);

    /**
     * Installs an overlay on top of an overlayable component.
     *
     * @param targetComponent
     *            the target component.
     * @param overlay
     *            the overlay.
     * @param position
     *            the overlay position.
     * @param insets
     *            the target component location insets. A <code>null</code> value make no changes. Can be overriden
     *            using {@link #showOverlay(JComponent, JComponent)} and
     *            {@link #hideOverlay(JComponent, JComponent)} methods.
     *
     * @return <code>true</code> if success and <code>false</code> in other case.
     */
    Boolean installOverlay(JComponent targetComponent, JComponent overlay, int position, Insets insets);

    /**
     * Uninstalls an overlay.
     *
     * @param targetComponent
     *            the target component.
     * @param overlay
     *            the overlay.
     *
     * @return <code>true</code> if success and <code>false</code> in other case.
     */
    Boolean uninstallOverlay(JComponent targetComponent, JComponent overlay);

    /**
     * Uninstalls an overlay.
     *
     * @param targetComponent
     *            the target component.
     * @param overlay
     *            the overlay.
     * @param insets
     *            the target component location insets. A <code>null</code> value make no changes.
     *
     * @return <code>true</code> if success and <code>false</code> in other case.
     */
    Boolean uninstallOverlay(JComponent targetComponent, JComponent overlay, Insets insets);

    /**
     * Shows an overlay on top of a given overlayable.
     *
     * @param targetComponent
     *            the target component.
     * @param overlay
     *            the overlay.,
     *
     * @return <code>true</code> if success and <code>false</code> in other case.
     */
    Boolean showOverlay(JComponent targetComponent, JComponent overlay);

    /**
     * Hides an overlay on top of a given overlayable.
     *
     * @param targetComponent
     *            the target component.
     * @param overlay
     *            the overlay.,
     *
     * @return <code>true</code> if success and <code>false</code> in other case.
     */
    Boolean hideOverlay(JComponent targetComponent, JComponent overlay);
}
