package org.valkyriercp.form.binding.swing;

import org.valkyriercp.binding.form.FormModel;
import org.valkyriercp.binding.value.ValueModel;
import org.valkyriercp.binding.value.swing.FormattedTextFieldAdapter;
import org.valkyriercp.binding.value.swing.ValueCommitPolicy;
import org.valkyriercp.form.binding.support.AbstractBinding;

import javax.swing.*;

public class FormattedTextFieldBinding extends AbstractBinding {

    private final JFormattedTextField formattedTextField;

    public FormattedTextFieldBinding(JFormattedTextField formattedTextField, FormModel formModel,
            String formPropertyPath, Class requiredSourceClass) {
        super(formModel, formPropertyPath, requiredSourceClass);
        this.formattedTextField = formattedTextField;
    }

    protected JComponent doBindControl() {
        final ValueModel valueModel = getValueModel();
        formattedTextField.setValue(valueModel.getValue());
        new FormattedTextFieldAdapter(formattedTextField, valueModel, ValueCommitPolicy.AS_YOU_TYPE);
        return formattedTextField;
    }

    protected void readOnlyChanged() {
        formattedTextField.setEditable(!isReadOnly());
    }

    protected void enabledChanged() {
        formattedTextField.setEnabled(isEnabled());
    }
}