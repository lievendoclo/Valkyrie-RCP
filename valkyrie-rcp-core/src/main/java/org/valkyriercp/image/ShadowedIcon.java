package org.valkyriercp.image;

import javax.swing.*;
import java.awt.*;

/**
 * Code taken from
 * http://www.jroller.com/santhosh/entry/beautify_swing_applications_toolbar_with
 *
 * @author Santhosh Kumar
 */
public class ShadowedIcon implements Icon {
	private int shadowWidth = 2;

	private int shadowHeight = 2;

	private Icon icon, shadow;

	public ShadowedIcon(Icon icon) {
		this.icon = icon;
		shadow = new ImageIcon(GrayFilter.createDisabledImage(((ImageIcon) icon).getImage()));
	}

	public ShadowedIcon(Icon icon, int shadowWidth, int shadowHeight) {
		this(icon);
		this.shadowWidth = shadowWidth;
		this.shadowHeight = shadowHeight;
	}

	public int getIconHeight() {
		return icon.getIconWidth() + shadowWidth;
	}

	public int getIconWidth() {
		return icon.getIconHeight() + shadowHeight;
	}

	public void paintIcon(Component c, Graphics g, int x, int y) {
		shadow.paintIcon(c, g, x + shadowWidth, y + shadowHeight);
		icon.paintIcon(c, g, x, y);
	}
}

