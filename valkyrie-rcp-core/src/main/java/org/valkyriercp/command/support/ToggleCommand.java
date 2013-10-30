package org.valkyriercp.command.support;

import org.springframework.util.Assert;
import org.valkyriercp.command.config.CommandButtonConfigurer;
import org.valkyriercp.command.config.CommandFaceDescriptor;
import org.valkyriercp.factory.ButtonFactory;
import org.valkyriercp.factory.MenuFactory;

import javax.swing.*;
import java.util.Iterator;

public abstract class ToggleCommand extends ActionCommand {

	public static final String SELECTED_PROPERTY = "selected";

	private boolean selected;

	private ExclusiveCommandGroupSelectionController exclusiveController;

	public ToggleCommand() {
	}

	public ToggleCommand(String commandId) {
		super(commandId);
	}

	public ToggleCommand(String id, CommandFaceDescriptor face) {
		super(id, face);
	}

	public ToggleCommand(String id, String encodedLabel) {
		super(id, encodedLabel);
	}

	public ToggleCommand(String id, String encodedLabel, Icon icon, String caption) {
		super(id, encodedLabel, icon, caption);
	}

	public void setExclusiveController(ExclusiveCommandGroupSelectionController exclusiveController) {
		this.exclusiveController = exclusiveController;
	}

	public boolean isExclusiveGroupMember() {
		return exclusiveController != null;
	}

	public JMenuItem createMenuItem(String faceDescriptorId, MenuFactory factory,
			CommandButtonConfigurer buttonConfigurer) {
		JMenuItem menuItem;
		if (isExclusiveGroupMember()) {
			menuItem = factory.createRadioButtonMenuItem();
		}
		else {
			menuItem = factory.createCheckBoxMenuItem();
		}
		attach(menuItem, faceDescriptorId, buttonConfigurer);
		return menuItem;
	}

	public AbstractButton createButton(String faceDescriptorId, ButtonFactory buttonFactory,
			CommandButtonConfigurer configurer) {
		AbstractButton button = buttonFactory.createToggleButton();
		attach(button, faceDescriptorId, configurer);
		return button;
	}

	public final AbstractButton createCheckBox() {
		return createCheckBox(getDefaultFaceDescriptorId(), getButtonFactory(), getDefaultButtonConfigurer());
	}

	public final AbstractButton createCheckBox(ButtonFactory buttonFactory) {
		return createCheckBox(getDefaultFaceDescriptorId(), buttonFactory, getDefaultButtonConfigurer());
	}

	public final AbstractButton createCheckBox(String faceDescriptorId, ButtonFactory buttonFactory) {
		return createCheckBox(faceDescriptorId, buttonFactory, getDefaultButtonConfigurer());
	}

	public AbstractButton createCheckBox(String faceDescriptorId, ButtonFactory buttonFactory,
			CommandButtonConfigurer configurer) {
		AbstractButton checkBox = buttonFactory.createCheckBox();
		attach(checkBox, configurer);
		return checkBox;
	}

	public final AbstractButton createRadioButton() {
		return createRadioButton(getDefaultFaceDescriptorId(), getButtonFactory(), getDefaultButtonConfigurer());
	}

	public final AbstractButton createRadioButton(ButtonFactory buttonFactory) {
		return createRadioButton(getDefaultFaceDescriptorId(), buttonFactory, getDefaultButtonConfigurer());
	}

	public final AbstractButton createRadioButton(String faceDescriptorId, ButtonFactory buttonFactory) {
		return createRadioButton(faceDescriptorId, buttonFactory, getDefaultButtonConfigurer());
	}

	public AbstractButton createRadioButton(String faceDescriptorId, ButtonFactory buttonFactory,
			CommandButtonConfigurer configurer) {
		Assert.state(isExclusiveGroupMember(),
                "Can't create radio buttons for toggle commands that aren't members of an exclusive group");
		AbstractButton radioButton = buttonFactory.createRadioButton();
		attach(radioButton, faceDescriptorId, configurer);
		return radioButton;
	}

	/**
	 * {@inheritDoc}
	 */
	protected void onButtonAttached(AbstractButton button) {
		super.onButtonAttached(button);
		button.setSelected(selected);
	}

	/**
	 * Returns <code>true</code> if the command is selected.
	 */
	public final boolean isSelected() {
		return this.selected;
	}

	/**
	 * Set the selection state of the command.
	 */
	public final void setSelected(boolean selected) {
		if (isExclusiveGroupMember()) {
			boolean oldState = isSelected();
			exclusiveController.handleSelectionRequest(this, selected);
			// set back button state if controller didn't change this command;
			// needed b/c of natural button check box toggling in swing
			if (oldState == isSelected()) {
				Iterator iter = buttonIterator();
				while (iter.hasNext()) {
					AbstractButton button = (AbstractButton)iter.next();
					button.setSelected(isSelected());
				}
			}
		}
		else {
			requestSetSelection(selected);
		}
	}

	/**
	 * Handles the switching of the selected state. All attached buttons are updated.
	 *
	 * @param selected select state to set.
	 * @return the select state afterwards.
	 */
	protected boolean requestSetSelection(boolean selected) {
		boolean previousState = isSelected();

		if (previousState != selected) {
			this.selected = onSelection(selected);
			if (logger.isDebugEnabled()) {
				logger.debug("Toggle command selection returned '" + this.selected + "'");
			}
		}

		// we must always update toggle buttons
		Iterator it = buttonIterator();
		if (logger.isDebugEnabled()) {
			logger.debug("Updating all attached toggle buttons to '" + isSelected() + "'");
		}
		while (it.hasNext()) {
			AbstractButton button = (AbstractButton)it.next();
			button.setSelected(isSelected());
		}

		if (previousState != isSelected()) {
			if (logger.isDebugEnabled()) {
				logger.debug("Selection changed; firing property change event");
			}
			firePropertyChange(SELECTED_PROPERTY, previousState, isSelected());
		}

		return isSelected();
	}

	/**
	 * Executing a toggleCommand will flip its select state.
	 */
	protected final void doExecuteCommand() {
		setSelected(!isSelected());
	}

	/**
	 * Hook method to perform the toggle action.  Subclasses may override.
	 * <p>
	 * The toggle selection request can be vetoed by returning a boolean result (for example if onSelected
	 * is handed 'true', signaling the toggle command was activated, a subclass can veto that by
	 * returning false.)
	 * @param selected The newly requested selection state of this toggle command
	 * @return the value of selected, if allowed, !selection if vetoed.
	 */
	protected boolean onSelection(boolean selected) {
		if (selected) {
			onSelection();
		}
		else {
			onDeselection();
		}
		return selected;
	}

	/**
	 * Convenience hook method for processing a selection action.
	 */
	protected void onSelection() {

	}

	/**
	 * Convenience hook method for processing a deselection action.
	 */
	protected void onDeselection() {

	}

	public void requestDefaultIn(RootPaneContainer container) {
		throw new UnsupportedOperationException();
	}

}
