package org.valkyriercp.command;

import org.valkyriercp.command.CommandServices;
import org.valkyriercp.command.config.CommandFaceDescriptorRegistry;
import org.valkyriercp.command.support.AbstractCommand;
import org.valkyriercp.command.support.CommandGroup;
import org.valkyriercp.factory.CommandFactory;

import java.util.List;

public interface CommandManager extends CommandServices, CommandRegistry, CommandFaceDescriptorRegistry,
        CommandConfigurer, CommandFactory {
    public void addCommandInterceptor(String commandId, ActionCommandInterceptor interceptor);

    public void removeCommandInterceptor(String commandId, ActionCommandInterceptor interceptor);

    CommandGroup createCommandGroup(List<? extends AbstractCommand> members);

    CommandGroup createCommandGroup(String groupId, Object[] members);

    CommandGroup createCommandGroup(String groupId, List<? extends AbstractCommand> members);

    CommandGroup createCommandGroup(String groupId, Object[] members, CommandConfigurer configurer);

    CommandGroup createCommandGroup(String groupId, Object[] members,
                                    boolean exclusive, CommandConfigurer configurer);
}
