package org.valkyriercp.application.support;

import java.awt.*;

import javax.help.*;
import javax.swing.*;

import org.springframework.beans.factory.annotation.*;
import org.springframework.core.io.*;
import org.valkyriercp.application.*;
import org.valkyriercp.application.config.*;
import org.valkyriercp.util.*;

/**
 * A simple implementation of a help contents frame for an application using
 * javahelp.
 *
 * @author Keith Donald
 */
@Configurable
public class HelpContents {

    private Resource helpSetPath = new ClassPathResource("help/helpset.hs");
    private JFrame helpFrame;

    @Autowired
    private ApplicationConfig applicationConfig;


    public HelpContents() {
    	// empty
    }

    public void setHelpSetPath(Resource helpSetPath) {
        this.helpSetPath = helpSetPath;
    }

    protected String getApplicationName() {
    	ApplicationDescriptor appDesc = applicationConfig.applicationDescriptor();
        return appDesc.getCaption();
    }

    protected Image getApplicationImage() {
    	ApplicationDescriptor appDesc = applicationConfig.applicationDescriptor();
        return appDesc.getImage();
    }

    public void display(Window parent) {
        if (helpFrame == null) {
            helpFrame = new JFrame();
            helpFrame.getGlassPane().setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            try {
                HelpSet helpSet = new HelpSet(null, helpSetPath.getURL());
                JHelp jhelp = new JHelp(helpSet);
                helpFrame = new JFrame("Help - " + getApplicationName());
                helpFrame.getContentPane().add(jhelp);
                helpFrame.setIconImage(getApplicationImage());
                helpFrame.pack();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            helpFrame.getGlassPane().setCursor(Cursor.getDefaultCursor());
        }
        if (!helpFrame.isVisible()) {
        	WindowUtils.centerOnParent(helpFrame, parent);
            helpFrame.setVisible(true);
        }
        if ((helpFrame.getExtendedState() & Frame.NORMAL) == 0) {
            helpFrame.setExtendedState(Frame.NORMAL);
        }
        helpFrame.toFront();
    }

}