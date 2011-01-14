package org.valkyriercp.sample.simple;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.valkyriercp.application.exceptionhandling.ThrowExceptionCommand;
import org.valkyriercp.command.config.AbstractCommandConfig;
import org.valkyriercp.command.support.AbstractCommand;
import org.valkyriercp.command.support.CommandGroup;
import org.valkyriercp.command.support.CommandGroupFactoryBean;
import org.valkyriercp.command.support.WidgetViewCommand;
import org.valkyriercp.widget.AbstractWidget;
import org.valkyriercp.widget.Widget;
import org.valkyriercp.widget.WidgetViewDescriptor;

import javax.swing.*;

@Configuration
public class SimpleSampleCommandConfig extends AbstractCommandConfig {
    @Bean
    @Qualifier("menubar")
    public CommandGroup menuBarCommandGroup() {
        return new CommandGroup();
//        CommandGroupFactoryBean menuFactory = new CommandGroupFactoryBean();
//        menuFactory.setGroupId("menu");
//
//        CommandGroupFactoryBean fileMenuFactory = new CommandGroupFactoryBean();
//        fileMenuFactory.setGroupId("fileMenu");
//        fileMenuFactory.setMembers(newWindowCommand(), throwExceptionCommand(), showEmptyWidgetCommand(), showOtherWidgetCommand(),exitCommand());
//
//        menuFactory.setMembers(fileMenuFactory.getCommandGroup());
//        return menuFactory.getCommandGroup();
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

    @Bean
    public AbstractCommand showEmptyWidgetCommand() {
        return new WidgetViewCommand("emptyWidgetCommand", new WidgetViewDescriptor("emptyWidget", Widget.EMPTY_WIDGET));
    }

    @Bean
    public AbstractCommand showOtherWidgetCommand() {
        return new WidgetViewCommand("otherWidgetCommand", new WidgetViewDescriptor("otherWidget",new AbstractWidget() {
            @Override
            public JComponent getComponent() {
                return new JPanel();
            }
        }));
    }
}
