package org.valkyriercp.taskpane;

import org.valkyriercp.application.ApplicationPage;
import org.valkyriercp.application.config.ApplicationConfig;
import org.valkyriercp.application.support.DefaultApplicationWindow;
import org.valkyriercp.command.support.AbstractCommand;
import org.valkyriercp.command.support.CommandGroup;

import javax.swing.*;

public class TaskPaneNavigatorApplicationWindow extends DefaultApplicationWindow
{
    private JSplitPane framedPage;

    private boolean onlyOneExpanded;

    private IconGenerator<AbstractCommand> taskPaneIconGenerator;

    public TaskPaneNavigatorApplicationWindow(ApplicationConfig config) {
        super(config);
    }

    @Override
    protected JComponent createWindowContentPane()
    {
        CommandGroup navigationCommandGroup = getAdvisor()
                .getNavigationCommandGroup();
        TaskPaneNavigatorView taskPaneNavigatorView = new TaskPaneNavigatorView(navigationCommandGroup);
        taskPaneNavigatorView.setIconGenerator(getTaskPaneIconGenerator());
        taskPaneNavigatorView.setOnlyOneExpanded(onlyOneExpanded);

        framedPage = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true, new JScrollPane(taskPaneNavigatorView.getControl(), JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER),
                null);
        framedPage.setOneTouchExpandable(false);

        return framedPage;
    }

    public boolean hasOnlyOneExpanded()
    {
        return onlyOneExpanded;
    }

    public void setOnlyOneExpanded(boolean onlyOneExpanded)
    {
        this.onlyOneExpanded = onlyOneExpanded;
    }

    @Override
    protected void setActivePage(ApplicationPage page)
    {
        framedPage.setRightComponent(page.getControl());
        framedPage.revalidate();
    }

    public IconGenerator<AbstractCommand> getTaskPaneIconGenerator()
    {
        return taskPaneIconGenerator;
    }

    public void setTaskPaneIconGenerator(IconGenerator<AbstractCommand> taskPaneIconGenerator)
    {
        this.taskPaneIconGenerator = taskPaneIconGenerator;
    }
}

