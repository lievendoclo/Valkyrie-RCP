package org.valkyriercp.command;

import org.valkyriercp.command.support.ApplicationWindowAwareCommand;
import org.valkyriercp.util.ValkyrieRepository;

public class NewWindowCommand extends ApplicationWindowAwareCommand {

    private static final String ID = "newWindowCommand";

    public NewWindowCommand() {
        super(ID);
    }

    protected void doExecuteCommand() {
        ValkyrieRepository.getInstance().getApplicationConfig().application().openWindow(getApplicationWindow().getPage().getId());
    }

}
