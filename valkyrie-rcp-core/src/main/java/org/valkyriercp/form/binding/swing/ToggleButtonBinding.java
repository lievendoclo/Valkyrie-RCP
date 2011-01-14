package org.valkyriercp.form.binding.swing;

import org.valkyriercp.binding.form.FormModel;
import org.valkyriercp.form.binding.support.CustomBinding;

import javax.swing.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

/**
 * @author Mathias Broekelmann
 *
 */
public class ToggleButtonBinding extends CustomBinding {

    private final JToggleButton toggleButton;

    private ItemListener selectionListener = new SelectionListener();

    private boolean configureFace = true;

    public ToggleButtonBinding(JToggleButton toggleButton, FormModel formModel, String formPropertyPath) {
        super(formModel, formPropertyPath, Boolean.class);
        this.toggleButton = toggleButton;
    }

    protected JComponent doBindControl() {
        if(configureFace) {
            getFieldFace().configure(toggleButton);
        }
        toggleButton.getModel().addItemListener(selectionListener);
        toggleButton.setSelected(Boolean.TRUE.equals(getValue()));
        return toggleButton;
    }

    void setConfigureFace(boolean configureFace) {
        this.configureFace = configureFace;
    }

    protected void readOnlyChanged() {
        toggleButton.setEnabled(isEnabled() && !isReadOnly());
    }

    protected void enabledChanged() {
        toggleButton.setEnabled(isEnabled() && !isReadOnly());
    }

    protected void valueModelChanged(Object newValue) {
        toggleButton.setSelected(Boolean.TRUE.equals(newValue));
    }

    protected class SelectionListener implements ItemListener {

        public void itemStateChanged(ItemEvent e) {
            controlValueChanged(Boolean.valueOf(toggleButton.isSelected()));
        }

    }
}

