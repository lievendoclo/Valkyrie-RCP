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
package org.valkyriercp.binding.value.support;

import org.valkyriercp.binding.value.ValueModel;
import org.valkyriercp.binding.value.ValueModelWrapper;

import java.beans.PropertyChangeListener;

/**
 * @author Keith Donald
 */
public abstract class AbstractValueModelWrapper implements ValueModel, ValueModelWrapper {
    private final ValueModel wrappedModel;

    public AbstractValueModelWrapper(ValueModel valueModel) {
        this.wrappedModel = valueModel;
    }

    public Object getValue() {
        return wrappedModel.getValue();
    }

    public final void setValue(Object value) {
        setValueSilently(value, null);
    }

    public void setValueSilently(Object value, PropertyChangeListener listenerToSkip) {
        wrappedModel.setValueSilently(value, listenerToSkip);
    }

    public ValueModel getWrappedValueModel() {
        return wrappedModel;
    }

    public ValueModel getInnerMostWrappedValueModel() {
        if (wrappedModel instanceof ValueModelWrapper)
            return ((ValueModelWrapper)wrappedModel).getInnerMostWrappedValueModel();

        return wrappedModel;
    }

    public Object getInnerMostValue() {
        if (wrappedModel instanceof ValueModelWrapper)
            return ((ValueModelWrapper)wrappedModel).getInnerMostWrappedValueModel().getValue();

        return wrappedModel.getValue();
    }

    public void addValueChangeListener(PropertyChangeListener listener) {
        wrappedModel.addValueChangeListener(listener);
    }

    public void removeValueChangeListener(PropertyChangeListener listener) {
        wrappedModel.removeValueChangeListener(listener);
    }
}
