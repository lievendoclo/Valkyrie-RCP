package org.valkyriercp.command;

import org.valkyriercp.command.support.CommandRegistryEvent;

import java.util.EventListener;

public interface CommandRegistryListener extends EventListener {
    public void commandRegistered(CommandRegistryEvent event);
}
