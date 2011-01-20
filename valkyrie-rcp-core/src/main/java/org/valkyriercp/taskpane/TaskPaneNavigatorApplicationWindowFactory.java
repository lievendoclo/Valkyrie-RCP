package org.valkyriercp.taskpane;

import org.springframework.beans.factory.annotation.Autowired;
import org.valkyriercp.application.ApplicationWindow;
import org.valkyriercp.application.ApplicationWindowFactory;
import org.valkyriercp.application.config.ApplicationConfig;
import org.valkyriercp.application.config.ApplicationLifecycleAdvisor;
import org.valkyriercp.command.support.AbstractCommand;

public class TaskPaneNavigatorApplicationWindowFactory implements ApplicationWindowFactory {
    private IconGenerator<AbstractCommand> taskPaneIconGenerator;

    @Autowired
    private ApplicationLifecycleAdvisor lifecycleAdvisor;

    @Autowired
    private ApplicationConfig applicationConfig;

    public ApplicationWindow createApplicationWindow() {
        TaskPaneNavigatorApplicationWindow window = new TaskPaneNavigatorApplicationWindow(applicationConfig);
        window.setTaskPaneIconGenerator(getTaskPaneIconGenerator());
        return window;
    }

    public IconGenerator<AbstractCommand> getTaskPaneIconGenerator() {
        return taskPaneIconGenerator;
    }

    public void setTaskPaneIconGenerator(IconGenerator<AbstractCommand> taskPaneIconGenerator) {
        this.taskPaneIconGenerator = taskPaneIconGenerator;
    }
}
