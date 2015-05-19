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
package org.valkyriercp.core.support;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.ObjectUtils;
import org.valkyriercp.core.PropertyChangePublisher;

import java.beans.*;

/**
 * Base implementation of the {@link PropertyChangePublisher} interface
 * providing basic listener support.
 */
public abstract class AbstractPropertyChangePublisher implements PropertyChangePublisher {

	protected final Log logger = LogFactory.getLog(getClass());

	private transient PropertyChangeSupport changeSupport;

	private transient VetoableChangeSupport vetoSupport;

	public final void addPropertyChangeListener(PropertyChangeListener listener) {
		if (listener == null) {
			return;
		}
		if (changeSupport == null) {
			changeSupport = new PropertyChangeSupport(this);
		}
		changeSupport.addPropertyChangeListener(listener);
	}

	public final void removePropertyChangeListener(PropertyChangeListener listener) {
		if (listener == null || changeSupport == null) {
			return;
		}
		changeSupport.removePropertyChangeListener(listener);
	}

	public final void addPropertyChangeListener(String propertyName, PropertyChangeListener listener) {
		if (listener == null) {
			return;
		}
		if (changeSupport == null) {
			changeSupport = new PropertyChangeSupport(this);
		}
		changeSupport.addPropertyChangeListener(propertyName, listener);
	}

	public final void removePropertyChangeListener(String propertyName, PropertyChangeListener listener) {
		if (listener == null || changeSupport == null) {
			return;
		}
		changeSupport.removePropertyChangeListener(propertyName, listener);
	}

	public final void addVetoableChangeListener(VetoableChangeListener listener) {
		if (listener == null) {
			return;
		}
		if (vetoSupport == null) {
			vetoSupport = new VetoableChangeSupport(this);
		}
		vetoSupport.addVetoableChangeListener(listener);
	}

	public final void removeVetoableChangeListener(VetoableChangeListener listener) {
		if (listener == null || vetoSupport == null) {
			return;
		}
		vetoSupport.removeVetoableChangeListener(listener);
	}

	public final void addVetoableChangeListener(String propertyName, VetoableChangeListener listener) {
		if (listener == null) {
			return;
		}
		if (vetoSupport == null) {
			vetoSupport = new VetoableChangeSupport(this);
		}
		vetoSupport.addVetoableChangeListener(propertyName, listener);
	}

	public final void removeVetoableChangeListener(String propertyName, VetoableChangeListener listener) {
		if (listener == null || vetoSupport == null) {
			return;
		}
		vetoSupport.removeVetoableChangeListener(propertyName, listener);
	}

	public final PropertyChangeListener[] getPropertyChangeListeners() {
		if (changeSupport == null) {
			return new PropertyChangeListener[0];
		}
		return changeSupport.getPropertyChangeListeners();
	}

	public final PropertyChangeListener[] getPropertyChangeListeners(String propertyName) {
		if (changeSupport == null) {
			return new PropertyChangeListener[0];
		}
		return changeSupport.getPropertyChangeListeners(propertyName);
	}

	public final VetoableChangeListener[] getVetoableChangeListeners() {
		if (vetoSupport == null) {
			return new VetoableChangeListener[0];
		}
		return vetoSupport.getVetoableChangeListeners();
	}

	public final VetoableChangeListener[] getVetoableChangeListeners(String propertyName) {
		if (vetoSupport == null) {
			return new VetoableChangeListener[0];
		}
		return vetoSupport.getVetoableChangeListeners(propertyName);
	}

	protected final void firePropertyChange(String propertyName, Object oldValue, Object newValue) {
		PropertyChangeSupport aChangeSupport = this.changeSupport;
		if (aChangeSupport == null) {
			return;
		}
		aChangeSupport.firePropertyChange(propertyName, oldValue, newValue);
	}

	protected final void firePropertyChange(String propertyName, boolean oldValue, boolean newValue) {
		PropertyChangeSupport aChangeSupport = this.changeSupport;
		if (aChangeSupport == null) {
			return;
		}
		aChangeSupport.firePropertyChange(propertyName, oldValue, newValue);
	}

	protected final void firePropertyChange(String propertyName, double oldValue, double newValue) {
		firePropertyChange(propertyName, new Double(oldValue), new Double(newValue));
	}

	protected final void firePropertyChange(String propertyName, float oldValue, float newValue) {
		firePropertyChange(propertyName, new Float(oldValue), new Float(newValue));
	}

	protected final void firePropertyChange(String propertyName, int oldValue, int newValue) {
		PropertyChangeSupport aChangeSupport = this.changeSupport;
		if (aChangeSupport == null) {
			return;
		}
		aChangeSupport.firePropertyChange(propertyName, oldValue, newValue);
	}

	protected final void firePropertyChange(String propertyName, long oldValue, long newValue) {
		firePropertyChange(propertyName, new Long(oldValue), new Long(newValue));
	}

	protected final void firePropertiesChanged() {
		firePropertyChange(null, null, null);
	}

	protected final void fireVetoableChange(String propertyName, Object oldValue, Object newValue)
			throws PropertyVetoException {
		VetoableChangeSupport aVetoSupport = this.vetoSupport;
		if (aVetoSupport == null) {
			return;
		}
		aVetoSupport.fireVetoableChange(propertyName, oldValue, newValue);
	}

	protected final void fireVetoableChange(String propertyName, boolean oldValue, boolean newValue)
			throws PropertyVetoException {
		VetoableChangeSupport aVetoSupport = this.vetoSupport;
		if (aVetoSupport == null) {
			return;
		}
		aVetoSupport.fireVetoableChange(propertyName, oldValue, newValue);
	}

	protected final void fireVetoableChange(String propertyName, double oldValue, double newValue)
			throws PropertyVetoException {
		fireVetoableChange(propertyName, new Double(oldValue), new Double(newValue));
	}

	protected final void fireVetoableChange(String propertyName, int oldValue, int newValue)
			throws PropertyVetoException {
		VetoableChangeSupport aVetoSupport = this.vetoSupport;
		if (aVetoSupport == null) {
			return;
		}
		aVetoSupport.fireVetoableChange(propertyName, oldValue, newValue);
	}

	protected final void fireVetoableChange(String propertyName, float oldValue, float newValue)
			throws PropertyVetoException {
		fireVetoableChange(propertyName, new Float(oldValue), new Float(newValue));
	}

	protected final void fireVetoableChange(String propertyName, long oldValue, long newValue)
			throws PropertyVetoException {
		fireVetoableChange(propertyName, new Long(oldValue), new Long(newValue));
	}

	protected boolean hasChanged(Object currentValue, Object proposedValue) {
		return !ObjectUtils.nullSafeEquals(currentValue, proposedValue);
	}

	protected final boolean hasChanged(boolean currentValue, boolean proposedValue) {
		return currentValue != proposedValue;
	}

	protected final boolean hasChanged(int currentValue, int proposedValue) {
		return currentValue != proposedValue;
	}

	protected final boolean hasChanged(long currentValue, long proposedValue) {
		return currentValue != proposedValue;
	}

	protected final boolean hasChanged(float currentValue, float proposedValue) {
		return currentValue != proposedValue;
	}

	protected final boolean hasChanged(double currentValue, double proposedValue) {
		return currentValue != proposedValue;
	}

}
