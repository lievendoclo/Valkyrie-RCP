package org.valkyriercp.binding.value.support;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.valkyriercp.binding.value.ValueModel;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * Abstract base class for objects that adapt a value model to some other
 * model. e.g. a GUI component.
 *
 * @author Oliver Hutchison
 */
public abstract class AbstractValueModelAdapter {
    protected static final Log logger = LogFactory.getLog(AbstractValueModelAdapter.class);

    private final ValueModelChangeHandler valueModelChangeHandler = new ValueModelChangeHandler();

    private ValueModel valueModel;

    public AbstractValueModelAdapter(ValueModel valueModel) {
        this.valueModel = valueModel;
        this.valueModel.addValueChangeListener(valueModelChangeHandler);
    }

    /**
     * Must be called to initialize the adapted value. Usually the
     * last call in the constructor.
     */
    protected void initalizeAdaptedValue() {
        valueModelValueChanged(valueModel.getValue());
    }

    protected ValueModel getValueModel() {
        return valueModel;
    }

    /**
     * Subclasses must called this when the value being adapted has changed.
     *
     * @param newValue the new adapted value
     */
    protected void adaptedValueChanged(Object newValue) {
        if (valueModel != null) {
            valueModel.setValueSilently(newValue, valueModelChangeHandler);
        }
    }

    /**
     * Called when the value held by the value model has changes
     *
     * @param newValue the new value held by the value model
     */
    protected abstract void valueModelValueChanged(Object newValue);

    private class ValueModelChangeHandler implements PropertyChangeListener {
        public void propertyChange(PropertyChangeEvent evt) {
            valueModelValueChanged(valueModel.getValue());
        }
    }
}
