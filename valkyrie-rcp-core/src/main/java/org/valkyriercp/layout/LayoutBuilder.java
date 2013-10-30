package org.valkyriercp.layout;

import javax.swing.*;

/**
 * LayoutBuilders exist to make it easier to do component layouts. Typical usage
 * is to create an instance of the builder, add components to it, the call the
 * builder's {@link #getPanel()}method.
 */
public interface LayoutBuilder {

    /**
     * Creates and returns a JPanel with all the given components in it, using
     * the "hints" that were provided to the builder.
     *
     * @return a new JPanel with the components laid-out in it; never null
     */
    JPanel getPanel();

}
