package org.valkyriercp.application.docking.view;

import org.valkyriercp.application.ApplicationWindow;
import org.valkyriercp.application.ViewDescriptor;
import org.valkyriercp.command.support.ApplicationWindowAwareCommand;

/**
 * Slight modification of the Spring RCP ShowViewCommand to
 * use the viewDescriptor.getId as the id for the show view
 * command, as opposed to the viewDescriptor.getDisplayName.
 * Integrates better with command labels and icons then.
 * 
 * @author Jonny Wray
 *
 */
public class ShowViewCommand extends ApplicationWindowAwareCommand {
    private ViewDescriptor viewDescriptor;

    public ShowViewCommand(ViewDescriptor viewDescriptor, ApplicationWindow window) {
        setViewDescriptor(viewDescriptor);
        setApplicationWindow(window);
        setEnabled(true);
    }

    public final void setViewDescriptor(ViewDescriptor viewDescriptor) {
        setId(viewDescriptor.getId()); 
        setLabel(viewDescriptor.getShowViewCommandLabel());
        setIcon(viewDescriptor.getIcon());
        setCaption(viewDescriptor.getCaption());
        this.viewDescriptor = viewDescriptor;
    }

    protected void doExecuteCommand() {
        getApplicationWindow().getPage().showView(viewDescriptor);
    }


}
