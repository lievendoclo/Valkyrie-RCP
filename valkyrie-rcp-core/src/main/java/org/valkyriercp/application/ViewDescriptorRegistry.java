package org.valkyriercp.application;

import org.valkyriercp.application.ViewDescriptor;

/**
 * A registry for {@link ViewDescriptor} definitions.
 *
 * @author Keith Donald
 *
 */
public interface ViewDescriptorRegistry {

    /**
     * Returns an array of all the view descriptors in the registry.
     *
     * @return An array of all the view descriptors in the registry. The array may be empty but
     * will never be null.
     */
    public ViewDescriptor[] getViewDescriptors();

    /**
     * Returns the view descriptor with the given identifier, or null if no such descriptor
     * exists in the registry.
     *
     * @param viewDescriptorId The id of the view descriptor to be returned.
     * @return The view descriptor with the given id, or null.
     *
     * @throws IllegalArgumentException if {@code viewDescriptorId} is null.
     */
    public ViewDescriptor getViewDescriptor(String viewDescriptorId);

}
