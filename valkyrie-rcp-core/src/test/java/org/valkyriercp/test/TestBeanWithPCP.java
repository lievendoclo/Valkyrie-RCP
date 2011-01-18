package org.valkyriercp.test;

import org.valkyriercp.binding.value.support.PropertyChangeSupport;
import org.valkyriercp.core.PropertyChangePublisher;

import java.beans.PropertyChangeListener;

/**
 * @author Oliver Hutchison
 */
public class TestBeanWithPCP implements PropertyChangePublisher {
    private PropertyChangeSupport pcs = new PropertyChangeSupport(this);

    private Object boundProperty;

    public Object getBoundProperty() {
        return boundProperty;
    }

    public void setBoundProperty(Object boundProperty) {
        Object oldBoundProperty = this.boundProperty;
        this.boundProperty = boundProperty;
        pcs.firePropertyChange("boundProperty", oldBoundProperty, boundProperty);
    }

    public PropertyChangeListener[] getPropertyChangeListeners(String propertyName) {
        return pcs.getPropertyChangeListeners(propertyName);
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(listener);
    }

    public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(propertyName, listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        pcs.removePropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(String propertyName, PropertyChangeListener listener) {
        pcs.removePropertyChangeListener(propertyName, listener);
    }
}
