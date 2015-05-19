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
package org.valkyriercp.binding.form.support;

import org.valkyriercp.binding.form.FieldMetadata;
import org.valkyriercp.binding.form.FormModel;
import org.valkyriercp.core.support.AbstractPropertyChangePublisher;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import java.util.Map;

/**
 * FieldMetadata implementation for read-only properties
 */
public class ReadOnlyFieldMetadata extends AbstractPropertyChangePublisher implements FieldMetadata {

	private Class propertyType;

	private boolean enabled = true;

	private boolean oldEnabled;

	private FormModel formModel;

	private final PropertyChangeListener formChangeHandler = new FormModelChangeHandler();

    private final Map userMetadata = new HashMap();

	public ReadOnlyFieldMetadata(FormModel formModel, Class propertyType) {
		this(formModel, propertyType, null);
	}

	public ReadOnlyFieldMetadata(FormModel formModel, Class propertyType, Map userMetadata) {
		this.propertyType = propertyType;
		this.formModel = formModel;
		this.formModel.addPropertyChangeListener(ENABLED_PROPERTY, formChangeHandler);
        if(userMetadata != null) {
            this.userMetadata.putAll(userMetadata);
        }
	}

	public Map getAllUserMetadata() {
		return userMetadata;
	}

	public Class getPropertyType() {
		return propertyType;
	}

	public Object getUserMetadata(String key) {
		return userMetadata.get(key);
	}

	public boolean isDirty() {
		return false;
	}

    public boolean isEnabled() {
        return enabled && formModel.isEnabled();
    }

	public boolean isReadOnly() {
		return true;
	}

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        firePropertyChange(ENABLED_PROPERTY, oldEnabled, isEnabled());
        oldEnabled = isEnabled();
    }

	public void setReadOnly(boolean readOnly) {
	}

    /**
     * Responsible for listening for changes to the enabled
     * property of the FormModel
     */
    private class FormModelChangeHandler implements PropertyChangeListener {
        public void propertyChange(PropertyChangeEvent evt) {
            if (FormModel.ENABLED_PROPERTY.equals(evt.getPropertyName())) {
                firePropertyChange(ENABLED_PROPERTY, Boolean.valueOf(oldEnabled), Boolean.valueOf(isEnabled()));
                oldEnabled = isEnabled();
            }
        }
    }
}