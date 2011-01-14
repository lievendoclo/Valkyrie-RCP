package org.valkyriercp.binding.value.swing;

import org.valkyriercp.binding.value.ValueModel;
import org.valkyriercp.binding.value.support.AbstractValueModelAdapter;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * Adapts a value model to a JSpinner control.
 *
 * @author Oliver Hutchison
 */
public class SpinnerAdapter extends AbstractValueModelAdapter {

    private final SpinnerChangeListener listener = new SpinnerChangeListener();

    private final JSpinner spinner;

    public SpinnerAdapter(JSpinner spinner, ValueModel valueModel) {
        super(valueModel);
        this.spinner = spinner;
        this.spinner.addChangeListener(listener);
        initalizeAdaptedValue();
    }

    protected void valueModelValueChanged(Object newValue) {
        if (newValue == null) {
            spinner.setValue(new Integer(0));
        }
        else {
            spinner.setValue(newValue);
        }
    }

    private class SpinnerChangeListener implements ChangeListener {
        public void stateChanged(ChangeEvent e) {
            adaptedValueChanged(spinner.getValue());
        }
    }
}
