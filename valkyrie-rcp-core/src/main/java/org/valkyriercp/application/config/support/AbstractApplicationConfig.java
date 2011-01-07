package org.valkyriercp.application.config.support;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.context.support.StaticMessageSource;
import org.springframework.stereotype.Component;
import org.valkyriercp.application.*;
import org.valkyriercp.application.config.ApplicationConfig;
import org.valkyriercp.application.config.ApplicationLifecycleAdvisor;
import org.valkyriercp.application.config.ApplicationObjectConfigurer;
import org.valkyriercp.application.support.*;
import org.valkyriercp.command.CommandConfigurer;
import org.valkyriercp.command.CommandRegistry;
import org.valkyriercp.command.CommandServices;
import org.valkyriercp.command.config.DefaultCommandConfig;
import org.valkyriercp.command.config.DefaultCommandConfigurer;
import org.valkyriercp.command.support.DefaultCommandRegistry;
import org.valkyriercp.command.support.DefaultCommandServices;
import org.valkyriercp.factory.*;
import org.valkyriercp.image.DefaultIconSource;
import org.valkyriercp.image.DefaultImageSource;
import org.valkyriercp.image.IconSource;
import org.valkyriercp.image.ImageSource;
import org.valkyriercp.security.SecurityControllerManager;
import org.valkyriercp.security.support.DefaultSecurityControllerManager;

public abstract class AbstractApplicationConfig implements ApplicationConfig {
    @Autowired
    private ApplicationContext applicationContext;

    public ApplicationContext applicationContext() {
        return applicationContext;
    }

    @Bean
    public Application application() {
        return new DefaultApplication();
    }

    @Bean
    public ApplicationPageFactory applicationPageFactory() {
        return new DefaultApplicationPageFactory();
    }

    @Bean
    public ApplicationWindowFactory applicationWindowFactory() {
        return new DefaultApplicationWindowFactory();
    }

    @Bean
    public ApplicationLifecycleAdvisor applicationLifecycleAdvisor() {
        DefaultApplicationLifecycleAdvisor advisor = new DefaultApplicationLifecycleAdvisor();
        advisor.setCommandConfigClass(getCommandConfigClass());
        return advisor;
    }

    @Bean
    public ApplicationDescriptor applicationDescriptor() {
        return new DefaultApplicationDescriptor();
    }

    @Bean
    public abstract ImageSource imageSource();

    @Bean
    public WindowManager windowManager() {
        return new WindowManager();
    }

    @Bean
    public CommandServices commandServices() {
        return new DefaultCommandServices();
    }

    @Bean
    public CommandConfigurer commandConfigurer() {
        return new DefaultCommandConfigurer();
    }

    @Bean
    public CommandRegistry commandRegistry() {
        return new DefaultCommandRegistry();
    }

    @Bean
    public PageComponentPaneFactory pageComponentPaneFactory() {
        return new DefaultPageComponentPaneFactory();
    }

    @Bean
    public ViewDescriptorRegistry viewDescriptorRegistry() {
        return new BeanFactoryViewDescriptorRegistry();
    }

    @Bean
    public IconSource iconSource() {
        return new DefaultIconSource();
    }

    @Bean
    public ComponentFactory componentFactory() {
        return new DefaultComponentFactory();
    }

    @Bean
    public ButtonFactory buttonFactory() {
        return new DefaultButtonFactory();
    }

    @Bean
    public MenuFactory menuFactory() {
        return new DefaultMenuFactory();
    }

    @Bean
    public ButtonFactory toolbarButtonFactory() {
        return new DefaultButtonFactory();
    }

    @Bean
    public MessageSourceAccessor messageSourceAccessor() {
        return new MessageSourceAccessor(messageSource());
    }

    @Bean
    public abstract MessageSource messageSource();

    @Bean
    public ApplicationObjectConfigurer applicationObjectConfigurer() {
        return new DefaultApplicationObjectConfigurer();
    }

    @Bean
    public SecurityControllerManager securityControllerManager() {
        return new DefaultSecurityControllerManager();
    }

    public Class<?> getCommandConfigClass() {
        return DefaultCommandConfig.class;
    }
}
