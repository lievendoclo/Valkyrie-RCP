package org.valkyriercp.factory;

import org.jdesktop.swingx.JXButton;

import javax.swing.*;

/**
 * Default implementation of a {@link ButtonFactory}.
 *
 * @author Keith Donald
 */
public class DefaultButtonFactory implements ButtonFactory {

    /**
     * {@inheritDoc}
     */
    public AbstractButton createButton() {
        return new JXButton();
    }

    /**
     * {@inheritDoc}
     */
    public AbstractButton createCheckBox() {
        return new JCheckBox();
    }

    /**
     * {@inheritDoc}
     */
    public AbstractButton createToggleButton() {
        return new JToggleButton();
    }

    /**
     * {@inheritDoc}
     */
    public AbstractButton createRadioButton() {
        return new JRadioButton();
    }
}