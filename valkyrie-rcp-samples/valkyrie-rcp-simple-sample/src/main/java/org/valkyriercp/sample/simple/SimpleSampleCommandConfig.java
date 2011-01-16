package org.valkyriercp.sample.simple;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.valkyriercp.application.exceptionhandling.ThrowExceptionCommand;
import org.valkyriercp.application.support.ApplicationWindowCommandManager;
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

    @Override
    public ApplicationWindowCommandManager applicationWindowCommandManager() {
        ApplicationWindowCommandManager applicationWindowCommandManager = super.applicationWindowCommandManager();
        applicationWindowCommandManager.setSharedCommandIds("newContactCommand", "propertiesCommand", "deleteCommand");
        return applicationWindowCommandManager;
    }

    @Bean
    @Qualifier("menubar")
    public CommandGroup menuBarCommandGroup() {
        CommandGroupFactoryBean menuFactory = new CommandGroupFactoryBean();
        menuFactory.setGroupId("menu");
        menuFactory.setMembers(fileMenu(), windowMenu());
        return menuFactory.getCommandGroup();
    }

    @Bean
    public CommandGroup fileMenu() {
        CommandGroupFactoryBean fileMenuFactory = new CommandGroupFactoryBean();
        fileMenuFactory.setGroupId("fileMenu");
        fileMenuFactory.setMembers(newMenu(), CommandGroupFactoryBean.SEPARATOR_MEMBER_CODE, "propertiesCommand",
                CommandGroupFactoryBean.SEPARATOR_MEMBER_CODE, "deleteCommand",
                CommandGroupFactoryBean.SEPARATOR_MEMBER_CODE, exitCommand());
        return fileMenuFactory.getCommandGroup();
    }

    @Bean
    public CommandGroup windowMenu() {
        CommandGroupFactoryBean windowMenuFactory = new CommandGroupFactoryBean();
        windowMenuFactory.setGroupId("windowMenu");
        windowMenuFactory.setMembers(newWindowCommand(), showViewMenu());
        return windowMenuFactory.getCommandGroup();
    }

    @Bean
    public CommandGroup newMenu() {
        CommandGroupFactoryBean factory = new CommandGroupFactoryBean();
        factory.setGroupId("newMenu");
        factory.setMembers("newContactCommand");
        return factory.getCommandGroup();
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
        return new WidgetViewCommand("otherWidgetCommand", new WidgetViewDescriptor("otherWidget", new AbstractWidget() {
            @Override
            public JComponent getComponent() {
                return new JPanel();
            }
        }));
    }
}
