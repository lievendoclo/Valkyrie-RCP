package org.valkyriercp.component;

import java.awt.Color;

import org.jdesktop.swingx.JXPanel;

/**
 * Configures the JXPanel used in TitlePane
 */
public interface TitlePaneConfigurer {

	public void configure(JXPanel pabel);

	public Color getBackgroundColor();

}
