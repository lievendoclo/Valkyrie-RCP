package org.valkyriercp.binding.value.swing;

import org.valkyriercp.binding.value.ValueModel;

import javax.swing.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * Adapts a value model to a ButtonModel
 *
 * @author Oliver Hutchison
 */
public class SelectableButtonModelAdapter extends DefaultButtonModel implements PropertyChangeListener {
    private final ValueModel valueModel;

    public SelectableButtonModelAdapter(ValueModel valueModel) {
        this.valueModel = valueModel;
        this.valueModel.addValueChangeListener(this);
        propertyChange(null);
    }

    public void propertyChange(PropertyChangeEvent e) {
        Boolean selected = (Boolean)valueModel.getValue();
        setSelected(selected == null ? false : selected.booleanValue());
    }

    public void setPressed(boolean pressed) {
        if ((isPressed() == pressed) || !isEnabled()) {
            return;
        } else if (! pressed && isArmed()) {
            setSelected(!this.isSelected());
        }
        super.setPressed(pressed);
    }

    public void setSelected(boolean selected) {
        if (isSelected() == selected) {
            return;
        }
        super.setSelected(selected);
        valueModel.setValue(Boolean.valueOf(isSelected()));
    }
}
