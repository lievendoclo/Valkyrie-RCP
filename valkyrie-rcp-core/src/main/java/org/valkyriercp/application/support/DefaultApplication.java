package org.valkyriercp.application.support;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.valkyriercp.application.*;
import org.valkyriercp.application.config.ApplicationConfig;
import org.valkyriercp.application.config.ApplicationLifecycleAdvisor;
import org.valkyriercp.image.ImageSource;
import org.valkyriercp.image.NoSuchImageResourceException;

import java.awt.*;

@Component
public class DefaultApplication implements Application {
    private static final String DEFAULT_APPLICATION_IMAGE_KEY = "applicationInfo.image";

    @Autowired
    protected ApplicationConfig applicationConfig;

    @Autowired
    protected ApplicationLifecycleAdvisor lifecycleAdvisor;

    @Autowired
    protected ApplicationWindowFactory applicationWindowFactory;

    @Autowired
    protected ApplicationPageFactory applicationPageFactory;

    @Autowired
    protected ApplicationDescriptor applicationDescriptor;

    boolean forceShutdown;

    @Override
    public void start() {
        lifecycleAdvisor.onPreStartup();
        openWindow(lifecycleAdvisor.getStartingPageId());
        lifecycleAdvisor.onPostStartup();
    }

    protected void openWindow(String startingPageId) {
        ApplicationWindow window = initWindow(createNewWindow());
        if(startingPageId == null) {
            window.showPage(applicationPageFactory.createApplicationPage(window, new MultiViewPageDescriptor()));
        }
    }

    protected ApplicationWindow initWindow(ApplicationWindow newWindow) {
         return newWindow;
    }

    protected ApplicationWindow createNewWindow() {
        return applicationWindowFactory.createApplicationWindow();
    }

     @Override
     public String getName() {
        if( applicationDescriptor != null && StringUtils.hasText(applicationDescriptor.getDisplayName()) )
            return applicationDescriptor.getDisplayName();

        return "Valkyrie RCP Application";
    }

    @Override
    public Image getImage() {
        if( applicationDescriptor != null && applicationDescriptor.getImage() != null )
            return applicationDescriptor.getImage();

        try {
        	ImageSource isrc = applicationConfig.imageSource();
        	return isrc.getImage(DEFAULT_APPLICATION_IMAGE_KEY);
        }
        catch (NoSuchImageResourceException e) {
        	return null;
        }
    }

     public void close() {
        close(false, 0);
    }

    public boolean isForceShutdown()
    {
        return forceShutdown;
    }


    public void close(boolean force, int exitCode) {
        forceShutdown = force;
        try {
            if (applicationConfig.windowManager().close() ) {
                forceShutdown = true;
                if( applicationConfig.applicationContext() instanceof ConfigurableApplicationContext) {
                    ((ConfigurableApplicationContext) applicationConfig.applicationContext()).close();
                }
                applicationConfig.applicationLifecycleAdvisor().onShutdown();
            }
        } finally {
            if (isForceShutdown()) {
                System.exit(exitCode);
            }
        }
    }
}
