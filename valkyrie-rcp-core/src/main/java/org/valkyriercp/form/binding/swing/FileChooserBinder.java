package org.valkyriercp.form.binding.swing;


import org.valkyriercp.binding.form.FormModel;
import org.valkyriercp.form.binding.Binder;
import org.valkyriercp.form.binding.Binding;

import javax.swing.*;
import java.util.Map;

public class FileChooserBinder implements Binder
{
    public static final String USE_FILE_KEY = "useFile";
    private boolean useFile = false;

    @Override
    public Binding bind(FormModel formModel, String formPropertyPath, Map context) {
        Boolean useFile;
        if(context.containsKey(USE_FILE_KEY))
            useFile = (Boolean) context.get(USE_FILE_KEY);
        else
            useFile = this.useFile;
        return new FileChooserBinding(formModel, formPropertyPath, getPropertyType(formModel, formPropertyPath), useFile);
    }

    @Override
    public Binding bind(JComponent control, FormModel formModel, String formPropertyPath, Map context) {
        throw new UnsupportedOperationException("This binder creates its own control");
    }

    protected Class<?> getPropertyType(FormModel formModel, String formPropertyPath)
    {
        return formModel.getFieldMetadata(formPropertyPath).getPropertyType();
    }

    public boolean isUseFile() {
        return useFile;
    }

    public void setUseFile(boolean useFile) {
        this.useFile = useFile;
    }
}
