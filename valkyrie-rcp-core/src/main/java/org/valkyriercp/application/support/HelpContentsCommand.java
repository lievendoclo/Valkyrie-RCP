package org.valkyriercp.application.support;

import org.springframework.core.io.*;
import org.valkyriercp.command.support.*;

/**
 * @author Keith Donald
 */
public class HelpContentsCommand extends ApplicationWindowAwareCommand {
    private static final String ID = "helpContentsCommand";

    private HelpContents helpContents = new HelpContents();

    public HelpContentsCommand() {
        super(ID);
    }

    public void setHelpSetPath(Resource path) {
        this.helpContents.setHelpSetPath(path);
    }

    protected void doExecuteCommand() {
        helpContents.display(getApplicationWindow().getControl());
    }
}