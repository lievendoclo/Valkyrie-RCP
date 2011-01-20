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
