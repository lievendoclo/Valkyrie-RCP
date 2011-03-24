package org.valkyriercp.application.config.support;

import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.binding.convert.ConversionService;
import org.springframework.binding.convert.converters.PropertyEditorConverter;
import org.springframework.binding.convert.service.DefaultConversionService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.core.io.Resource;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.vote.AffirmativeBased;
import org.springframework.security.access.vote.RoleVoter;
import org.valkyriercp.application.*;
import org.valkyriercp.application.config.ApplicationConfig;
import org.valkyriercp.application.config.ApplicationLifecycleAdvisor;
import org.valkyriercp.application.config.ApplicationMode;
import org.valkyriercp.application.config.ApplicationObjectConfigurer;
import org.valkyriercp.application.exceptionhandling.DelegatingExceptionHandler;
import org.valkyriercp.application.exceptionhandling.JXErrorDialogExceptionHandler;
import org.valkyriercp.application.exceptionhandling.RegisterableExceptionHandler;
import org.valkyriercp.application.exceptionhandling.SimpleExceptionHandlerDelegate;
import org.valkyriercp.application.session.ApplicationSession;
import org.valkyriercp.application.session.ApplicationSessionInitializer;
import org.valkyriercp.application.support.*;
import org.valkyriercp.binding.form.BindingErrorMessageProvider;
import org.valkyriercp.binding.form.FieldFaceSource;
import org.valkyriercp.binding.form.support.DefaultBindingErrorMessageProvider;
import org.valkyriercp.binding.form.support.MessageSourceFieldFaceSource;
import org.valkyriercp.binding.value.ValueChangeDetector;
import org.valkyriercp.binding.value.support.DefaultValueChangeDetector;
import org.valkyriercp.command.CommandConfigurer;
import org.valkyriercp.command.CommandManager;
import org.valkyriercp.command.CommandRegistry;
import org.valkyriercp.command.CommandServices;
import org.valkyriercp.command.config.DefaultCommandConfig;
import org.valkyriercp.command.config.DefaultCommandConfigurer;
import org.valkyriercp.command.support.DefaultCommandManager;
import org.valkyriercp.command.support.DefaultCommandRegistry;
import org.valkyriercp.command.support.DefaultCommandServices;
import org.valkyriercp.component.DefaultOverlayService;
import org.valkyriercp.component.OverlayService;
import org.valkyriercp.convert.support.CollectionToListModelConverter;
import org.valkyriercp.convert.support.ListToListModelConverter;
import org.valkyriercp.factory.*;
import org.valkyriercp.form.binding.Binder;
import org.valkyriercp.form.binding.BinderSelectionStrategy;
import org.valkyriercp.form.binding.BindingFactoryProvider;
import org.valkyriercp.form.binding.config.DefaultBinderConfig;
import org.valkyriercp.form.binding.swing.*;
import org.valkyriercp.form.binding.swing.date.JXDatePickerDateFieldBinder;
import org.valkyriercp.form.builder.*;
import org.valkyriercp.image.DefaultIconSource;
import org.valkyriercp.image.DefaultImageSource;
import org.valkyriercp.image.IconSource;
import org.valkyriercp.image.ImageSource;
import org.valkyriercp.rules.RulesSource;
import org.valkyriercp.rules.reporting.DefaultMessageTranslatorFactory;
import org.valkyriercp.rules.reporting.MessageTranslatorFactory;
import org.valkyriercp.rules.support.DefaultRulesSource;
import org.valkyriercp.security.ApplicationSecurityManager;
import org.valkyriercp.security.SecurityAwareConfigurer;
import org.valkyriercp.security.SecurityController;
import org.valkyriercp.security.SecurityControllerManager;
import org.valkyriercp.security.support.AuthorityConfigurableSecurityController;
import org.valkyriercp.security.support.DefaultApplicationSecurityManager;
import org.valkyriercp.security.support.DefaultSecurityControllerManager;
import org.valkyriercp.util.DialogFactory;
import org.valkyriercp.widget.Widget;
import org.valkyriercp.widget.WidgetViewDescriptor;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
@Lazy
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
        advisor.setStartingPageDescriptor(new SingleViewPageDescriptor(emptyViewDescriptor()));
        return advisor;
    }

    @Bean
    public ViewDescriptor emptyViewDescriptor() {
        return new WidgetViewDescriptor("empty", Widget.EMPTY_WIDGET);
    }

    @Bean
    public ApplicationDescriptor applicationDescriptor() {
        DefaultApplicationDescriptor defaultApplicationDescriptor = new DefaultApplicationDescriptor();
        applicationObjectConfigurer().configure(defaultApplicationDescriptor, "applicationDescriptor");
        return defaultApplicationDescriptor;
    }

    @Bean
    public ImageSource imageSource() {
        PropertiesFactoryBean propertiesFactoryBean = new PropertiesFactoryBean();
        propertiesFactoryBean.setLocations(getImageSourceResources().values().toArray(new Resource[getImageSourceResources().size()]));
        DefaultImageSource imageSource = null;
        try {
            propertiesFactoryBean.afterPropertiesSet();
            imageSource = new DefaultImageSource(propertiesFactoryBean.getObject());
        } catch (IOException e) {
            throw new IllegalArgumentException("Error getting imagesource property file", e);
        }
        imageSource.setBrokenImageIndicator(applicationContext().getResource("classpath:/com/famfamfam/silk/error.png"));
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
        return new SimplePageComponentPaneFactory();
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
        DefaultSecurityControllerManager defaultSecurityControllerManager = new DefaultSecurityControllerManager();
        defaultSecurityControllerManager.setFallbackSecurityController(authorityConfigurableSecurityController());
        return defaultSecurityControllerManager;
    }

    @Bean
    public SecurityController authorityConfigurableSecurityController() {
        AuthorityConfigurableSecurityController authorityConfigurableSecurityController = new AuthorityConfigurableSecurityController();
        AffirmativeBased accessDecisionManager = new AffirmativeBased();
        RoleVoter roleVoter = new RoleVoter();
        roleVoter.setRolePrefix("");
        accessDecisionManager.setDecisionVoters(Lists.<AccessDecisionVoter>newArrayList(roleVoter));
        authorityConfigurableSecurityController.setAccessDecisionManager(accessDecisionManager);
        return authorityConfigurableSecurityController;
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

    @Bean
    public MessageResolver messageResolver() {
        return new MessageResolver();
    }

    @Bean
    public CommandManager commandManager() {
        return new DefaultCommandManager();
    }

    @Bean
    public ValueChangeDetector valueChangeDetector() {
        return new DefaultValueChangeDetector();
    }

    @Bean
    public MessageTranslatorFactory messageTranslatorFactory() {
        return new DefaultMessageTranslatorFactory();
    }

    @Bean
    public RulesSource rulesSource() {
        return new DefaultRulesSource();
    }

    @Bean
    public FieldFaceSource fieldFaceSource() {
        return new MessageSourceFieldFaceSource();
    }

    @Bean
    public ConversionService conversionService() {
        DefaultConversionService conversionService =  new DefaultConversionService();
        conversionService.addConverter(new ListToListModelConverter());
        conversionService.addConverter(new CollectionToListModelConverter());
        return conversionService;
    }

    @Bean
    public FormComponentInterceptorFactory formComponentInterceptorFactory() {
        ChainedInterceptorFactory factory = new ChainedInterceptorFactory();
        List<FormComponentInterceptorFactory> factories = Lists.newArrayList();
        factories.add(new ColorValidationInterceptorFactory());
        factories.add(new OverlayValidationInterceptorFactory());
        factories.add(new ShowCaptionInStatusBarInterceptorFactory());
        factories.add(new ShowDescriptionInStatusBarInterceptorFactory());
        factory.setInterceptorFactories(factories);
        return factory;
    }

    public DefaultBinderConfig defaultBinderConfig() {
        return new DefaultBinderConfig();
    }

    @Bean
    public BinderSelectionStrategy binderSelectionStrategy() {
        SwingBinderSelectionStrategy swingBinderSelectionStrategy = new SwingBinderSelectionStrategy();
        registerBinders(swingBinderSelectionStrategy);
        return swingBinderSelectionStrategy;
    }

    protected void registerBinders(BinderSelectionStrategy binderSelectionStrategy) {
        binderSelectionStrategy.registerBinderForControlType(JTextComponent.class, textComponentBinder());
        binderSelectionStrategy.registerBinderForControlType(JFormattedTextField.class, formattedTextFieldBinder());
        binderSelectionStrategy.registerBinderForControlType(JTextArea.class, textAreaBinder());
        binderSelectionStrategy.registerBinderForControlType(JToggleButton.class, toggleButtonBinder());
        binderSelectionStrategy.registerBinderForControlType(JCheckBox.class, checkBoxBinder());
        binderSelectionStrategy.registerBinderForControlType(JComboBox.class, comboBoxBinder());
        binderSelectionStrategy.registerBinderForControlType(JList.class, listBinder());
        binderSelectionStrategy.registerBinderForControlType(JLabel.class, labelBinder());
        binderSelectionStrategy.registerBinderForControlType(JScrollPane.class, new ScrollPaneBinder(binderSelectionStrategy, JTextArea.class));
        binderSelectionStrategy.registerBinderForPropertyType(String.class, textComponentBinder());
        binderSelectionStrategy.registerBinderForPropertyType(boolean.class, checkBoxBinder());
        binderSelectionStrategy.registerBinderForPropertyType(Boolean.class, checkBoxBinder());
        binderSelectionStrategy.registerBinderForPropertyType(Enum.class, enumComboBoxBinder());
    }

    @Bean
    public BindingFactoryProvider bindingFactoryProvider() {
        return new SwingBindingFactoryProvider();
    }

    @Bean
    public BindingErrorMessageProvider bindingErrorMessageProvider() {
        return new DefaultBindingErrorMessageProvider();
    }

    @Bean
    public DialogFactory dialogFactory() {
        return new DialogFactory();
    }

    @Bean
    public OverlayService overlayService() {
        return new DefaultOverlayService();
    }

    @Bean
    public TableFactory tableFactory() {
        return new DefaultTableFactory();
    }

    @Bean
    public ApplicationSecurityManager applicationSecurityManager() {
        return new DefaultApplicationSecurityManager(false);
    }

    // Binders

    @Bean
    public Binder jxDatePickerDateFieldBinder() {
        return new JXDatePickerDateFieldBinder();
    }

    @Bean
    public Binder checkBoxBinder() {
        return new CheckBoxBinder();
    }

    @Bean
    public Binder formattedTextFieldBinder() {
        return new FormattedTextFieldBinder(null);
    }

    @Bean
    public Binder formattedTextFieldStringBinder() {
        return new FormattedTextFieldBinder(String.class);
    }

    @Bean
    public Binder comboBoxBinder() {
        return new ComboBoxBinder();
    }

    @Bean
    public Binder enumComboBoxBinder() {
        return new EnumComboBoxBinder();
    }

    @Bean
    public Binder labelBinder() {
        return new LabelBinder();
    }

    @Bean
    public Binder numberBinder() {
        return new NumberBinder();
    }

    @Bean
    public Binder textComponentBinder() {
        return new TextComponentBinder();
    }

    @Bean
    public Binder listBinder() {
        return new ListBinder();
    }

    @Bean
    public Binder textAreaBinder() {
        return new TextAreaBinder();
    }

    @Bean
    public Binder toggleButtonBinder() {
        return new ToggleButtonBinder();
    }

    @Override
    public ApplicationMode getApplicationMode() {
        return ApplicationMode.PRODUCTION;
    }

    @Bean
    public SecurityAwareConfigurer securityAwareConfigurer() {
         return new SecurityAwareConfigurer();
    }
}
