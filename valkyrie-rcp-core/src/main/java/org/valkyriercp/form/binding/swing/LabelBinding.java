package org.valkyriercp.form.binding.swing;

import org.valkyriercp.binding.form.FormModel;
import org.valkyriercp.form.binding.Binding;
import org.valkyriercp.form.binding.support.CustomBinding;

import javax.swing.*;

/**
 * @author Mauro Ransolin
 */
public class LabelBinding extends CustomBinding {

    private final JLabel label;

    public LabelBinding(JLabel label, FormModel formModel, String formPropertyPath) {
        super(formModel, formPropertyPath, String.class);
        this.label = label;
    }

    protected JComponent doBindControl() {
        label.setText((String)getValueModel().getValue());
        return label;
    }

    protected void readOnlyChanged() {
        label.setEnabled(isEnabled() && !isReadOnly());
    }

    protected void enabledChanged() {
        label.setEnabled(isEnabled() && !isReadOnly());
    }

    protected void valueModelChanged(Object newValue) {
        label.setText((String)newValue);
    }
}