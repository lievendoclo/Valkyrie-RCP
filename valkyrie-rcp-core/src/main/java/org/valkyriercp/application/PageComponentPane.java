package org.valkyriercp.application;

import org.valkyriercp.factory.ControlFactory;

/**
 * A <code>PageComponentPane</code> is a container that holds the
 * <code>PageComponent</code>'s control, and can add extra decorations (add a toolbar,
 * a border, ...)
 * <p>
 * This allows for adding extra behaviour to <code>PageComponent</code>s that have to
 * be applied to all <code>PageComponent</code>.
 *
 * @author Peter De Bruycker
 */
public interface PageComponentPane extends ControlFactory {
    /**
     * Returns the contained <code>PageComponent</code>.
     *
     * @return the <code>PageComponent</code>
     */
    public PageComponent getPageComponent();
}
