package org.valkyriercp.command.support;

import org.jdesktop.swingx.JXFrame;
import org.valkyriercp.application.ApplicationException;
import org.valkyriercp.application.ApplicationWindow;

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
            for(ApplicationWindow appWindow : applicationConfig.windowManager().getWindows()) {
                JXFrame control = appWindow.getControl();
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
