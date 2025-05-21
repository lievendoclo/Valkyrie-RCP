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
package org.valkyriercp.sample.simple;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.valkyriercp.application.support.ApplicationWindowCommandManager;
import org.valkyriercp.application.support.HelpContentsCommand;
import org.valkyriercp.command.config.AbstractCommandConfig;
import org.valkyriercp.command.support.CommandGroup;
import org.valkyriercp.command.support.CommandGroupFactoryBean;
import org.valkyriercp.command.support.GlobalCommandIds;

@Configuration
public class SimpleSampleCommandConfig extends AbstractCommandConfig {

	@Override
	public ApplicationWindowCommandManager applicationWindowCommandManager() {
		ApplicationWindowCommandManager applicationWindowCommandManager = super
				.applicationWindowCommandManager();
		applicationWindowCommandManager.setSharedCommandIds(
				"newContactCommand", "propertiesCommand", "deleteCommand");
		return applicationWindowCommandManager;
	}

	@Override
	@Bean
	@Qualifier("menubar")
	public CommandGroup menuBarCommandGroup() {
		CommandGroupFactoryBean menuFactory = new CommandGroupFactoryBean();
		menuFactory.setGroupId("menu");
		menuFactory.setMembers(fileMenu(), windowMenu(), helpMenu());
		return menuFactory.getCommandGroup();
	}

	@Bean
	public CommandGroup fileMenu() {
		CommandGroupFactoryBean fileMenuFactory = new CommandGroupFactoryBean();
		fileMenuFactory.setGroupId("fileMenu");
		fileMenuFactory.setMembers(newMenu(),
				CommandGroupFactoryBean.SEPARATOR_MEMBER_CODE,
				GlobalCommandIds.PROPERTIES,
				CommandGroupFactoryBean.SEPARATOR_MEMBER_CODE, GlobalCommandIds.DELETE,
				CommandGroupFactoryBean.SEPARATOR_MEMBER_CODE, logoutCommand(),
				exitCommand());
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
	public CommandGroup helpMenu() {
		CommandGroupFactoryBean factory = new CommandGroupFactoryBean();
		factory.setGroupId("helpMenu");
		HelpContentsCommand helpContentsCommand = helpContentsCommand();
		helpContentsCommand.setHelpSetPath(new ClassPathResource("help/simple.hs"));
		factory.setMembers(aboutCommand(), helpContentsCommand);
		return factory.getCommandGroup();
	}

	@Override
	@Bean
	@Qualifier("toolbar")
	public CommandGroup toolBarCommandGroup() {
		CommandGroupFactoryBean toolbarFactory = new CommandGroupFactoryBean();
		toolbarFactory.setGroupId("toolbar");
		toolbarFactory.setMembers("newContactCommand", "propertiesCommand",
				"deleteCommand");
		return toolbarFactory.getCommandGroup();
	}
}
