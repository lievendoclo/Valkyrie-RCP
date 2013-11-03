package org.valkyriercp.sample.simple;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.jgoodies.looks.plastic.PlasticXPLookAndFeel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.valkyriercp.application.ApplicationPageFactory;
import org.valkyriercp.application.ViewDescriptor;
import org.valkyriercp.application.config.ApplicationLifecycleAdvisor;
import org.valkyriercp.application.config.support.AbstractApplicationConfig;
import org.valkyriercp.application.config.support.UIManagerConfigurer;
import org.valkyriercp.application.docking.VLDockingApplicationPageFactory;
import org.valkyriercp.application.docking.VLDockingPageDescriptor;
import org.valkyriercp.application.docking.VLDockingViewDescriptor;
import org.valkyriercp.application.session.ApplicationSessionInitializer;
import org.valkyriercp.rules.RulesSource;
import org.valkyriercp.sample.simple.domain.ContactDataStore;
import org.valkyriercp.sample.simple.domain.SimpleValidationRulesSource;
import org.valkyriercp.sample.simple.ui.ContactView;
import org.valkyriercp.sample.simple.ui.InitialView;

import java.util.List;
import java.util.Map;

@Configuration
public class VLDockingSampleApplicationConfig extends AbstractApplicationConfig {

    @Override
    public ApplicationLifecycleAdvisor applicationLifecycleAdvisor() {
        ApplicationLifecycleAdvisor lifecycleAdvisor =  super.applicationLifecycleAdvisor();
        VLDockingPageDescriptor descriptor = new VLDockingPageDescriptor();
        descriptor.getViewDescriptors().add(initialView());
        lifecycleAdvisor.setStartingPageDescriptor(descriptor);
        return lifecycleAdvisor;
    }

    @Override
    public ApplicationPageFactory applicationPageFactory() {
        return new VLDockingApplicationPageFactory();
    }

    @Override
    public ApplicationSessionInitializer applicationSessionInitializer() {
        ApplicationSessionInitializer initializer = new ApplicationSessionInitializer();
        initializer.setPreStartupCommands(Lists.newArrayList("loginCommand"));
        return initializer;
    }

    @Override
    public List<String> getResourceBundleLocations() {
        List<String> list = super.getResourceBundleLocations();
        list.add("org.valkyriercp.sample.vldocking.vldocking");
        return list;
    }

    public Map<String, Resource> getImageSourceResources() {
        Map<String, Resource> resources = super.getImageSourceResources();
        resources.put("simple", applicationContext().getResource("classpath:/org/valkyriercp/sample/vldocking/images.properties"));
        return resources;
    }

    @Override
    public Class<?> getCommandConfigClass() {
        return VLDockingSampleCommandConfig.class;
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
        VLDockingViewDescriptor initialView = new VLDockingViewDescriptor("initialView", InitialView.class);
        initialView.setFloatEnabled(true);
        initialView.setAutoHideEnabled(true);
        initialView.setCloseEnabled(true);
        Map<String,Object> viewProperties = Maps.newHashMap();
        viewProperties.put("firstMessage", "firstMessage.text");
        viewProperties.put("descriptionTextPath", "org/valkyriercp/sample/vldocking/ui/initialViewText.html");
        initialView.setViewProperties(viewProperties);
        return initialView;
    }

    @Bean
    public ViewDescriptor contactView() {
        VLDockingViewDescriptor contactView = new VLDockingViewDescriptor("contactView", ContactView.class);
        contactView.setFloatEnabled(true);
        contactView.setAutoHideEnabled(true);
        contactView.setCloseEnabled(true);
        Map<String,Object> viewProperties = Maps.newHashMap();
        viewProperties.put("contactDataStore", new ContactDataStore());
        contactView.setViewProperties(viewProperties);
        return contactView;
    }
}
