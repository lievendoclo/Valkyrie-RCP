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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.valkyriercp.binding.form.FormModel;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * A helper implementation for binding to custom controls.
 *
 * @author Oliver Hutchison
 */
public abstract class CustomBinding extends AbstractBinding {

    private Logger logger = LoggerFactory.getLogger(getClass());

    private final ValueModelChangeHandler valueModelChangeHandler;

    /** Possible forced read-only. */
    private boolean readOnly = false;

    protected CustomBinding(FormModel formModel, String formPropertyPath, Class requiredSourceClass) {
        super(formModel, formPropertyPath, requiredSourceClass);
        valueModelChangeHandler = new ValueModelChangeHandler();
        getValueModel().addValueChangeListener(valueModelChangeHandler);
    }

    /**
     * Called when the underlying property's value model value changes.
     */
    protected abstract void valueModelChanged(Object newValue);

    /**
     * Should be called when the bound component's value changes.
     */
    protected final void controlValueChanged(Object newValue) {
        getValueModel().setValueSilently(newValue, valueModelChangeHandler);
    }

    private class ValueModelChangeHandler implements PropertyChangeListener {
        public void propertyChange(PropertyChangeEvent evt) {
            if (logger.isDebugEnabled()) {
                logger.debug("Notifying binding of value model value changed");
            }
            valueModelChanged(getValue());
        }
    }

    /**
     * Force this binding to be readonly, whatever the metaInfo.
     *
     * @param readOnly <code>true</code> if only read-access should be allowed.
     */
    public void setReadOnly(boolean readOnly)
    {
        this.readOnly = readOnly;
        readOnlyChanged();
    }

    /**
     * We were using an override to check the setter's visibility on the backing object.
     */
    @Override
    protected boolean isReadOnly()
    {
        return this.readOnly || super.isReadOnly();
    }
}
