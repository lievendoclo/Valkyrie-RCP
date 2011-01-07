package org.valkyriercp.command.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.valkyriercp.command.support.CommandGroup;

@Configuration
public class DefaultCommandConfig extends AbstractCommandConfig {
    @Bean
    @Qualifier("menubar")
    public CommandGroup menuBarCommandGroup() {
        return new CommandGroup();
    }

    @Bean
    @Qualifier("toolbar")
    public CommandGroup toolBarCommandGroup() {
        return new CommandGroup();
    }
}
