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
package org.valkyriercp.command.support;

import org.jdesktop.swingx.JXFrame;
import org.valkyriercp.application.ApplicationException;
import org.valkyriercp.application.ApplicationWindow;
import org.valkyriercp.util.ValkyrieRepository;

import javax.swing.*;
import java.awt.*;

public class ChangeLookAndFeelCommand extends ActionCommand {
    private String lookAndFeelClass;

    public ChangeLookAndFeelCommand(String id, String lookAndFeelClass) {
        super(id);
        this.lookAndFeelClass = lookAndFeelClass;
    }

    @Override
    protected void doExecuteCommand() {
        try {
            UIManager.setLookAndFeel(lookAndFeelClass);
            for(ApplicationWindow appWindow : ValkyrieRepository.getInstance().getApplicationConfig().windowManager().getWindows()) {
                JFrame control = appWindow.getControl();
                SwingUtilities.updateComponentTreeUI(control);
                for(Window window : control.getOwnedWindows()) {
                    updateWindow(window);
                }
            }

        } catch (Exception e) {
            throw new ApplicationException("Error changing look and feel");
        }
    }

    private void updateWindow(Window window) {
        SwingUtilities.updateComponentTreeUI(window);
        for(Window subWindow : window.getOwnedWindows()) {
            updateWindow(subWindow);
        }
    }
}
