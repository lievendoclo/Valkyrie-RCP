package org.valkyriercp.sample.simple;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.valkyriercp.application.exceptionhandling.ThrowExceptionCommand;
import org.valkyriercp.command.config.AbstractCommandConfig;
import org.valkyriercp.command.support.AbstractCommand;
import org.valkyriercp.command.support.CommandGroup;
import org.valkyriercp.command.support.CommandGroupFactoryBean;

@Configuration
public class SimpleSampleCommandConfig extends AbstractCommandConfig {
    @Bean
    @Qualifier("menubar")
    public CommandGroup menuBarCommandGroup() {
        CommandGroupFactoryBean menuFactory = new CommandGroupFactoryBean();
        menuFactory.setGroupId("menu");

        CommandGroupFactoryBean fileMenuFactory = new CommandGroupFactoryBean();
        fileMenuFactory.setGroupId("fileMenu");
        fileMenuFactory.setMembers(exitCommand(), throwExceptionCommand());

        menuFactory.setMembers(fileMenuFactory.getCommandGroup());
        return menuFactory.getCommandGroup();
    }

    @Bean
    @Qualifier("toolbar")
    public CommandGroup toolBarCommandGroup() {
        return new CommandGroup();
    }

    @Bean
    public AbstractCommand throwExceptionCommand() {
        return new ThrowExceptionCommand();
    }
}
