package org.valkyriercp.application.support;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import net.miginfocom.swing.MigLayout;
import org.valkyriercp.command.support.CommandGroup;
import org.valkyriercp.command.support.CommandGroupJComponentBuilder;

import javax.swing.*;

public abstract class AbstractNavigatorView extends AbstractView
{
    private CommandGroup currentNavigation;

    protected AbstractNavigatorView(CommandGroup currentNavigation)
    {
        super("abstractNavigatorView");
        this.currentNavigation = currentNavigation;
    }

    public abstract CommandGroupJComponentBuilder getNavigationBuilder();

    protected JComponent createControl()
    {
        JPanel navigationView = new JPanel(new MigLayout("fill"));
        navigationView.add(getNavigationBuilder().buildComponent(this.currentNavigation), "grow,push");
        return navigationView;
    }
}

