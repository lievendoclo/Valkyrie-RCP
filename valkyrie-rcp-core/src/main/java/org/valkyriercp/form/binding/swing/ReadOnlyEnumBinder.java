package org.valkyriercp.form.binding.swing;

import org.valkyriercp.binding.form.FormModel;
import org.valkyriercp.form.binding.Binder;
import org.valkyriercp.form.binding.Binding;

import javax.swing.*;
import java.util.Map;

public class ReadOnlyEnumBinder implements Binder
{

    private Class requiredClass;

    public Class getRequiredClass()
    {
        return requiredClass;
    }

    public void setRequiredClass(Class requiredClass)
    {
        this.requiredClass = requiredClass;
    }

    @Override
    public Binding bind(FormModel formModel, String formPropertyPath, Map context)
    {
        if (requiredClass == null)
        {
            return new ReadOnlyEnumBinding(formModel, formPropertyPath);
        }
        else
        {
            return new ReadOnlyEnumBinding(formModel, formPropertyPath, requiredClass);
        }
    }

    @Override
    public Binding bind(JComponent control, FormModel formModel, String formPropertyPath, Map context)
    {
        if (control instanceof JTextField)
        {
            return new ReadOnlyEnumBinding((JTextField) control, formModel, formPropertyPath);
        }
        else
        {
            throw new IllegalArgumentException("control must be a JTextField");
        }
    }

}