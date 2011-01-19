package org.valkyriercp.widget.editor;

import org.springframework.util.Assert;
import org.valkyriercp.command.support.WidgetViewCommand;
import org.valkyriercp.widget.Widget;
import org.valkyriercp.widget.WidgetViewDescriptor;

import java.util.Map;

public class DataEditorWidgetViewCommand extends WidgetViewCommand
{
    public DataEditorWidgetViewCommand(String id, WidgetViewDescriptor widgetViewDescriptor) {
        super(id, widgetViewDescriptor);
    }

    /**
     * {@inheritDoc}
     *
     * Open de dataeditor.
     */
    protected void doExecuteCommand()
    {
        Widget widget = super.getWidget();
        Assert.isInstanceOf(AbstractDataEditorWidget.class, widget);
        AbstractDataEditorWidget dataEditorWidget = (AbstractDataEditorWidget)widget;
        Object dataEditorParameters = getParameter(DefaultDataEditorWidget.PARAMETER_MAP);
        if(dataEditorParameters != null)
        {
            dataEditorWidget.executeFilter((Map<String, Object>)dataEditorParameters);
        }

        super.doExecuteCommand();
    }
}
