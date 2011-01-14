package org.valkyriercp.form.binding.swing;

import org.springframework.util.Assert;
import org.valkyriercp.binding.form.FormModel;
import org.valkyriercp.form.binding.Binder;
import org.valkyriercp.form.binding.Binding;
import org.valkyriercp.form.binding.support.AbstractBinder;

import javax.swing.*;
import java.util.Map;

public class TextAreaBinder extends AbstractBinder {

    public static final String ROWS_KEY = "rows";

    public static final String COLUMNS_KEY = "columns";

    public TextAreaBinder() {
        super(String.class, new String[] {ROWS_KEY, COLUMNS_KEY});
    }

    protected Binding doBind(JComponent control, FormModel formModel, String formPropertyPath, Map context) {
        Assert.isTrue(control instanceof JTextArea, "Control must be an instance of JTextArea.");
        JTextArea textArea = (JTextArea)control;
        Integer rows = (Integer)context.get(ROWS_KEY);
        if (rows != null) {
            textArea.setRows(rows.intValue());
        }
        Integer columns = (Integer)context.get(COLUMNS_KEY);
        if (columns != null) {
            textArea.setColumns(columns.intValue());
        }
        return new TextComponentBinding((JTextArea)control, formModel, formPropertyPath);
    }

    protected JComponent createControl(Map context) {
        return getComponentFactory().createTextArea();
    }
}
