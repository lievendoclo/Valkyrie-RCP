/**
 * Copyright (C) 2015 Valkyrie RCP
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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

