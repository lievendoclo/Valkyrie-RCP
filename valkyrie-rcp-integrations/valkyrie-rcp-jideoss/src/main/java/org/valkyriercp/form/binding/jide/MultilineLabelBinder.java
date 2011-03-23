package org.valkyriercp.form.binding.jide;

import com.jidesoft.swing.MultilineLabel;
import org.springframework.util.Assert;
import org.valkyriercp.binding.form.FormModel;
import org.valkyriercp.form.binding.Binding;
import org.valkyriercp.form.binding.swing.TextAreaBinder;
import org.valkyriercp.form.binding.swing.TextComponentBinding;

import javax.swing.*;
import java.util.Map;

public class MultilineLabelBinder extends TextAreaBinder {
    protected Binding doBind(JComponent control, FormModel formModel, String formPropertyPath, Map context) {
        Assert.isTrue(control instanceof MultilineLabel, "Control must be an instance of StyledLabel.");
        return new TextComponentBinding((MultilineLabel)control, formModel, formPropertyPath);
    }

    protected JComponent createControl(Map context) {
        return new MultilineLabel("");
    }
}
