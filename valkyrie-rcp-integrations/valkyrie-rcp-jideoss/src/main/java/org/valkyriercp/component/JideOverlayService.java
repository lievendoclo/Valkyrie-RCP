/**
 * Copyright (C) 2015 Valkyrie RCP
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.valkyriercp.component;

import com.jidesoft.swing.DefaultOverlayable;

import javax.swing.*;
import java.awt.*;
import java.text.MessageFormat;

/**
 * Jide OSS implementation of overlay service.
 * <p>
 * Expects the target component to get attached an instance of <code>DefaultOverlayable</code> (AKA "overlayable"), in
 * other case this will not work.
 * <p>
 * For that reason this implementation should be used together with <code>JideBindingFactory</code> in order to find
 * <code>DefaultOverlayable</code> in the overlayable component hierarchy.
 *
 * @author <a href = "mailto:julio.arguello@gmail.com" >Julio Arg??ello (JAF)</a>
 *
 * @see DefaultOverlayable
 */
public class JideOverlayService implements OverlayService, SwingConstants {
    /**
     * Return value for searchs with no result.
     */
    private static final int NOT_FOUND = -1;

    /**
     * Message when overlay is already installed.
     */
    private static final MessageFormat NOT_FOUND_FMT = new MessageFormat("Overlayable not found for \"{0}\"");

    /**
     * Message when overlay is already installed.
     */
    private static final MessageFormat ALREADY_INSTALLED_FMT = new MessageFormat(
            "Overlay \"{0}\" already installed into \"{1}\"");

    /**
     * Message when overlay is not installed yet.
     */
    private static final MessageFormat NOT_INSTALLED_FMT = new MessageFormat(
            "Overlay \"{0}\" not installed into \"{1}\"");

    /**
     * The {@value #TARGET_COMPONENT_PARAM} method parameter.
     */
    private static final String TARGET_COMPONENT_PARAM = "targetComponent";

    /**
     * The {@value #OVERLAY} method parameter.
     */
    private static final String OVERLAY = "overlay";

    /**
     * {@inheritDoc}
     */
    public Boolean isOverlayInstalled(JComponent targetComponent, JComponent overlay) {

        final DefaultOverlayable overlayable = this.findOverlayable(targetComponent);

        return this.isOverlayInstalled(overlayable, overlay);
    }

    /**
     * {@inheritDoc}
     * <p>
     * Employs a default position <code>SwingConstants.NORTH_WEST</code> and <code>null</code> insets to avoid changes.
     *
     * @see #installOverlay(JComponent, JComponent, int, Insets)
     */
    @Override
    public Boolean installOverlay(JComponent targetComponent, JComponent overlay) {

        return this.installOverlay(targetComponent, overlay, SwingConstants.NORTH_WEST, null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Boolean installOverlay(JComponent targetComponent, JComponent overlay, int position, Insets insets) {
        final DefaultOverlayable overlayable = this.findOverlayable(targetComponent);

        if ((overlayable != null) && !this.isOverlayInstalled(overlayable, overlay)) {

            // Install overlay and set location insets
            overlayable.addOverlayComponent(overlay, position, -1);
            if (insets != null) {
                overlayable.setOverlayLocationInsets(insets);
            }

            return this.hideOverlay(targetComponent, overlay);
        } else if (overlayable == null) {
            return Boolean.FALSE;
        } else {
           return Boolean.FALSE;
        }
    }

    /**
     * {@inheritDoc}
     * <p>
     * Employs <code>null</code> insets to avoid changes.
     *
     * @see #uninstallOverlay(JComponent, JComponent, Insets)
     */
    public Boolean uninstallOverlay(JComponent targetComponent, JComponent overlay) {

        return this.uninstallOverlay(targetComponent, overlay, null);
    }

    /**
     * {@inheritDoc}
     */
    public Boolean uninstallOverlay(JComponent targetComponent, JComponent overlay, Insets insets) {
        final DefaultOverlayable overlayable = this.findOverlayable(targetComponent);

        // If overlay is installed...
        if ((overlayable != null) && this.isOverlayInstalled(overlayable, overlay)) {

            // Uninstall overlay
            overlayable.removeOverlayComponent(overlay);

            // If location insets are not null then change them
            if (insets != null) {
                overlayable.setOverlayLocationInsets(insets);
            }

            return Boolean.TRUE;
        } else if (overlayable == null) {
           return Boolean.FALSE;
        } else {
            return Boolean.FALSE;
        }
    }

    /**
     * {@inheritDoc}
     *
     */
    @Override
    public Boolean showOverlay(JComponent targetComponent, JComponent overlay) {

        return this.setVisible(targetComponent, overlay, Boolean.TRUE);
    }

    /**
     * {@inheritDoc}
     *
     */
    @Override
    public Boolean hideOverlay(JComponent targetComponent, JComponent overlay) {

        return this.setVisible(targetComponent, overlay, Boolean.FALSE);
    }

    /**
     * {@inheritDoc}
     */
    protected final Boolean isOverlayInstalled(DefaultOverlayable overlayable, JComponent overlay) {
        if (overlayable != null) {

            return (overlayable.getOverlayLocation(overlay) != JideOverlayService.NOT_FOUND);
        }

        return Boolean.FALSE;
    }

    /**
     * Tries to find a <code>DefaultOverlayable</code> given a target component.
     *
     * @param targetComponent
     *            the target component.
     * @return the associated <code>Overlayable</code> if found and <code>null</code> in other case.
     */
    protected final DefaultOverlayable findOverlayable(JComponent targetComponent) {

        // Find overlayable in tart component container
        final Container parent = targetComponent.getParent();

        if ((parent != null) && (parent instanceof DefaultOverlayable)) {

            return (DefaultOverlayable) parent;
        }

        return null;
    }

    /**
     * Show or hide the given overlay depending on the given <code>show</code> parameter.
     *
     * @param targetComponent
     *            the target component.
     * @param overlay
     *            the overlay component.
     * @param show
     *            whether to show or hide the given overlay (<code>true</code> for showing).
     *
     * @return <code>true</code> if success and <code>false</code> in other case.
     */
    protected final Boolean setVisible(JComponent targetComponent, JComponent overlay, Boolean show) {
        // If overlay is installed...
        if (this.isOverlayInstalled(targetComponent, overlay)) {

            // Definitely show or hide overlay
            overlay.setVisible(show);
            overlay.repaint();

            return Boolean.TRUE;
        }

        return Boolean.FALSE;
    }
}
