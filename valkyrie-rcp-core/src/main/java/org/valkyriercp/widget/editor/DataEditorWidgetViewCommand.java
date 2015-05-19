/**
 * Copyright (C) 2015 Valkyrie RCP
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
        setWidget(widgetViewDescriptor.getWidget());
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
