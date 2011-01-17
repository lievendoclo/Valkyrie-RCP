package org.valkyriercp.sample.simple;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.jgoodies.looks.plastic.PlasticXPLookAndFeel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.Resource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.memory.InMemoryDaoImpl;
import org.springframework.security.core.userdetails.memory.UserMap;
import org.valkyriercp.application.ViewDescriptor;
import org.valkyriercp.application.config.ApplicationLifecycleAdvisor;
import org.valkyriercp.application.config.ApplicationMode;
import org.valkyriercp.application.config.support.AbstractApplicationConfig;
import org.valkyriercp.application.config.support.UIManagerConfigurer;
import org.valkyriercp.application.session.ApplicationSessionInitializer;
import org.valkyriercp.application.support.DefaultViewDescriptor;
import org.valkyriercp.application.support.SingleViewPageDescriptor;
import org.valkyriercp.component.DefaultOverlayService;
import org.valkyriercp.component.JideOssComponentFactory;
import org.valkyriercp.component.JideOverlayService;
import org.valkyriercp.component.OverlayService;
import org.valkyriercp.factory.ComponentFactory;
import org.valkyriercp.form.binding.BinderSelectionStrategy;
import org.valkyriercp.form.binding.BindingFactoryProvider;
import org.valkyriercp.form.binding.config.DefaultBinderConfig;
import org.valkyriercp.form.binding.jide.JideBindingFactoryProvider;
import org.valkyriercp.form.binding.swing.SwingBinderSelectionStrategy;
import org.valkyriercp.form.binding.swing.date.JXDatePickerDateFieldBinder;
import org.valkyriercp.form.builder.*;
import org.valkyriercp.rules.RulesSource;
import org.valkyriercp.sample.simple.domain.ContactDataStore;
import org.valkyriercp.sample.simple.domain.SimpleValidationRulesSource;
import org.valkyriercp.sample.simple.ui.ContactView;
import org.valkyriercp.sample.simple.ui.InitialView;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
public class SimpleSampleApplicationConfig extends AbstractApplicationConfig {

    @Override
    public ApplicationLifecycleAdvisor applicationLifecycleAdvisor() {
        ApplicationLifecycleAdvisor lifecycleAdvisor =  super.applicationLifecycleAdvisor();
        lifecycleAdvisor.setStartingPageDescriptor(new SingleViewPageDescriptor(initialView()));
        return lifecycleAdvisor;
    }

//    @Override
//    public ApplicationSessionInitializer applicationSessionInitializer() {
//        ApplicationSessionInitializer initializer = new ApplicationSessionInitializer();
//        initializer.setPreStartupCommands(Lists.newArrayList("loginCommand"));
//        return initializer;
//    }

    @Override
    public List<String> getResourceBundleLocations() {
        List<String> list = super.getResourceBundleLocations();
        list.add("org.valkyriercp.sample.simple.simple");
        return list;
    }

    public Map<String, Resource> getImageSourceResources() {
        Map<String, Resource> resources = super.getImageSourceResources();
        resources.put("simple", applicationContext().getResource("classpath:/org/valkyriercp/sample/simple/images.properties"));
        return resources;
    }

    @Override
    public Class<?> getCommandConfigClass() {
        return SimpleSampleCommandConfig.class;
    }

    @Override
    public RulesSource rulesSource() {
        return new SimpleValidationRulesSource();
    }

    @Bean
    public UIManagerConfigurer uiManagerConfigurer() {
        UIManagerConfigurer configurer = new UIManagerConfigurer();
        configurer.setLookAndFeel(PlasticXPLookAndFeel.class);
        return configurer;
    }

    @Bean
    public ViewDescriptor initialView() {
        DefaultViewDescriptor initialView = new DefaultViewDescriptor("initialView", InitialView.class);
        Map<String,Object> viewProperties = Maps.newHashMap();
        viewProperties.put("firstMessage", "firstMessage.text");
        viewProperties.put("descriptionTextPath", "org/valkyriercp/sample/simple/ui/initialViewText.html");
        initialView.setViewProperties(viewProperties);
        return initialView;
    }

    @Bean
    public ViewDescriptor contactView() {
        DefaultViewDescriptor initialView = new DefaultViewDescriptor("contactView", ContactView.class);
        Map<String,Object> viewProperties = Maps.newHashMap();
        viewProperties.put("contactDataStore", new ContactDataStore());
        initialView.setViewProperties(viewProperties);
        return initialView;
    }
}
