package org.valkyriercp.command.support;

import com.l2fprod.common.swing.JLinkButton;
import com.l2fprod.common.swing.JTaskPaneGroup;
import org.jdesktop.swingx.JXTaskPane;
import org.jdesktop.swingx.JXTaskPaneContainer;
import org.valkyriercp.command.support.AbstractCommand;
import org.valkyriercp.command.support.CommandGroup;
import org.valkyriercp.command.support.CommandGroupJComponentBuilder;
import org.valkyriercp.taskpane.JTaskPaneCommandButtonConfigurer;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

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
