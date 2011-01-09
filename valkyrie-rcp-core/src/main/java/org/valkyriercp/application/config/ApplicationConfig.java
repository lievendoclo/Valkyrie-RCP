package org.valkyriercp.application.config;

import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.MessageSourceAccessor;
import org.valkyriercp.application.*;
import org.valkyriercp.application.exceptionhandling.RegisterableExceptionHandler;
import org.valkyriercp.application.session.ApplicationSession;
import org.valkyriercp.application.session.ApplicationSessionInitializer;
import org.valkyriercp.application.support.MessageResolver;
import org.valkyriercp.command.CommandConfigurer;
import org.valkyriercp.command.CommandManager;
import org.valkyriercp.command.CommandServices;
import org.valkyriercp.factory.ButtonFactory;
import org.valkyriercp.factory.ComponentFactory;
import org.valkyriercp.factory.MenuFactory;
import org.valkyriercp.image.IconSource;
import org.valkyriercp.image.ImageSource;
import org.valkyriercp.security.SecurityControllerManager;

@Configuration
public interface ApplicationConfig {
    ApplicationContext applicationContext();
    Application application();
    ApplicationPageFactory applicationPageFactory();
    ApplicationWindowFactory applicationWindowFactory();
    ApplicationLifecycleAdvisor applicationLifecycleAdvisor();
    ApplicationDescriptor applicationDescriptor();
    ImageSource imageSource();
    WindowManager windowManager();
    CommandServices commandServices();
    CommandConfigurer commandConfigurer();
    PageComponentPaneFactory pageComponentPaneFactory();
    ViewDescriptorRegistry viewDescriptorRegistry();
    IconSource iconSource();
    ComponentFactory componentFactory();
    ButtonFactory buttonFactory();
    MenuFactory menuFactory();
    ButtonFactory toolbarButtonFactory();
    MessageSourceAccessor messageSourceAccessor();
    MessageSource messageSource();
    ApplicationObjectConfigurer applicationObjectConfigurer();
    SecurityControllerManager securityControllerManager();
    RegisterableExceptionHandler registerableExceptionHandler();
    ApplicationSession applicationSession();
    ApplicationSessionInitializer applicationSessionInitializer();
    MessageResolver messageResolver();
    CommandManager commandManager();
}