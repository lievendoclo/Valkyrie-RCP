package ${package};

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.valkyriercp.command.config.AbstractCommandConfig;
import org.valkyriercp.command.support.CommandGroup;
import org.valkyriercp.command.support.CommandGroupFactoryBean;

@Configuration
public class SimpleCommandConfig extends AbstractCommandConfig {
}
