package org.valkyriercp.test;

import javax.swing.*;
import java.awt.*;

/**
 * Icon suitable for testing purposes.
 *
 * @author Peter De Bruycker
 */
public class TestIcon implements Icon {

	private Color color;

	public TestIcon(Color color) {
		this.color = color;
	}

	public int getIconHeight() {
		return 16;
	}

	public int getIconWidth() {
		return 16;
	}

	public void paintIcon(Component c, Graphics g, int x, int y) {
		Graphics2D graphics = (Graphics2D) g.create();

		graphics.setColor(color);
		graphics.fillRect(x, y, 16, 16);

		graphics.dispose();
	}

}
