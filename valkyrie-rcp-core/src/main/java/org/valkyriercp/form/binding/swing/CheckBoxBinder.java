package org.valkyriercp.form.binding.swing;

import org.valkyriercp.binding.form.FormModel;
import org.valkyriercp.form.binding.Binder;

import javax.swing.*;
import java.util.Map;

/**
 * @author Oliver Hutchison
 */
public class CheckBoxBinder extends ToggleButtonBinder {

    public CheckBoxBinder() {
        super();
    }

    protected CheckBoxBinder(Class requiredSourceClass, String[] supportedContextKeys) {
        super(requiredSourceClass, supportedContextKeys);
    }

    protected CheckBoxBinder(Class requiredSourceClass) {
        super(requiredSourceClass);
    }

    protected CheckBoxBinder(String[] supportedContextKeys) {
        super(supportedContextKeys);
    }

    protected JComponent createControl(Map context) {
        return getComponentFactory().createCheckBox("");
    }

    protected void applyContext(ToggleButtonBinding toggleButtonBinding, FormModel formModel, String formPropertyPath, Map context) {
        super.applyContext(toggleButtonBinding, formModel, formPropertyPath, context);
        toggleButtonBinding.setConfigureFace(false);
    }
}