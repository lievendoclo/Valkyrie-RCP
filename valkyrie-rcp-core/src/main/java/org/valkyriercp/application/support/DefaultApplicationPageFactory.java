package org.valkyriercp.application.support;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.valkyriercp.application.ApplicationPage;
import org.valkyriercp.application.ApplicationPageFactory;
import org.valkyriercp.application.ApplicationWindow;
import org.valkyriercp.application.PageDescriptor;

public class DefaultApplicationPageFactory implements ApplicationPageFactory {
    private static final Logger logger = LoggerFactory.getLogger(DefaultApplicationPageFactory.class);

    public ApplicationPage createApplicationPage( ApplicationWindow window, PageDescriptor descriptor ) {
        logger.info( "Creating new DefaultApplicationPage" );

        DefaultApplicationPage page = new DefaultApplicationPage();
        page.setApplicationWindow( window );
        page.setDescriptor( descriptor );

        return page;
    }
}