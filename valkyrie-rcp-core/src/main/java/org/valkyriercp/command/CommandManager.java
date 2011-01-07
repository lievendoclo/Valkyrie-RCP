package org.valkyriercp.command;

import org.valkyriercp.command.CommandServices;
import org.valkyriercp.command.config.CommandFaceDescriptorRegistry;
import org.valkyriercp.factory.CommandFactory;

public interface CommandManager extends CommandServices, CommandRegistry, CommandFaceDescriptorRegistry,
        CommandConfigurer, CommandFactory {
    public void addCommandInterceptor(String commandId, ActionCommandInterceptor interceptor);

    public void removeCommandInterceptor(String commandId, ActionCommandInterceptor interceptor);

}
