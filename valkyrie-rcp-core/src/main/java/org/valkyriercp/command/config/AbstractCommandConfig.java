package org.valkyriercp.command.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.valkyriercp.application.config.ApplicationConfig;
import org.valkyriercp.application.support.ApplicationWindowCommandManager;
import org.valkyriercp.command.NewWindowCommand;
import org.valkyriercp.command.support.AbstractCommand;
import org.valkyriercp.command.support.CommandGroup;
import org.valkyriercp.command.support.ExitCommand;

public abstract class AbstractCommandConfig {
    @Autowired
    protected ApplicationConfig parentConfig;

    @Bean
    public ApplicationWindowCommandManager applicationWindowCommandManager()
    {
         return new ApplicationWindowCommandManager();
    }

    @Bean
    @Qualifier("menubar")
    public abstract CommandGroup menuBarCommandGroup();

    @Bean
    @Qualifier("toolbar")
    public abstract CommandGroup toolBarCommandGroup();

    @Bean
    public AbstractCommand exitCommand() {
        return new ExitCommand();
    }

    @Bean
    public AbstractCommand newWindowCommand() {
        return new NewWindowCommand();
    }
}
