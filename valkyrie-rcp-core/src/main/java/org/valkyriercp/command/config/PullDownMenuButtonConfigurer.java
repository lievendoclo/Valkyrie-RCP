package org.valkyriercp.command.config;

import org.valkyriercp.command.support.AbstractCommand;
import org.valkyriercp.image.ArrowIcon;

import javax.swing.*;
import java.awt.*;

/**
 * <code>CommandButtonConfigurer</code> for pulldown menu buttons.
 * <p>
 * Sets a custom icon (arrow down), and the text is shown before the icon.
 *
 * @author Keith Donald
 */
public final class PullDownMenuButtonConfigurer extends DefaultCommandButtonConfigurer {
	private static final ArrowIcon PULL_DOWN_ICON = new ArrowIcon(ArrowIcon.Direction.DOWN, 3, Color.BLACK);

	public void configure(AbstractButton button, AbstractCommand command, CommandFaceDescriptor faceDescriptor) {
		super.configure(button, command, faceDescriptor);
		button.setIcon(PULL_DOWN_ICON);
		button.setHorizontalTextPosition(SwingConstants.LEADING);
	}
}