package org.valkyriercp.command.config;

import org.springframework.core.style.ToStringCreator;
import org.springframework.util.Assert;
import org.valkyriercp.command.support.AbstractCommand;
import org.valkyriercp.core.ColorConfigurable;
import org.valkyriercp.core.DescribedElement;
import org.valkyriercp.core.DescriptionConfigurable;
import org.valkyriercp.core.VisualizedElement;
import org.valkyriercp.core.support.AbstractPropertyChangePublisher;

import javax.swing.*;
import java.awt.*;

/**
 * A parameter object that contains the information to describe the visual representation of a 
 * command object.
 * 
 * 
 * @author Keith Donald
 */
public class CommandFaceDescriptor extends AbstractPropertyChangePublisher implements DescribedElement,
        VisualizedElement, CommandLabelConfigurable, DescriptionConfigurable, CommandIconConfigurable, ColorConfigurable {

    /** The property name used when firing events for the {@code labelInfo} property. */
    public static final String LABEL_INFO_PROPERTY = "labelInfo";

    /** The property name used when firing events for the {@code icon} property. */
    public static final String ICON_PROPERTY = "icon";

    /** The property name used when firing events for the {@code largeIcon} property. */
    public static final String LARGE_ICON_PROPERTY = "largeIcon";

    /** The property name used when firing events for the {@code iconInfo} property. */
    public static final String ICON_INFO_PROPERTY = "iconInfo";

    /** The property name used when firing events for the {@code largeIconInfo} property. */
    public static final String LARGE_ICON_INFO_PROPERTY = "largeIconInfo";
    
    /** The property name used when firing events for the {@code background} property. */
    public static final String BACKGROUND_PROPERTY = "background";
    
    /** The property name used when firing events for the {@code foreground} property. */
    public static final String FOREGROUND_PROPERTY = "foreground";

    private String caption;

    private String description;
    
    private Color background;
    
    private Color foreground;

    private CommandButtonLabelInfo labelInfo;

    private CommandButtonIconInfo iconInfo = CommandButtonIconInfo.BLANK_ICON_INFO;

    private CommandButtonIconInfo largeIconInfo = CommandButtonIconInfo.BLANK_ICON_INFO;

    /**
     * Creates a new {@code CommandFaceDescriptor} that uses the given encoded label descriptor
     * to provide the label properties. 
     *
     * @param encodedLabel The encoded label descriptor. May be null or empty to define a blank label.
     * 
     * @see CommandButtonLabelInfo#valueOf(String)
     */
    public CommandFaceDescriptor(String encodedLabel) {
        this(encodedLabel, null, null);
    }

    /**
     * Creates a new {@code CommandFaceDescriptor} that uses the given encoded label descriptor
     * to provide the label properties, along with the given icon and caption.
     *
     * @param encodedLabel The encoded label descriptor. May be null or empty.
     * @param icon The main default icon to be displayed by the command. May be null.
     * @param caption The caption to be displayed on rollover of the command. May be null or empty.
     * 
     * @see CommandButtonLabelInfo#valueOf(String)
     */
    public CommandFaceDescriptor(String encodedLabel, Icon icon, String caption) {
        this.labelInfo = CommandButtonLabelInfo.valueOf(encodedLabel);
        if (icon != null) {
            this.iconInfo = new CommandButtonIconInfo(icon);
        }
        this.caption = caption;
    }

    /**
     * Creates a new {@code CommandFaceDescriptor} with a blank label and no icon or caption.
     */
    public CommandFaceDescriptor() {
        this(CommandButtonLabelInfo.BLANK_BUTTON_LABEL);
    }

    /**
     * Creates a new {@code CommandFaceDescriptor} whose label information is provided by the 
     * given {@link CommandButtonLabelInfo} instance.
     *
     * @param labelInfo The label information for the command.
     */
    public CommandFaceDescriptor(CommandButtonLabelInfo labelInfo) {
        Assert.notNull(labelInfo, "The labelInfo property is required");
        this.labelInfo = labelInfo;
    }

    /**
     * Returns true if no command label information is provided by this descriptor. 
     *
     * @return true if there is no label information, false otherwise.
     */
    public boolean isBlank() {
        return labelInfo == CommandButtonLabelInfo.BLANK_BUTTON_LABEL;
    }

    /**
     * Returns the label text specified by this descriptor.
     *
     * @return The label text. May be null or empty.
     */
    public String getText() {
        return labelInfo.getText();
    }

    /**
     * {@inheritDoc}
     */
    public String getDisplayName() {
        return getText();
    }

    /**
     * {@inheritDoc}
     */
    public String getCaption() {
        return caption;
    }

    /**
     * {@inheritDoc}
     */
    public String getDescription() {
        return description;
    }

    /**
     * Returns the mnemonic to be associated with the command.
     *
     * @return The command mnemonic.
     */
    public int getMnemonic() {
        return labelInfo.getMnemonic();
    }

    /**
     * Returns the zero-based index of the mnemonic character within the label text associated with
     * the command. 
     *
     * @return The mnemonic index, or -1 if no mnemonic index is associated with the command.
     */
    public int getMnemonicIndex() {
        return labelInfo.getMnemonicIndex();
    }

    /**
     * {@inheritDoc}
     */
    public Image getImage() {
        
        if (this.iconInfo == null) {
            return null;
        }
        else {
            return iconInfo.getImage();
        }
        
    }

    /**
     * {@inheritDoc}
     */
    public Icon getIcon() {
        
        if (this.iconInfo == null) {
            return null;
        }
        else {
            return iconInfo.getIcon();
        }
        
    }

    /**
     * Returns the main default large icon associated with the command. 
     *
     * @return The large icon, or null.
     */
    public Icon getLargeIcon() {
        
        if (this.largeIconInfo == null) {
            return null;
        }
        else {
            return largeIconInfo.getIcon();
        }
        
    }

    /**
     * Returns the keystroke accelerator associated with the command.
     *
     * @return The keystroke accelerator, or null.
     */
    public KeyStroke getAccelerator() {
        return labelInfo.getAccelerator();
    }

    /**
     * Returns the command button label info object.
     *
     * @return The command button label info, or null.
     */
    protected CommandButtonLabelInfo getLabelInfo() {
        return labelInfo;
    }

    /**
     * Returns the label information for the command.
     *
     * @return The label information, never null.
     */
    protected CommandButtonIconInfo getIconInfo() {
        return iconInfo;
    }

    /**
     * Returns the large icon information object for the command.
     *
     * @return The large icon information, or null.
     */
    protected CommandButtonIconInfo getLargeIconInfo() {
        return largeIconInfo;
    }

    /**
     * {@inheritDoc}
     */
    public void setCaption(String shortDescription) {
        String old = this.caption;
        this.caption = shortDescription;
        firePropertyChange(DescribedElement.CAPTION_PROPERTY, old, this.caption);
    }

    /**
     * {@inheritDoc}
     */
    public void setDescription(String longDescription) {
        String old = this.description;
        this.description = longDescription;
        firePropertyChange(DescribedElement.DESCRIPTION_PROPERTY, old, this.description);
    }

    /**
     * {@inheritDoc}
     */
	public void setBackground(Color background) {
		Color old = this.background;
		this.background = background;
		firePropertyChange(BACKGROUND_PROPERTY, old, this.background);
	}

    /**
     * {@inheritDoc}
     */
	public void setForeground(Color foreground) {
		Color old = this.foreground;
		this.foreground = foreground;
		firePropertyChange(FOREGROUND_PROPERTY, old, this.foreground);		
	}

    /**
     * Sets the label information for the command using the given encoded label descriptor.
     *
     * @param encodedLabelInfo The encoded label descriptor. May be null or empty.
     * 
     * @see CommandButtonLabelInfo#valueOf(String)
     */
    public void setButtonLabelInfo(String encodedLabelInfo) {
        CommandButtonLabelInfo newLabelInfo = CommandButtonLabelInfo.valueOf(encodedLabelInfo);
        setLabelInfo(newLabelInfo);
    }

    /**
     * {@inheritDoc}
     */
    public void setLabelInfo(CommandButtonLabelInfo labelInfo) {
        if (labelInfo == null) {
            labelInfo = CommandButtonLabelInfo.BLANK_BUTTON_LABEL;
        }
        CommandButtonLabelInfo old = this.labelInfo;
        this.labelInfo = labelInfo;
        firePropertyChange(LABEL_INFO_PROPERTY, old, this.labelInfo);
    }

    /**
     * {@inheritDoc}
     */
    public void setIconInfo(CommandButtonIconInfo iconInfo) {
        if (iconInfo == null) {
            iconInfo = CommandButtonIconInfo.BLANK_ICON_INFO;
        }
        CommandButtonIconInfo old = this.iconInfo;
        this.iconInfo = iconInfo;
        firePropertyChange(ICON_INFO_PROPERTY, old, this.iconInfo);
    }

    /**
     * {@inheritDoc}
     */
    public void setLargeIconInfo(CommandButtonIconInfo largeIconInfo) {
        if (largeIconInfo == null) {
            largeIconInfo = CommandButtonIconInfo.BLANK_ICON_INFO;
        }
        CommandButtonIconInfo old = this.largeIconInfo;
        this.largeIconInfo = largeIconInfo;
        firePropertyChange(LARGE_ICON_INFO_PROPERTY, old, this.largeIconInfo);
    }

    /**
     * Set the main default icon to be associated with the command.
     *
     * @param icon The main default icon. May be null.
     */
    public void setIcon(Icon icon) {
        Icon old = null;
        if (iconInfo == CommandButtonIconInfo.BLANK_ICON_INFO) {
            if (icon != null) {
                // New IconInfo fires event
                setIconInfo(new CommandButtonIconInfo(icon));
            }
        }
        else {
            old = iconInfo.getIcon();
            this.iconInfo.setIcon(icon);
        }
        firePropertyChange(ICON_PROPERTY, old, icon);
    }

    /**
     * Sets the main default large icon for the command.
     *
     * @param icon The large icon. May be null.
     */
    public void setLargeIcon(Icon icon) {
        Icon old = null;
        if (largeIconInfo == CommandButtonIconInfo.BLANK_ICON_INFO) {
            if (icon != null) {
                // new IconInfo fires event
                setLargeIconInfo(new CommandButtonIconInfo(icon));
            }
        }
        else {
            old = largeIconInfo.getIcon();
            this.largeIconInfo.setIcon(icon);
        }
        firePropertyChange(LARGE_ICON_PROPERTY, old, icon);
    }

    /**
     * Configures the given button with the label information contained in this descriptor.
     *
     * @param button The button to be configured. Must not be null.
     * 
     * @throws IllegalArgumentException if {@code button} is null.
     */
    public void configureLabel(AbstractButton button) {
        Assert.notNull(button, "button");
        labelInfo.configure(button);
    }

    /**
     * Configures the given button with the icon information contained in this descriptor.
     *
     * @param button The button to be configured. Must not be null.
     * 
     * @throws IllegalArgumentException if {@code button} is null.
     */
    public void configureIcon(AbstractButton button) {
        Assert.notNull(button, "button");
        configureIconInfo(button, false);
    }

    /**
     * Configures the given button with the icon information contained in this descriptor.
     *
     * @param button The button to be configured. Must not be null.
     * @param useLargeIcons Set to true to configure the button with large icons. False will use
     * default size icons.
     * 
     * @throws IllegalArgumentException if {@code button} is null.
     */
    public void configureIconInfo(AbstractButton button, boolean useLargeIcons) {
        
        Assert.notNull(button, "button");
        
        if (useLargeIcons) {
            largeIconInfo.configure(button);
        }
        else {
            iconInfo.configure(button);
        }
    }
    
    /**
     * Configures the given button with colours for background and foreground if available.
     * 
     * @param button The button to be configured. Must not be null.
     */
    public void configureColor(AbstractButton button) {
        Assert.notNull(button, "button");
        if (foreground != null) {
        	button.setForeground(foreground);
        }
        if (background != null) {
        	button.setBackground(background);
        }
    }

    /**
     * Configures the given button and command using the given configurer and the information 
     * contained in this instance.
     *
     * @param button The button to be configured. Must not be null.
     * @param command The command to be configured. May be null.
     * @param configurer The configurer. Must not be null.
     * 
     * @throws IllegalArgumentException if {@code button} or {@code configurer} are null.
     */
    public void configure(AbstractButton button, AbstractCommand command, CommandButtonConfigurer configurer) {
        Assert.notNull(button, "button");
        Assert.notNull(configurer, "configurer");
        configurer.configure(button, command, this);
    }

    /**
     * Configures the given action with the information contained in this descriptor.
     *
     * @param action The action to be configured. Must not be null.
     * 
     * @throws IllegalArgumentException if {@code action} is null.
     */
    public void configure(Action action) {
        Assert.notNull(action, "The swing action to configure is required");
        action.putValue(Action.NAME, getText());
        action.putValue(Action.MNEMONIC_KEY, new Integer(getMnemonic()));
        action.putValue(Action.SMALL_ICON, getIcon());
        action.putValue("LargeIcon", getLargeIcon());
        action.putValue(Action.ACCELERATOR_KEY, getAccelerator());
        action.putValue(Action.SHORT_DESCRIPTION, caption);
        action.putValue(Action.LONG_DESCRIPTION, description);
    }

    /**
     * {@inheritDoc}
     */
    public String toString() {
        return new ToStringCreator(this).append("caption", caption).append("description", description).append(
                "buttonLabelInfo", labelInfo).append("buttonIconInfo", iconInfo).toString();
    }
}

