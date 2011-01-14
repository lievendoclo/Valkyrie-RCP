package org.valkyriercp.application.config;

import org.valkyriercp.application.ApplicationWindow;
import org.valkyriercp.application.ApplicationWindowConfigurer;
import org.valkyriercp.application.PageDescriptor;
import org.valkyriercp.application.StatusBar;
import org.valkyriercp.application.exceptionhandling.RegisterableExceptionHandler;
import org.valkyriercp.application.session.ApplicationSessionInitializer;
import org.valkyriercp.application.support.ApplicationWindowCommandManager;
import org.valkyriercp.command.support.CommandGroup;

public interface ApplicationLifecycleAdvisor {
    PageDescriptor getStartingPageDescriptor();

    void setStartingPageDescriptor(PageDescriptor descriptor);

    void onPreStartup();

    void onPostStartup();

    void onWindowCreated(ApplicationWindow window);

    void onWindowOpened(ApplicationWindow window);

    boolean onPreWindowClose(ApplicationWindow window);

    void onCommandsCreated(ApplicationWindow window);

    void onPreWindowOpen(ApplicationWindowConfigurer configurer);

    void setOpeningWindow(ApplicationWindow window);

    ApplicationWindowCommandManager createWindowCommandManager();

    CommandGroup getMenuBarCommandGroup();

    CommandGroup getToolBarCommandGroup();

    StatusBar getStatusBar();

    void onShutdown();

    ApplicationSessionInitializer getApplicationSessionInitializer();

    RegisterableExceptionHandler getRegisterableExceptionHandler();
}
