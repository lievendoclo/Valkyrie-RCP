package org.valkyriercp.core;

import java.awt.*;

/**
 * Implemented by objects that may have a coloured foreground or background.
 *
 * @author Jan Hoskens
 *
 */
public interface ColorConfigurable {

	/**
	 * Set the background colour.
	 *
	 * @param background the colour to be used as background.
	 */
	void setBackground(Color background);

	/**
	 * Set the foreground colour.
	 *
	 * @param foreground the colour to be used as foreground.
	 */
	void setForeground(Color foreground);
}