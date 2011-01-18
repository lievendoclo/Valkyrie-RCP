package org.valkyriercp.application.support;

import org.valkyriercp.application.PageComponent;
import org.valkyriercp.application.PageComponentPane;
import org.valkyriercp.application.PageComponentPaneFactory;

/**
 * Factory for <code>SimplePageComponentPane</code> instances.
 *
 * @author Peter De Bruycker
 *
 */
public class SimplePageComponentPaneFactory implements PageComponentPaneFactory {

    public PageComponentPane createPageComponentPane( PageComponent component ) {
        return new SimplePageComponentPane( component );
    }

}