package org.valkyriercp.command;

import org.valkyriercp.command.support.CommandGroupEvent;

import java.util.EventListener;

public interface CommandGroupListener extends EventListener {
    public void membersChanged(CommandGroupEvent e);
}
