package org.valkyriercp.command.config;

import org.springframework.core.style.ToStringCreator;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;
import org.valkyriercp.core.ButtonConfigurer;
import org.valkyriercp.core.VisualizedElement;

import javax.swing.*;
import java.awt.*;

/**
 * A parameter object consisting of the various icons that may be displayed on a
 * single command button depending on its state.
 *
 * <p>
 * The set of states for which this object maintains icons are as follows:
 *
 * <ul>
 * <li>default</li>
 * <li>selected</li>
 * <li>disabled</li>
 * <li>pressed</li>
 * <li>rollover</li>
 * </ul>
 *
 * </p>
 *
 * @author Keith Donald
 *
 */
public class CommandButtonIconInfo implements ButtonConfigurer, VisualizedElement {

	/**
	 * A {@code CommandButtonIconInfo} instance that can be used for command
	 * buttons that have no icon information associated with them.
	 */
	// FIXME this is a mutable object so we probably shouldn't be reusing the
	// same instance for
	// a blank IconInfo. What happens if multiple blanks have been dished out
	// and one of them is modified?
	public static final CommandButtonIconInfo BLANK_ICON_INFO = new CommandButtonIconInfo(null);

	private Icon icon;

	private Icon selectedIcon;

	private Icon disabledIcon;

	private Icon pressedIcon;

	private Icon rolloverIcon;

	/**
	 * Creates a new {@code CommandButtonIconInfo} with the given icon. No
	 * optional icons will be associated with this instance.
	 *
	 * @param icon The main displayable icon.
	 */
	public CommandButtonIconInfo(Icon icon) {
		this.icon = icon;
	}

	/**
	 * Creates a new {@code CommandButtonIconInfo} with the given icons.
	 *
	 * @param icon The main default icon. May be null.
	 * @param selectedIcon The icon to be displayed when the command button is
	 * in a selected state. May be null.
	 */
	public CommandButtonIconInfo(Icon icon, Icon selectedIcon) {
		this.icon = icon;
		this.selectedIcon = selectedIcon;
	}

	/**
	 * Creates a new {@code CommandButtonIconInfo} with the given icons.
	 *
	 * @param icon The main default icon. May be null.
	 * @param selectedIcon The icon to be displayed when the command button is
	 * in a selected state.
	 * @param rolloverIcon The icon to be displayed when the mouse pointer rolls
	 * over the command button. May be null.
	 */
	public CommandButtonIconInfo(Icon icon, Icon selectedIcon, Icon rolloverIcon) {
		this.icon = icon;
		this.selectedIcon = selectedIcon;
		this.rolloverIcon = rolloverIcon;
	}

	/**
	 * Creates a new {@code CommandButtonIconInfo} with the given icons.
	 *
	 * @param icon The main default icon. May be null.
	 * @param selectedIcon The icon to be displayed when the command button is
	 * in a selected state.
	 * @param rolloverIcon The icon to be displayed when the mouse pointer rolls
	 * over the command button. May be null.
	 * @param disabledIcon The icon to be displayed when the command button is
	 * in a disabled state. May be null.
	 * @param pressedIcon The icon to be displayed when the command button is in
	 * a pressed state. May be null.
	 */
	public CommandButtonIconInfo(Icon icon, Icon selectedIcon, Icon rolloverIcon, Icon disabledIcon, Icon pressedIcon) {
		this.icon = icon;
		this.selectedIcon = selectedIcon;
		this.rolloverIcon = rolloverIcon;
		this.disabledIcon = disabledIcon;
		this.pressedIcon = pressedIcon;
	}

	/**
	 * Configures the given command button with the icon values from this
	 * instance.
	 *
	 * @param button The button to be configured with icons. Must not be null.
	 *
	 * @throws IllegalArgumentException if {@code button} is null.
	 */
	public AbstractButton configure(AbstractButton button) {
		Assert.notNull(button, "The button to configure is required");

		button.setIcon(icon);
		button.setSelectedIcon(selectedIcon);
		button.setDisabledIcon(disabledIcon);
		button.setPressedIcon(pressedIcon);
		button.setRolloverIcon(rolloverIcon);

		return button;
	}

