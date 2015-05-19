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
package org.valkyriercp.command.config;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;
import org.valkyriercp.application.config.ApplicationConfig;
import org.valkyriercp.application.config.ApplicationObjectConfigurer;
import org.valkyriercp.command.CommandConfigurer;
import org.valkyriercp.command.CommandServices;
import org.valkyriercp.command.support.AbstractCommand;

/**
 * @author Keith Donald
 */
public class DefaultCommandConfigurer implements CommandConfigurer {
    private final Log logger = LogFactory.getLog(getClass());

    @Autowired
    private ApplicationConfig applicationConfig;

    private CommandServices commandServices;

    private ApplicationObjectConfigurer objectConfigurer;

    public DefaultCommandConfigurer() {
    }

    public DefaultCommandConfigurer(CommandServices commandServices) {
        setCommandServices(commandServices);
    }

    public void setApplicationObjectConfigurer(ApplicationObjectConfigurer configurer) {
        this.objectConfigurer = configurer;
    }

    public void setCommandServices(CommandServices services) {
        this.commandServices = services;
    }

    public AbstractCommand configure(AbstractCommand command) {
        Assert.notNull(command, "command");
        return configure(command, getObjectConfigurer());
    }

    protected ApplicationObjectConfigurer getObjectConfigurer() {
        if (objectConfigurer == null) {
            objectConfigurer = applicationConfig.applicationObjectConfigurer();
        }
        return objectConfigurer;
    }

    public AbstractCommand configure(AbstractCommand command, ApplicationObjectConfigurer configurer) {
        command.setCommandServices(getCommandServices());
        String objectName = command.getId();
        if (command.isAnonymous()) {
            objectName = ClassUtils.getShortNameAsProperty(command.getClass());
            int lastDot = objectName.lastIndexOf('.');
            if (lastDot != -1) {
                objectName = StringUtils.uncapitalize(objectName.substring(lastDot + 1));
            }
        }

        // Configure the command itself
        configurer.configure( command, objectName );

        // Configure the command face
        if (logger.isDebugEnabled()) {
            logger.debug("Configuring faces (aka visual appearance descriptors) for " + command);
        }
        CommandFaceDescriptor face = new CommandFaceDescriptor();
        configurer.configure(face, objectName);
        command.setFaceDescriptor(face);
        return command;
    }

    protected CommandServices getCommandServices() {
        if (commandServices == null) {
            commandServices = applicationConfig.commandServices();
        }
        return commandServices;
    }

}