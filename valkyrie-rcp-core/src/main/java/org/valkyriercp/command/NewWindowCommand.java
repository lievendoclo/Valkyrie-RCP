package org.valkyriercp.command;

import org.valkyriercp.command.support.ApplicationWindowAwareCommand;

public class NewWindowCommand extends ApplicationWindowAwareCommand {

    private static final String ID = "newWindowCommand";

    public NewWindowCommand() {
        super(ID);
    }

    protected void doExecuteCommand() {
        applicationConfig.application().openWindow(getApplicationWindow().getPage().getId());
    }

}
