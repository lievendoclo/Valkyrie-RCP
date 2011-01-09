package org.valkyriercp.sample.simple;

import org.pushingpixels.substance.api.skin.SubstanceBusinessBlueSteelLookAndFeel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.valkyriercp.application.ApplicationPageFactory;
import org.valkyriercp.application.config.support.AbstractApplicationConfig;
import org.valkyriercp.application.config.support.UIManagerConfigurer;
import org.valkyriercp.application.support.TabbedApplicationPageFactory;

import java.util.List;

@Configuration
public class SimpleSampleApplicationConfig extends AbstractApplicationConfig {

    @Override
    public List<String> getResourceBundleLocations() {
        List<String> list = super.getResourceBundleLocations();
        list.add("org.valkyriercp.sample.simple.simple");
        return list;
    }

    @Override
    public Class<?> getCommandConfigClass() {
        return SimpleSampleCommandConfig.class;
    }

    @Bean
    public UIManagerConfigurer uiManagerConfigurer() {
        UIManagerConfigurer configurer = new UIManagerConfigurer();
        configurer.setLookAndFeel(SubstanceBusinessBlueSteelLookAndFeel.class);
        return configurer;
    }

    @Override
    public ApplicationPageFactory applicationPageFactory() {
        return new TabbedApplicationPageFactory();
    }
}
