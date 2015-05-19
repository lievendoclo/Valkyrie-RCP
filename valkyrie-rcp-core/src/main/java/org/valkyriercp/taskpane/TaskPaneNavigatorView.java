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
package org.valkyriercp.taskpane;

import org.valkyriercp.application.support.AbstractNavigatorView;
import org.valkyriercp.command.support.AbstractCommand;
import org.valkyriercp.command.support.CommandGroup;
import org.valkyriercp.command.support.CommandGroupJComponentBuilder;

public class TaskPaneNavigatorView extends AbstractNavigatorView
{
    private boolean onlyOneExpanded = true;

    private IconGenerator<AbstractCommand> iconGenerator;

    public TaskPaneNavigatorView(CommandGroup navigation)
    {
        super(navigation);
    }

    public boolean hasOnlyOneExpanded()
    {
        return onlyOneExpanded;
    }

    public void setOnlyOneExpanded(boolean onlyOneExpanded)
    {
        this.onlyOneExpanded = onlyOneExpanded;
    }

    public CommandGroupJComponentBuilder getNavigationBuilder()
    {
        JTaskPaneBuilder navigationBuilder = new JTaskPaneBuilder();
        navigationBuilder.setIconGenerator(getIconGenerator());
        navigationBuilder.setOnlyOneExpanded(onlyOneExpanded);
        return navigationBuilder;
    }

    public IconGenerator<AbstractCommand> getIconGenerator()
    {
        return iconGenerator;
    }

    public void setIconGenerator(IconGenerator<AbstractCommand> iconGenerator)
    {
        this.iconGenerator = iconGenerator;
    }
}
