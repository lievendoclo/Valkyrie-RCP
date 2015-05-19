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
