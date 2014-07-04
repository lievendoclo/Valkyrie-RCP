package org.valkyriercp.command.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.valkyriercp.application.config.ApplicationConfig;
import org.valkyriercp.application.support.ApplicationWindowCommandManager;
import org.valkyriercp.application.support.HelpContentsCommand;
import org.valkyriercp.command.NewWindowCommand;
import org.valkyriercp.command.support.*;
import org.valkyriercp.security.LoginCommand;
import org.valkyriercp.security.LogoutCommand;

@Configuration
public class AbstractCommandConfig implements CommandConfig {
    @Autowired
    protected ApplicationConfig parentConfig;

    @Bean
    public ApplicationWindowCommandManager applicationWindowCommandManager() {
        return new ApplicationWindowCommandManager();
    }

    @Override
    @Bean
    @Qualifier("menubar")
    public CommandGroup menuBarCommandGroup() {
        CommandGroupFactoryBean menuFactory = new CommandGroupFactoryBean();
        menuFactory.setGroupId("menu");
        populateMenuBar(menuFactory);
        return menuFactory.getCommandGroup();
    }

    @Override
    @Bean
    @Qualifier("toolbar")
    public CommandGroup toolBarCommandGroup() {
        CommandGroupFactoryBean toolbarFactory = new CommandGroupFactoryBean();
        toolbarFactory.setGroupId("toolbar");
        populateToolBar(toolbarFactory);
        return toolbarFactory.getCommandGroup();
    }

    protected void populateMenuBar(CommandGroupFactoryBean menuBar) {

    }

    protected void populateToolBar(CommandGroupFactoryBean toolBar) {

    }

    @Override
    @Bean
    @Qualifier("navigation")
    public CommandGroup navigationCommandGroup() {
        return new CommandGroup();
    }


    @Bean
    public AbstractCommand exitCommand() {
        return new ExitCommand();
    }

    @Bean
    public AbstractCommand newWindowCommand() {
        return new NewWindowCommand();
    }

    @Bean
    public CommandGroup showViewMenu() {
        return new ShowViewMenu();
    }

    @Bean
    public LoginCommand loginCommand() {
        LoginCommand loginCommand = new LoginCommand();
        loginCommand.setDisplaySuccess(false);
        loginCommand.setClearPasswordOnFailure(true);
        return loginCommand;
    }

    @Bean
    public LogoutCommand logoutCommand() {
        LogoutCommand logoutCommand = new LogoutCommand(loginCommand());
        logoutCommand.setDisplaySuccess(false);
        return logoutCommand;
    }

    @Bean
    public AboutCommand aboutCommand() {
        AboutCommand aboutCommand = new AboutCommand();
        return aboutCommand;
    }

    @Bean
    public HelpContentsCommand helpContentsCommand() {
        HelpContentsCommand helpContentsCommand = new HelpContentsCommand();
        return helpContentsCommand;
    }
}
