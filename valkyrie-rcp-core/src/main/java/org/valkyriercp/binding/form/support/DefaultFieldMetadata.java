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
import org.valkyriercp.binding.value.DirtyTrackingValueModel;
import org.valkyriercp.core.support.AbstractPropertyChangePublisher;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import java.util.Map;

/**
 * Default implementation of FieldMetadata.
 * <p>
 * NOTE: This is a framework internal class and should not be
 * instantiated in user code.
 *
 * @author Oliver Hutchison
 */
public class DefaultFieldMetadata extends AbstractPropertyChangePublisher implements FieldMetadata {

    private final FormModel formModel;

    private final DirtyTrackingValueModel valueModel;

    private final Class propertyType;

    private final boolean forceReadOnly;

    private final Map userMetadata = new HashMap();

    private boolean oldReadOnly;

    private boolean readOnly;

    private boolean enabled = true;

    private boolean oldEnabled = true;

    private final DirtyChangeHandler dirtyChangeHandler = new DirtyChangeHandler();

    private final PropertyChangeListener formChangeHandler = new FormModelChangeHandler();

    /**
     * Constructs a new instance of DefaultFieldMetadata.
     *
     * @param formModel the form model
     * @param valueModel the value model for the property
     * @param propertyType the type of the property
     * @param forceReadOnly should readOnly be forced to true; this is
     *                      required if the property can not be modified. e.g.
     *                      at the PropertyAccessStrategy level.
     * @param userMetadata map using String keys containing user defined
     *                     metadata.  As an example, tiger extensions
     *                     currently use this to expose JDK 1.5 annotations on
     *                     the backing object as property metadata.  This
     *                     parameter may be <code>null</code>.
     */
    public DefaultFieldMetadata(FormModel formModel, DirtyTrackingValueModel valueModel, Class propertyType, boolean forceReadOnly, Map userMetadata) {
        this.formModel = formModel;
        this.valueModel = valueModel;
        this.valueModel.addPropertyChangeListener(DirtyTrackingValueModel.DIRTY_PROPERTY, dirtyChangeHandler);
        this.propertyType = propertyType;
        this.forceReadOnly = forceReadOnly;
        this.formModel.addPropertyChangeListener(ENABLED_PROPERTY, formChangeHandler);
        this.formModel.addPropertyChangeListener(READ_ONLY_PROPERTY, formChangeHandler);
        this.oldReadOnly = isReadOnly();
        this.oldEnabled = isEnabled();
        if(userMetadata != null) {
            this.userMetadata.putAll(userMetadata);
        }
    }

    public void setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;
        firePropertyChange(READ_ONLY_PROPERTY, oldReadOnly, isReadOnly());
        oldReadOnly = isReadOnly();
    }

    public boolean isReadOnly() {
        return forceReadOnly || readOnly || formModel.isReadOnly();
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        firePropertyChange(ENABLED_PROPERTY, oldEnabled, isEnabled());
        oldEnabled = isEnabled();
    }

    public boolean isEnabled() {
        return enabled && formModel.isEnabled();
    }

    public boolean isDirty() {
        return valueModel.isDirty();
    }

    public Class getPropertyType() {
        return propertyType;
    }

    public Object getUserMetadata(final String key) {
        return userMetadata.get(key);
    }

    public Map getAllUserMetadata() {
        return userMetadata;
    }

  /**
     * Sets custom metadata to be associated with this property.  A property
     * change event will be fired (from this FieldMetadata, not from the
     * associated form property) if <code>value</code> differs from the
     * current value of the specified <code>key</code>.  The property change
     * event will use the value of <code>key</code> as the property name in
     * the property change event.
     *
     * @param key
     * @param value
     */
    public void setUserMetadata(final String key, final Object value) {
        final Object old = userMetadata.put(key, value);
        firePropertyChange(key, old, value);
    }

    /**
     * Clears all custom metadata associated with this property.  A property
     * change event will be fired for every <code>key</code> that contained a
     * non-null value before this method was invoked.  It is possible for a
     * PropertyChangeListener to mutate user metadata, by setting a key value
     * for example, in response to one of these property change events fired
     * during the course of the clear operation.  Because of this, there is
     * no guarantee that all user metadata is in fact completely clear and
     * empty by the time this method returns.
     */
    public void clearUserMetadata() {
        // Copy keys into array to avoid concurrent modification exceptions
        // if any PropertyChangeListeners should modify user metadata during
        // clear operation.
        final Object[] keys = userMetadata.keySet().toArray();
        for(int i = keys.length - 1;i >= 0;i--) {
            final Object old = userMetadata.remove(keys[i]);
            if(old != null) {
                firePropertyChange((String)keys[i], old, null);
            }
        }
    }

  /**
     * Propagates dirty changes from the value model on to
     * the dirty change listeners attached to this class.
     */
  private class DirtyChangeHandler extends CommitListenerAdapter implements PropertyChangeListener {

        public void propertyChange(PropertyChangeEvent evt) {
            firePropertyChange(DIRTY_PROPERTY, evt.getOldValue(), evt.getNewValue());
        }
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
            else if (FormModel.READONLY_PROPERTY.equals(evt.getPropertyName())) {
                firePropertyChange(READ_ONLY_PROPERTY, Boolean.valueOf(oldReadOnly), Boolean.valueOf(isReadOnly()));
                oldReadOnly = isReadOnly();
            }
        }
    }
}
