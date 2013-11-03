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
