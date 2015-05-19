/**
 * Copyright (C) 2015 Valkyrie RCP
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
