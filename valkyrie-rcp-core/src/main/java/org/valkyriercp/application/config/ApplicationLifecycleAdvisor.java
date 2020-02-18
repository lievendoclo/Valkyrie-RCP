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
package org.valkyriercp.application.config;

import org.valkyriercp.application.ApplicationWindow;
import org.valkyriercp.application.ApplicationWindowConfigurer;
import org.valkyriercp.application.PageDescriptor;
import org.valkyriercp.application.StatusBar;
import org.valkyriercp.application.exceptionhandling.RegisterableExceptionHandler;
import org.valkyriercp.application.session.ApplicationSessionInitializer;
import org.valkyriercp.application.support.ApplicationWindowCommandManager;
import org.valkyriercp.command.support.CommandGroup;

import java.util.function.Consumer;
import java.util.function.Supplier;

public interface ApplicationLifecycleAdvisor {
    PageDescriptor getStartingPageDescriptor();

    void setStartingPageDescriptor(Supplier<PageDescriptor> descriptor);

    void onPreStartup();

    void onPostStartup();

    void setOnWindowCreated(Consumer<ApplicationWindow> fn);

    void setOnWindowOpened(Consumer<ApplicationWindow> fn);

    Consumer<ApplicationWindow> getOnWindowCreated();

    Consumer<ApplicationWindow> getOnWindowOpened();

    boolean onPreWindowClose(ApplicationWindow window);

    void onCommandsCreated(ApplicationWindow window);

    void onPreWindowOpen(ApplicationWindowConfigurer configurer);

    void setOpeningWindow(ApplicationWindow window);

    ApplicationWindowCommandManager createWindowCommandManager();

    CommandGroup getMenuBarCommandGroup();

    CommandGroup getToolBarCommandGroup();

    CommandGroup getNavigationCommandGroup();

    StatusBar getStatusBar();

    void onShutdown();

    ApplicationSessionInitializer getApplicationSessionInitializer();

    RegisterableExceptionHandler getRegisterableExceptionHandler();
}
