package org.valkyriercp.application.support;

import org.springframework.beans.factory.annotation.Configurable;
import org.valkyriercp.command.CommandRegistry;
import org.valkyriercp.command.CommandServices;
import org.valkyriercp.command.support.ActionCommand;
import org.valkyriercp.command.support.DefaultCommandManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

@Configurable
public class ApplicationWindowCommandManager extends DefaultCommandManager {
    private List sharedCommands;

    public ApplicationWindowCommandManager() {
        super();
    }

    public ApplicationWindowCommandManager(CommandRegistry parent) {
        super(parent);
    }

    public ApplicationWindowCommandManager(CommandServices commandServices) {
        super(commandServices);
    }

    public void setSharedCommandIds(String... sharedCommandIds) {
        if (sharedCommandIds.length == 0) {
            sharedCommands = Collections.EMPTY_LIST;
        }
        else {
            this.sharedCommands = new ArrayList(sharedCommandIds.length);
            for (int i = 0; i < sharedCommandIds.length; i++) {
                ActionCommand globalCommand = createTargetableActionCommand(sharedCommandIds[i], null);
                sharedCommands.add(globalCommand);
            }
        }
    }

    public Iterator getSharedCommands() {
        if (sharedCommands == null) {
            return Collections.EMPTY_LIST.iterator();
        }
        return sharedCommands.iterator();
    }

}
