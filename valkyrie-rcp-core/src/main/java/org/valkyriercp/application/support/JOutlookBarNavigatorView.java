package org.valkyriercp.application.support;

import net.miginfocom.swing.MigLayout;
import org.valkyriercp.command.support.CommandGroup;
import org.valkyriercp.command.support.CommandGroupJComponentBuilder;
import org.valkyriercp.command.support.JOutlookBarBuilder;

import javax.swing.*;

public class JOutlookBarNavigatorView extends AbstractNavigatorView {
    private CommandGroup navigation;

    public JOutlookBarNavigatorView(CommandGroup navigation)
    {
        super(navigation);
        this.navigation = navigation;
    }

    public CommandGroupJComponentBuilder getNavigationBuilder()
    {
        return new JOutlookBarBuilder();
    }

    protected JComponent createControl()
    {
        JPanel navigationView = new JPanel(new MigLayout("fill"));
        navigationView.add(getNavigationBuilder().buildComponent(this.navigation), "grow,push");
        return navigationView;
    }
}
