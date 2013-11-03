package org.valkyriercp.application.support;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.valkyriercp.application.ApplicationWindow;
import org.valkyriercp.application.ApplicationWindowFactory;
import org.valkyriercp.application.config.ApplicationConfig;

/**
 * <code>ApplicationWindowFactory</code> implementation for
 * <code>DefaultApplicationWindow</code>.
 *
 * @author Peter De Bruycker
 *
 */
@Component
public class DefaultApplicationWindowFactory implements ApplicationWindowFactory {
    private static final Logger logger = LoggerFactory.getLogger(DefaultApplicationWindowFactory.class);

    @Autowired
    private ApplicationConfig applicationConfig;

    public ApplicationWindow createApplicationWindow() {
        logger.info( "Creating new DefaultApplicationWindow" );

        return new DefaultApplicationWindow(applicationConfig);
    }
}
