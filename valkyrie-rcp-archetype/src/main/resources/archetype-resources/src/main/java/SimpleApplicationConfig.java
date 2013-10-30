package ${package};

import org.springframework.context.annotation.Configuration;
import org.valkyriercp.application.config.support.AbstractApplicationConfig;

@Configuration
public class SimpleApplicationConfig extends AbstractApplicationConfig {
    @Override
    public Class<?> getCommandConfigClass() {
        return SimpleCommandConfig.class;
    }
}
