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
package org.valkyriercp.command.config;

import org.valkyriercp.command.support.AbstractCommand;
import org.valkyriercp.image.ShadowedIcon;

import javax.swing.*;
import java.awt.*;

/**
 * Custom <code>CommandButtonConfigurer</code> for buttons on the toolbar.
 * <p>
 * Configurable Properties: <table border="1">
 * <tr>
 * <td><b>Property</b></td>
 * <td><b>Default</b></td>
 * <td><b>Purpose</b></td>
 * </tr>
 * <tr>
 * <td><code>showText</code></td>
 * <td>false</td>
 * <td>determines whether text is shown</td>
 * </tr>
 * <tr>
 * <td><code>textBelowIcon</code></td>
 * <td>true</td>
 * <td>indicates whether the text is shown below the icon (as is default in
 * most applications)</td>
 * </tr>
 * <tr>
 * <td><code>enableShadow</code></td>
 * <td>false</td>
 * <td>toggles shadow effect on rollover. If the icon already had a rollover
 * icon attached, no shadow effect is applied</td>
 * </tr>
 * </table>
 *
 * @author Keith Donald
 * @author Peter De Bruycker
 */
public class ToolBarCommandButtonConfigurer extends DefaultCommandButtonConfigurer {
	private boolean showText = false;

	private boolean textBelowIcon = true;

	private boolean enableShadow = false;

	public boolean isEnableShadow() {
		return enableShadow;
	}

	public void setEnableShadow(boolean enableShadow) {
		this.enableShadow = enableShadow;
	}

	public void setTextBelowIcon(boolean textBelowIcon) {
		this.textBelowIcon = textBelowIcon;
	}

	public boolean isTextBelowIcon() {
		return textBelowIcon;
	}

	public void setShowText(boolean showText) {
		this.showText = showText;
	}

	public boolean isShowText() {
		return showText;
	}

	public void configure(AbstractButton button, AbstractCommand command, CommandFaceDescriptor faceDescriptor) {
		super.configure(button, command, faceDescriptor);

		if (textBelowIcon) {
			button.setHorizontalTextPosition(JButton.CENTER);
			button.setVerticalTextPosition(JButton.BOTTOM);
		}

		if (!showText) {
			if (button.getIcon() != null) {
				button.setText(null);
			}
		}

		if (enableShadow && button.getIcon() != null && button.getRolloverIcon() == null) {
			button.setRolloverEnabled(true);
			button.setRolloverIcon(new ShadowedIcon(button.getIcon()));
		}

		button.setMargin(new Insets(2, 5, 2, 5));
	}
}

