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
package org.valkyriercp.command;

import org.valkyriercp.command.config.CommandFaceDescriptorRegistry;
import org.valkyriercp.command.support.AbstractCommand;
import org.valkyriercp.command.support.ActionCommand;
import org.valkyriercp.command.support.CommandGroup;
import org.valkyriercp.factory.CommandFactory;

import java.util.List;

public interface CommandManager extends CommandServices, CommandRegistry, CommandFaceDescriptorRegistry,
        CommandConfigurer, CommandFactory {
    public void addCommandInterceptor(String commandId, ActionCommandInterceptor interceptor);

    public void removeCommandInterceptor(String commandId, ActionCommandInterceptor interceptor);

    CommandGroup createCommandGroup(List<? extends Object> members);

    CommandGroup createCommandGroup(String groupId, Object[] members);

    CommandGroup createCommandGroup(String groupId, List<? extends AbstractCommand> members);

    CommandGroup createCommandGroup(String groupId, Object[] members, CommandConfigurer configurer);

    CommandGroup createCommandGroup(String groupId, Object[] members,
                                    boolean exclusive, CommandConfigurer configurer);

    ActionCommand createDummyCommand(final String id, final String msg);
}
