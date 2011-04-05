package org.valkyriercp.sample.dataeditor;

import com.jgoodies.looks.plastic.PlasticXPLookAndFeel;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.core.io.Resource;
import org.valkyriercp.application.ApplicationWindowFactory;
import org.valkyriercp.application.config.ApplicationLifecycleAdvisor;
import org.valkyriercp.application.config.support.AbstractApplicationConfig;
import org.valkyriercp.application.config.support.UIManagerConfigurer;
import org.valkyriercp.application.support.SingleViewPageDescriptor;
import org.valkyriercp.form.binding.Binder;
import org.valkyriercp.form.builder.ChainedInterceptorFactory;
import org.valkyriercp.form.builder.FormComponentInterceptorFactory;
import org.valkyriercp.form.builder.ToolTipInterceptorFactory;
import org.valkyriercp.sample.dataeditor.domain.ItemService;
import org.valkyriercp.sample.dataeditor.domain.SupplierService;
import org.valkyriercp.sample.dataeditor.ui.*;
import org.valkyriercp.taskpane.TaskPaneNavigatorApplicationWindowFactory;
import org.valkyriercp.text.SelectAllFormComponentInterceptorFactory;
import org.valkyriercp.widget.WidgetViewDescriptor;

import java.util.List;
import java.util.Map;

@Configuration
public class DataEditorApplicationConfig extends AbstractApplicationConfig {
    @Override
    public ApplicationLifecycleAdvisor applicationLifecycleAdvisor() {
        ApplicationLifecycleAdvisor lifecycleAdvisor =  super.applicationLifecycleAdvisor();
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
    public Class<?> getCommandConfigClass() {
        return DataEditorCommandConfig.class;
    }

    @Bean
    public UIManagerConfigurer uiManagerConfigurer() {
        UIManagerConfigurer configurer = new UIManagerConfigurer();
        configurer.setLookAndFeel(PlasticXPLookAndFeel.class);
        return configurer;
    }

//    @Override
//    public ApplicationPageFactory applicationPageFactory() {
//        return new TabbedApplicationPageFactory();
//    }

    @Override
    public ApplicationWindowFactory applicationWindowFactory() {
        return new TaskPaneNavigatorApplicationWindowFactory();
    }

    @Override
    public FormComponentInterceptorFactory formComponentInterceptorFactory() {
        ChainedInterceptorFactory formComponentInterceptorFactory = (ChainedInterceptorFactory) super.formComponentInterceptorFactory();
        formComponentInterceptorFactory.getInterceptorFactories().add(new SelectAllFormComponentInterceptorFactory());
        formComponentInterceptorFactory.getInterceptorFactories().add(new ToolTipInterceptorFactory());
        return formComponentInterceptorFactory;    //To change body of overridden methods use File | Settings | File Templates.
    }

    // widgets

    @Bean
    @Scope(BeanDefinition.SCOPE_PROTOTYPE)
    public ItemDataEditor itemDataEditor() {
        return new ItemDataEditor(itemDataProvider());
    }

    public ItemDataProvider itemDataProvider() {
        return new ItemDataProvider(itemService());
    }

    @Bean
    @Scope(BeanDefinition.SCOPE_PROTOTYPE)
    public SupplierDataEditor supplierDataEditor() {
        return new SupplierDataEditor(supplierDataProvider());
    }

    public SupplierDataProvider supplierDataProvider() {
        return new SupplierDataProvider(supplierService());
    }

    // views

    @Bean
    @Scope(BeanDefinition.SCOPE_PROTOTYPE)
    public WidgetViewDescriptor itemView() {
        return itemDataEditor().createViewDescriptor("itemView");

    }

    @Bean
    @Scope(BeanDefinition.SCOPE_PROTOTYPE)
    public WidgetViewDescriptor supplierView() {
         return supplierDataEditor().createViewDescriptor("supplierView");
    }

    // Services

    public SupplierService supplierService() {
        return new SupplierService();
    }

    public ItemService itemService() {
        return new ItemService();
    }

    // Binders

    @Bean
    public Binder supplierBinder() {
        return new SupplierBinder();
    }
}
