package org.valkyriercp.binding.form.support;

import org.springframework.beans.BeansException;
import org.valkyriercp.binding.PropertyAccessStrategy;
import org.valkyriercp.binding.PropertyMetadataAccessStrategy;
import org.valkyriercp.binding.form.FormModel;

import java.util.Map;

/**
 * Adapts the properties of <code>FormModel</code> so that they are accessible using the
 * <code>PropertyAccessStrategy</code> interface.
 *
 * @author Oliver Hutchison
 */
public class FormModelPropertyAccessStrategy implements PropertyAccessStrategy {

    private final FormModel formModel;

    public FormModelPropertyAccessStrategy(FormModel formModel) {
        this.formModel = formModel;
    }

    public Object getPropertyValue(String propertyPath) throws BeansException {
        return formModel.getValueModel(propertyPath).getValue();
    }

    public PropertyMetadataAccessStrategy getMetadataAccessStrategy() {
        return new FormModelPropertyMetadataAccessStrategy();
    }

    public Object getDomainObject() {
        return formModel.getFormObject();
    }

    private class FormModelPropertyMetadataAccessStrategy implements PropertyMetadataAccessStrategy {

        private FormModelPropertyMetadataAccessStrategy() {
        }

        /**
         * @return Always true, current implementation doesn't allow for non-readable properties.
         */
        public boolean isReadable(String propertyName) {
            return true;
        }

        /**
         * @return True if property isn't readOnly
         */
        public boolean isWriteable(String propertyName) {
            return !formModel.getFieldMetadata(propertyName).isReadOnly();
        }

        public Class getPropertyType(String propertyName) {
            return formModel.getFieldMetadata(propertyName).getPropertyType();
        }

        public Object getUserMetadata(String propertyName, String key) {
            return formModel.getFieldMetadata(propertyName).getUserMetadata(key);
        }

        public Map getAllUserMetadata(String propertyName) {
            return formModel.getFieldMetadata(propertyName).getAllUserMetadata();
        }
    }
}

