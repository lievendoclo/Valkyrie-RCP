package org.valkyriercp.dialog.control;

import javax.swing.*;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * Represents a single tab in a tabbed pane.
 * <p>
 * TODO: move this to another package?
 *
 * @author Peter De Bruycker
 */
public class Tab {
	public static final String TITLE_PROPERTY = "title";
	public static final String ICON_PROPERTY = "icon";
	public static final String TOOLTIP_PROPERTY = "tooltip";
	public static final String COMPONENT_PROPERTY = "component";
	public static final String VISIBLE_PROPERTY = "visible";
	public static final String MNEMONIC_PROPERTY = "mnemonic";
	public static final String ENABLED_PROPERTY = "enabled";

	private String title;
	private Icon icon;
	private String tooltip;
	private JComponent component;
	private boolean visible = true;
	private int mnemonic;
	private boolean enabled = true;

	private PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);

	public Tab(String title, JComponent component) {
		this.title = title;
		this.component = component;
	}

	public Tab() {

	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		boolean old = this.visible;
		this.visible = visible;
		if (old != visible) {
			propertyChangeSupport.firePropertyChange(VISIBLE_PROPERTY, old, visible);
		}
	}

	public JComponent getComponent() {
		return component;
	}

	public void setComponent(JComponent component) {
		JComponent old = this.component;
		this.component = component;
		if (old != component) {
			propertyChangeSupport.firePropertyChange(COMPONENT_PROPERTY, old, component);
		}
	}

	public Icon getIcon() {
		return icon;
	}

	public void setIcon(Icon icon) {
		Icon old = this.icon;
		this.icon = icon;
		if (old != icon) {
			propertyChangeSupport.firePropertyChange(ICON_PROPERTY, old, icon);
		}
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		String old = this.title;
		this.title = title;
		if(old != null && !old.equals(title) || title != null) {
			propertyChangeSupport.firePropertyChange(TITLE_PROPERTY, old, title);
		}
	}

	public String getTooltip() {
		return tooltip;
	}

	public void setTooltip(String tooltip) {
		String old = this.tooltip;
		this.tooltip = tooltip;
		if(old != null && !old.equals(tooltip) || tooltip != null) {
			propertyChangeSupport.firePropertyChange(TOOLTIP_PROPERTY, old, tooltip);
		}
	}

	public void addPropertyChangeListener(PropertyChangeListener l) {
		propertyChangeSupport.addPropertyChangeListener(l);
	}

	public void addPropertyChangeListener(String propertyName, PropertyChangeListener l) {
		propertyChangeSupport.addPropertyChangeListener(propertyName, l);
	}

	public void removePropertyChangeListener(PropertyChangeListener l) {
		propertyChangeSupport.addPropertyChangeListener(l);
	}

	public void removePropertyChangeListener(String propertyName, PropertyChangeListener l) {
		propertyChangeSupport.addPropertyChangeListener(propertyName, l);
	}

	public void setMnemonic(int mnemonic) {
		int old = this.mnemonic;
		this.mnemonic = mnemonic;
		if(mnemonic != old)  {
			propertyChangeSupport.firePropertyChange(MNEMONIC_PROPERTY, old, mnemonic);
		}
	}

	public int getMnemonic() {
		return mnemonic;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		boolean old = this.enabled;
		this.enabled = enabled;
		if(enabled != old)  {
			propertyChangeSupport.firePropertyChange(ENABLED_PROPERTY, old, enabled);
		}
	}
}

