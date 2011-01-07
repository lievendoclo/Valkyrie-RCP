package org.valkyriercp.command.support;

import org.springframework.util.Assert;
import org.valkyriercp.command.GroupContainerPopulator;
import org.valkyriercp.command.config.CommandButtonConfigurer;

import javax.swing.*;
import java.awt.*;
import java.util.*;

/**
 * A implementation of the {@link GroupMember} interface that can be associated
 * with instances of {@link Component}s.
 *
 * @author Nils Olsson
 */
public class ComponentGroupMember extends GroupMember {

	private Component component;

	/**
	 * Creates a new {@code ComponentGroupMember}.
	 * @param component The component that this group member represents.
	 */
	public ComponentGroupMember(Component component) {
		Assert.notNull(component, "component");
		this.component = component;
	}

	/**
	 * Forwards the enabled flag to the managed component.
	 * @param enabled The enabled flag.
	 */
	public void setEnabled(boolean enabled) {
		component.setEnabled(enabled);
	}

	/**
	 * Searches through the component and nested components in order to see if
	 * the command with supplied id exists in this component.
	 *
	 * @param commandId The id of the command to be checked for. May be null.
	 * @return true if the component, or any of its nested components, is a
	 * command with the given command id.
	 */
	public boolean managesCommand(String commandId) {
		if (null != commandId) {
			return managesCommand(component, commandId);
		}
		else {
			return false;
		}
	}

	/**
	 * Searches through the component and nested components in order to see if
	 * the command with supplied id exists in this component.
	 *
	 * @param component The component that should be searched.
	 * @param commandId The id of the command to be checked for.
	 * @return true if the component, or any of its nested components, is a
	 * command with the given command id.
	 */
	private boolean managesCommand(Component component, String commandId) {
		if (component instanceof AbstractButton) {
			AbstractButton button = (AbstractButton) component;
			if (null != button.getActionCommand()) {
				if (button.getActionCommand().equals(commandId)) {
					return true;
				}
			}
		}
		else if (component instanceof Container) {
			Component[] subComponents = ((Container) component).getComponents();
			for (int i = 0; i < subComponents.length; ++i) {
				if (managesCommand(subComponents[i], commandId)) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Asks the given container populator to add a component to its underlying
	 * container.
	 */
	protected void fill(GroupContainerPopulator containerPopulator, Object controlFactory,
			CommandButtonConfigurer buttonConfigurer, java.util.List previousButtons) {
		Assert.notNull(containerPopulator, "containerPopulator");
		containerPopulator.add(component);
	}
}
