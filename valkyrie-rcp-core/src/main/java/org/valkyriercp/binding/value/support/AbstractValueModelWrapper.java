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
