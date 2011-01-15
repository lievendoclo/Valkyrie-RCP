package org.valkyriercp.form.binding.swing;

import org.springframework.util.Assert;
import org.valkyriercp.binding.form.FormModel;
import org.valkyriercp.binding.format.support.AbstractFormatterFactory;
import org.valkyriercp.form.binding.Binder;
import org.valkyriercp.form.binding.Binding;
import org.valkyriercp.form.binding.support.AbstractBinder;

import javax.swing.*;
import java.util.Map;

/**
 * @author Oliver Hutchison
 */
public class FormattedTextFieldBinder extends AbstractBinder {

    public static final String FORMATTER_FACTORY_KEY = "formatterFactory";

    public FormattedTextFieldBinder(Class requiredSourceClass) {
        super(requiredSourceClass, new String[] {FORMATTER_FACTORY_KEY});
    }

    protected Binding doBind(JComponent control, FormModel formModel, String formPropertyPath, Map context) {
        Assert.isTrue(control instanceof JFormattedTextField, "Control must be an instance of JFormattedTextField.");
        return new FormattedTextFieldBinding((JFormattedTextField)control, formModel, formPropertyPath,
                getRequiredSourceClass());
    }

    protected JComponent createControl(Map context) {
        return getComponentFactory().createFormattedTextField(
                (AbstractFormatterFactory)context.get(FORMATTER_FACTORY_KEY));
    }
}
