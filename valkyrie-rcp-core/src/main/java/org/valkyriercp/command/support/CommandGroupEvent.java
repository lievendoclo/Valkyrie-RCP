package org.valkyriercp.command.support;

import java.util.EventObject;

public class CommandGroupEvent extends EventObject {
    public CommandGroupEvent(Object source) {
        super(source);
    }

    public CommandGroup getGroup() {
        return (CommandGroup)getSource();
    }
}
