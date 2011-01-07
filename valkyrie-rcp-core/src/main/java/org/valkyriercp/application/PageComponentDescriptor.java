package org.valkyriercp.application;

import org.valkyriercp.core.DescribedElement;
import org.valkyriercp.core.PropertyChangePublisher;
import org.valkyriercp.core.VisualizedElement;

/**
 * Metadata about a page component; a page component is effectively a
 * singleton page component definition. A descriptor also acts as a factory
 * which produces new instances of a given page component when requested,
 * typically by a requesting application page. A page component descriptor
 * can also produce a command which launches a page component for display
 * on the page within the current active window.
 */
public interface PageComponentDescriptor extends PropertyChangePublisher, DescribedElement, VisualizedElement {

    /**
     * Returns the identifier of this descriptor.
     * @return The descriptor id.
     */
    public String getId();

    /**
     * Creates the page component defined by this descriptor.
     * @return The page component, never null.
     */
    public PageComponent createPageComponent();

}
