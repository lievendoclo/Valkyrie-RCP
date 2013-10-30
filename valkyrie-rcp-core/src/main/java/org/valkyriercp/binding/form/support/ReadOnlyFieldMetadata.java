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