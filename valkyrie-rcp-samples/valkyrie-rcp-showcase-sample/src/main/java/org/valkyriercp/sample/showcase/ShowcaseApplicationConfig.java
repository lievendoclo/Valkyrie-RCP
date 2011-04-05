package org.valkyriercp.sample.showcase;

import com.google.common.collect.Lists;
import com.jgoodies.looks.plastic.PlasticXPLookAndFeel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.valkyriercp.application.ApplicationPageFactory;
import org.valkyriercp.application.config.ApplicationLifecycleAdvisor;
import org.valkyriercp.application.config.support.AbstractApplicationConfig;
import org.valkyriercp.application.config.support.UIManagerConfigurer;
import org.valkyriercp.application.session.ApplicationSessionInitializer;
import org.valkyriercp.application.support.JideTabbedApplicationPageFactory;
import org.valkyriercp.application.support.SingleViewPageDescriptor;

import java.util.List;
import java.util.Map;

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
        initializer.setPreStartupCommands(Lists.newArrayList("loginCommand"));
        return initializer;
    }

    @Bean
    public UIManagerConfigurer uiManagerConfigurer() {
        UIManagerConfigurer configurer = new UIManagerConfigurer();
        configurer.setLookAndFeel(PlasticXPLookAndFeel.class);
        return configurer;
    }


//    @Override
//    public MessageSource messageSource() {
//        JoddPropsMessageSource messageSource = new JoddPropsMessageSource("properties");
//        messageSource.addBaseName("/org/valkyriercp/messages/default");
//        messageSource.addBaseName("/org/valkyriercp/sample/showcase/messages");
//        return messageSource;
//    }

    @Override
    public List<String> getResourceBundleLocations() {
        List<String> resourceBundleLocations = super.getResourceBundleLocations();
        resourceBundleLocations.add("org.valkyriercp.sample.showcase.messages");
        return resourceBundleLocations;
    }

    @Override
    public Map<String, Resource> getImageSourceResources() {
        Map<String, Resource> imageSourceResources = super.getImageSourceResources();
        imageSourceResources.put("showcase", new ClassPathResource("/org/valkyriercp/sample/showcase/images.properties"));
        return imageSourceResources;
    }
}
