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
package org.valkyriercp.form.binding.support;

import org.springframework.util.Assert;
import org.valkyriercp.binding.form.FieldFace;
import org.valkyriercp.binding.form.FieldMetadata;
import org.valkyriercp.binding.form.FormModel;
import org.valkyriercp.binding.value.ValueModel;
import org.valkyriercp.factory.AbstractControlFactory;
import org.valkyriercp.form.binding.Binding;

import javax.swing.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * Default base implementation of <code>Binding</code>. Provides helper methods for
 * access to commonly needed properties.
 *
 * @author Oliver Hutchison
 */
public abstract class AbstractBinding extends AbstractControlFactory implements Binding {

    protected final FormModel formModel;

    protected final String formPropertyPath;

    protected final FieldMetadata fieldMetadata;

    private final FieldMetadataChangeHandler fieldMetadataChangeHandler = new FieldMetadataChangeHandler();

    private final Class requiredSourceClass;

    protected AbstractBinding(FormModel formModel, String formPropertyPath, Class requiredSourceClass) {
        this.formModel = formModel;
        this.formPropertyPath = formPropertyPath;
        this.fieldMetadata = this.formModel.getFieldMetadata(formPropertyPath);
        this.requiredSourceClass = requiredSourceClass;
        fieldMetadata.addPropertyChangeListener(FieldMetadata.ENABLED_PROPERTY, fieldMetadataChangeHandler);
        fieldMetadata.addPropertyChangeListener(FieldMetadata.READ_ONLY_PROPERTY, fieldMetadataChangeHandler);
    }

    public String getProperty() {
        return formPropertyPath;
    }

    public FormModel getFormModel() {
        return formModel;
    }

    protected FieldFace getFieldFace() {
        return formModel.getFieldFace(formPropertyPath);
    }

    protected Class getPropertyType() {
        return fieldMetadata.getPropertyType();
    }

    protected JComponent createControl() {
        JComponent control = doBindControl();
        control.setName(getProperty());
        readOnlyChanged();
        enabledChanged();
        return control;
    }

    protected abstract JComponent doBindControl();

    /**
     * Called when the read only state of the bound property changes.
     */
    protected abstract void readOnlyChanged();

    /**
     * Called when the enabled state of the bound property changes.
     */
    protected abstract void enabledChanged();

    /**
     * Is the bound property in the read only state.
     */
    protected boolean isReadOnly() {
        return fieldMetadata.isReadOnly();
    }

    /**
     * Is the bound property in the enabled state.
     */
    protected boolean isEnabled() {
        return fieldMetadata.isEnabled();
    }

    protected ValueModel getValueModel() {
        ValueModel valueModel = (requiredSourceClass == null) ? formModel.getValueModel(formPropertyPath)
                : formModel.getValueModel(formPropertyPath, requiredSourceClass);
        Assert.notNull(valueModel, "Unable to locate value model for property '" + formPropertyPath + "'.");
        return valueModel;
    }

    protected Object getValue() {
        return getValueModel().getValue();
    }

    private class FieldMetadataChangeHandler implements PropertyChangeListener {
        public void propertyChange(PropertyChangeEvent evt) {
            if (FieldMetadata.ENABLED_PROPERTY.equals(evt.getPropertyName())) {
                enabledChanged();
            }
            else if (FieldMetadata.READ_ONLY_PROPERTY.equals(evt.getPropertyName())) {
                readOnlyChanged();
            }
        }
    }
}

