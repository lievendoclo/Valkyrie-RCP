package org.valkyriercp.command;

import java.awt.*;

/**
 * A strategy interface for adding components to an underlying {@link java.awt.Container}.
 *
 * @author Keith Donald
 *
 * @see java.awt.Container
 */
public interface GroupContainerPopulator {

    /**
     * Returns the underlying container that this instance is responsible for populating.
     *
     * @return The underlying container, never null.
     */
    public Container getContainer();

    /**
     * Adds the given component to the underlying container.
     *
     * @param component The component to be added. Must not be null.
     *
     * @throws IllegalArgumentException if {@code component} is null.
     */
    public void add(Component component);

    /**
     * Adds a separator to the underlying container.
     */
    public void addSeparator();

    /**
     * Called to perform any required actions once the container has been populated.
     */
    public void onPopulated();

}