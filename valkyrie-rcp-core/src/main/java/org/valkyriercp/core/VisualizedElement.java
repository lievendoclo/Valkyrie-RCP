package org.valkyriercp.core;

import javax.swing.*;
import java.awt.*;

public interface VisualizedElement {
    /**
	 * Returns the image associated with the application component, or null.
	 */
	public Image getImage();

	/**
	 * Returns the icon associated with the application component, or null.
	 */
	public Icon getIcon();

}
