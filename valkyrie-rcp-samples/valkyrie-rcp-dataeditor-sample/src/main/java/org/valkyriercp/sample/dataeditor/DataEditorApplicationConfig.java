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

import com.google.common.collect.Lists;
import com.jidesoft.swing.JideTabbedPane;
import org.pushingpixels.substance.api.skin.SubstanceMistAquaLookAndFeel;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
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
import org.valkyriercp.text.TextComponentPopupInterceptorFactory;
import org.valkyriercp.widget.Widget;
import org.valkyriercp.widget.WidgetProvider;
import org.valkyriercp.widget.WidgetViewDescriptor;

import java.util.List;
import java.util.Map;

@Configuration
public class DataEditorApplicationConfig extends AbstractApplicationConfig {
    @Override
    public ApplicationLifecycleAdvisor applicationLifecycleAdvisor() {
        ApplicationLifecycleAdvisor lifecycleAdvisor = super.applicationLifecycleAdvisor();
        lifecycleAdvisor.setStartingPageDescriptor(new SingleViewPageDescriptor(itemView()));
        return lifecycleAdvisor;
    }

    @Override
    public List<String> getResourceBundleLocations() {
        List<String> list = super.getResourceBundleLocations();
        list.add("org.valkyriercp.sample.dataeditor.messages");
        return list;
    }

    public Map<String, Resource> getImageSourceResources() {
        Map<String, Resource> resources = super.getImageSourceResources();
        resources.put("simple", applicationContext().getResource("classpath:/org/valkyriercp/sample/dataeditor/images.properties"));
        return resources;
    }

    @Override
    public ApplicationSessionInitializer applicationSessionInitializer() {
        ApplicationSessionInitializer initializer = new ApplicationSessionInitializer();
        initializer.setPreStartupCommands(Lists.newArrayList("loginCommand"));
        return initializer;
    }

    @Override
    protected void configureAuthorityMap(Map<String, String> idAuthorityMap) {
        super.configureAuthorityMap(idAuthorityMap);
        idAuthorityMap.put("itemDataEditor.addrow", "ADMIN");
        idAuthorityMap.put("itemDataEditor.removerow", "ADMIN");
        idAuthorityMap.put("itemDataEditor.update", "ADMIN");
        idAuthorityMap.put("itemDataEditor.create", "ADMIN");
        idAuthorityMap.put("itemForm.save", "ADMIN");
        idAuthorityMap.put("itemForm", "ADMIN");
    }

    @Override
    public Class<?> getCommandConfigClass() {
        return DataEditorCommandConfig.class;
    }

    @Bean
    public UIManagerConfigurer uiManagerConfigurer() {
        UIManagerConfigurer configurer = new UIManagerConfigurer();
        configurer.setLookAndFeel(SubstanceMistAquaLookAndFeel.class);
        return configurer;
    }

    @Override
    public ApplicationWindowFactory applicationWindowFactory() {
        JXTaskPaneNavigatorApplicationWindowFactory navigatorApplicationWindowFactory = new JXTaskPaneNavigatorApplicationWindowFactory();
        return navigatorApplicationWindowFactory;
    }

    @Override
    public FormComponentInterceptorFactory formComponentInterceptorFactory() {
        ChainedInterceptorFactory formComponentInterceptorFactory = (ChainedInterceptorFactory) super.formComponentInterceptorFactory();
//        formComponentInterceptorFactory.getInterceptorFactories().add(new SelectAllFormComponentInterceptorFactory());
        formComponentInterceptorFactory.getInterceptorFactories().add(new ToolTipInterceptorFactory());
        formComponentInterceptorFactory.getInterceptorFactories().add(new TextComponentPopupInterceptorFactory());
        formComponentInterceptorFactory.getInterceptorFactories().add(new SearchableInterceptorFactory());
        return formComponentInterceptorFactory;    //To change body of overridden methods use File | Settings | File Templates.
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
        return new WidgetViewDescriptor("itemView", new WidgetProvider<Widget>() {
            @Override
            public Widget getWidget() {
                return itemDataEditor();
            }
        });

    }

    @Bean
    public WidgetViewDescriptor supplierView() {
        return new WidgetViewDescriptor("supplierView", new WidgetProvider<Widget>() {
            @Override
            public Widget getWidget() {
                return supplierDataEditor();
            }
        });
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
        List<UserDetails> userDetailsList = Lists.newArrayList();
        userDetailsList.add(new User("admin", "admin", Lists.newArrayList(new SimpleGrantedAuthority("ADMIN"))));
        userDetailsList.add(new User("user", "user", Lists.newArrayList(new SimpleGrantedAuthority("READ"))));
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(new InMemoryUserDetailsManager(userDetailsList));
        return new ProviderManager(Lists.<AuthenticationProvider>newArrayList(provider));
    }
}
