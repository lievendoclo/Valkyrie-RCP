package org.valkyriercp.application;

import org.jdesktop.swingx.JXFrame;
import org.valkyriercp.application.support.ApplicationWindowCommandManager;

import java.util.Iterator;

public interface ApplicationWindow {
    JXFrame getControl();
    ApplicationPage getPage();
    void showPage(String id);
    void showPage(PageDescriptor pageDescriptor);
    void showPage(ApplicationPage page);
    boolean close();
    void setWindowManager(WindowManager windowManager);
    public Iterator getSharedCommands();

    ApplicationWindowCommandManager getCommandManager();

    StatusBar getStatusBar();
}
