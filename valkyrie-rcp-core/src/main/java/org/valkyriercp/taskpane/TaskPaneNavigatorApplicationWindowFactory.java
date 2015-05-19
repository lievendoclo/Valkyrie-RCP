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
