package org.valkyriercp.application;

import org.valkyriercp.command.config.CommandButtonLabelInfo;
import org.valkyriercp.command.support.ActionCommand;
import org.valkyriercp.core.DescribedElement;
import org.valkyriercp.core.VisualizedElement;

public interface PageDescriptor extends VisualizedElement, DescribedElement{
    public String getId();

    public void buildInitialLayout(PageLayoutBuilder pageLayout);

    /**
     * Create a command that when executed, will attempt to show the
     * page component described by this descriptor in the provided
     * application window.
     *
     * @param window The window
     *
     * @return The show page component command.
     */
    public ActionCommand createShowPageCommand(ApplicationWindow window);

    public CommandButtonLabelInfo getShowPageCommandLabel();
}
