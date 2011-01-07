package org.valkyriercp.sample.simple;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.core.io.Resource;
import org.valkyriercp.application.config.support.AbstractApplicationConfig;
import org.valkyriercp.image.DefaultImageSource;
import org.valkyriercp.image.ImageSource;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class SimpleSampleApplicationConfig extends AbstractApplicationConfig {
    @Override
    public ImageSource imageSource() {
        Map<String, Resource> resources = new HashMap<String, Resource>();
        resources.put("default", applicationContext().getResource("classpath:/org/valkyriercp/images/images.properties"));
        DefaultImageSource imageSource = new DefaultImageSource(resources);
        imageSource.setBrokenImageIndicator(applicationContext().getResource("classpath:/org/valkyriercp/images/alert/error_obj.gif"));
        return imageSource;
    }

    @Override
    public MessageSource messageSource() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasenames(new String[] {"org.valkyriercp.sample.simple.simple"});
        return messageSource;
    }

    @Override
    public Class<?> getCommandConfigClass() {
        return SimpleSampleCommandConfig.class;
    }
}
