package org.valkyriercp.form.binding.jide;

import com.jidesoft.swing.StyledLabel;
import org.springframework.util.Assert;
import org.valkyriercp.binding.form.FormModel;
import org.valkyriercp.form.binding.Binding;
import org.valkyriercp.form.binding.swing.LabelBinder;
import org.valkyriercp.form.binding.swing.LabelBinding;

import javax.swing.*;
import java.util.Map;

public class StyledLabelBinder extends LabelBinder {
    protected Binding doBind(JComponent control, FormModel formModel, String formPropertyPath, Map context) {
        Assert.isTrue(control instanceof StyledLabel, "Control must be an instance of StyledLabel.");
        return new LabelBinding((StyledLabel)control, formModel, formPropertyPath);
    }

    protected JComponent createControl(Map context) {
        return new StyledLabel("");
    }
}
