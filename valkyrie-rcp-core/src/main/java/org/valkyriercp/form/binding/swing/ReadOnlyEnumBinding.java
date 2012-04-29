package org.valkyriercp.form.binding.swing;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.MessageSourceAccessor;
import org.valkyriercp.binding.form.FormModel;
import org.valkyriercp.form.binding.support.CustomBinding;

import javax.swing.*;

public class ReadOnlyEnumBinding extends CustomBinding
{
    @Autowired
    private MessageSourceAccessor messageSourceAccessor;

    private JTextField label;

    public ReadOnlyEnumBinding(FormModel formModel, String formPropertyPath)
    {
        this(formModel, formPropertyPath, Enum.class);
    }

    public ReadOnlyEnumBinding(FormModel formModel, String formPropertyPath, Class requiredClass)
    {
        this(new JTextField(), formModel, formPropertyPath, requiredClass);
    }

    public ReadOnlyEnumBinding(JTextField label, FormModel formModel, String formPropertyPath)
    {
        this(label, formModel, formPropertyPath, Enum.class);
    }

    public ReadOnlyEnumBinding(JTextField label, FormModel formModel, String formPropertyPath,
                               Class requiredClass)
    {
        super(formModel, formPropertyPath, requiredClass);
        this.label = label;
        setReadOnly(true);
    }

    @Override
    protected void valueModelChanged(Object newValue)
    {
        label.setText(getEnumString((Enum) newValue));

    }

    private String getEnumString(Enum e)
    {
        if (e != null)
        {
            Class<? extends Enum> valueClass = e.getClass();
            return messageSourceAccessor.getMessage(valueClass.getName() + "." + e.name());
        }
        return "";
    }

    @Override
    protected JComponent doBindControl()
    {
        return label;
    }

    @Override
    protected void enabledChanged()
    {
        label.setEnabled(isEnabled());

    }

    @Override
    protected void readOnlyChanged()
    {
        label.setEditable(!isReadOnly());
    }

    @Override
    public void setReadOnly(boolean readOnly)
    {
        // always readonly
        super.setReadOnly(true);
    }

}