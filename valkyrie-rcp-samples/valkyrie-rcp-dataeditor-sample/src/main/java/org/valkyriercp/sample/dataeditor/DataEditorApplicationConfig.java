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
package org.valkyriercp.sample.dataeditor;

import com.jgoodies.looks.plastic.PlasticXPLookAndFeel;
import com.jidesoft.swing.JideTabbedPane;
import jiconfont.IconFont;
import jiconfont.swing.IconFontSwing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.valkyriercp.application.ApplicationPageFactory;
import org.valkyriercp.application.ApplicationWindowFactory;
import org.valkyriercp.application.config.ApplicationLifecycleAdvisor;
import org.valkyriercp.application.config.support.AbstractApplicationConfig;
import org.valkyriercp.application.config.support.UIManagerConfigurer;
import org.valkyriercp.application.session.ApplicationSessionInitializer;
import org.valkyriercp.application.support.JXTaskPaneNavigatorApplicationWindowFactory;
import org.valkyriercp.application.support.JideTabbedApplicationPageFactory;
import org.valkyriercp.application.support.SingleViewPageDescriptor;
import org.valkyriercp.component.SearchableInterceptorFactory;
import org.valkyriercp.form.binding.Binder;
import org.valkyriercp.form.builder.ChainedInterceptorFactory;
import org.valkyriercp.form.builder.FormComponentInterceptorFactory;
import org.valkyriercp.form.builder.ToolTipInterceptorFactory;
import org.valkyriercp.sample.dataeditor.domain.ItemService;
import org.valkyriercp.sample.dataeditor.domain.SupplierService;
import org.valkyriercp.sample.dataeditor.ui.*;
import org.valkyriercp.security.LoginCommand;
import org.valkyriercp.text.TextComponentPopupInterceptorFactory;
import org.valkyriercp.widget.WidgetViewDescriptor;

import javax.swing.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Configuration
public class DataEditorApplicationConfig extends AbstractApplicationConfig {
    @Autowired
    private ResourceLoader resourceLoader;

    @Override
    public ApplicationLifecycleAdvisor applicationLifecycleAdvisor() {
        ApplicationLifecycleAdvisor lifecycleAdvisor = super.applicationLifecycleAdvisor();
        lifecycleAdvisor.setStartingPageDescriptor(() -> new SingleViewPageDescriptor(itemView()));
        lifecycleAdvisor.setOnWindowCreated(window -> window.getControl().setExtendedState(JFrame.MAXIMIZED_BOTH));
        return lifecycleAdvisor;
    }

    @Override
    public List<String> getResourceBundleLocations() {
        List<String> list = super.getResourceBundleLocations();
        list.add("org.valkyriercp.sample.dataeditor.messages");
        return list;
    }

    public Map<String, Resource> getImageSourceResources() {
        IconFontSwing.register(new IconFont() {
            @Override
            public String getFontFamily() {
                return "IcoFont";
            }

            @Override
            public InputStream getFontInputStream() {
                try {
                    return resourceLoader.getResource("classpath:/org/valkyriercp/sample/dataeditor/icofont.ttf").getInputStream();
                } catch (IOException e) {
                    throw new RuntimeException("Could not load icon font", e);
                }
            }
        });
        Map<String, Resource> resources = super.getImageSourceResources();
        resources.put("simple", applicationContext().getResource("classpath:/org/valkyriercp/sample/dataeditor/images.yaml"));
        return resources;
    }

    @Override
    public ApplicationSessionInitializer applicationSessionInitializer() {
        ApplicationSessionInitializer initializer = new ApplicationSessionInitializer();
        initializer.setPreStartupCommandIds(LoginCommand.ID);
        return initializer;
    }

    @Override
    protected void configureAuthorityMap(Map<String, String> idAuthorityMap) {
        super.configureAuthorityMap(idAuthorityMap);
        idAuthorityMap.put("itemDataEditor.addrow", "ROLE_ADMIN");
        idAuthorityMap.put("itemDataEditor.removerow", "ROLE_ADMIN");
        idAuthorityMap.put("itemDataEditor.update", "ROLE_ADMIN");
        idAuthorityMap.put("itemDataEditor.create", "ROLE_ADMIN");
        idAuthorityMap.put("itemForm.save", "ROLE_ADMIN");
        idAuthorityMap.put("itemForm", "ROLE_ADMIN");
    }

    @Override
    public Class<?> getCommandConfigClass() {
        return DataEditorCommandConfig.class;
    }

    @Bean
    public UIManagerConfigurer uiManagerConfigurer() {
        UIManagerConfigurer configurer = new UIManagerConfigurer();
        configurer.setLookAndFeel(PlasticXPLookAndFeel.class);
        return configurer;
    }

    @Override
    public ApplicationWindowFactory applicationWindowFactory() {
        return new JXTaskPaneNavigatorApplicationWindowFactory();
    }

    @Override
    public FormComponentInterceptorFactory formComponentInterceptorFactory() {
        ChainedInterceptorFactory formComponentInterceptorFactory = (ChainedInterceptorFactory) super.formComponentInterceptorFactory();
        formComponentInterceptorFactory.getInterceptorFactories().add(new ToolTipInterceptorFactory());
        formComponentInterceptorFactory.getInterceptorFactories().add(new TextComponentPopupInterceptorFactory());
        formComponentInterceptorFactory.getInterceptorFactories().add(new SearchableInterceptorFactory());
        return formComponentInterceptorFactory;
    }

    @Override
    public ApplicationPageFactory applicationPageFactory() {
        JideTabbedApplicationPageFactory jideTabbedApplicationPageFactory = new JideTabbedApplicationPageFactory();
        jideTabbedApplicationPageFactory.setShowCloseButton(true);
        jideTabbedApplicationPageFactory.setTabShape(JideTabbedPane.SHAPE_ROUNDED_VSNET);
        return jideTabbedApplicationPageFactory;
    }

    // widgets

    @Bean
    @Scope(BeanDefinition.SCOPE_PROTOTYPE)
    @Lazy
    public ItemDataEditor itemDataEditor() {
        return new ItemDataEditor(itemDataProvider());
    }

    public ItemDataProvider itemDataProvider() {
        return new ItemDataProvider(itemService());
    }

    @Bean
    @Scope(BeanDefinition.SCOPE_PROTOTYPE)
    @Lazy
    public SupplierDataEditor supplierDataEditor() {
        return new SupplierDataEditor(supplierDataProvider());
    }

    public SupplierDataProvider supplierDataProvider() {
        return new SupplierDataProvider(supplierService());
    }

    // views

    @Bean
    public WidgetViewDescriptor itemView() {
        return new WidgetViewDescriptor("itemView", this::itemDataEditor);

    }

    @Bean
    public WidgetViewDescriptor supplierView() {
        return new WidgetViewDescriptor("supplierView", this::supplierDataEditor);
    }

    // Services

    @Bean
    public SupplierService supplierService() {
        return new SupplierService();
    }

    @Bean
    public ItemService itemService() {
        return new ItemService();
    }

    // Binders

    @Bean
    public Binder supplierBinder() {
        return new SupplierBinder();
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
