package org.valkyriercp.factory;

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
        return new JButton();
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