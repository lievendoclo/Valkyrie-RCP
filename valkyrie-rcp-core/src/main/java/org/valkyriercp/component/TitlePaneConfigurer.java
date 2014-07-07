package org.valkyriercp.component;

import org.jdesktop.swingx.JXPanel;

import java.awt.*;

/**
 * Configures the JXPanel used in TitlePane
 */
public interface TitlePaneConfigurer {

    public void configure(JXPanel pabel);

    public Color getBackgroundColor();

}