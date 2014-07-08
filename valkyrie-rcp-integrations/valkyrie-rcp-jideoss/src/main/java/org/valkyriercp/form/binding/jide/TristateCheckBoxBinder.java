package org.valkyriercp.form.binding.jide;

import com.jidesoft.swing.TristateCheckBox;
import org.valkyriercp.binding.form.FormModel;
import org.valkyriercp.form.binding.Binder;
import org.valkyriercp.form.binding.Binding;

import javax.swing.*;
import java.util.Map;

public class TristateCheckBoxBinder implements Binder {
    @Override
    public Binding bind(FormModel formModel, String formPropertyPath, Map context) {
        return new TristateCheckBoxBinding(formModel, formPropertyPath);
    }

    @Override
    public Binding bind(JComponent control, FormModel formModel, String formPropertyPath, Map context) {
        if(control instanceof TristateCheckBox) {
            return new TristateCheckBoxBinding(formModel, formPropertyPath, (TristateCheckBox) control);
        } else {
            throw new IllegalArgumentException("component should be a TristateCheckBox");
        }
    }
}
