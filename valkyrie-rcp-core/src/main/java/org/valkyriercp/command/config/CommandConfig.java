package org.valkyriercp.command.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.valkyriercp.command.support.CommandGroup;

public interface CommandConfig {
    @Bean
    @Qualifier("menubar")
    CommandGroup menuBarCommandGroup();

    @Bean
    @Qualifier("toolbar")
    CommandGroup toolBarCommandGroup();

    @Bean
    @Qualifier("navigation")
    CommandGroup navigationCommandGroup();
}
