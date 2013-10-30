package org.valkyriercp.form.binding.jodatime;

import org.jdesktop.swingx.JXDatePicker;
import org.joda.time.LocalDate;
import org.valkyriercp.binding.form.FormModel;
import org.valkyriercp.form.binding.Binding;
import org.valkyriercp.form.binding.support.AbstractBinder;

import javax.swing.*;
import java.util.Map;

public class JodaLocalDateBinder extends AbstractBinder
{
    public static final String READ_ONLY_KEY = "readOnly";

    private boolean readOnly = false;

    public JodaLocalDateBinder()
    {
        super(LocalDate.class);
    }

    public void setReadOnly(boolean readOnly)
    {
        this.readOnly = readOnly;
    }

    @SuppressWarnings("unchecked")
    protected JComponent createControl(Map context)
    {
        JXDatePicker datePicker = new JXDatePicker();
        datePicker.setToolTipText(applicationConfig.messageResolver().getMessage("datePicker", "tooltip", "text"));
        return datePicker;
    }

    @SuppressWarnings("unchecked")
    protected Binding doBind(JComponent control, FormModel formModel, String formPropertyPath, Map context)
    {
        Boolean readOnly;
        if(context.containsKey(READ_ONLY_KEY)) {
             readOnly = (Boolean) context.get(READ_ONLY_KEY);
        } else {
            readOnly = this.readOnly;
        }
  
        return new JodaLocalDateBinding(formModel, formPropertyPath, ((JXDatePicker) control), readOnly);
    }
}
