package org.valkyriercp.application;

/**
 * Service interface for creating <code>PageComponentPane</code>s.
 *
 * @author Peter De Bruycker
 */
public interface PageComponentPaneFactory {
    /**
     * Creates a new <code>PageComponentPane</code> for the given
     * <code>PageComponent</code>.
     *
     * @param component the <code>PageComponent</code>
     * @return the new <code>PageComponentPane</code>
     */
    PageComponentPane createPageComponentPane( PageComponent component );
}