	/**
	 * Returns the image from the main default icon of this instance if it is
	 * actually an instance of an {@link ImageIcon}.
	 *
	 * @return The image from the main default icon, or null if there is no
	 * default icon or it is not an {@link ImageIcon}.
	 */
	public Image getImage() {
		if (getIcon() instanceof ImageIcon) {
			return ((ImageIcon) getIcon()).getImage();
		}

		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	public Icon getIcon() {
		return icon;
	}

	/**
	 * Returns the icon to be displayed when the command button is in a disabled
	 * state.
	 *
	 * @return The icon for the command button in disabled state, or null.
	 */
	public Icon getDisabledIcon() {
		return disabledIcon;
	}

	/**
	 * Returns the icon to be displayed when the command button is in a pressed
	 * state.
	 *
	 * @return The icon for the command button in pressed state, or null.
	 */
	public Icon getPressedIcon() {
		return pressedIcon;
	}

	/**
	 * Returns the icon to be displayed when the mouse pointer rolls over the
	 * command button.
	 *
	 * @return The icon for the command button when rolled over by the mouse
	 * pointer, or null.
	 */
	public Icon getRolloverIcon() {
		return rolloverIcon;
	}

	/**
	 * Returns the icon to be displayed when the command button is in a selected
	 * state.
	 *
	 * @return The icon for the command button in selected state, or null.
	 */
	public Icon getSelectedIcon() {
		return selectedIcon;
	}

	/**
	 * Sets the main default icon for the command button.
	 *
	 * @param icon The main default icon for the command button. May be null.
	 */
	public void setIcon(Icon icon) {
		this.icon = icon;
	}

	/**
	 * Sets the icon to be displayed when the command button is in a disabled
	 * state.
	 *
	 * @param disabledIcon The icon for the button in a disabled state. May be
	 * null.
	 */
	public void setDisabledIcon(Icon disabledIcon) {
		this.disabledIcon = disabledIcon;
	}

	/**
	 * Sets the icon to be displayed when the command button is in a pressed
	 * state.
	 *
	 * @param pressedIcon The icon for the button in a pressed state. May be
	 * null.
	 */
	public void setPressedIcon(Icon pressedIcon) {
		this.pressedIcon = pressedIcon;
	}

	/**
	 * Sets the icon to be displayed when the mouse pointer rolls over the
	 * command button.
	 *
	 * @param rolloverIcon The icon for the button in a rolled over. May be
	 * null.
	 */
	public void setRolloverIcon(Icon rolloverIcon) {
		this.rolloverIcon = rolloverIcon;
	}

	/**
	 * Sets the icon to be displayed when the command button is in a pressed
	 * state.
	 *
	 * @param selectedIcon The icon for the button in a pressed state. May be
	 * null.
	 */
	public void setSelectedIcon(Icon selectedIcon) {
		this.selectedIcon = selectedIcon;
	}

    /**
	 * {@inheritDoc}
	 */
	public boolean equals(Object o) {
		if (!(o instanceof CommandButtonIconInfo)) {
			return false;
		}
		CommandButtonIconInfo other = (CommandButtonIconInfo) o;
		return ObjectUtils.nullSafeEquals(icon, other.icon)
				&& ObjectUtils.nullSafeEquals(disabledIcon, other.disabledIcon)
				&& ObjectUtils.nullSafeEquals(pressedIcon, other.pressedIcon)
				&& ObjectUtils.nullSafeEquals(rolloverIcon, other.rolloverIcon)
				&& ObjectUtils.nullSafeEquals(selectedIcon, other.selectedIcon);
	}

	/**
	 * {@inheritDoc}
	 */
	public int hashCode() {
		int hash = 1;
		hash = hash * 31 + (icon == null ? 0 : icon.hashCode());
		hash = hash * 31 + (disabledIcon == null ? 0 : disabledIcon.hashCode());
		hash = hash * 31 + (pressedIcon == null ? 0 : pressedIcon.hashCode());
		hash = hash * 31 + (rolloverIcon == null ? 0 : rolloverIcon.hashCode());
		hash = hash * 31 + (selectedIcon == null ? 0 : selectedIcon.hashCode());
		return hash;
	}

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "CommandButtonIconInfo{" +
                "icon=" + icon +
                ", selectedIcon=" + selectedIcon +
                ", disabledIcon=" + disabledIcon +
                ", pressedIcon=" + pressedIcon +
                ", rolloverIcon=" + rolloverIcon +
                '}';
    }
}
