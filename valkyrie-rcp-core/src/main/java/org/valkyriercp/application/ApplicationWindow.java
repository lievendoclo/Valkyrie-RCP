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
package org.valkyriercp.application;

import org.jdesktop.swingx.JXFrame;
import org.valkyriercp.application.support.ApplicationWindowCommandManager;

import javax.swing.*;
import java.util.Iterator;

public interface ApplicationWindow {
    JFrame getControl();
    ApplicationPage getPage();
    void showPage(String id);
    void showPage(PageDescriptor pageDescriptor);
    void showPage(ApplicationPage page);
    boolean close();
    void setWindowManager(WindowManager windowManager);
    Iterator getSharedCommands();
    ApplicationWindowCommandManager getCommandManager();
    StatusBar getStatusBar();
    void enable();
    void disable();
    void addPageListener(PageListener listener);

    void removePageListener(PageListener listener);
}
