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
package org.valkyriercp.sample.dockingframes;

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
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.valkyriercp.application.ApplicationPageFactory;
import org.valkyriercp.application.PageDescriptor;
import org.valkyriercp.application.ViewDescriptor;
import org.valkyriercp.application.config.ApplicationLifecycleAdvisor;
import org.valkyriercp.application.config.support.AbstractApplicationConfig;
import org.valkyriercp.application.config.support.UIManagerConfigurer;
import org.valkyriercp.application.docking.DockingFramesApplicationPageFactory;
import org.valkyriercp.application.session.ApplicationSessionInitializer;
import org.valkyriercp.application.support.DefaultViewDescriptor;
import org.valkyriercp.application.support.SingleViewPageDescriptor;
import org.valkyriercp.rules.RulesSource;
import org.valkyriercp.sample.dockingframes.domain.ContactDataStore;
import org.valkyriercp.sample.dockingframes.domain.SimpleValidationRulesSource;
import org.valkyriercp.sample.dockingframes.ui.ContactView;
import org.valkyriercp.sample.dockingframes.ui.InitialView;
import org.valkyriercp.security.LoginCommand;

import java.util.*;

@Configuration
public class DockingFramesSampleApplicationConfig extends AbstractApplicationConfig {

    @Override
    public ApplicationLifecycleAdvisor applicationLifecycleAdvisor() {
        ApplicationLifecycleAdvisor lifecycleAdvisor =  super.applicationLifecycleAdvisor();
        PageDescriptor descriptor = new SingleViewPageDescriptor(initialView());
        lifecycleAdvisor.setStartingPageDescriptor(() -> descriptor);
        return lifecycleAdvisor;
    }

    @Override
    public ApplicationPageFactory applicationPageFactory() {
        return new DockingFramesApplicationPageFactory();
    }

    @Override
    public ApplicationSessionInitializer applicationSessionInitializer() {
        ApplicationSessionInitializer initializer = new ApplicationSessionInitializer();
        initializer.setPreStartupCommandIds(LoginCommand.ID);
        return initializer;
    }

    @Override
    public List<String> getResourceBundleLocations() {
        List<String> list = super.getResourceBundleLocations();
        list.add("org.valkyriercp.sample.dockingframes.messages");
        return list;
    }

    public Map<String, Resource> getImageSourceResources() {
        Map<String, Resource> resources = super.getImageSourceResources();
        resources.put("simple", applicationContext().getResource("classpath:/org/valkyriercp/sample/dockingframes/images.properties"));
        return resources;
    }

    @Override
    public Class<?> getCommandConfigClass() {
        return DockingFramesSampleCommandConfig.class;
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
        Map<String,Object> viewProperties = new HashMap<>();
        viewProperties.put("firstMessage", "firstMessage.text");
        viewProperties.put("descriptionTextPath", "org/valkyriercp/sample/dockingframes/ui/initialViewText.html");
        initialView.setViewProperties(viewProperties);
        return initialView;
    }

    @Bean
    public ViewDescriptor contactView() {
        DefaultViewDescriptor contactView = new DefaultViewDescriptor("contactView", ContactView.class);
        Map<String,Object> viewProperties = new HashMap<>();
        viewProperties.put("contactDataStore", new ContactDataStore());
        contactView.setViewProperties(viewProperties);
        return contactView;
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        List<UserDetails> userDetailsList = new ArrayList<>();
        PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        userDetailsList.add(User.builder().username("admin").password(encoder.encode("admin")).roles("ADMIN").build());
        userDetailsList.add(User.builder().username("user").password(encoder.encode("user")).roles("READ").build());
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(new InMemoryUserDetailsManager(userDetailsList));
        return new ProviderManager(Collections.singletonList(provider));
    }
}
