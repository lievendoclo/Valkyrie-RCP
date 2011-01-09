package org.valkyriercp.application.config.support;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.context.support.StaticMessageSource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.valkyriercp.application.*;
import org.valkyriercp.application.config.ApplicationConfig;
import org.valkyriercp.application.config.ApplicationLifecycleAdvisor;
import org.valkyriercp.application.config.ApplicationObjectConfigurer;
import org.valkyriercp.application.exceptionhandling.*;
import org.valkyriercp.application.session.ApplicationSession;
import org.valkyriercp.application.session.ApplicationSessionInitializer;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        DefaultApplicationDescriptor defaultApplicationDescriptor = new DefaultApplicationDescriptor();
        applicationObjectConfigurer().configure(defaultApplicationDescriptor, "applicationDescriptor");
        return defaultApplicationDescriptor;
    }

    @Bean
    public ImageSource imageSource() {
        DefaultImageSource imageSource = new DefaultImageSource(getImageSourceResources());
        imageSource.setBrokenImageIndicator(applicationContext().getResource("classpath:/org/valkyriercp/images/alert/error_obj.gif"));
        return imageSource;
    }

    public Map<String, Resource> getImageSourceResources() {
        Map<String, Resource> resources = new HashMap<String, Resource>();
        resources.put("default", applicationContext().getResource("classpath:/org/valkyriercp/images/images.properties"));
        return resources;
    }

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
    public MessageSource messageSource() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasenames(getResourceBundleLocations().toArray(new String[getResourceBundleLocations().size()]));
        return messageSource;
    }

    public List<String> getResourceBundleLocations() {
        ArrayList<String> list =  new ArrayList<String>();
        list.add("org.valkyriercp.messages.default");
        return list;
    }

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

    @Bean
    public RegisterableExceptionHandler registerableExceptionHandler() {
        JXErrorDialogExceptionHandler errorDialogExceptionHandler = new JXErrorDialogExceptionHandler();
        DelegatingExceptionHandler handler = new DelegatingExceptionHandler();
        handler.getDelegateList().add(new SimpleExceptionHandlerDelegate(Throwable.class, errorDialogExceptionHandler));
        return handler;
    }

    @Bean
    public ApplicationSession applicationSession() {
        return new ApplicationSession();
    }

    @Bean
    public ApplicationSessionInitializer applicationSessionInitializer() {
        return new ApplicationSessionInitializer();
    }
}
