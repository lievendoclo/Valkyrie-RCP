package ${package};

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.valkyriercp.application.ViewDescriptor;
import org.valkyriercp.application.config.ApplicationLifecycleAdvisor;
import org.valkyriercp.application.config.support.AbstractApplicationConfig;
import org.valkyriercp.application.support.DefaultViewDescriptor;
import org.valkyriercp.application.support.SingleViewPageDescriptor;
import org.valkyriercp.widget.Widget;
import org.valkyriercp.widget.WidgetViewDescriptor;

import java.util.List;
import java.util.Map;

@Configuration
public class SimpleApplicationConfig extends AbstractApplicationConfig {

    @Override
    public ApplicationLifecycleAdvisor applicationLifecycleAdvisor() {
        ApplicationLifecycleAdvisor lifecycleAdvisor =  super.applicationLifecycleAdvisor();
        lifecycleAdvisor.setStartingPageDescriptor(new SingleViewPageDescriptor(initialView()));
        return lifecycleAdvisor;
    }

    @Override
    public Class<?> getCommandConfigClass() {
        return SimpleCommandConfig.class;
    }

    @Bean
    public ViewDescriptor initialView() {
        DefaultViewDescriptor initialView = new WidgetViewDescriptor("initialView", Widget.EMPTY_WIDGET);
        return initialView;
    }
}
