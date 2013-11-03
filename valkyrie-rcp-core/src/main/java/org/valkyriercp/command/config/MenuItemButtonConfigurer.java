package org.valkyriercp.command.config;

import org.valkyriercp.command.support.AbstractCommand;

import javax.swing.*;

/**
 * <code>CommandButtonConfigurer</code> implementation for menu items.
 * <p>
 * Sets the tooltip text of menu items to <code>null</code>.
 *
 * @author Keith Donald
 */
public class MenuItemButtonConfigurer extends DefaultCommandButtonConfigurer {
	public void configure(AbstractButton button, AbstractCommand command, CommandFaceDescriptor faceDescriptor) {
		super.configure(button, command, faceDescriptor);
		button.setToolTipText(null);
	}
}