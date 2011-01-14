package org.valkyriercp.form.binding.swing;

import org.springframework.util.Assert;
import org.valkyriercp.binding.form.FormModel;
import org.valkyriercp.form.binding.Binding;
import org.valkyriercp.form.binding.support.AbstractBinder;

import javax.swing.*;
import java.util.Map;

/**
 * @author Mathias Broekelmann
 *
 */
public class ToggleButtonBinder extends AbstractBinder {

    public ToggleButtonBinder() {
        this(Boolean.class);
    }

    protected ToggleButtonBinder(String[] supportedContextKeys) {
        super(Boolean.class, supportedContextKeys);
    }

    protected ToggleButtonBinder(Class requiredSourceClass) {
        super(requiredSourceClass);
    }

    protected ToggleButtonBinder(Class requiredSourceClass, String[] supportedContextKeys) {
        super(requiredSourceClass, supportedContextKeys);
    }

    protected Binding doBind(JComponent control, FormModel formModel, String formPropertyPath, Map context) {
        Assert.isTrue(control instanceof JToggleButton, "Control must be an instance of JToggleButton.");
        ToggleButtonBinding toggleButtonBinding = new ToggleButtonBinding((JToggleButton) control, formModel,
                formPropertyPath);
        applyContext(toggleButtonBinding, formModel, formPropertyPath, context);
        return toggleButtonBinding;
    }

    protected void applyContext(ToggleButtonBinding toggleButtonBinding, FormModel formModel, String formPropertyPath,
            Map context) {
    }

    protected JComponent createControl(Map context) {
        return getComponentFactory().createToggleButton("");
    }

}

