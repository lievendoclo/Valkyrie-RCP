package org.valkyriercp.factory;

import org.valkyriercp.command.support.CommandGroup;

public interface CommandFactory {
    public CommandGroup createCommandGroup(String groupId, Object[] members);
}
