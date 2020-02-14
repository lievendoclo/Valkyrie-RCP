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
package org.valkyriercp.sample.showcase;

import com.jgoodies.looks.plastic.PlasticXPLookAndFeel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.valkyriercp.application.ApplicationPageFactory;
import org.valkyriercp.application.config.ApplicationLifecycleAdvisor;
import org.valkyriercp.application.config.support.AbstractApplicationConfig;
import org.valkyriercp.application.config.support.UIManagerConfigurer;
import org.valkyriercp.application.session.ApplicationSessionInitializer;
import org.valkyriercp.application.support.JideTabbedApplicationPageFactory;
import org.valkyriercp.application.support.SingleViewPageDescriptor;
import org.valkyriercp.security.LoginCommand;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Configuration
@Import({ShowcaseViews.class, ShowcaseBinders.class})
public class ShowcaseApplicationConfig extends AbstractApplicationConfig {
    @Autowired
    private ShowcaseViews views;

    @Autowired
    private ShowcaseBinders binders;

    public ShowcaseViews getViews() {
        return views;
    }

    public ShowcaseBinders getBinders() {
        return binders;
    }

    @Override
    public Class<?> getCommandConfigClass() {
        return ShowcaseCommandConfig.class;
    }

    @Override
    public ApplicationLifecycleAdvisor applicationLifecycleAdvisor() {
        ApplicationLifecycleAdvisor lifecycleAdvisor = super.applicationLifecycleAdvisor();
        lifecycleAdvisor.setStartingPageDescriptor(new SingleViewPageDescriptor(getViews().startView()));
        return lifecycleAdvisor;
    }

    @Override
    public ApplicationPageFactory applicationPageFactory() {
        JideTabbedApplicationPageFactory jideTabbedApplicationPageFactory = new JideTabbedApplicationPageFactory();
        jideTabbedApplicationPageFactory.setShowCloseButton(true);
        return jideTabbedApplicationPageFactory;
    }

    @Override
    public ApplicationSessionInitializer applicationSessionInitializer() {
        ApplicationSessionInitializer initializer = new ApplicationSessionInitializer();
        initializer.setPreStartupCommandIds(LoginCommand.ID);
        return initializer;
    }

    @Bean
    public UIManagerConfigurer uiManagerConfigurer() {
        UIManagerConfigurer configurer = new UIManagerConfigurer();
        configurer.setLookAndFeel(PlasticXPLookAndFeel.class);
        return configurer;
    }

    @Override
    public List<String> getResourceBundleLocations() {
        List<String> resourceBundleLocations = super.getResourceBundleLocations();
        resourceBundleLocations.add("org.valkyriercp.sample.showcase.messages");
        return resourceBundleLocations;
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

    @Override
    protected ResourceBundleMessageSource createMessageSourceImpl() {
        return new ResourceBundleMessageSource();
    }
}
