package org.valkyriercp.application.support;

import org.jdesktop.swingx.JXPanel;
import org.valkyriercp.application.ApplicationPage;
import org.valkyriercp.application.config.ApplicationConfig;

import javax.swing.*;
import java.awt.*;

public class DefaultApplicationWindow extends AbstractApplicationWindow {
    private JXPanel panel;

    public DefaultApplicationWindow(ApplicationConfig config) {
        super(config);
    }

    public DefaultApplicationWindow( int number,ApplicationConfig config ) {
        super(number, config);
    }

    protected JComponent createWindowContentPane(){
        panel = new JXPanel(new BorderLayout());
        return panel;
    }

    protected void setActivePage( ApplicationPage page ) {
        panel.removeAll();
        panel.add(page.getControl());
        panel.validate();
    }


}
