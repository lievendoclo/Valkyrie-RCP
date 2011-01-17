package org.valkyriercp.command.support;

import org.springframework.core.io.Resource;
import org.valkyriercp.application.support.AboutBox;

/**
 * @author Keith Donald
 */
public class AboutCommand extends ApplicationWindowAwareCommand {
    private static final String ID = "aboutCommand";

    private AboutBox aboutBox = new AboutBox();

    public AboutCommand() {
        super(ID);
    }

    /**
     * Set the path to the HTML file to display.  This is optional.  If
     * it is not specified, then the scrolling HTML panel will not be
     * displayed.
     *
     * @param path
     */
    public void setAboutTextPath(Resource path) {
        this.aboutBox.setAboutTextPath(path);
    }

    protected void doExecuteCommand() {
        aboutBox.display(getApplicationWindow().getControl());
    }

}
