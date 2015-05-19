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

