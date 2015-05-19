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

import com.l2fprod.common.swing.JLinkButton;
import org.jdesktop.swingx.JXTaskPane;
import org.jdesktop.swingx.JXTaskPaneContainer;
import org.valkyriercp.taskpane.JTaskPaneCommandButtonConfigurer;

import javax.swing.*;

public class JXTaskPaneBuilder extends CommandGroupJComponentBuilder
{
    private JTaskPaneCommandButtonConfigurer configurer = new JTaskPaneCommandButtonConfigurer();

    protected JComponent buildRootComponent(AbstractCommand command)
    {
        JXTaskPaneContainer pane = new JXTaskPaneContainer();
        pane.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        pane.setOpaque(false);
        return pane;
    }

    protected JComponent buildChildComponent(JComponent parentComponent, final AbstractCommand command, int level)
    {
        if (command != null)
        {
            JLinkButton button = new JLinkButton();
            command.attach(button, configurer);
            parentComponent.add(button);
            return button;
        }
        return null;
    }

    protected JComponent buildGroupComponent(JComponent parentComponent, CommandGroup command, int level)
    {
        if (level == 0)
            return parentComponent;

        Icon icon;
        icon = command.getIcon();

        if (parentComponent instanceof JXTaskPaneContainer)
        {
            final JXTaskPaneContainer parent = (JXTaskPaneContainer) parentComponent;
            final JXTaskPane group = new JXTaskPane();
            group.setTitle(command.getText());
            group.setIcon(icon);
            group.setCollapsed(true);

            parent.add(group);

            return group;
        }
        else
        {
            final JXTaskPane parent = (JXTaskPane) parentComponent;
            final JXTaskPane group = new JXTaskPane();
            group.setTitle(command.getText());
            group.setIcon(icon);
            group.setCollapsed(true);

            parent.add(group);

            return group;
        }
    }
}
