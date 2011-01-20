package org.valkyriercp.command.support;

import org.valkyriercp.application.ViewDescriptor;
import org.valkyriercp.widget.WidgetViewDescriptor;

/**
 * Widget command that shows a widget in a view
 */
public class WidgetViewCommand extends AbstractWidgetCommand
{
    protected final WidgetViewDescriptor widgetViewDescriptor;

    public WidgetViewCommand(String id, WidgetViewDescriptor widgetViewDescriptor) {
        super(id);
        this.widgetViewDescriptor = widgetViewDescriptor;
    }

    /**
     * Shows the widget in the view
     */
    protected void doExecuteCommand()
    {
        getApplicationWindow().getPage().showView(widgetViewDescriptor);
    }

    @Override
    public void setAuthorized(boolean authorized)
    {
        super.setAuthorized(authorized);
        if ((this.widgetViewDescriptor != null) && !authorized)
            if (this.widgetViewDescriptor.getId().equals(getApplicationWindow().getPage().getActiveComponent().getId()))
                getApplicationWindow().getPage().showView((ViewDescriptor) null);
    }
}


