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

import com.l2fprod.common.swing.JLinkButton;
import com.l2fprod.common.swing.JTaskPane;
import com.l2fprod.common.swing.JTaskPaneGroup;
import org.valkyriercp.command.support.AbstractCommand;
import org.valkyriercp.command.support.CommandGroup;
import org.valkyriercp.command.support.CommandGroupJComponentBuilder;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class JTaskPaneBuilder extends CommandGroupJComponentBuilder
{
    private boolean onlyOneExpanded = true;
    private JTaskPaneCommandButtonConfigurer configurer = new JTaskPaneCommandButtonConfigurer();

    private IconGenerator<AbstractCommand> iconGenerator;

    protected JComponent buildRootComponent(AbstractCommand command)
    {
        JTaskPane pane = new JTaskPane();
        pane.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        pane.setOpaque(false);
        return pane;
    }

    protected JComponent buildChildComponent(JComponent parentComponent, AbstractCommand command, int level)
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

    public boolean hasOnlyOneExpanded()
    {
        return onlyOneExpanded;
    }

    public void setOnlyOneExpanded(boolean onlyOneExpanded)
    {
        this.onlyOneExpanded = onlyOneExpanded;
    }

    protected JComponent buildGroupComponent(JComponent parentComponent, CommandGroup command, int level)
    {
        if (level == 0)
            return parentComponent;
        Icon icon;
        if(command.getIcon() == null && iconGenerator != null)
        {
            icon = iconGenerator.generateIcon(command);
        }
        else
        {
            icon = command.getIcon();
        }

        if (parentComponent instanceof JTaskPaneGroup)
        {
            final JTaskPaneGroup parent = (JTaskPaneGroup) parentComponent;
            final JTaskPaneGroup group = new JTaskPaneGroup();
            group.setTitle(command.getText());
            group.setIcon(icon);
            group.setExpanded(false);

            if (hasOnlyOneExpanded())
            {
                group.addPropertyChangeListener(JTaskPaneGroup.EXPANDED_CHANGED_KEY, new PropertyChangeListener()
                {
                    public void propertyChange(PropertyChangeEvent evt)
                    {
                        if ((Boolean) evt.getNewValue())
                        {
                            Component[] comps = parent.getComponents();
                            for (int i = 0; i < comps.length; i++)
                            {
                                if (comps[i] instanceof JTaskPaneGroup && comps[i] != group)
                                {
                                    JTaskPaneGroup g = ((JTaskPaneGroup) comps[i]);
                                    if (g.isExpanded())
                                    {
                                        g.setExpanded(false);
                                    }
                                }
                            }
                        }
                    }
                });
            }

            parent.add(group);

            return group;
        }
        else
        {
            final JTaskPane parent = (JTaskPane) parentComponent;
            final JTaskPaneGroup group = new JTaskPaneGroup();
            group.setTitle(command.getText());
            group.setIcon(icon);
            group.setExpanded(false);

            if (hasOnlyOneExpanded())
            {
                group.addPropertyChangeListener(JTaskPaneGroup.EXPANDED_CHANGED_KEY, new PropertyChangeListener()
                {
                    public void propertyChange(PropertyChangeEvent evt)
                    {
                        if ((Boolean) evt.getNewValue())
                        {
                            Component[] comps = parent.getComponents();
                            for (int i = 0; i < comps.length; i++)
                            {
                                if (comps[i] instanceof JTaskPaneGroup && comps[i] != group)
                                {
                                    JTaskPaneGroup g = ((JTaskPaneGroup) comps[i]);
                                    if (g.isExpanded())
                                    {
                                        g.setExpanded(false);
                                    }
                                }
                            }
                        }
                    }
                });
            }

            parent.add(group);

            return group;
        }
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
