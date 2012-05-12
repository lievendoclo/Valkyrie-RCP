package org.valkyriercp.application.support;

import org.springframework.beans.factory.annotation.Autowired;
import org.valkyriercp.application.ApplicationWindow;
import org.valkyriercp.application.ApplicationWindowFactory;
import org.valkyriercp.application.config.ApplicationConfig;
import org.valkyriercp.application.config.ApplicationLifecycleAdvisor;
import org.valkyriercp.application.config.support.DefaultApplicationLifecycleAdvisor;

public class JXTaskPaneNavigatorApplicationWindowFactory implements ApplicationWindowFactory
{
    @Autowired
    private ApplicationLifecycleAdvisor lifecycleAdvisor;

    @Autowired
    private ApplicationConfig applicationConfig;

    private boolean onlyOneExpanded;

    public boolean isOnlyOneExpanded() {
        return onlyOneExpanded;
    }

    public void setOnlyOneExpanded(boolean onlyOneExpanded) {
        this.onlyOneExpanded = onlyOneExpanded;
    }

    public ApplicationWindow createApplicationWindow()
    {
        if (lifecycleAdvisor instanceof DefaultApplicationLifecycleAdvisor)
        {
            return new JXTaskPaneNavigatorApplicationWindow(applicationConfig);
        }
        else
        {
            throw new IllegalArgumentException();
        }
    }
}
