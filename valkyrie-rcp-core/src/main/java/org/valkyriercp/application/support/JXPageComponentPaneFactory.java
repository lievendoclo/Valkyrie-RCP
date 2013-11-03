package org.valkyriercp.application.support;


import org.valkyriercp.application.PageComponent;
import org.valkyriercp.application.PageComponentPane;
import org.valkyriercp.application.PageComponentPaneFactory;

public class JXPageComponentPaneFactory implements PageComponentPaneFactory {

    public PageComponentPane createPageComponentPane( PageComponent component ) {
        return new JXPageComponentPane( component );
    }

}
