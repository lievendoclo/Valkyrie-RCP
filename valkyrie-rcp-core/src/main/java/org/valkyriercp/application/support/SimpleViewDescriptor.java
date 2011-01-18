package org.valkyriercp.application.support;

import org.springframework.util.Assert;
import org.valkyriercp.application.ApplicationWindow;
import org.valkyriercp.application.PageComponent;
import org.valkyriercp.application.View;
import org.valkyriercp.application.ViewDescriptor;
import org.valkyriercp.binding.value.support.PropertyChangeSupport;
import org.valkyriercp.command.config.CommandButtonLabelInfo;
import org.valkyriercp.command.support.ActionCommand;
import org.valkyriercp.command.support.ShowViewCommand;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeListener;

/**
 * {@link ViewDescriptor} implementation for internal purposes (mostly testing).
 * <p>
 * This class accepts an existing {@link View} instance, and returns this in the {@link #createPageComponent()} method.
 * <p>
 * Normally you should never use this class directly.
 *
 * @author Peter De Bruycker
 */
public class SimpleViewDescriptor implements ViewDescriptor {

    private View view;
    private String id;
    private PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);

    public SimpleViewDescriptor(String id, View view) {
        Assert.notNull(view, "view cannot be null");

        this.id = id;
        this.view = view;

        view.setDescriptor(this);
    }

    public ActionCommand createShowViewCommand(ApplicationWindow window) {
        return new ShowViewCommand(this, window);
    }

    public CommandButtonLabelInfo getShowViewCommandLabel() {
        return new CommandButtonLabelInfo(getDisplayName());
    }

    public PageComponent createPageComponent() {
        return view;
    }

    public String getId() {
        return id;
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(listener);
    }

    public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(propertyName, listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.removePropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(String propertyName, PropertyChangeListener listener) {
        propertyChangeSupport.removePropertyChangeListener(propertyName, listener);
    }

    public String getCaption() {
        return id;
    }

    public String getDescription() {
        return id;
    }

    public String getDisplayName() {
        return id;
    }

    public Icon getIcon() {
        return null;
    }

    public Image getImage() {
        return null;
    }

}

