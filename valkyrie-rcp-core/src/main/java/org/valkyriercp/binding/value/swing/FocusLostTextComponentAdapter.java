package org.valkyriercp.binding.value.swing;

import org.springframework.util.Assert;
import org.valkyriercp.binding.value.ValueModel;
import org.valkyriercp.binding.value.support.AbstractValueModelAdapter;

import javax.swing.text.JTextComponent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

public class FocusLostTextComponentAdapter extends AbstractValueModelAdapter implements FocusListener {
    private final JTextComponent control;

    public FocusLostTextComponentAdapter(JTextComponent component, ValueModel valueModel) {
        super(valueModel);
        Assert.notNull(component);
        this.control = component;
        this.control.addFocusListener(this);
        initalizeAdaptedValue();
    }

    protected void valueModelValueChanged(Object value) {
        control.setText((String)value);
    }

    public void focusLost(FocusEvent e) {
        adaptedValueChanged(control.getText());
    }

    public void focusGained(FocusEvent e) {
    }
}
