package org.valkyriercp.application.support;

import net.miginfocom.swing.MigLayout;
import org.valkyriercp.command.support.CommandGroup;
import org.valkyriercp.command.support.CommandGroupJComponentBuilder;
import org.valkyriercp.command.support.JXTaskPaneBuilder;

import javax.swing.*;

public class JXTaskPaneNavigatorView extends AbstractNavigatorView
{

    private CommandGroup navigation;

    public JXTaskPaneNavigatorView(CommandGroup navigation)
    {
        super(navigation);
        this.navigation = navigation;
    }

    public CommandGroupJComponentBuilder getNavigationBuilder()
    {
        return new JXTaskPaneBuilder();
    }

     protected JComponent createControl()
    {
        JPanel navigationView = new JPanel(new MigLayout("fill"));
        navigationView.add(getNavigationBuilder().buildComponent(this.navigation), "grow,push");
        return navigationView;
    }
}
