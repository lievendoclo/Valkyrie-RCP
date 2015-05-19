/**
 * Copyright (C) 2015 Valkyrie RCP
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.valkyriercp.sample.vldocking;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.jgoodies.looks.plastic.PlasticXPLookAndFeel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
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
import org.valkyriercp.sample.vldocking.domain.ContactDataStore;
import org.valkyriercp.sample.vldocking.domain.SimpleValidationRulesSource;
import org.valkyriercp.sample.vldocking.ui.ContactView;
import org.valkyriercp.sample.vldocking.ui.InitialView;

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

    @Bean
    public AuthenticationManager authenticationManager() {
        List<UserDetails> userDetailsList = Lists.newArrayList();
        userDetailsList.add(new User("admin", "admin", Lists.newArrayList(new SimpleGrantedAuthority("ADMIN"))));
        userDetailsList.add(new User("user", "user", Lists.newArrayList(new SimpleGrantedAuthority("READ"))));
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(new InMemoryUserDetailsManager(userDetailsList));
        return new ProviderManager(Lists.<AuthenticationProvider>newArrayList(provider));
    }
}
