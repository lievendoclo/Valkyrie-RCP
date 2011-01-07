package org.valkyriercp.core;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.valkyriercp.application.config.ApplicationConfig;

@Configurable
public class ApplicationConfigAccessor {
    @Autowired
    protected ApplicationConfig applicationConfig;

    protected ApplicationConfig getApplicationConfig() {
        return applicationConfig;
    }

    protected void setApplicationConfig(ApplicationConfig applicationConfig) {
        this.applicationConfig = applicationConfig;
    }
}
