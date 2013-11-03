package org.valkyriercp.sample.showcase;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.valkyriercp.command.config.AbstractCommandConfig;
import org.valkyriercp.command.support.AbstractCommand;
import org.valkyriercp.command.support.CommandGroup;
import org.valkyriercp.command.support.CommandGroupFactoryBean;
import org.valkyriercp.command.support.WidgetViewCommand;

@Configuration
public class ShowcaseCommandConfig extends AbstractCommandConfig {


    @Override
    protected void populateMenuBar(CommandGroupFactoryBean menuBar) {
        menuBar.setMembers(fileMenu(), demoMenu());
    }

    /* Menus */
    @Bean
    public CommandGroup fileMenu() {
        CommandGroupFactoryBean fileMenuFactory = new CommandGroupFactoryBean();
        fileMenuFactory.setGroupId("fileMenu");
        fileMenuFactory.setMembers(logoutCommand(), exitCommand());
        return fileMenuFactory.getCommandGroup();
    }

    @Bean
    public CommandGroup demoMenu() {
        CommandGroupFactoryBean demoMenuFactory = new CommandGroupFactoryBean();
        demoMenuFactory.setGroupId("demoMenu");
        demoMenuFactory.setMembers(binderDemoCommand());
        return demoMenuFactory.getCommandGroup();
    }

    @Bean
    public AbstractCommand binderDemoCommand() {
        return new WidgetViewCommand("binderDemoCommand", getApplicationConfig().getViews().binderDemoView());
    }

    public ShowcaseApplicationConfig getApplicationConfig() {
        return (ShowcaseApplicationConfig) parentConfig;
    }
}
