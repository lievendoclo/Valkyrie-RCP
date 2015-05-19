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
package org.valkyriercp.command.support;

import org.valkyriercp.application.ViewDescriptor;

/**
 * Widget command that shows a widget in a view
 */
public class WidgetViewCommand extends AbstractWidgetCommand
{
    protected final ViewDescriptor widgetViewDescriptor;

    public WidgetViewCommand(String id, ViewDescriptor widgetViewDescriptor) {
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


